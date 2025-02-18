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
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$")
    private String nickname;

    private int age;


    @Builder
    public EditMemberReq(String nickname, int age) {
        this.nickname = nickname;
        this.age = age;
    }

    @Builder
    public EditMemberReq(String nickname) {
        this.nickname = nickname;
    }
}
