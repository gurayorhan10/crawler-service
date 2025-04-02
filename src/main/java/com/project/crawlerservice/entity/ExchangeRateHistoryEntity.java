package com.project.crawlerservice.entity;

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
@Table(name = "EXCHANGE_RATE_HISTORY")
public class ExchangeRateHistoryEntity {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false)
    private Currency currency;
    @Column(name = "BUY", nullable = false, precision = 12, scale = 5)
    private BigDecimal buy;
    @Column(name = "SELL", nullable = false, precision = 12, scale = 5)
    private BigDecimal sell;
    @Column(name = "INSERT_DATE", nullable = false)
    private Date insertDate;
}
