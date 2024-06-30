package com.example.pensionBuying.api;


import com.example.pensionBuying.domain.dto.dto.BuyingDto;
import com.example.pensionBuying.global.dto.BuyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feignPayment", url = "192.168.0.10:8083")
public interface FeignBuying {

    @PutMapping("/api/v1/payments/{userId}")
    BuyResponseDto buy(@PathVariable("userId") String userId, @RequestBody BuyingDto buyingDto);

}