package com.project.crawlerservice.entity;

import com.project.crawlerservice.entity.embeddable.DataEmbeddableId;
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
@Table(name = "DATA")
public class DataEntity {
    @Id
    private DataEmbeddableId dataEmbeddableId;
    @Column(name = "NAME", nullable = false, length = 250)
    private String name;
    @Column(name = "VALUE", nullable = false, precision = 12, scale = 5)
    private BigDecimal value;
    @Column(name = "DAILY_VALUE", nullable = false, precision = 12, scale = 5)
    private BigDecimal dailyValue;
    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false)
    private Currency currency;
    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;
    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    private Date lastUpdateDate;
}
