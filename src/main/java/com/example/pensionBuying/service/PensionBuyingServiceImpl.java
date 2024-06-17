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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PensionBuyingServiceImpl implements PensionBuyingService {

    private final SelectedNumberRepository selectedNumberRepository;
    private final PurchasedTicketsRepository purchasedTicketsRepository;

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

        if (all.isEmpty()) {
            throw new RuntimeException("선택된 번호가 없습니다.");
        }

        if((all.size()*1000L) > purchaseItem.balance()) {
            throw new RuntimeException("잔액이 부족합니다.");
        }

        //Todo: 이미 구매한 티켓 처리 해줘야함
        for (SelectedNumber selectedNumber : all) {
            redissonTicketing(purchaseItem.userEmail(), userBalance, selectedNumber);
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

    @RedissonLock(value = "#selectedNumber.round + '-' "
        + "+ #selectedNumber.groupNum + '-' "
        + "+ #selectedNumber.first + '-' "
        + "+ #selectedNumber.second + '-' "
        + "+ #selectedNumber.third + '-' "
        + "+ #selectedNumber.fourth + '-' "
        + "+ #selectedNumber.fifth + '-' "
        + "+ #selectedNumber.sixth")
    public void redissonTicketing(String userEmail, Long balance, SelectedNumber selectedNumber) {
        PurchasedTickets ticket = purchasedTicketsRepository.findByTicket(
            selectedNumber.getRound(), selectedNumber.getGroupNum(), selectedNumber.getFirst(),
            selectedNumber.getSecond(), selectedNumber.getThird(), selectedNumber.getFourth(),
            selectedNumber.getFifth(), selectedNumber.getSixth()
        );

        if(ticket != null) {
            throw new IllegalArgumentException("Ticket already exists");
        }

        extracted(userEmail, balance, selectedNumber);
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
