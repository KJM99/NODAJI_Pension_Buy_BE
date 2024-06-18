package com.example.pensionBuying.service;

import com.example.pensionBuying.aspect.annotation.RedissonLock;
import com.example.pensionBuying.domain.dto.request.PurchaseItem;
import com.example.pensionBuying.domain.dto.request.SelectItem;
import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.domain.entity.SelectedNumber;
import com.example.pensionBuying.domain.repository.PurchasedTicketsRepository;
import com.example.pensionBuying.domain.repository.SelectedNumberRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PensionBuyingServiceImpl implements PensionBuyingService {

    private final SelectedNumberRepository selectedNumberRepository;
    private final PurchasedTicketsRepository purchasedTicketsRepository;

    private final RedissonClient redissonClient;


    @Override
    public List<PurchasedTickets> getPensionBuyingTickets(Integer round) {
        return purchasedTicketsRepository.findByRound(round);
    }

    @Override
    public void selectNumber(SelectItem selectItem) {

        // 이미 선택된 번호 처리
        List<SelectedNumber> byUserId = selectedNumberRepository.findBySelectedNumber(
            selectItem.userId(), selectItem.round(), selectItem.group(), selectItem.first(),
            selectItem.second(), selectItem.third(), selectItem.fourth(), selectItem.fifth(), selectItem.sixth()
        );

        if(!byUserId.isEmpty()) {
            throw new IllegalArgumentException("이미 선택된 번호입니다.");
        }

        // 선택된 번호가 최대 구매 수량 이상일 때 처리
        List<SelectedNumber> byUserId1 = selectedNumberRepository.findByUserId(selectItem.userId());
        if(byUserId1.size() > 19){
            throw new IllegalArgumentException("최대 구매 수량을 초과했습니다.");
        }

        selectedNumberRepository.save(selectItem.toEntity());
    }

    @Override
    public void purchaseTicket(PurchaseItem purchaseItem) {
        List<SelectedNumber> all = getSelectedNumbers(purchaseItem.userId());
        Long userBalance = purchaseItem.balance();
        LocalDateTime currentTime = LocalDateTime.now();
        DayOfWeek dayOfWeek = currentTime.getDayOfWeek();

        if ((dayOfWeek == DayOfWeek.WEDNESDAY && currentTime.getHour() >= 22) ||
            (dayOfWeek == DayOfWeek.THURSDAY && currentTime.getHour() <= 20)) {
            throw new IllegalArgumentException("지금은 구매할 수 없습니다.");
        }

        if (all.isEmpty()) {
            throw new RuntimeException("선택된 번호가 없습니다.");
        }

        if((all.size()*1000L) > purchaseItem.balance()) {
            throw new RuntimeException("잔액이 부족합니다.");
        }

        //Todo: 이미 구매한 티켓 처리 해줘야함
        for (SelectedNumber selectedNumber : all) {
            lockTicketing(purchaseItem.userEmail(), userBalance, selectedNumber);
            userBalance -= 1000L;
        }

        //구매가 진행되면 임시테이블 데이터 날리기
        deleteSelectedNumbers(purchaseItem.userId());
    }

    public void ticketing(String userEmail, Long balance, SelectedNumber selectedNumber) {
        PurchasedTickets ticket = purchasedTicketsRepository.findByTicket(
            selectedNumber.getRound(), selectedNumber.getGroupNum(), selectedNumber.getFirst(),
            selectedNumber.getSecond(), selectedNumber.getThird(), selectedNumber.getFourth(),
            selectedNumber.getFifth(), selectedNumber.getSixth()
        );

        if(ticket == null) {
            extracted(userEmail, balance, selectedNumber);
        }
    }

    public void lockTicketing(String userEmail, Long balance, SelectedNumber selectedNumber) {
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
                        extracted(userEmail, balance, selectedNumber);
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

    private void extracted(String userEmail, Long balance,
        SelectedNumber selectedNumber) {
        PurchasedTickets ticket =
            PurchasedTickets.builder()
                .round(selectedNumber.getRound())
                .userId(selectedNumber.getUserId())
                .userEmail(userEmail)
                .userAccBalance(balance - 1000L)
                .groupNum(selectedNumber.getGroupNum())
                .first(selectedNumber.getFirst())
                .second(selectedNumber.getSecond())
                .third(selectedNumber.getThird())
                .fourth(selectedNumber.getFourth())
                .fifth(selectedNumber.getFifth())
                .sixth(selectedNumber.getSixth())
                .createAt(LocalDateTime.now())
                .build();

        purchasedTicketsRepository.save(ticket);
    }


    private List<SelectedNumber> getSelectedNumbers(String userId) {
        return selectedNumberRepository.findByUserId(userId);
    }

    private void deleteSelectedNumbers(String userId) {
        selectedNumberRepository.deleteAll(selectedNumberRepository.findByUserId(userId));
    }
}
