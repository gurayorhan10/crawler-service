package com.project.crawlerservice.job.asset.dto;

import com.project.crawlerservice.rabbit.data.EmailSendMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DailyAssetChangeWriterDTO {
    private EmailSendMessage emailSendMessage;
}
