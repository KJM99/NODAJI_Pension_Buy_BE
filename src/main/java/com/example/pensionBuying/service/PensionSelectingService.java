package com.example.pensionBuying.service;

import com.example.pensionBuying.domain.dto.request.SelectItemRequest;
import com.example.pensionBuying.domain.dto.response.SelectItemResponse;
import com.example.pensionBuying.global.util.TokenInfo;
import java.util.List;

public interface PensionSelectingService {
    void selectNumber(TokenInfo tokenInfo, SelectItemRequest selectItem);

    List<SelectItemResponse> getPensionSelectingTickets(TokenInfo tokenInfo);

    void deleteSelectedTicket(Long selectedNumberId);
}