package com.fastcampus.boardserver.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginReq {

    @NotBlank
    private String userId;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "((?=.*[a-z])(?=.*[/d])(?=.*[^a-zA-Z0-9]).{8,})")
    private String password;

    @Builder
    public LoginReq(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
