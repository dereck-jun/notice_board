package com.fastcampus.boardserver.domain.user.dto;

import com.fastcampus.boardserver.constant.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)  // jackson 으로 데이터를 직렬화할 떄 값에 표시를 조절하는 어노테이션
public class UserDto {

    private int age;

    @NotBlank
    @Length(min = 2, max = 12)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$")
    private String nickname;

    private String phoneNumber;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Builder
    public UserDto(int age, String nickname, String phoneNumber, LocalDateTime createTime, LocalDateTime updateTime) {
        this.age = age;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
