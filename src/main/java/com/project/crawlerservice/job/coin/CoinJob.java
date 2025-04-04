package com.project.crawlerservice.job.coin;

import com.project.crawlerservice.job.coin.tasklet.CoinTasklet;
import com.project.crawlerservice.job.BaseJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class CoinJob extends BaseJob {

    @Bean(name = "coin")
    public Job coinJob() {
        return getJobBuilder("coinJob")
                .incrementer(new RunIdIncrementer())
                .start(coinStep())
                .build();
    }

    public Step coinStep() {
        return getStepBuilder("coinStep")
                .tasklet(coinTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public CoinTasklet coinTasklet(){
        return new CoinTasklet();
    }

}
