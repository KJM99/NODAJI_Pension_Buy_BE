package com.example.pensionBuying.domain.repository;

import com.example.pensionBuying.domain.entity.SelectedNumber;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SelectedNumberRepository extends JpaRepository<SelectedNumber, Long> {

    @Query("select s from SelectedNumber s where s.userId = :uuid "
        + "and s.round = :round "
        + "and s.groupNum = :group "
        + "and s.first = :first "
        + "and s.second = :second "
        + "and s.third = :third "
        + "and s.fourth = :fourth "
        + "and s.fifth = :fifth "
        + "and s.sixth = :sixth")
    List<SelectedNumber> findBySelectedNumber(
        String uuid, Integer round, Integer group, Integer first, Integer second, Integer third,
        Integer fourth, Integer fifth, Integer sixth
    );

    List<SelectedNumber> findByUserId(String userId);
}
