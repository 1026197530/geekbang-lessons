package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.impl.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Collection;

/**
 * 输出 “Hello,World” Controller
 */
@Path("/login")
public class loginController implements PageController {

    private UserService userService = new UserServiceImpl();

    @Override
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {

//        String user = request.getParameter("user");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        System.out.printf("password: %s, email: %s\n", password, email);
//        if (user == null || password == null) {
//            return "register.jsp";
//        }
        Collection<User> all = userService.getAll();

        for (User userTmp:all){
            System.out.println("111|||||"+userTmp.toString());
            if (userTmp.getEmail().equals(email) && userTmp.getPassword().equals(password)){
                return "success.jsp";
            }
        }

        return "failed.jsp";
    }
}
