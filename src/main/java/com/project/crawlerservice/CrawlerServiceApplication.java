package com.project.crawlerservice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Objects;

@Slf4j
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "com.project")
public class CrawlerServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(CrawlerServiceApplication.class, args);
		int responseCode = 0;
		if(args.length > 0 && args[0].startsWith("batch=")){
			try {
				String batchBeanName = args[0].replace("batch=","");
				JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
				JobExecution jobExecution = jobLauncher.run(applicationContext.getBean(batchBeanName, Job.class), new JobParametersBuilder().toJobParameters());
				responseCode = jobExecution.getExitStatus().equals(ExitStatus.COMPLETED) ? 1 : 0;
			}catch (Exception e){
				log.error("Job failed. Error: " + e.getLocalizedMessage());
				responseCode = 1;
			}finally {
				MDC.clear();
				if(Objects.nonNull(applicationContext)){
					applicationContext.close();
				}
				System.exit(responseCode);
			}
		}
	}

}
