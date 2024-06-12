package com.example.pensionBuying.service;

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
    public void purchaseTicket(Integer round, UUID userId, String userEmail, Long balance) {
        List<SelectedNumber> all = getSelectedNumbers();

        if (all.isEmpty()) {
            throw new RuntimeException("No selected numbers found");
        }

        if((all.size()*1000L) > balance) {
            throw new RuntimeException("Not enough balance");
        }

        //Todo: 이미 구매한 티켓 처리 해줘야함
        for (SelectedNumber selectedNumber : all) {
            PurchasedTickets ticket =
                PurchasedTickets.builder()
                    .round(round)
                    .userId(userId)
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

        //구매가 진행되면 임시테이블 데이터 날리기
        deleteSelectedNumbers();
    }

    private List<SelectedNumber> getSelectedNumbers() {
        return selectedNumberRepository.findAll();
    }

    private void deleteSelectedNumbers() {
        selectedNumberRepository.deleteAll();
    }
}
