package com.example.pensionBuying.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.domain.entity.SelectedNumber;
import com.example.pensionBuying.domain.repository.PurchasedTicketsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class PensionBuyingServiceImplTest {

    @Autowired
    PensionBuyingServiceImpl pensionBuyingService;



    @Test
    public void testConcurrentTicketing() throws InterruptedException {
        SelectedNumber selectedNumber = SelectedNumber.builder()
            .userId(UUID.randomUUID())
            .round(1)
            .groupNum(1)
            .first(1)
            .second(2)
            .third(3)
            .fourth(4)
            .fifth(5)
            .sixth(6)
            .build();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            pensionBuyingService.ticketing("aaa@aaa.com", 5000L, selectedNumber);
        };

        Runnable task2 = () -> {
            pensionBuyingService.redissonTicketing("aaa@aaa.com", 5000L, selectedNumber);
        };

        executorService.submit(task1);
        executorService.submit(task2);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("테스트 완료");
    }


}