package com.clone.team4.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomStatusAndMessageAndDataResponseDto<T>  {

    Integer status;
    String msg;
    T data;

}
