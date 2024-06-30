package com.example.pensionBuying.api;

import com.example.pensionBuying.domain.dto.dto.BuyingDto;
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
public class ApiBuying {
    public final FeignBuying feignBuying;
    public static List<Map<String, Object>> fail = new ArrayList<>();

    @Async
    public BuyResponseDto buying(String userId, String type, Long amount) {
        BuyingDto dto = new BuyingDto(type, amount);
        BuyResponseDto buy = null;
        try {
            buy = feignBuying.buy(userId, dto);
        } catch (Exception e) {
            Map<String , Object> map = new HashMap<>();
            map.put("buy", dto);
            fail.add(map);
        }

        return buy;
    }
}
