package com.project.crawlerservice.entity;

import com.project.crawlerservice.entity.embeddables.DataEmbeddableId;
import com.project.crawlerservice.entity.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EXCHANGE_RATE")
public class ExchangeRateEntity {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false)
    private Currency currency;
    @Column(name = "NAME", nullable = false, length = 250)
    private String name;
    @Column(name = "BUY", nullable = false, precision = 5, scale = 5)
    private BigDecimal buy;
    @Column(name = "SELL", nullable = false, precision = 5, scale = 5)
    private BigDecimal sell;
    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    private Date lastUpdateDate;
}
