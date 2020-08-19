package com.viettelpost.api.base;

public class VtException extends Exception {
    int code;
    String message;
    Object data;

    public VtException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public VtException(String message) {
        this.message = message;
    }

    public boolean verifyError(String errorCode) {
        return code > 0;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
