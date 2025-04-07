package com.project.crawlerservice.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CoinMarketDTO {
    private String id;
    private String symbol;
    private String name;
    private String image;
    private BigDecimal current_price;
    private double market_cap;
    private int market_cap_rank;
    private double price_change_percentage_24h;
}
