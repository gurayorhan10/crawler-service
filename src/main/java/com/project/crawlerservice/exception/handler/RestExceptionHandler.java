package com.project.crawlerservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler{

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception exception) {
        Map<String, String> error = new HashMap<>();
        error.put("timestamp",new Date().toString());
        error.put("message", exception.getLocalizedMessage());
        error.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.put("status", "" + HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
