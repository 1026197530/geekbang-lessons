package org.geektimes.projects.user.web.controller;

import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * 输出 “Hello,World” Controller
 */
@Path("/register")
public class RegisterController implements PageController {
//
//    private UserService userService = new UserServiceImpl();

    private  UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");

    private Validator validator = ComponentContext.getInstance().getComponent("bean/Validator");

    @Override
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        String name = request.getParameter("userName");
//        String password = request.getParameter("password");
//        String email = request.getParameter("email");
//        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        System.out.printf("user: %s, password: %s\n", name, password);
        if (name == null) {
            return "register.jsp";
        }
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        Set<ConstraintViolation<User>> validates = validator.validate(user);
        Optional<String> message = validates.stream().map(c ->
                c.getPropertyPath().toString() + " " + c.getMessage()).findAny();
        if (message.isPresent()) {
            System.out.println("===========" + message.get() + "===========");
//            user.setPhoneNumber(phoneNumber);
            return "register.jsp";
        } else if (userService.register(user)) {

            return "success.jsp";
        }
        return "fail.jsp";
    }
//        if (user == null || password == null) {
//            return "register.jsp";
//        }
////
////        if (userService.register(new User(user, password, email, phoneNumber))) {
//////            return "login.jsp";
////            return "login-form.jsp";
////        }
//        return "failed.jsp";
//    }

}
