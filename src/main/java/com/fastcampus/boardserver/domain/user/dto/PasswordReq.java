package com.fastcampus.boardserver.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordReq {

    private Long id;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "((?=.*[a-z])(?=.*[/d])(?=.*[^a-zA-Z0-9]).{8,})")
    private String password;

    @Builder
    public PasswordReq(Long id, String password) {
        this.id = id;
        this.password = password;
    }
}
