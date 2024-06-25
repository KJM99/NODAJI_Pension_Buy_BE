package com.example.pensionBuying.domain.dto.dto;


import com.example.pensionBuying.domain.entity.PurchasedTickets;
import com.example.pensionBuying.domain.entity.SelectedNumber;
import java.time.LocalDate;

public record PurchasedTicketDto(
    SelectedNumber selectedNumber

) {
    public PurchasedTickets toPurchasedTickets(SelectedNumber selectedNumber) {
        return PurchasedTickets.builder()
            .round(selectedNumber().getRound())
            .userId(selectedNumber().getUserId())
            .groupNum(selectedNumber().getGroupNum())
            .first(selectedNumber().getFirst())
            .second(selectedNumber().getSecond())
            .third(selectedNumber().getThird())
            .fourth(selectedNumber().getFourth())
            .fifth(selectedNumber().getFifth())
            .sixth(selectedNumber().getSixth())
            .createAt(LocalDate.now())
            .build();
    }
}
