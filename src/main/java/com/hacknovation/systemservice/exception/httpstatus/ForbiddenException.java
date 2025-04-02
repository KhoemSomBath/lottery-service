package com.hacknovation.systemservice.exception.httpstatus;

public class ForbiddenException extends RuntimeException {


    private String message;
    private Object data;

    public ForbiddenException(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ForbiddenException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

}
