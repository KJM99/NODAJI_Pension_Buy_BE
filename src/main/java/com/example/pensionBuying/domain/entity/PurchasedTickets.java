package com.example.pensionBuying.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "PURCHASED_TICKETS")
public class PurchasedTickets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PURCHASED_TICKET_ID")
    private Long purchasedTicketId;

    @Column(name = "ROUND")
    private Integer round;

    @Column(name = "USER_ID")
    private String userId;

    // @Column(name = "USER_EMAIL")
    // private String userEmail;
    //
    // @Column(name = "USER_ACC_BALANCE")
    // private Long userAccBalance;

    @Column(name = "GROUP_NUM")
    private Integer groupNum;

    @Column(name = "FIRST")
    private Integer first;

    @Column(name = "SECOND")
    private Integer second;

    @Column(name = "THIRD")
    private Integer third;

    @Column(name = "FOURTH")
    private Integer fourth;

    @Column(name = "FIFTH")
    private Integer fifth;

    @Column(name = "SIXTH")
    private Integer sixth;

    @Column(name = "CREATE_AT")
    private LocalDate createAt;

}
