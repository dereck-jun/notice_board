package com.fastcampus.boardserver.domain.user;

import com.fastcampus.boardserver.constant.Role;
import com.fastcampus.boardserver.constant.Status;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@Table(name = "User")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Setter
    private String password;

    @Column(name = "nickname")
    private String nickname;

    private Integer age;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Builder
    public User(String userId, String password, String nickname, String phoneNumber, Integer age) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }

    public void setStatusToDelete() {
        this.status = Status.DELETED;
    }
}
