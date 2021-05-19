package org.geektimes.security.hash;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Node {

    private final String name;
    private final String host;
    private final String port;
    private final Map<String, Object> data = new ConcurrentHashMap<>();

    public Node(String name, String host, String port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public int size() {
        return this.data.size();
    }

    public boolean containsKey(String key) {
        return this.data.containsKey(key);
    }

    public Map<String, Object> getData() {
        return data;
    }

}
