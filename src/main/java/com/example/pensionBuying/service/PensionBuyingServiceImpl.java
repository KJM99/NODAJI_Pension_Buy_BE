package com.example.pensionBuying.service;

import com.example.pensionBuying.aspect.annotation.RedissonLock;
import com.example.pensionBuying.domain.dto.request.SelectItem;
import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.domain.entity.SelectedNumber;
import com.example.pensionBuying.domain.repository.PurchasedTicketsRepository;
import com.example.pensionBuying.domain.repository.SelectedNumberRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PensionBuyingServiceImpl implements PensionBuyingService {

    private SelectedNumberRepository selectedNumberRepository;
    private PurchasedTicketsRepository purchasedTicketsRepository;

    @Override
    public void selectNumber(SelectItem selectItem) {
        selectedNumberRepository.save(selectItem.toEntity());
    }

    @Override
    public void purchaseTicket(UUID userId, String userEmail, Long balance) {
        List<SelectedNumber> all = getSelectedNumbers();

        if (all.isEmpty()) {
            throw new RuntimeException("No selected numbers found");
        }

        if((all.size()*1000L) > balance) {
            throw new RuntimeException("Not enough balance");
        }

        //Todo: 이미 구매한 티켓 처리 해줘야함
        for (SelectedNumber selectedNumber : all) {
            redissonTicketing(userEmail, balance, selectedNumber);
        }

        //구매가 진행되면 임시테이블 데이터 날리기
        deleteSelectedNumbers();
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


    private List<SelectedNumber> getSelectedNumbers() {
        return selectedNumberRepository.findAll();
    }

    private void deleteSelectedNumbers() {
        selectedNumberRepository.deleteAll();
    }
}
