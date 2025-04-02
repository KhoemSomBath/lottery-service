package com.hacknovation.systemservice.v1_0_0.ui.model.request.settlement;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author KHOEM Sombath
 * Date: 6/8/2021
 * Time: 10:43 AM
 */

@Data
public class SettlementRQ {
    private Long id;
    private String userCode;
    private String remark;
    private String issuedAt;
    private String status;
    private MultipartFile thumbnail;
//    private List<SettlementsItemsRQ> items;
}
