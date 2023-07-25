package com.clone.team4.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "email not allow")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z\\d]{8,}$", message = "password not allow")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z|A-Z|\\d]{2,15}$", message = "nickname not allow")
    private String nickname;
}
