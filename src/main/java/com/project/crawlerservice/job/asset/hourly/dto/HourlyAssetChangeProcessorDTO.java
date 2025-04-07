package com.project.crawlerservice.job.asset.hourly.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HourlyAssetChangeProcessorDTO {
    private String username;
    private String mail;
}
