package com.example.pensionBuying.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PensionRoundSchedulerServiceImpl implements PensionRoundSchedulerService {

    public static Integer round = 1;

    // @Scheduled(cron = "0 5 10 ? * THU")
    @Scheduled(cron = "0 */3 * * * *")
    public void pensionRoundScheduler() {
        round += 1;
    }

    @Override
    public Integer getRound() {
        return round;
    }
}
