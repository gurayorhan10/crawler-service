package com.project.crawlerservice.dto;

import com.project.crawlerservice.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AssetDataDTO {
    private String name;
    private BigDecimal piece;
    private BigDecimal average;
    private Currency assetCurrency;
    private BigDecimal dailyValue;
    private Currency dataCurrency;
}
