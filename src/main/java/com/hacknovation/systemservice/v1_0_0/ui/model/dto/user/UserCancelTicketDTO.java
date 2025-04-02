package com.hacknovation.systemservice.v1_0_0.ui.model.dto.user;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 04/03/2023
 * time: 13:25
 */
@Data
public class UserCancelTicketDTO {
    private String memberCode;
    private String username;
    private String superSeniorCode;
}
