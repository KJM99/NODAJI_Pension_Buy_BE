package com.example.pensionBuying.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PensionBuyingErrorCode {
    SELECT_DUPLICATED("이미 선택한 번호입니다.", HttpStatus.CONFLICT),
    SElECT_NO_TICKET("선택된 번호가 없습니다.", HttpStatus.BAD_REQUEST),
    MAX_SELECTED("최대 구매 수량을 초과했습니다.", HttpStatus.CONFLICT),
    TIME_OUT("지금은 선택/구매할 수 없습니다.", HttpStatus.CONFLICT),
    PURCHASE_DUPLICATED("이미 구매된 번호입니다.",HttpStatus.CONFLICT),
    NOT_ENOUGH_MONEY("예치금이 부족합니다.", HttpStatus.BAD_REQUEST);


    private final String errorMessage;
    private final HttpStatus httpStatus;

}
