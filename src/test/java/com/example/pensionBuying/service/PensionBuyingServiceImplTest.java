package com.example.pensionBuying.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.example.pensionBuying.api.ApiBuying;
import com.example.pensionBuying.domain.dto.response.SelectItemResponse;
import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.domain.entity.SelectedNumber;
import com.example.pensionBuying.domain.repository.PurchasedTicketsRepository;
import com.example.pensionBuying.domain.repository.SelectedNumberRepository;
import com.example.pensionBuying.global.dto.BuyResponseDto;
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
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PensionBuyingServiceImplTest {

    @MockBean
    private ApiBuying apiBuying;

    @Autowired
    PensionBuyingServiceImpl pensionBuyingService;

    @Autowired
    // @MockBean
    PurchasedTicketsRepository purchasedTicketsRepository;

    // @Autowire
    @MockBean
    private SelectedNumberRepository selectedNumberRepository;

    @Test
    void getPensionSelectingTickets() {
        // BDDMockito.given(apiBuying.buying("testUser", "연금복권", 1000L))
        //     .willReturn(BuyResponseDto.from("success"));

        String userId = "testUser";
        SelectedNumber selectedNumber =
            new SelectedNumber(null, "testUser", 1,1,1,1,1,1,1,1);

        when(selectedNumberRepository.findByUserId(userId)).thenReturn(List.of(selectedNumber));

        List<SelectItemResponse> responses = pensionBuyingService.getPensionSelectingTickets(userId);

        assertEquals(1, responses.size());
        assertEquals(userId, responses.get(0).userId());

    }

    @Test
    void deleteSelectedTicket() {
        Long selectedNumberId = 1L;
        doNothing().when(selectedNumberRepository).deleteById(selectedNumberId);

        pensionBuyingService.deleteSelectedTicket(selectedNumberId);

        verify(selectedNumberRepository, times(1)).deleteById(selectedNumberId);
    }

    @Test
    void getPensionBuyingTickets() {
        Integer round = 1;
        PurchasedTickets ticket = new PurchasedTickets();
        when(purchasedTicketsRepository.findByRound(round)).thenReturn(List.of(ticket));

        List<PurchasedTickets> tickets = pensionBuyingService.getPensionBuyingTickets(round);

        assertEquals(1, tickets.size());
    }

    @Test
    @DisplayName("동시성 처리 X")
    public void testConcurrentTicketing() throws InterruptedException {

        int n = 100;
        purchasedTicketsRepository.deleteAll();

        BDDMockito.given(apiBuying.buying("c9f8eac60", "연금복권", 1000L))
            .willReturn(BuyResponseDto.from("success"));

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
                pensionBuyingService.ticketing(selectedNumber);
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

        int n = 500;
        purchasedTicketsRepository.deleteAll();

        BDDMockito.given(apiBuying.buying("c9f8eac60", "연금복권", 1000L))
            .willReturn(BuyResponseDto.from("success"));

        ExecutorService executorService = Executors.newFixedThreadPool(n);
        Runnable[] tasks = new Runnable[500];
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
                pensionBuyingService.lockTicketing(selectedNumber);
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