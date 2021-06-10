package org.geekbang.projects.springcloud.configserver.file.redis;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigProperties {

    private String password;


    private Cluster cluster;

    private Integer  slaveConnectionMinimumIdleSize;

    private Integer  masterConnectionMinimumIdleSize;

    private Integer masterConnectionPoolSize;

    private Integer slaveConnectionPoolSize;

  public static class Cluster{
    private List<String> nodes;

    public List<String> getNodes() {
      return nodes;
    }

    public void setNodes(List<String> nodes) {
      this.nodes = nodes;
    }
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Cluster getCluster() {
    return cluster;
  }

  public void setCluster(Cluster cluster) {
    this.cluster = cluster;
  }

  public Integer getSlaveConnectionMinimumIdleSize() {
    return slaveConnectionMinimumIdleSize;
  }

  public void setSlaveConnectionMinimumIdleSize(Integer slaveConnectionMinimumIdleSize) {
    this.slaveConnectionMinimumIdleSize = slaveConnectionMinimumIdleSize;
  }

  public Integer getMasterConnectionMinimumIdleSize() {
    return masterConnectionMinimumIdleSize;
  }

  public void setMasterConnectionMinimumIdleSize(Integer masterConnectionMinimumIdleSize) {
    this.masterConnectionMinimumIdleSize = masterConnectionMinimumIdleSize;
  }

  public Integer getMasterConnectionPoolSize() {
    return masterConnectionPoolSize;
  }

  public void setMasterConnectionPoolSize(Integer masterConnectionPoolSize) {
    this.masterConnectionPoolSize = masterConnectionPoolSize;
  }

  public Integer getSlaveConnectionPoolSize() {
    return slaveConnectionPoolSize;
  }

  public void setSlaveConnectionPoolSize(Integer slaveConnectionPoolSize) {
    this.slaveConnectionPoolSize = slaveConnectionPoolSize;
  }
}
