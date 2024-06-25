package com.example.pensionBuying.api;


import com.example.pensionBuying.domain.dto.dto.BuyingDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient/*(name = "feignPayment", url = "${feign.client.url}")*/
public interface FeignBuying {

    @PostMapping("/api/v1/pension/payments/{userId}")
    Object buy(@PathVariable("userId") String userId, @RequestParam BuyingDto buyingDto);

}
