package org.geekbang.projects.springcloud.configserver.file.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FilePropertySourceLocator implements PropertySourceLocator {

    private final Logger logger = LoggerFactory.getLogger(FilePropertySourceLocator.class);

    @Override
    public PropertySource<?> locate(Environment environment) {
        String configFilePath = environment.getProperty("file.uri");
        String actuaConfigFilePath = configFilePath.replace("file://", "").replace('/', File.separatorChar);
        Properties properties = loadProperties(actuaConfigFilePath);
        Map<String, Object> map = convertToMap(properties);
        return new MapPropertySource("fileConfig", map);
    }

    /**
     * 将{@link Properties}转换为{@link Map}
     * @param properties properties数据源
     * @return
     */
    private Map<String, Object> convertToMap(Properties properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return Collections.emptyMap();
        }

        Map<String, Object> result = new HashMap<>();
        properties.forEach((k, v) -> result.put(String.valueOf(k), v));
        return result;
    }

    /**
     * 读取properties文件
     * @param actuaConfigFilePath 文件路径
     * @return
     */
    private Properties loadProperties(String actuaConfigFilePath) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(actuaConfigFilePath));
            return properties;
        } catch (IOException e) {
            logger.error("load properties error, path: " + actuaConfigFilePath, e);
            throw new RuntimeException(e);
        }
    }

}
