package com.project.crawlerservice.entity;

import com.project.crawlerservice.enums.Currency;
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
@Table(name = "INTEREST")
public class InterestEntity {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String username;
    @Column(name = "PIECE", nullable = false, precision = 15, scale = 2)
    private BigDecimal piece;
    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false)
    private Currency currency;
    @Column(name = "RATIO", nullable = false, precision = 5, scale = 2)
    private BigDecimal ratio;
    @Column(name = "TAX_RATIO", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRatio;
    @Column(name = "EXCHANGE_RATE", nullable = false, precision = 12, scale = 5)
    private BigDecimal exchangeRate;
    @Column(name = "DAY", nullable = false, precision = 5, scale = 2)
    private Integer day;
    @Column(name = "AGAIN", nullable = false)
    private Boolean again;
    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;
    @Column(name = "CALCULATION_DATE", nullable = false)
    private Date calculationDate;
    @Column(name = "FUTURE_CALCULATION_DATE", nullable = false)
    private Date futureCalculationDate;
}
