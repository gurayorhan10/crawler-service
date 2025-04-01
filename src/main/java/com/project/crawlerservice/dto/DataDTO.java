package com.project.crawlerservice.dto;

import com.project.crawlerservice.entity.enums.Currency;
import com.project.crawlerservice.entity.enums.Type;
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
public class DataDTO {
    private String code;
    private String name;
    private Type type;
    private BigDecimal value;
    private Currency currency;
    private Boolean active;
    private Date lastUpdateDate;
}
