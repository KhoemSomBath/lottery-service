package com.hacknovation.systemservice.exception.httpstatus;

import com.hacknovation.systemservice.constant.MessageConstant;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadRequestException() {
        super(MessageConstant.BAD_REQUEST);
    }
}
