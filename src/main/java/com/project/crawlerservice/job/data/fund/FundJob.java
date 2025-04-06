package com.project.crawlerservice.job.data.fund;

import com.project.crawlerservice.job.BaseJob;
import com.project.crawlerservice.job.data.fund.tasklet.FundTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class FundJob extends BaseJob {

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
