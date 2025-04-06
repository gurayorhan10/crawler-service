package com.project.crawlerservice.job.interest;

import com.project.crawlerservice.job.BaseJob;
import com.project.crawlerservice.job.interest.dto.InterestCalculationProcessorDTO;
import com.project.crawlerservice.job.interest.dto.InterestCalculationWriterDTO;
import com.project.crawlerservice.job.interest.step.InterestCalculationProcessor;
import com.project.crawlerservice.job.interest.step.InterestCalculationWriter;
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
public class InterestCalculationJob extends BaseJob {

    @Bean(name = "interestCalculation")
    public Job interestCalculationJob(DataSource dataSource) {
        setDataSource(dataSource);
        return getJobBuilder("interestCalculationJob")
                .incrementer(new RunIdIncrementer())
                .start(interestCalculationStep(threadPoolTaskExecutor()))
                .build();
    }

    public Step interestCalculationStep(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return getStepBuilder("interestCalculationStep")
                .<InterestCalculationProcessorDTO, InterestCalculationWriterDTO>chunk(100,platformTransactionManager)
                .reader(interestDTOSynchronizedItemStreamReader())
                .processor(interestCalculationProcessor())
                .writer(interestCalculationWriter())
                .taskExecutor(threadPoolTaskExecutor)
                .stepOperations(taskExecutorRepeatTemplate(threadPoolTaskExecutor))
                .allowStartIfComplete(true)
                .build();
    }

    public SynchronizedItemStreamReader<InterestCalculationProcessorDTO> interestDTOSynchronizedItemStreamReader(){
        JdbcCursorItemReaderBuilder<InterestCalculationProcessorDTO> interestDTOJdbcCursorItemReaderBuilder = new JdbcCursorItemReaderBuilder<>();
        interestDTOJdbcCursorItemReaderBuilder.fetchSize(1000);
        interestDTOJdbcCursorItemReaderBuilder.saveState(false);
        interestDTOJdbcCursorItemReaderBuilder.dataSource(getDataSource());
        interestDTOJdbcCursorItemReaderBuilder.sql("SELECT DISTINCT(I.USERNAME) FROM INTEREST I WHERE DATEDIFF(I.FUTURE_CALCULATION_DATE,CURDATE()) <= 0 AND DATEDIFF(I.CALCULATION_DATE,I.FUTURE_CALCULATION_DATE) != 0");
        interestDTOJdbcCursorItemReaderBuilder.rowMapper(new BeanPropertyRowMapper<>(InterestCalculationProcessorDTO.class));
        SynchronizedItemStreamReader<InterestCalculationProcessorDTO> interestDTOSynchronizedItemStreamReader = new SynchronizedItemStreamReader<>();
        interestDTOSynchronizedItemStreamReader.setDelegate(interestDTOJdbcCursorItemReaderBuilder.build());
        return interestDTOSynchronizedItemStreamReader;
    }

    @Bean
    public InterestCalculationProcessor interestCalculationProcessor(){
        return new InterestCalculationProcessor();
    }

    @Bean
    public InterestCalculationWriter interestCalculationWriter(){
        return new InterestCalculationWriter();
    }

}
