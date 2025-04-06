package com.project.crawlerservice.job.asset;

import com.project.crawlerservice.job.BaseJob;
import com.project.crawlerservice.job.asset.dto.DailyAssetChangeProcessorDTO;
import com.project.crawlerservice.job.asset.dto.DailyAssetChangeWriterDTO;
import com.project.crawlerservice.job.asset.step.DailyAssetChangeProcessor;
import com.project.crawlerservice.job.asset.step.DailyAssetChangeWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;


@Lazy
@Configuration
public class DailyAssetChangeJob extends BaseJob {

    @Bean(name = "dailyAssetChange")
    public Job dailyAssetChangeJob(DataSource dataSource) {
        setDataSource(dataSource);
        return getJobBuilder("dailyAssetChangeJob")
                .incrementer(new RunIdIncrementer())
                .start(dailyAssetChangeStep(threadPoolTaskExecutor()))
                .build();
    }

    public Step dailyAssetChangeStep(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return getStepBuilder("dailyAssetChangeStep")
                .<DailyAssetChangeProcessorDTO, DailyAssetChangeWriterDTO>chunk(100,platformTransactionManager)
                .reader(dailyAssetChangeProcessorDTOSynchronizedItemStreamReader())
                .processor(dailyAssetChangeProcessor())
                .writer(dailyAssetChangeWriter())
                .taskExecutor(threadPoolTaskExecutor)
                .stepOperations(taskExecutorRepeatTemplate(threadPoolTaskExecutor))
                .allowStartIfComplete(true)
                .build();
    }

    public SynchronizedItemStreamReader<DailyAssetChangeProcessorDTO> dailyAssetChangeProcessorDTOSynchronizedItemStreamReader(){
        JdbcCursorItemReaderBuilder<DailyAssetChangeProcessorDTO> dailyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder = new JdbcCursorItemReaderBuilder<>();
        dailyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.fetchSize(1000);
        dailyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.saveState(false);
        dailyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.dataSource(getDataSource());
        dailyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.sql("SELECT U.USERNAME, U.MAIL FROM USER U WHERE U.ACTIVE = 1 AND (SELECT 1 FROM ASSET A WHERE A.USERNAME = U.USERNAME) = 1;");
        dailyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.rowMapper(new BeanPropertyRowMapper<>(DailyAssetChangeProcessorDTO.class));
        SynchronizedItemStreamReader<DailyAssetChangeProcessorDTO> dailyAssetChangeProcessorDTOSynchronizedItemStreamReader = new SynchronizedItemStreamReader<>();
        dailyAssetChangeProcessorDTOSynchronizedItemStreamReader.setDelegate(dailyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.build());
        return dailyAssetChangeProcessorDTOSynchronizedItemStreamReader;
    }

    @Bean
    public DailyAssetChangeProcessor dailyAssetChangeProcessor(){
        return new DailyAssetChangeProcessor();
    }

    @Bean
    public DailyAssetChangeWriter dailyAssetChangeWriter(){
        return new DailyAssetChangeWriter();
    }

}
