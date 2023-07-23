package com.clone.team4.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDto<T> {

    private String status;

    private String msg;

    private T data;

    private BaseResponseDto(){}

    public BaseResponseDto(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static BaseResponseDtoBuilder builder(){
        return new BaseResponseDtoBuilder();
    }
    public static BaseMessageResponseDtoBuilder MessageBuilder(){
        return new BaseMessageResponseDtoBuilder();
    }

    public static class BaseResponseDtoBuilder{
        private BaseResponseDto result;

        private BaseResponseDtoBuilder(){
            this.result = new BaseResponseDto();
        }

        public BaseResponseDtoBuilder status(int status){
            this.result.status = String.valueOf(status);
            return this;
        }

        public BaseResponseDtoBuilder status(String status){
            this.result.status = status;
            return this;
        }

        public BaseResponseDtoBuilder msg(String msg){
            this.result.msg = msg;
            return this;
        }

        public <T> BaseResponseDtoBuilder data(T data){
            this.result.data = data;
            return this;
        }

        public BaseResponseDto build(){
            return this.result;
        }
    }
    public static class BaseMessageResponseDtoBuilder{
        private BaseResponseDto result;

        private BaseMessageResponseDtoBuilder(){
            this.result = new BaseResponseDto();
            this.result.data = new LinkedHashMap<String, String>();
        }

        public BaseMessageResponseDtoBuilder status(int status){
            this.result.status = String.valueOf(status);
            return this;
        }

        public BaseMessageResponseDtoBuilder status(String status){
            this.result.status = status;
            return this;
        }

        public BaseMessageResponseDtoBuilder msg(String msg){
            this.result.msg = msg;
            return this;
        }

        public BaseMessageResponseDtoBuilder addMessage(String key, String value){
            ((Map<String,String>)this.result.data).put(key, value);
            return this;
        }

        public BaseResponseDto build(){
            return this.result;
        }
    }
}
