package com.project.crawlerservice.batch.config;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public abstract class BaseBatch {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    public PlatformTransactionManager platformTransactionManager;

    public JobBuilder getJobBuilder(String name) {
        return new JobBuilder(name,jobRepository);
    }

    public StepBuilder getStepBuilder(String name) {
        return new StepBuilder(name,jobRepository);
    }

}