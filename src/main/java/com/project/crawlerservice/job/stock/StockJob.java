package com.project.crawlerservice.job.stock;

import com.project.crawlerservice.job.BaseJob;
import com.project.crawlerservice.job.stock.tasklet.StockTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class StockJob extends BaseJob {

    @Bean(name = "stock")
    public Job coinJob() {
        return getJobBuilder("stockJob")
                .incrementer(new RunIdIncrementer())
                .start(stockStep())
                .build();
    }

    public Step stockStep() {
        return getStepBuilder("stockStep")
                .tasklet(stockTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public StockTasklet stockTasklet(){
        return new StockTasklet();
    }

}
