package org.geektimes.security;

import org.geektimes.security.entity.UserEntity;
import org.geektimes.security.mapper.UserMapper;
import org.geektimes.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
class SpringbootSecurityInactionApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        UserEntity user = userService.getByIdFromDB(1);
        assertNotNull(user);
    }

}
