package org.geektimes.configuration.microprofile.config.source.servlet;

import org.geektimes.configuration.microprofile.config.source.filter.ServletConfigFilter;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.util.EnumSet;
import java.util.Set;
@HandlesTypes(WebApplicationInitializer.class)
public class ServletConfigInitializer implements ServletContainerInitializer {

    @Override
//    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
//        // 增加 ServletContextListener
//        servletContext.addListener(ServletContextConfigInitializer.class);
//    }
//        if (webAppInitializerClasses != null) {
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {

        for (Class<?> clz : webAppInitializerClasses) {
                try {
                    WebApplicationInitializer newInstance = (WebApplicationInitializer) clz.newInstance();
                    newInstance.onStartup(servletContext);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }

            }
        //注册ServletConfigFilter
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("ServletConfigFilter", ServletConfigFilter.class);
        filterRegistration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType .class), true, "/*");

    }


    }
