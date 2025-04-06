package com.project.crawlerservice.job.asset.step;

import com.project.crawlerservice.job.asset.dto.DailyAssetChangeWriterDTO;
import com.project.crawlerservice.rabbit.producer.EmailSendProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DailyAssetChangeWriter implements ItemWriter<DailyAssetChangeWriterDTO> {

    @Autowired
    private EmailSendProducer emailSendProducer;

    @Override
    public void write(Chunk<? extends DailyAssetChangeWriterDTO> chunk) {
        chunk.forEach(f -> emailSendProducer.sendMessage(f.getEmailSendMessage()));
    }

}
