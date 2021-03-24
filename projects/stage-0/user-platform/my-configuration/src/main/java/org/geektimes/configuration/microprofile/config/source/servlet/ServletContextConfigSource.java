package org.geektimes.configuration.microprofile.config.source.servlet;

import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author wangyongfei
 */
public class ServletContextConfigSource extends MapBasedConfigSource {

    private final ServletContext servletContext;

    protected ServletContextConfigSource(ServletContext servletContext) {
        super("ServletContext Config Source", 500, false);
        this.servletContext = servletContext;
        super.source = getProperties();
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName, servletContext.getInitParameter(parameterName));
        }
    }
}
