package com.project.crawlerservice.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledJob {

    @Autowired
    private StarterJob job;

    @Scheduled(cron = "0 5 0 * * *")
    public void interestCalculationScheduled(){
        job.run("interestCalculation");
    }

    @Scheduled(cron = "0 15 0 * * *")
    public void dailyAssetChangeScheduled(){
        job.run("dailyAssetChange");
    }

    @Scheduled(cron = "0 0 1-23 * * *")
    public void hourlyAssetChangeScheduled(){
        job.run("hourlyAssetChange");
    }

}