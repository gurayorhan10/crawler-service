package com.project.crawlerservice.batch;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class JobStarter {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    void init(){
        run("coin");
        run("fund");
        run("mine");
        run("stock");
    }

    @Async("threadPoolTaskExecutor")
    public void run(String name){
        try {
            log.info("Batch " + name + " is starting...");
            JobExecution jobExecution = jobLauncher.run(applicationContext.getBean(name, Job.class), new JobParameters());
            log.info("Batch " + name + " is ended. Id:" + jobExecution.getJobId());
        } catch (Exception e) {
            log.warn(name + "Job execution failed: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void coinScheduled(){
        run("coin");
    }

    @Scheduled(cron = "0 */5 9-18 * * 1-5")
    public void stockScheduled(){
        run("stock");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void mineScheduled(){
        run("mine");
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void fundScheduled(){
        run("fund");
    }

}