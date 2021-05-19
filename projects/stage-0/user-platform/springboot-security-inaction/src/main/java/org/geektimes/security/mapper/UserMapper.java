package org.geektimes.security.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.geektimes.security.entity.UserEntity;

@Mapper
public interface UserMapper {

    UserEntity getById(Integer id);

}
