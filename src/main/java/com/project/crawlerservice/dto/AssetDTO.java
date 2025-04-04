package com.project.crawlerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.enums.Type;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class AssetDTO {
    @JsonIgnore
    private String username;
    @NotNull
    @JsonProperty
    private String code;
    @NotNull
    @JsonProperty
    private Type type;
    @NotNull
    @JsonProperty
    @Min(0)
    @Max(Long.MAX_VALUE)
    private BigDecimal piece;
    @NotNull
    @JsonProperty
    @Min(0)
    @Max(Long.MAX_VALUE)
    private BigDecimal average;
    @NotNull
    @JsonProperty
    private Currency currency;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdateDate;
}
