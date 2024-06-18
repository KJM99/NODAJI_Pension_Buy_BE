package com.example.pensionBuying.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    @Autowired
    PurchasedTicketsRepository purchasedTicketsRepository;


    @Test
    @DisplayName("동시성 처리 X")
    public void testConcurrentTicketing() throws InterruptedException {

        int n = 100;
        purchasedTicketsRepository.deleteAll();

        ExecutorService executorService = Executors.newFixedThreadPool(n);
        Runnable[] tasks = new Runnable[100];
        for (int i = 0; i < n; i++) {
            SelectedNumber selectedNumber = SelectedNumber.builder()
                .userId("c9f8eac6" + i)
                .round(1)
                .groupNum(1)
                .first(1)
                .second(2)
                .third(3)
                .fourth(4)
                .fifth(5)
                .sixth(6)
                .build();

            Runnable task1 = () -> {
                pensionBuyingService.ticketing("aaa@aaa.com", 5000L, selectedNumber);
            };
            tasks[i] = task1;

        }
        for (int i = 0; i < n; i++) {executorService.submit(tasks[i]);}


        Thread.sleep(10000);
        executorService.shutdown();

        System.out.println("테스트 완료");

        List<PurchasedTickets> all = purchasedTicketsRepository.findAll();

        assertNotEquals(1, all.size());
        // assertEquals(100, all.size());

    }


    @Test
    @DisplayName("동시성 처리 O")
    public void testConcurrentRedissonTicketing() throws InterruptedException {

        int n = 100;
        purchasedTicketsRepository.deleteAll();

        ExecutorService executorService = Executors.newFixedThreadPool(n);
        Runnable[] tasks = new Runnable[100];
        for (int i = 0; i < n; i++) {
            SelectedNumber selectedNumber = SelectedNumber.builder()
                .userId("c9f8eac6" + i)
                .round(1)
                .groupNum(1)
                .first(1)
                .second(2)
                .third(3)
                .fourth(4)
                .fifth(5)
                .sixth(6)
                .build();

            Runnable task1 = () -> {
                pensionBuyingService.lockTicketing("aaa@aaa.com", 5000L, selectedNumber);
            };
            tasks[i] = task1;

        }
        for (int i = 0; i < n; i++) {executorService.submit(tasks[i]);}




        Thread.sleep(10000);
        executorService.shutdown();

        System.out.println("테스트 완료");

        List<PurchasedTickets> all = purchasedTicketsRepository.findAll();

        assertEquals(1, all.size());

    }


}