package com.example.pensionBuying.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PensionBuyingErrorResponse {

    private PensionBuyingErrorCode errorCode;
    private String errorMessage;

}
