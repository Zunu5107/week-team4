package com.clone.team4.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomStatusResponseDto {
    private boolean status;

    public CustomStatusResponseDto(boolean status){
        this.status =status;
    }
}
