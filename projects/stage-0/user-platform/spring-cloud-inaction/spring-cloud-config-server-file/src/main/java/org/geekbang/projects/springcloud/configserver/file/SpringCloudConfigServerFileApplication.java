package org.geekbang.projects.springcloud.configserver.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudConfigServerFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerFileApplication.class, args);
    }

}
