package com.example.pensionBuying.controller;


import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.global.util.TokenInfo;
import com.example.pensionBuying.service.PensionBuyingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*"
    ,methods = {
    RequestMethod.GET,
    RequestMethod.POST,
    RequestMethod.DELETE,
    RequestMethod.PUT,
    RequestMethod.OPTIONS}, allowedHeaders = "*")
@RequestMapping("/api/v1/pension/purchased")
public class PensionBuyingController {

    private final PensionBuyingService pensionBuyingService;

    @GetMapping
    public List<PurchasedTickets> getPensionBuyingTickets(
        @RequestParam Integer round
    ) {
        return pensionBuyingService.getPensionBuyingTickets(round);
    }

    @PostMapping
    public void purchaseTicket(
        @AuthenticationPrincipal TokenInfo tokenInfo
    ) {
        pensionBuyingService.purchaseTicket(tokenInfo);
    }


}
