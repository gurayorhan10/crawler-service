package com.project.crawlerservice.dto;

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
public class ExchangeRateDTO {
    private Currency currency;
    private String name;
    private BigDecimal buy;
    private BigDecimal sell;
    private Date lastUpdateDate;
}
