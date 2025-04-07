package com.project.crawlerservice.job.asset.hourly.step;

import com.project.crawlerservice.job.asset.hourly.dto.HourlyAssetChangeWriterDTO;
import com.project.crawlerservice.rabbit.producer.EmailSendProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class HourlyAssetChangeWriter implements ItemWriter<HourlyAssetChangeWriterDTO> {

    @Autowired
    private EmailSendProducer emailSendProducer;

    @Override
    public void write(Chunk<? extends HourlyAssetChangeWriterDTO> chunk) {
        chunk.forEach(f -> emailSendProducer.sendMessage(f.getEmailSendMessage()));
    }

}
