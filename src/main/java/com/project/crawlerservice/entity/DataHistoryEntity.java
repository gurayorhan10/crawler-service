package com.project.crawlerservice.entity;

import com.project.crawlerservice.entity.enums.Type;
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
@Table(name = "DATA_HISTORY", indexes = {
        @Index(name = "CODE_AND_TYPE_INDEX", columnList = "CODE, TYPE")
})
public class DataHistoryEntity {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CODE", nullable = false, length =  20)
    private String code;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length =  10)
    private Type type;
    @Column(name = "VALUE", nullable = false, precision = 12, scale = 5)
    private BigDecimal value;
    @Column(name = "INSERT_DATE", nullable = false)
    private Date insertDate;
}
