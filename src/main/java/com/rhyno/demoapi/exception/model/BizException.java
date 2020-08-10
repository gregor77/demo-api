package com.rhyno.demoapi.exception.model;

public class BizException extends RuntimeException {
    public BizException(String errorMessage) {
        super(errorMessage);
    }
}
