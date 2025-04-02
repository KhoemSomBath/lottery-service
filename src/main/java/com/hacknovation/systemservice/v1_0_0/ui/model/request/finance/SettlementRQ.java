package com.hacknovation.systemservice.v1_0_0.ui.model.request.finance;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * author : phokkinnky
 * date : 6/22/21
 */
@Data
public class SettlementRQ {
    private String remark;
    @NotNull
    private MultipartFile thumbnail;
}
