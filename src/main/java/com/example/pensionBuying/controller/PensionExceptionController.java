package com.example.pensionBuying.controller;

import com.example.pensionBuying.exception.PensionBuyingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PensionExceptionController {

    @ExceptionHandler(PensionBuyingException.class)
    public ResponseEntity<String> invalidValueException(PensionBuyingException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getErrorMessage());
    }

}