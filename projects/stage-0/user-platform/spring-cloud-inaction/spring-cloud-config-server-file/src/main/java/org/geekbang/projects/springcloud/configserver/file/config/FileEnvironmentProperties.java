package org.geekbang.projects.springcloud.configserver.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.server.support.EnvironmentRepositoryProperties;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spring.cloud.config.server.file")
public class FileEnvironmentProperties implements EnvironmentRepositoryProperties {

    private int order = Ordered.LOWEST_PRECEDENCE;

    /**
     * 配置文件路径
     */
    private String uri;

    public int getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
