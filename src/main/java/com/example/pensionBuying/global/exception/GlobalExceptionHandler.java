package com.example.pensionBuying.global.exception;

import com.example.pensionBuying.exception.PensionBuyingErrorResponse;
import com.example.pensionBuying.exception.PensionBuyingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {
    @ExceptionHandler(PensionBuyingException.class)
    public ResponseEntity<PensionBuyingErrorResponse> handlePensionBuyingException(PensionBuyingException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(PensionBuyingErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getErrorMessage())
                .build());
    }
}
