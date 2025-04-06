package com.project.crawlerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.enums.Type;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDTO {
    @JsonProperty
    private String code;
    @JsonProperty
    private String name;
    @JsonProperty
    private Type type;
    @JsonProperty
    private BigDecimal value;
    @JsonProperty
    private BigDecimal dailyValue;
    @JsonProperty
    private Currency currency;
    @JsonProperty
    private Boolean active;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;
}

