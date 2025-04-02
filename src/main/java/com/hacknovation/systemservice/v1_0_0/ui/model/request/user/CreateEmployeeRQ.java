package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import com.hacknovation.systemservice.enums.UserTypeEnum;
import com.hacknovation.systemservice.exception.anotation.FieldMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@FieldMatch(first = "password", second = "confirmPassword")
public class CreateEmployeeRQ {
    private Long userId;

    @NotBlank(message = "Please provide a username")
    private String username;

    @NotBlank(message = "Please provide a password")
    private String password;

    @NotBlank(message = "Please provide a confirm password")
    private String confirmPassword;
    private List<String> lotteryType;
    private String platformType;
    private String roleCode;
    private String nickname;
    private String currencyCode;
    private UserTypeEnum userType;
    private String createdBy;
    private String updatedBy;
    private Integer parentId;
    private Boolean isOnline = Boolean.FALSE;
    private List<CommissionLotteryRQ> commissions;

    public String getUsername() {
        if (username != null)
            return username.toUpperCase();
        return null;
    }
}
