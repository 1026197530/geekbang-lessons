package org.geektimes.security;

import org.geektimes.security.mybatis.annotation.EnableMyBatis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMyBatis(dataSource = "dataSource",
        configLocation = "classpath:mybatis/mybatis-config.xml",
        mapperLocations = {"classpath*:mybatis/mappers/*.xml"},
        environment = "development")
@SpringBootApplication
public class SpringbootSecurityInactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecurityInactionApplication.class, args);
    }

}
