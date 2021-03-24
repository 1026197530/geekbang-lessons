package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.geektimes.configuration.microprofile.config.source.filter.ServletConfigThreadLocal;
import org.geektimes.web.mvc.controller.PageController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 输出 “Hello,World” Controller
 */
@Path("/hello")
public class HelloWorldController implements PageController {

 // /hello/world -> HelloWorldController
//    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
////        return "index.jsp";
//
//    }
 @GET
 @Path("/world")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        Config config = ServletConfigThreadLocal.get();
        String configValue = config.getValue("application.name", String.class);
        System.out.println("[user-web模块]通过ThreadLocal获取[application.name]配置值:" + configValue);
        return "index.jsp";
    }
}
