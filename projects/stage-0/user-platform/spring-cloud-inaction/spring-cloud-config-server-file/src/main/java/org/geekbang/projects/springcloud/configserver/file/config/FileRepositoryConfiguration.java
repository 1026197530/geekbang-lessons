package org.geekbang.projects.springcloud.configserver.file.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
@Profile("file")
public class FileRepositoryConfiguration {
}
