package com.example.pensionBuying.domain.repository;

import com.example.pensionBuying.domain.entity.PurchasedTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchasedTicketsRepository extends JpaRepository<PurchasedTickets, Long> {


    @Query("select p from PurchasedTickets p "
        + "where p.round = :round "
        + "and p.groupNum = :groupNum "
        + "and p.first = :first "
        + "and p.second = :second "
        + "and p.third = :third "
        + "and p.fourth = :fourth "
        + "and p.fifth = :fifth "
        + "and p.sixth = :sixth"
    )
    PurchasedTickets findByTicket(
        Integer round, Integer groupNum, Integer first, Integer second, Integer third,
        Integer fourth, Integer fifth, Integer sixth);
}
