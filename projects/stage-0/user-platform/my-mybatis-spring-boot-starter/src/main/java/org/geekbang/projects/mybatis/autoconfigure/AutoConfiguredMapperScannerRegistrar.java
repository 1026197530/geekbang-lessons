package org.geekbang.projects.mybatis.autoconfigure;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoConfiguredMapperScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

    private static final Logger logger = LoggerFactory.getLogger(AutoConfiguredMapperScannerRegistrar.class);

    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        if (!AutoConfigurationPackages.has(this.beanFactory)) {
            logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.");
            return;
        }

        logger.debug("Searching for mappers annotated with @Mapper");

        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        if (logger.isDebugEnabled()) {
            packages.forEach(pkg -> logger.debug("Using auto-configuration base package '{}'", pkg));
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", true);
        builder.addPropertyValue("annotationClass", Mapper.class);
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
        BeanWrapper beanWrapper = new BeanWrapperImpl(MapperScannerConfigurer.class);
        Set<String> propertyNames = Stream.of(beanWrapper.getPropertyDescriptors()).map(PropertyDescriptor::getName)
                .collect(Collectors.toSet());
        if (propertyNames.contains("lazyInitialization")) {
            // Need to mybatis-spring 2.0.2+
            builder.addPropertyValue("lazyInitialization", "${mybatis.lazy-initialization:false}");
        }
        if (propertyNames.contains("defaultScope")) {
            // Need to mybatis-spring 2.0.6+
            builder.addPropertyValue("defaultScope", "${mybatis.mapper-default-scope:}");
        }
        registry.registerBeanDefinition(MapperScannerConfigurer.class.getName(), builder.getBeanDefinition());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
