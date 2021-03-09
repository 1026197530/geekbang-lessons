package org.geektimes.projects.user.repository;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.sql.Insert;
import org.geektimes.projects.user.sql.Select;

import java.util.Collection;
import java.util.List;

/**
 * 用户存储仓库
 *
 * @since 1.0
 */
public interface UserRepository {
//     @Insert("insert into users (%s) values (%s)")
    boolean save(User user);

    boolean deleteById(Long userId);

    boolean update(User user);

    User getById(Long userId);

    User getByNameAndPassword(String userName, String password);

//    @Select(value = "select * from users", returnType = "org.geektimes.projects.user.domain.User")
    Collection<User> getAll();
}
