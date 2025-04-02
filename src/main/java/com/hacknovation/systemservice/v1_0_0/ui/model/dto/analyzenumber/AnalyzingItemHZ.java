package com.hacknovation.systemservice.v1_0_0.ui.model.dto.analyzenumber;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/*
* author: kangto
* createdAt: 12/09/2022
* time: 16:19
*/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyzingItemHZ implements Serializable {

    private static final long serialVersionUID = 2L;

    private String postCode;
    private String postGroup;
    private String rebateCode;
    private Boolean isLo = false;
    private Boolean isRange = false;
    private String number;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal commissionAmount = BigDecimal.ZERO;
    private BigDecimal prizeAmount = BigDecimal.ZERO;

    private BigDecimal superCommissionAmount = BigDecimal.ZERO;
    private BigDecimal superPrizeAmount = BigDecimal.ZERO;

    private BigDecimal masterCommissionAmount = BigDecimal.ZERO;
    private BigDecimal masterPrizeAmount = BigDecimal.ZERO;

}
