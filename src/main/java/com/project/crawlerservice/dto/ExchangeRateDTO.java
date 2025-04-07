package com.project.crawlerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.crawlerservice.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateDTO {
    @JsonProperty
    private Currency currency;
    @JsonProperty
    private String name;
    @JsonProperty
    private BigDecimal buy;
    @JsonProperty
    private BigDecimal sell;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss")
    private Date lastUpdateDate;
}
