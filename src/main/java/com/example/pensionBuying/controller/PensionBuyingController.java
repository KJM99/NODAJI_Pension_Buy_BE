package com.example.pensionBuying.controller;

import com.example.pensionBuying.domain.dto.request.PurchaseItem;
import com.example.pensionBuying.domain.dto.request.SelectItem;
import com.example.pensionBuying.service.PensionBuyingService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pension")
public class PensionBuyingController {

    private final PensionBuyingService pensionBuyingService;

    @PostMapping("/selection")
    public void selectNumber(@RequestBody SelectItem selectItem) {
        pensionBuyingService.selectNumber(selectItem);
    }

    @PostMapping("/buying")
    public void purchaseTicket(
        //Todo: 토큰 들어오면 토큰으로 바꿔줘야함
        @RequestBody PurchaseItem purchaseItem
    ) {
        pensionBuyingService.purchaseTicket(purchaseItem);
    }
}
