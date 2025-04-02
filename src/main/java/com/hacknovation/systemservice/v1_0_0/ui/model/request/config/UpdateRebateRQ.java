package com.hacknovation.systemservice.v1_0_0.ui.model.request.config;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * author : phokkinnky
 * date : 7/7/21
 */
@Data
public class UpdateRebateRQ {
    @NotEmpty(message = "Please provide user code")
    @NotNull(message = "Please provide user code")
    private String userCode;
    private BigDecimal twoD;
    private BigDecimal threeD;
    private BigDecimal fourD;
    private BigDecimal fiveD;
    private BigDecimal sixD;

    public BigDecimal getTwoD() {
        if (this.twoD == null)
            return BigDecimal.ZERO;
        return twoD;
    }

    public BigDecimal getThreeD() {
        if (this.threeD == null)
            return BigDecimal.ZERO;
        return threeD;
    }

    public BigDecimal getFourD() {
        if (this.fourD == null)
            return BigDecimal.ZERO;
        return fourD;
    }

    public BigDecimal getFiveD() {
        if (this.fiveD == null)
            return BigDecimal.ZERO;
        return fiveD;
    }

    public BigDecimal getSixD() {
        if (this.sixD == null)
            return BigDecimal.ZERO;
        return sixD;
    }
}
