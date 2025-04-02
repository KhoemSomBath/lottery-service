package com.hacknovation.systemservice.exception.httpstatus;

public class PermanentException extends RuntimeException {

    private String message;
    private Object data;

    public PermanentException(String message, Object data) {
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

