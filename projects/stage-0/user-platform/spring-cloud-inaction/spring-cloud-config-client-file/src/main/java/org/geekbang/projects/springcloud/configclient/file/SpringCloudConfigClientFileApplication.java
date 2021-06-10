package org.geekbang.projects.springcloud.configclient.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class SpringCloudConfigClientFileApplication {

    @Value("${name}")
    private String name;

    @Value("${age}")
    private int age;

    @Bean
    public ApplicationRunner runner() {
        return args -> System.out.printf("name = %s, age = %d %n", name, age);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigClientFileApplication.class, args);
    }

}
