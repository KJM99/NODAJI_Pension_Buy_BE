package com.example.pensionBuying.api;

import com.example.pensionBuying.domain.entity.PurchasedTickets;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feignMatching", url = "localhost:8082")
public interface FeignMatching {

    @PostMapping("/api/v1/pension/purchased")
    void purchasedTicket(@RequestBody List<PurchasedTickets> purchasedTickets);
}
