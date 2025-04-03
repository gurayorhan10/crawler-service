package com.project.crawlerservice.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobScheduled {

    @Autowired
    private JobStarter job;

    @Scheduled(cron = "0 */1 * * * *")
    public void coinScheduled(){
        job.run("coin");
    }

    @Scheduled(cron = "0 */5 9-18 * * 1-5")
    public void stockScheduled(){
        job.run("stock");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void mineScheduled(){
        job.run("mine");
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void fundScheduled(){
        job.run("fund");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void exchangeRateScheduled(){
        job.run("exchangeRate");
    }

}