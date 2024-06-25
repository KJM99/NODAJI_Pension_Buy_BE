package com.example.pensionBuying.global.dto;

public record BuyResponseDto (String status){
    public static BuyResponseDto from(String status) {
        return new BuyResponseDto(
            status
        );
    }
}