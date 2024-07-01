package com.example.pensionBuying.service;

import com.example.pensionBuying.domain.repository.SelectedNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PensionRoundSchedulerServiceImpl implements PensionRoundSchedulerService {

    public static Integer round = 1;
    public final SelectedNumberRepository selectedNumberRepository;

    // @Scheduled(cron = "0 5 10 ? * THU")
    @Scheduled(cron = "0 */20 * * * *")
    public void pensionRoundScheduler() {
        selectedNumberRepository.deleteAll();
        round += 1;
    }

    // 주석

    @Override
    public Integer getRound() {
        return round;
    }
}
