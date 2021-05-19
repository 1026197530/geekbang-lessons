package org.geektimes.security.hash;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ConsistencyHashCluster}
 */
public class ConsistencyHashClusterTest {

    private Cluster cluster;
    private static final int dataCount = 200000;
    private static final String template = "节点名称: %s, 数据量: %d, 百分比: %s";

    @Before
    public void setUp() {
        cluster = new ConsistencyHashCluster();
        cluster.addNode(new Node("node1", "192.168.16.1", "7001"));
        cluster.addNode(new Node("node2", "192.168.16.2", "7002"));
        cluster.addNode(new Node("node3", "192.168.16.3", "7003"));
        cluster.addNode(new Node("node4", "192.168.16.4", "7004"));

        String preKey = "Data_";

        for (int index = 0; index < dataCount; index++) {
            Node node = cluster.getNode(preKey + index);
            node.put(preKey + index, "测试");
        }
    }

    @Test
    public void test() {
        System.out.println("============初始数据分布情况============");
        printNodeDataDistribution();

        cluster.removeNode("node3");
        System.out.println("============删除node3后数据分布情况============");
        printNodeDataDistribution();

        cluster.addNode(new Node("node5", "192.168.16.5", "7005"));
        System.out.println("============增加node5后数据分布情况============");
        printNodeDataDistribution();

        cluster.addNode(new Node("node6", "192.168.16.6", "7006"));
        cluster.addNode(new Node("node7", "192.168.16.7", "7007"));
        cluster.addNode(new Node("node8", "192.168.16.8", "7008"));
        cluster.addNode(new Node("node9", "192.168.16.9", "7009"));
        cluster.addNode(new Node("node10", "192.168.16.10", "7010"));
        cluster.addNode(new Node("node11", "192.168.16.11", "7011"));
        System.out.println("============增加node6-node11后数据分布情况============");
        printNodeDataDistribution();
    }

    /**
     * 打印节点数据分布情况
     */
    private void printNodeDataDistribution() {
        cluster.getNodes().forEach(node -> {
            float value = node.size() * 1f / dataCount;
            String percent = getPercent(value);
            System.out.printf((template) + "%n", node.getName(), node.size(), percent);
        });
    }

    private String getPercent(float value) {
        java.text.NumberFormat percentFormat = java.text.NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2); //最大小数位数
        percentFormat.setMaximumIntegerDigits(3); //最大整数位数
        percentFormat.setMinimumFractionDigits(1); //最小小数位数
        percentFormat.setMinimumIntegerDigits(1); //最小整数位数
        return percentFormat.format(value);
    }

}