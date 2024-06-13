package com.example.pensionBuying.domain.dto.request;

public record PurchaseItem(
    String userId,
    String userEmail,
    Long balance
) {

}
