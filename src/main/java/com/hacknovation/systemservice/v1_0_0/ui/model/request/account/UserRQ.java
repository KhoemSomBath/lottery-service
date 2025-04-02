package com.hacknovation.systemservice.v1_0_0.ui.model.request.account;

import com.hacknovation.systemservice.constant.UserConstant;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserRQ {
    private String code;
    @NotEmpty(message = "Please provide a username")
    private String username;
    @NotEmpty(message = "Please provide a password")
    private List<String> lotteryType;
    private String platformType;
    private String password;
    private String nickname;
    private String roleCode;
    private String userType;
    private String superSeniorCode;
    private String seniorCode;
    private String masterCode;
    private String agentCode;
    private String phoneCode;
    private String phoneNumber;
    private String email;
    private String address;
    private String currency;
    private String languageCode;
    private String gender;
    private Integer parentId;
    private String createdBy;
    private String updatedBy;
    private Boolean isLocked = Boolean.TRUE;
    private Boolean isOnline = Boolean.FALSE;
    private String status = UserConstant.ACTIVATE;
}
