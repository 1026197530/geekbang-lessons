package org.geektimes.security.service.impl;

import org.geektimes.security.entity.User;
import org.geektimes.security.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager redisCacheManager;

    private Cache redisCache;

    @Before
    public void setUp() {
        this.redisCache = redisCacheManager.getCache("user");
    }

    @Test
    public void test_getById() {
        // 缓存user:1
        User user = userService.getById(1L);
        // 获取缓存的user
        User cachedUser = this.redisCache.get(1L, User.class);
        assertEquals(user, cachedUser);
    }

    @After
    public void tearDown() {
        this.redisCache.clear();
    }

}