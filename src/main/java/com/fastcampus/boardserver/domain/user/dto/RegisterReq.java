package com.fastcampus.boardserver.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterReq {

    @NotBlank
    @Length(min = 4, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$")
    private String userId;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Length(min = 2, max = 12)
    @Pattern(regexp = "^[a-zA-Z0-9_-]{2,12}$")
    private String nickname;

    private int age;

    private String phoneNumber;

    @Builder
    public RegisterReq(String userId, String password, String nickname, int age, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }
}
