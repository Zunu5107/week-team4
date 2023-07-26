package com.clone.team4.global.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomStatusAndMessageListResponseDto extends CustomStatusResponseDto{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, String> msgList;

    public CustomStatusAndMessageListResponseDto(boolean status) {
        super(status);
        msgList = new LinkedHashMap<>();
    }
    public void addMessage(String field, String msg){
        this.msgList.put(field,msg);
    }
}
