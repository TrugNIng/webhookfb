package com.viettelpost.api.base;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    int status;
    boolean error;
    String message;
    Object data;

    public BaseResponse() {
    }

    public BaseResponse(HttpStatus status) {
        this.status = status.value();
    }

    public BaseResponse(HttpStatus status, boolean error, String message) {
        this.status = status.value();
        this.error = error;
        this.message = message;
    }

    public BaseResponse(int status, boolean error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public BaseResponse(int status, boolean error, String message, Object data) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
