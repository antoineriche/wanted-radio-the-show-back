package com.gaminho.theshowapp.web;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TheShowExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public static ResponseEntity<?> handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, Object> map = new HashMap<>();
        map.put("errorMsg", "ConstraintViolationException");
        map.put("status",  HttpStatus.BAD_REQUEST);
        map.put("errorDetail",  ex.getCause().getMessage());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public static ResponseEntity<?> handleEntityNotFoundException(
            EntityNotFoundException ex) {

        Map<String, Object> map = new HashMap<>();
        map.put("errorMsg", "EntityNotFoundException");
        map.put("status",  HttpStatus.BAD_REQUEST);
        map.put("errorDetail",  ex.getCause().getMessage());
        return ResponseEntity.badRequest().body(map);
    }

}
