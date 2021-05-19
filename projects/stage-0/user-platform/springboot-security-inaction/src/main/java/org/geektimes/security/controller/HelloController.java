package org.geektimes.security.controller;

import org.geektimes.security.entity.User;
import org.geektimes.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "hello world!";
    }

    @GetMapping("/user/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/user/nocache/{id}")
    public User getByIdWithoutCache(@PathVariable Long id) {
        return userService.getByIdWithoutCache(id);
    }

}
