package com.project.crawlerservice.batch.fund;

import com.project.crawlerservice.batch.config.BaseBatch;
import com.project.crawlerservice.batch.fund.tasklet.FundTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class FundJob extends BaseBatch {

    @Bean(name = "fund")
    public Job coinJob() {
        return getJobBuilder("fundJob")
                .incrementer(new RunIdIncrementer())
                .start(fundStep())
                .build();
    }

    public Step fundStep() {
        return getStepBuilder("fundStep")
                .tasklet(fundTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public FundTasklet fundTasklet(){
        return new FundTasklet();
    }

}
