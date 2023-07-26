package com.clone.team4.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CheckEmailRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "email not allow")
    private String email;
}
