package com.project.crawlerservice.job.mine;

import com.project.crawlerservice.job.BaseJob;
import com.project.crawlerservice.job.mine.tasklet.MineTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class MineJob extends BaseJob {

    @Bean(name = "mine")
    public Job coinJob() {
        return getJobBuilder("mineJob")
                .incrementer(new RunIdIncrementer())
                .start(mineStep())
                .build();
    }

    public Step mineStep() {
        return getStepBuilder("mineStep")
                .tasklet(mineTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public MineTasklet mineTasklet(){
        return new MineTasklet();
    }

}
