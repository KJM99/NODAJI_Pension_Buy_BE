package com.example.pensionBuying.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @AllArgsConstructor
@NoArgsConstructor @Builder
@Table(name = "SELECTED_NUMBER")
public class SelectedNumber {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SELECTED_NUMBER_ID")
    private Long selectedNumberId;

    @Column(name = "USER_ID")
    private UUID userId;

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

}
