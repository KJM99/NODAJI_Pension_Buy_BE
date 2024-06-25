package com.example.pensionBuying.domain.dto.response;

public record SelectItemResponse(
    Long selectedNumberId,
    String userId,
    Integer round,
    Integer group,
    Integer first,
    Integer second,
    Integer third,
    Integer fourth,
    Integer fifth,
    Integer sixth
) {

}
