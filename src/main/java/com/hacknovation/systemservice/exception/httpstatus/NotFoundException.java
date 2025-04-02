package com.hacknovation.systemservice.exception.httpstatus;

public class NotFoundException extends RuntimeException {

    private String message;
    private Object data;

    public NotFoundException(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public NotFoundException(String message) {
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
