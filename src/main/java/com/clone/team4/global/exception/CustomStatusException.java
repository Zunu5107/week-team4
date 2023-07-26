package com.clone.team4.global.exception;

import org.springframework.http.HttpStatus;

public class CustomStatusException extends RuntimeException{
    private HttpStatus status;

    private CustomStatusException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }



    public static CustomStatusExceptionBuilder builder(String message){
        return new CustomStatusExceptionBuilder(message);
    }

    public static class CustomStatusExceptionBuilder{
        private CustomStatusException result;

        public CustomStatusExceptionBuilder(String message) {
            this.result = new CustomStatusException(message);
            this.result.status = HttpStatus.NOT_FOUND;
        }

        public CustomStatusExceptionBuilder status(int status){
            this.result.status = HttpStatus.valueOf(status);
            return this;
        }
        public CustomStatusExceptionBuilder status(HttpStatus status){
            this.result.status = status;
            return this;
        }

        public CustomStatusException build(){
            return this.result;
        }
    }
}
