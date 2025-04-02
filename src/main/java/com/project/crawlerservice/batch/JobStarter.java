package com.project.crawlerservice.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class JobStarter {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ApplicationContext applicationContext;

    public void run(String name){
        try {
            log.info("Batch " + name + " is starting...");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("uuid", UUID.randomUUID().toString())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(applicationContext.getBean(name, Job.class), jobParameters);
            log.info("Batch " + name + " is ended. Id:" + jobExecution.getJobId());
        } catch (Exception e) {
            log.warn(name + "Job execution failed: " + e.getMessage());
        }
    }

}