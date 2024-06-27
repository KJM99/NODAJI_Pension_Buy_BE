package com.example.pensionBuying.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class PensionBuyingException extends RuntimeException {

    private PensionBuyingErrorCode errorCode;
    private String errorMessage;

    public PensionBuyingException(PensionBuyingErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getErrorMessage();
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
