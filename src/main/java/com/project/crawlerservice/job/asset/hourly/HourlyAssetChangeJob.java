package com.project.crawlerservice.job.asset.hourly;

import com.project.crawlerservice.job.BaseJob;
import com.project.crawlerservice.job.asset.daily.dto.DailyAssetChangeProcessorDTO;
import com.project.crawlerservice.job.asset.daily.dto.DailyAssetChangeWriterDTO;
import com.project.crawlerservice.job.asset.daily.step.DailyAssetChangeProcessor;
import com.project.crawlerservice.job.asset.daily.step.DailyAssetChangeWriter;
import com.project.crawlerservice.job.asset.hourly.dto.HourlyAssetChangeProcessorDTO;
import com.project.crawlerservice.job.asset.hourly.dto.HourlyAssetChangeWriterDTO;
import com.project.crawlerservice.job.asset.hourly.step.HourlyAssetChangeProcessor;
import com.project.crawlerservice.job.asset.hourly.step.HourlyAssetChangeWriter;
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
public class HourlyAssetChangeJob extends BaseJob {

    @Bean(name = "hourlyAssetChange")
    public Job hourlyAssetChangeJob(DataSource dataSource) {
        setDataSource(dataSource);
        return getJobBuilder("hourlyAssetChangeJob")
                .incrementer(new RunIdIncrementer())
                .start(hourlyAssetChangeStep(threadPoolTaskExecutor()))
                .build();
    }

    public Step hourlyAssetChangeStep(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return getStepBuilder("hourlyAssetChangeStep")
                .<HourlyAssetChangeProcessorDTO, HourlyAssetChangeWriterDTO>chunk(100,platformTransactionManager)
                .reader(hourlyAssetChangeProcessorDTOSynchronizedItemStreamReader())
                .processor(hourlyAssetChangeProcessor())
                .writer(hourlyAssetChangeWriter())
                .taskExecutor(threadPoolTaskExecutor)
                .stepOperations(taskExecutorRepeatTemplate(threadPoolTaskExecutor))
                .allowStartIfComplete(true)
                .build();
    }

    public SynchronizedItemStreamReader<HourlyAssetChangeProcessorDTO> hourlyAssetChangeProcessorDTOSynchronizedItemStreamReader(){
        JdbcCursorItemReaderBuilder<HourlyAssetChangeProcessorDTO> hourlyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder = new JdbcCursorItemReaderBuilder<>();
        hourlyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.fetchSize(1000);
        hourlyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.saveState(false);
        hourlyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.dataSource(getDataSource());
        hourlyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.sql("SELECT U.USERNAME, U.MAIL FROM USER U WHERE U.ACTIVE = 1 AND (SELECT COUNT(*) FROM NOTIFICATION N WHERE U.USERNAME = N.USERNAME AND N.TYPE = 'HOURLY') AND (SELECT COUNT(*) FROM ASSET A WHERE A.USERNAME = U.USERNAME) > 0;");
        hourlyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.rowMapper(new BeanPropertyRowMapper<>(HourlyAssetChangeProcessorDTO.class));
        SynchronizedItemStreamReader<HourlyAssetChangeProcessorDTO> hourlyAssetChangeProcessorDTOSynchronizedItemStreamReader = new SynchronizedItemStreamReader<>();
        hourlyAssetChangeProcessorDTOSynchronizedItemStreamReader.setDelegate(hourlyAssetChangeProcessorDTOJdbcCursorItemReaderBuilder.build());
        return hourlyAssetChangeProcessorDTOSynchronizedItemStreamReader;
    }

    @Bean
    public HourlyAssetChangeProcessor hourlyAssetChangeProcessor(){
        return new HourlyAssetChangeProcessor();
    }

    @Bean
    public HourlyAssetChangeWriter hourlyAssetChangeWriter(){
        return new HourlyAssetChangeWriter();
    }

}
