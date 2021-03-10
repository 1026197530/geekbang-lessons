package org.geektimes.projects.user.domain;

import org.geektimes.projects.user.orm.jpa.JpaDemo;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

/**
 * 用户领域对象
 *
 * @since 1.0
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

// <<<<<<< HEAD
//     public Long id;

//     public String name;

//     public String password;

//     public String email;

//     public String phoneNumber;

//     public User(String user, String password, String email, String phoneNumber) {
//         this.name = user;
//         this.password = password;
//         this.email = email;
//         this.phoneNumber = phoneNumber;
//     }
//     public User() {}
// =======
    @Id
    @GeneratedValue(strategy = AUTO)
    @NotNull(groups={JpaDemo.class})
    @Min(0)
    private Long id;

    @Column
    private String name;

    @Column
    @NotNull
    @Pattern(regexp = "^.{6,32}$", message = "密码格式错误")
    private String password;

    @Column
    private String email;

    @Column
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String phoneNumber;
// >>>>>>> upstream/master

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
