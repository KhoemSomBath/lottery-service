package com.hacknovation.systemservice.v1_0_0.ui.model.response.dashboard;

import lombok.Data;

@Data
public class UserSummaryRS {
    private String roleName;
    private Integer sortOrder;
    private Integer total;
}
