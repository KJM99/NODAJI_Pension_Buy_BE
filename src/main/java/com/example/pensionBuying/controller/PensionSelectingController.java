package com.example.pensionBuying.controller;

import com.example.pensionBuying.domain.dto.request.PurchaseItemRequest;
import com.example.pensionBuying.domain.dto.request.SelectItemRequest;
import com.example.pensionBuying.domain.dto.response.SelectItemResponse;
import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.global.util.TokenInfo;
import com.example.pensionBuying.service.PensionBuyingService;
import com.example.pensionBuying.service.PensionSelectingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/api/v1/pension/selected")
public class PensionSelectingController {

    private final PensionSelectingService pensionSelectingService;

    @GetMapping
    public List<SelectItemResponse> getPensionSelectingTickets(
        /*@RequestParam String userId*/@AuthenticationPrincipal TokenInfo tokenInfo) {
        return pensionSelectingService.getPensionSelectingTickets(tokenInfo);
        // return pensionSelectingService.getPensionSelectingTickets(userId);
    }

    @PostMapping
    public void selectNumber(@AuthenticationPrincipal TokenInfo tokenInfo,
        @RequestBody SelectItemRequest selectItem) {
        pensionSelectingService.selectNumber(tokenInfo, selectItem);
    }

    @DeleteMapping("{selectedNumberId}")
    public void deletePensionSelectingTicket(@PathVariable("selectedNumberId") Long selectedNumberId){
        pensionSelectingService.deleteSelectedTicket(selectedNumberId);
    }


}