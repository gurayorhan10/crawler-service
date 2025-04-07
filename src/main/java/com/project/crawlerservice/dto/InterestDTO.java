package com.project.crawlerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.crawlerservice.enums.Currency;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterestDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @JsonIgnore
    private String username;
    @Max(Long.MAX_VALUE)
    @JsonProperty
    @NotNull
    @Min(1)
    private BigDecimal piece;
    @JsonProperty
    @NotNull
    private Currency currency;
    @JsonProperty
    @NotNull
    @Max(100)
    @Min(0)
    private BigDecimal ratio;
    @JsonProperty
    @NotNull
    @Max(100)
    @Min(0)
    private BigDecimal taxRatio;
    @JsonIgnore
    private BigDecimal exchangeRate;
    @JsonProperty
    @NotNull
    @Max(10000)
    @Min(1)
    private Integer day;
    @JsonProperty
    @NotNull
    private Boolean again;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date calculationDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date futureCalculationDate;
}
