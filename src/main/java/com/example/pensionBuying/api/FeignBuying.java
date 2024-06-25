package com.example.pensionBuying.api;


import com.example.pensionBuying.domain.dto.dto.BuyingDto;
import com.example.pensionBuying.global.dto.BuyResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "feignPayment", url = "192.168.0.10:8080")
public interface FeignBuying {

    @PostMapping("/api/v1/pension/payments/{userId}")
    BuyResponseDto buy(@PathVariable("userId") String userId, @RequestParam BuyingDto buyingDto);

}
