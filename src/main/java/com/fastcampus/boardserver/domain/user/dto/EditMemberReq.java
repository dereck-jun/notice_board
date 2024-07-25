package com.fastcampus.boardserver.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditMemberReq {

    @NotBlank
    @Length(min = 2, max = 12)
    @Pattern(regexp = "^[a-zA-Z0-9_-]{2,12}$")
    private String nickname;

    private int age;

    private String phoneNumber;

    @Builder
    public EditMemberReq(String nickname, int age, String phoneNumber) {
        this.nickname = nickname;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    @Builder
    public EditMemberReq(String nickname, int age) {
        this.nickname = nickname;
        this.age = age;
    }

    @Builder
    public EditMemberReq(String nickname, String phoneNumber) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    @Builder
    public EditMemberReq(String nickname) {
        this.nickname = nickname;
    }
}
