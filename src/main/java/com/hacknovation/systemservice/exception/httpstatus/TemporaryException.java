package com.hacknovation.systemservice.exception.httpstatus;

public class TemporaryException extends RuntimeException {

    private String message;
    private Object data;

    public TemporaryException(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

}
