package com.project.crawlerservice.job.exchangerate;

import com.project.crawlerservice.job.BaseJob;
import com.project.crawlerservice.job.exchangerate.tasklet.ExchangeRateTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class ExchangeRateJob extends BaseJob {

    @Bean(name = "exchangeRate")
    public Job exchangeRateJob() {
        return getJobBuilder("exchangeRateJob")
                .incrementer(new RunIdIncrementer())
                .start(exchangeRateStep())
                .build();
    }

    public Step exchangeRateStep() {
        return getStepBuilder("exchangeRateStep")
                .tasklet(exchangeRateTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public ExchangeRateTasklet exchangeRateTasklet(){
        return new ExchangeRateTasklet();
    }

}
