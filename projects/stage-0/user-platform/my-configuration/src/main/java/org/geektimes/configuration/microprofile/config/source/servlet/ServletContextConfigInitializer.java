package org.geektimes.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.configuration.microprofile.config.DefaultConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 如何注册当前 ServletContextListener 实现
 *
 * @see ServletConfigInitializer
 */
public class ServletContextConfigInitializer implements WebApplicationInitializer {


    @Override
//    public void contextInitialized(ServletContextEvent servletContextEvent) {
      public void onStartup(ServletContext servletContext) throws ServletException{
    //        ServletContext servletContext = servletContextEvent.getServletContext();
//        ServletContextConfigSource servletContextConfigSource = new ServletContextConfigSource(servletContext);
//        // 获取当前 ClassLoader
//        ClassLoader classLoader = servletContext.getClassLoader();
//        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
//        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
//        // 配置 ClassLoader
//        configBuilder.forClassLoader(classLoader);
//        // 默认配置源（内建的，静态的）
//        configBuilder.addDefaultSources();
//        // 通过发现配置源（动态的）
//        configBuilder.addDiscoveredConverters();
//        // 增加扩展配置源（基于 Servlet 引擎）
//        configBuilder.withSources(servletContextConfigSource);
//        // 获取 Config
//        Config config = configBuilder.build();
//        // 注册 Config 关联到当前 ClassLoader
//        configProviderResolver.registerConfig(config, classLoader);
        ClassLoader classLoader = servletContext.getClassLoader();

        ServletContextConfigSource servletContextConfigSource = new ServletContextConfigSource(servletContext);

        ConfigProviderResolver configProviderResolver = DefaultConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        //替换classLoader
        configBuilder.forClassLoader(classLoader);
        //加载内建的配置源
        configBuilder.addDefaultSources();
        //加载动态发现的配置源
        configBuilder.addDiscoveredSources();
        //加载扩展的配置源
        configBuilder.withSources(servletContextConfigSource);
        //加载动态发现的转换器
        configBuilder.addDiscoveredConverters();

        //生成配置类
        Config config = configBuilder.build();
        //注册配置类
        configProviderResolver.registerConfig(config, classLoader);


        testLoadApplicationNameFromConfig(classLoader);

        //将CONFIG类保存至ServletContext
//        servletContext.setAttribute("CONFIG", config);
        //testLoadVersionFromConfig();
        //将CONFIG类保存至ServletContext
        servletContext.setAttribute("Servlet-config", config);

    }


    public void testLoadApplicationNameFromConfig(ClassLoader classLoader) {
        ConfigProviderResolver configProviderResolver = DefaultConfigProviderResolver.instance();
        String value = configProviderResolver.getConfig(classLoader).getValue("application.name", String.class);
//        System.out.println("读取到配置application.name:" + value);
        System.out.println("[my-configuration模块]获取[application.name]配置值:" + value);

    }
}
