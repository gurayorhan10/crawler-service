package com.project.crawlerservice.entity;

import com.project.crawlerservice.entity.embeddable.AssetEmbeddableId;
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
@Table(name = "ASSET")
public class AssetEntity {
    @Id
    private AssetEmbeddableId assetEmbeddableId;
    @Column(name = "PIECE", nullable = false, precision = 15, scale = 2)
    private BigDecimal piece;
    @Column(name = "AVERAGE", nullable = false, precision = 15, scale = 2)
    private BigDecimal average;
    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false)
    private Currency currency;
    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    private Date lastUpdateDate;
}
