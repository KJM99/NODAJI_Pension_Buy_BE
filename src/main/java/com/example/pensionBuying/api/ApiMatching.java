package com.example.pensionBuying.api;

import com.example.pensionBuying.domain.dto.dto.BuyingDto;
import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.global.dto.BuyResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiMatching {
    public final FeignMatching feignMatching;
    public static List<Map<String, Object>> fail = new ArrayList<>();

    @Async
    public void purchase(List<PurchasedTickets> purchasedTickets) {

        try {
            feignMatching.purchasedTicket(purchasedTickets);
        } catch (Exception e) {
            Map<String , Object> map = new HashMap<>();
            map.put("purchased", purchasedTickets);
            fail.add(map);
        }
    }
}