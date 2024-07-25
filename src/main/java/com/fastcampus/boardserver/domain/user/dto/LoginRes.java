package com.fastcampus.boardserver.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRes {

    private String userId;
    private String password;

    public LoginRes() {
    }

    @Builder
    public LoginRes(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
