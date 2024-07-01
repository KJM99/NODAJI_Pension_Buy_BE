package com.example.pensionBuying.service;

import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.global.util.TokenInfo;
import java.util.List;

public interface PensionBuyingService {
    void purchaseTicket(TokenInfo tokenInfo);

    List<PurchasedTickets> getPensionBuyingTickets(Integer round);

}
