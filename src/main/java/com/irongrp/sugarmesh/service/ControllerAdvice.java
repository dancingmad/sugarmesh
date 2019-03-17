package com.irongrp.sugarmesh.service;

import com.irongrp.sugarmesh.service.exception.ResourceAlreadyExistingException;
import com.irongrp.sugarmesh.service.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;


@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvice.class);

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)  // 409
    @ExceptionHandler(UnauthorizedException.class)
    public void unauthorized(HttpServletRequest req, UnauthorizedException ex) {
        LOGGER.warn("Unauthorized access " + ex.getLocalizedMessage());
    }

    @ResponseStatus(value= HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(ResourceAlreadyExistingException.class)
    public void alreadyExisting(HttpServletRequest req, ResourceAlreadyExistingException ex) {
        LOGGER.warn("Conflict " + ex.getLocalizedMessage());
    }
}
