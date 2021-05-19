package org.geektimes.security.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.geektimes.security.entity.User;
import org.geektimes.security.entity.UserEntity;
import org.geektimes.security.mapper.UserMapper;
import org.geektimes.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "user", cacheManager = "redisCacheManager")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Cacheable(key = "#id")
    @Override
    public User getById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("fkz");
        user.setEmail("aaaa@qq.com");
        user.setPassword("abc123");
        user.setPhoneNumber("13756824658");
        return user;
    }

    @Override
    public User getByIdWithoutCache(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("fkz");
        user.setEmail("aaaa@qq.com");
        user.setPassword("abc123");
        user.setPhoneNumber("13756824658");
        return user;
    }

    @Override
    public UserEntity getByIdFromDB(Integer id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        return userMapper.getById(id);
    }
}
