package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import com.hacknovation.systemservice.enums.UserTypeEnum;
import com.hacknovation.systemservice.exception.anotation.FieldMatch;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.CommissionLottery;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@FieldMatch(first = "password", second = "confirmPassword")
public class CreateUserRQ {
    private Long userId;

    private String username;

    @NotBlank(message = "Please provide a password")
    private String password;

    @NotBlank(message = "Please provide a confirm password")
    private String confirmPassword;
    private String levelUnderUserCode;
    private String roleCode;
    private String phoneCode;
    private String phoneNumber;
    private String nickname;
    private String platformType;
    private UserTypeEnum userType;
    private String email;
    private String createdBy;
    private String updatedBy;
    private String ignoreAt;
    private Integer generateCount;
    private List<String> lotteryType;
    private CommissionLottery commissionLottery;
    private List<CommissionLotteryRQ> commissions;

    public String getUsername() {
        if (username != null) {
            return username.toUpperCase();
        }
        return null;
    }
}
