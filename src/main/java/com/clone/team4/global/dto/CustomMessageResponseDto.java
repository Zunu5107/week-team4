package com.clone.team4.global.dto;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class CustomMessageResponseDto {
    private Map<String, String> data;

    private CustomMessageResponseDto(){
        data = new LinkedHashMap<>();
    }

    public static CustomMessageResponseDtoBuilder builder(String key, String value){
        return new CustomMessageResponseDtoBuilder(key, value);
    }

    public static class CustomMessageResponseDtoBuilder{
        private final CustomMessageResponseDto target;

        private CustomMessageResponseDtoBuilder() {
            this.target = new CustomMessageResponseDto();
        }

        private CustomMessageResponseDtoBuilder(String key, String value) {
            this.target = new CustomMessageResponseDto();
            target.data.put(key, value);
        }
        public CustomMessageResponseDtoBuilder addMessage(String key,String value){
            target.data.put(key, value);
            return this;
        }
        public CustomMessageResponseDto build(){ return target; }
    }
}
