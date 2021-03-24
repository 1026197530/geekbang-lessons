package org.geektimes.configuration.microprofile.config.source.filter;


import org.eclipse.microprofile.config.Config;

import javax.servlet.*;
import java.io.IOException;

public class ServletConfigFilter implements Filter {

    private Config config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        config = (Config) servletContext.getAttribute("Servlet-config");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletConfigThreadLocal.put(config);
        chain.doFilter(request, response);
        ServletConfigThreadLocal.clear();
    }

    @Override
    public void destroy() {

    }
}
