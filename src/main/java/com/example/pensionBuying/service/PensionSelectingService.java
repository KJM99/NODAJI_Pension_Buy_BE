package com.example.pensionBuying.service;

import com.example.pensionBuying.domain.dto.request.PurchaseItemRequest;
import com.example.pensionBuying.domain.dto.request.SelectItemRequest;
import com.example.pensionBuying.domain.dto.response.SelectItemResponse;
import com.example.pensionBuying.domain.entity.PurchasedTickets;
import java.util.List;

public interface PensionSelectingService {
    void selectNumber(SelectItemRequest selectItem);

    // List<SelectItemResponse> getPensionSelectingTickets(TokenInfo tokenInfo);
    List<SelectItemResponse> getPensionSelectingTickets(String userId);

    void deleteSelectedTicket(Long selectedNumberId);
}