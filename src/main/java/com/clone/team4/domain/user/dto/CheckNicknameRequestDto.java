package com.clone.team4.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CheckNicknameRequestDto {
    @Pattern(regexp = "^[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z|A-Z|\\d]{2,15}$", message = "nickname not allow")
    private String nickname;
}
