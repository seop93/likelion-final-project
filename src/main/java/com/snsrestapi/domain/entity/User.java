package com.snsrestapi.domain.entity;

import com.snsrestapi.enumerate.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    public static User registerUser(String userName, String password) {
        return User.builder()
                .userName(userName)
                .password(password)
                .role(UserRole.USER)
                .build();
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }

}
