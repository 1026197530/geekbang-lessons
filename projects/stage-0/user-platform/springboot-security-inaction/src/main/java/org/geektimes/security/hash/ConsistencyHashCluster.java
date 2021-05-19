package org.geektimes.security.hash;

import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * copy from
 * @see <a href="https://juejin.cn/post/6916292890171801613">一致性Hash算法Java版实现</a>
 */
public class ConsistencyHashCluster extends AbstractCluster {

    private static final int DEFAULT_VNODE_NUM = 150;

    private final int vNodeNum;
    private final SortedMap<Long, Node> vNodes = new TreeMap<>();

    public ConsistencyHashCluster() {
        this(DEFAULT_VNODE_NUM);
    }

    public ConsistencyHashCluster(int vNodeNum) {
        this.vNodeNum = vNodeNum;
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
        for (int i = 0; i < vNodeNum; i++) {
            String vNodeName = node.getName() + "_vnode_" + i;
            vNodes.put(hash(vNodeName), node);
        }

        for (int i=0; i<this.nodes.size() - 1; i++) {
            Node currentNode = this.nodes.get(i);
            movingDataIfNeed(currentNode);
        }
    }

    /**
     * 已存在数据重新hash, 获取节点, 如果节点变更, 则重新put
     * @param currentNode
     */
    private void movingDataIfNeed(Node currentNode) {
        Map<String, Object> data = currentNode.getData();
        data.forEach((k, v) -> {
            Node newNode = getNode(k);
            if (newNode != currentNode) {
                currentNode.remove(k);
                newNode.put(k, v);
            }
        });
    }

    @Override
    public void removeNode(String nodeName) {
        Optional<Node> optionalNode = this.nodes.stream()
                .filter(node -> node.getName().equals(nodeName))
                .findFirst();

        if (optionalNode.isPresent()) {
            Node node = optionalNode.get();
            this.nodes.remove(node);

            removeVNodes(nodeName);
            movingData(node);
        }
    }

    /**
     * 移动节点数据
     * @param node
     */
    private void movingData(Node node) {
        Map<String, Object> data = node.getData();
        data.forEach((k, v) -> {
            Node newNode = getNode(k);
            newNode.put(k, v);
        });
    }

    /**
     * 删除虚拟节点
     * @param nodeName
     */
    private void removeVNodes(String nodeName) {
        for (int i = 0; i < vNodeNum; i++) {
            String vNodeName = nodeName + "_vnode_" + i;
            vNodes.remove(hash(vNodeName));
        }
    }

    @Override
    public Node getNode(String key) {
        long hash = hash(key);
        SortedMap<Long, Node> subMap = vNodes.tailMap(hash);
        if (!subMap.isEmpty()) {
            return subMap.get(subMap.firstKey());
        }
        return vNodes.get(vNodes.firstKey());
    }

}
