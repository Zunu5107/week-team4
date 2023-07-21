package com.clone.team4.global.dto;

import lombok.Getter;

@Getter
public class BaseResponseDto<T> {

    private String status;

    private String msg;

    private T data;

    public BaseResponseDto(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
