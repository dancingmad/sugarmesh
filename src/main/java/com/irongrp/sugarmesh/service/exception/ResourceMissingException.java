package com.irongrp.sugarmesh.service.exception;

public class ResourceMissingException extends RuntimeException {
    public ResourceMissingException(String errorMessage) {
        super(errorMessage);
    }
}
