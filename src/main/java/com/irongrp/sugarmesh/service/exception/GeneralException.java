package com.irongrp.sugarmesh.service.exception;

public class GeneralException extends RuntimeException {

    public GeneralException(String errorMessage) {
        super(errorMessage);
    }

    public GeneralException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }

}
