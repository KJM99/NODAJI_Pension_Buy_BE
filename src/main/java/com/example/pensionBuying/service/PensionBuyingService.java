package com.example.pensionBuying.service;


import com.example.pensionBuying.domain.dto.request.SelectItem;
import java.util.UUID;

public interface PensionBuyingService {
    void selectNumber(SelectItem selectItem);

    //Todo: 토큰 들어오면 토큰으로 바꿔줘야함
    void purchaseTicket(Integer round, UUID userId, String userEmail, Long balance);
}
