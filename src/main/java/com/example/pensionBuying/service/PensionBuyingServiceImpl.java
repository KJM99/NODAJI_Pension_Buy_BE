package com.example.pensionBuying.service;

import com.example.pensionBuying.api.ApiBuying;
import com.example.pensionBuying.domain.dto.dto.PurchasedTicketDto;
import com.example.pensionBuying.domain.dto.request.PurchaseItemRequest;
import com.example.pensionBuying.domain.dto.request.SelectItemRequest;
import com.example.pensionBuying.domain.dto.response.SelectItemResponse;
import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.domain.entity.SelectedNumber;
import com.example.pensionBuying.domain.repository.PurchasedTicketsRepository;
import com.example.pensionBuying.domain.repository.SelectedNumberRepository;
// import com.example.pensionBuying.global.util.TokenInfo;
import com.example.pensionBuying.global.dto.BuyResponseDto;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PensionBuyingServiceImpl implements PensionBuyingService, PensionSelectingService {

    private final SelectedNumberRepository selectedNumberRepository;
    private final PurchasedTicketsRepository purchasedTicketsRepository;
    private final RedissonClient redissonClient;
    private final ApiBuying apiBuying;

    @Override
    // public List<SelectItemResponse> getPensionSelectingTickets(TokenInfo token) {
    public List<SelectItemResponse> getPensionSelectingTickets(String userId) {
        // List<SelectedNumber> byUserId = selectedNumberRepository.findByUserId(token.userId());
        List<SelectedNumber> byUserId = selectedNumberRepository.findByUserId(userId);
        return byUserId.stream()
            .map(number -> new SelectItemResponse(
                number.getSelectedNumberId(),
                number.getUserId(),
                number.getRound(),
                number.getGroupNum(),
                number.getFirst(),
                number.getSecond(),
                number.getThird(),
                number.getFourth(),
                number.getFifth(),
                number.getSixth()
            ))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteSelectedTicket(Long selectedNumberId) {
        selectedNumberRepository.deleteById(selectedNumberId);
    }

    @Override
    public List<PurchasedTickets> getPensionBuyingTickets(Integer round) {
        return purchasedTicketsRepository.findByRound(round);
    }

    @Override
    public void selectNumber(SelectItemRequest selectItem) {
        // 이미 선택된 번호 처리
        selectedTicketCheck(selectItem);

        // 선택된 번호가 최대 구매 수량 이상일 때 처리
        selectedMaxCheck(selectItem);

        selectedNumberRepository.save(selectItem.toEntity());
    }

    @Override
    public void purchaseTicket(PurchaseItemRequest purchaseItem) {
        List<SelectedNumber> all = getSelectedNumbers(purchaseItem.userId());
        LocalDateTime currentTime = LocalDateTime.now();
        DayOfWeek dayOfWeek = currentTime.getDayOfWeek();

        if ((dayOfWeek == DayOfWeek.WEDNESDAY && currentTime.getHour() >= 22) ||
            (dayOfWeek == DayOfWeek.THURSDAY && currentTime.getHour() <= 20)) {
            throw new IllegalArgumentException("지금은 구매할 수 없습니다.");
        }

        if (all.isEmpty()) {
            throw new RuntimeException("선택된 번호가 없습니다.");
        }

        BuyResponseDto buying = apiBuying.buying(purchaseItem.userId(), "연금복권", (all.size() * 1000L));

        System.out.println(buying);
        //Todo: 결과 값이 success 인 경우 구매 진행, 그 외는 분기처리
        for (SelectedNumber selectedNumber : all) {
            lockTicketing(selectedNumber);
        }

        //구매가 진행되면 임시테이블 데이터 날리기
        deleteSelectedNumbers(purchaseItem.userId());
    }

    // Test 용 ticketing 메소드
    public void ticketing(SelectedNumber selectedNumber) {
        PurchasedTickets ticket = purchasedTicketsRepository.findByTicket(
            selectedNumber.getRound(), selectedNumber.getGroupNum(), selectedNumber.getFirst(),
            selectedNumber.getSecond(), selectedNumber.getThird(), selectedNumber.getFourth(),
            selectedNumber.getFifth(), selectedNumber.getSixth()
        );

        if(ticket == null) {
            purchase(selectedNumber);
        }
    }

    public void lockTicketing(SelectedNumber selectedNumber) {
        String lockName = "lock:ticket:" + selectedNumber.getRound() + ":" + selectedNumber.getGroupNum() + ":" +
            selectedNumber.getFirst() + ":" + selectedNumber.getSecond() + ":" +
            selectedNumber.getThird() + ":" + selectedNumber.getFourth() + ":" +
            selectedNumber.getFifth() + ":" + selectedNumber.getSixth();

        RLock lock = redissonClient.getLock(lockName);

        try {
            // 락을 100ms 동안 기다리고 10초 동안 유지
            if (lock.tryLock(100, 10000, TimeUnit.MILLISECONDS)) {
                try {
                    // Repository에서 티켓 찾기
                    PurchasedTickets ticket = purchasedTicketsRepository.findByTicket(
                        selectedNumber.getRound(), selectedNumber.getGroupNum(), selectedNumber.getFirst(),
                        selectedNumber.getSecond(), selectedNumber.getThird(), selectedNumber.getFourth(),
                        selectedNumber.getFifth(), selectedNumber.getSixth()
                    );

                    // 티켓이 존재하지 않을 경우 처리
                    if(ticket == null) {
                        purchase(selectedNumber);
                    }
                } finally {
                    // 반드시 락을 해제
                    lock.unlock();
                }
            } else {
                // 락을 획득하지 못했을 경우 처리
                throw new RuntimeException("티켓팅을 위한 락 획득 실패");
            }
        } catch (InterruptedException e) {
            // 인터럽트 예외 처리
            Thread.currentThread().interrupt();
            throw new RuntimeException("스레드가 인터럽트되었습니다", e);
        }
    }

    private void purchase(SelectedNumber selectedNumber) {
        PurchasedTicketDto pTicket = new PurchasedTicketDto(selectedNumber);
        PurchasedTickets ticket = pTicket.toPurchasedTickets(selectedNumber);

        purchasedTicketsRepository.save(ticket);
    }


    private List<SelectedNumber> getSelectedNumbers(String userId) {
        return selectedNumberRepository.findByUserId(userId);
    }

    private void deleteSelectedNumbers(String userId) {
        selectedNumberRepository.deleteAll(selectedNumberRepository.findByUserId(userId));
    }

    private void selectedMaxCheck(SelectItemRequest selectItem) {
        List<SelectedNumber> byUserId1 = selectedNumberRepository.findByUserId(selectItem.userId());
        // if(byUserId1.size() > 19){
        if(byUserId1.size() > 200){
            throw new IllegalArgumentException("최대 구매 수량을 초과했습니다.");
        }
    }

    private void selectedTicketCheck(SelectItemRequest selectItem) {
        List<SelectedNumber> byUserId = selectedNumberRepository.findBySelectedNumber(
            selectItem.userId(), selectItem.round(), selectItem.group(), selectItem.first(),
            selectItem.second(), selectItem.third(), selectItem.fourth(), selectItem.fifth(), selectItem.sixth()
        );

        if(!byUserId.isEmpty()) {
            throw new IllegalArgumentException("이미 선택된 번호입니다.");
        }
    }
}
