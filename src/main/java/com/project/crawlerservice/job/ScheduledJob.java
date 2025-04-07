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

    @Scheduled(cron = "0 */2 * * * *")
    public void coinScheduled(){
        job.run("coin");
    }

    @Scheduled(cron = "0 50 9 * * 1-5")
    @Scheduled(cron = "0 */5 10-18 * * 1-5")
    @Scheduled(cron = "0 0-30/5 18 * * 1-5")
    public void stockScheduled(){
        job.run("stock");
    }

    @Scheduled(cron = "0 */5 * * * 1-5")
    public void mineScheduled(){
        job.run("mine");
    }

    @Scheduled(cron = "0 0 7 * * 1-5")
    public void fundScheduled(){
        job.run("fund");
    }

    @Scheduled(cron = "0 */5 * * * 1-5")
    public void exchangeRateScheduled(){
        job.run("exchangeRate");
    }

}