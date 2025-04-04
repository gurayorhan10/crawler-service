package com.project.crawlerservice.job;

import lombok.Getter;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.support.TaskExecutorRepeatTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public abstract class BaseJob {

    @Getter
    private DataSource dataSource;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    public PlatformTransactionManager platformTransactionManager;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Bean
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(5);
        threadPoolTaskExecutor.setKeepAliveSeconds(300);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    public TaskExecutorRepeatTemplate taskExecutorRepeatTemplate(ThreadPoolTaskExecutor threadPoolTaskExecutor){
        TaskExecutorRepeatTemplate taskExecutorRepeatTemplate = new TaskExecutorRepeatTemplate();
        taskExecutorRepeatTemplate.setTaskExecutor(threadPoolTaskExecutor);
        return taskExecutorRepeatTemplate;
    }

    public JobBuilder getJobBuilder(String name) {
        return new JobBuilder(name,jobRepository);
    }

    public StepBuilder getStepBuilder(String name) {
        return new StepBuilder(name,jobRepository);
    }

}