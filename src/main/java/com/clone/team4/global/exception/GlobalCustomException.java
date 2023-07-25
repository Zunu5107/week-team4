package com.clone.team4.global.exception;

import org.springframework.http.HttpStatus;

public abstract class GlobalCustomException extends RuntimeException{
    public HttpStatus status;

    public GlobalCustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public GlobalCustomException(HttpStatus status) {
        this.status = status;
    }

    public GlobalCustomException(String message) {
        super(message);
    }

    public GlobalCustomException() {
    }

    public void setStatus(int status){
        this.status = HttpStatus.valueOf(status);
    }
    public void setStatus(HttpStatus status){
        this.status = status;
    }
}
