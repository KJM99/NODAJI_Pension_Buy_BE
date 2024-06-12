package com.example.pensionBuying.domain.repository;

import com.example.pensionBuying.domain.entity.PurchasedTickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedTicketsRepository extends JpaRepository<PurchasedTickets, Long> {

}
