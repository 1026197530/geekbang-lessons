package org.geektimes.configuration.microprofile.config.source.servlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
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
        }
}
