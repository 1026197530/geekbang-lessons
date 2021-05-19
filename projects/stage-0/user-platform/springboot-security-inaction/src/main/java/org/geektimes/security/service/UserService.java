package org.geektimes.security.service;

import org.geektimes.security.entity.User;
import org.geektimes.security.entity.UserEntity;

public interface UserService {

    /**
     * 根据主键id获取用户
     * @param id
     * @return
     */
    User getById(Long id);

    /**
     * 根据主键id获取用户, 无缓存操作
     * @param id
     * @return
     */
    User getByIdWithoutCache(Long id);

    /**
     * 根据主键id从数据库获取用户
     * @param id
     * @return
     */
    UserEntity getByIdFromDB(Integer id);

}
