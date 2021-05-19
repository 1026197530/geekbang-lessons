/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.security.mybatis.annotation;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.geektimes.security.xml.Environments;
import org.geektimes.security.xml.XMLConfigParser;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 * Date : 2021-05-06
 */
public class MyBatisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private final Logger logger = LoggerFactory.getLogger(MyBatisBeanDefinitionRegistrar.class.getName());

    private Environment environment;

    private String sqlSessionBeanName;

    private BeanDefinitionRegistry registry;

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        this.registry = registry;
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(SqlSessionFactoryBean.class);

        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableMyBatis.class.getName());
        /**
         *  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
         *   <property name="dataSource" ref="dataSource" />
         *   <property name="mapperLocations" value="classpath*:" />
         *  </bean >
         */
        String configLocation = (String) attributes.get("configLocation");
        beanDefinitionBuilder.addPropertyReference("dataSource", (String) attributes.get("dataSource"));
        // Spring String 类型可以自动转化 Spring Resource
        beanDefinitionBuilder.addPropertyValue("configLocation", configLocation);
        beanDefinitionBuilder.addPropertyValue("mapperLocations", attributes.get("mapperLocations"));
        beanDefinitionBuilder.addPropertyValue("environment", resolvePlaceholder(attributes.get("environment")));
        // 自行添加其他属性

        // SqlSessionFactoryBean 的 BeanDefinition
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();

        sqlSessionBeanName = (String) attributes.get("value");
        registry.registerBeanDefinition(sqlSessionBeanName, beanDefinition);

        registerBeansByXml(configLocation, registry);
        registerMapperScannerConfigurerBean();
    }

    /**
     * mybatis-config.xml数据解析，并注册组件
     * {@link XMLConfigBuilder#parseConfiguration(org.apache.ibatis.parsing.XNode)}
     * @param configLocation
     * @param registry
     */
    private void registerBeansByXml(String configLocation, BeanDefinitionRegistry registry) {
        try {
            // 注册database
            String[] location = configLocation.split(":");
            ClassPathResource classPathResource = new ClassPathResource(location[1]);
            InputStream inputStream = classPathResource.getInputStream();
            XMLConfigParser xmlConfigParser = new XMLConfigParser(inputStream);
            xmlConfigParser.parse();

            Environments.DataSourceConfig dataSourceConfig = xmlConfigParser.getDataSourceConfig();
            BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(UnpooledDataSource.class);
            beanDefinitionBuilder.addPropertyValue("driver", dataSourceConfig.getDriver());
            beanDefinitionBuilder.addPropertyValue("url", dataSourceConfig.getUrl());
            beanDefinitionBuilder.addPropertyValue("username", dataSourceConfig.getUsername());
            beanDefinitionBuilder.addPropertyValue("password", dataSourceConfig.getPassword());

            registry.registerBeanDefinition("dataSource", beanDefinitionBuilder.getBeanDefinition());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
     *     <property name="basePackage" value="com.ework.upms.server.mapper" />
     *     <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
     * </bean>
     */
    private void registerMapperScannerConfigurerBean() {
//        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(SqlSessionFactoryBean.class);
//        beanDefinitionBuilder.addPropertyValue("basePackage", "org.geektimes.security.mapper");
//        beanDefinitionBuilder.addPropertyValue("sqlSessionFactoryBeanName", sqlSessionBeanName);
//        registry.registerBeanDefinition("mapperScannerConfigurer", beanDefinitionBuilder.getBeanDefinition());
    }

    private Object resolvePlaceholder(Object value) {
        if (value instanceof String) {
            return environment.resolvePlaceholders((String) value);
        }
        return value;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
