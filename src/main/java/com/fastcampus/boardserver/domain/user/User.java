package com.fastcampus.boardserver.domain.user;

import com.fastcampus.boardserver.constant.Role;
import com.fastcampus.boardserver.constant.Status;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@Table(name = "user")
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
    public User(String userId, String password, String nickname, String phoneNumber,
                Integer age, Role role, Status status, LocalDateTime createTime) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.role = role;
        this.status = status;
        this.createTime = createTime;
    }

    public void setStatusToDelete() {
        this.status = Status.DELETED;
    }

    public void editUserInfo(String nickname, Integer age) {
        this.nickname = nickname;
        this.age = age;
    }

    public void editUserNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        if (updateTime == null) {
            return "User{" +
                    "id=" + id +
                    ", userId='" + userId + '\'' +
                    ", password='" + password + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", age=" + age +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", role=" + role +
                    ", status=" + status +
                    ", createTime=" + createTime +
                    '}';
        } else {
            return "User{" +
                    "id=" + id +
                    ", userId='" + userId + '\'' +
                    ", password='" + password + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", age=" + age +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", role=" + role +
                    ", status=" + status +
                    ", updateTime=" + updateTime +
                    '}';
        }
    }
}
