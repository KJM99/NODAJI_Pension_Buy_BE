package com.example.pensionBuying.service;


import com.example.pensionBuying.domain.dto.request.PurchaseItemRequest;
import com.example.pensionBuying.domain.dto.request.SelectItemRequest;
import com.example.pensionBuying.domain.dto.response.SelectItemResponse;
import com.example.pensionBuying.domain.entity.PurchasedTickets;
// import com.example.pensionBuying.global.util.TokenInfo;
import com.example.pensionBuying.global.util.TokenInfo;
import java.util.List;

public interface PensionBuyingService {
    //Todo: 토큰 들어오면 토큰으로 바꿔줘야함
    void purchaseTicket(TokenInfo tokenInfo);

    List<PurchasedTickets> getPensionBuyingTickets(Integer round);

}
