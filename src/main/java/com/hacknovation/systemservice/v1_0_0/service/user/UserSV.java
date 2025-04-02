package com.hacknovation.systemservice.v1_0_0.service.user;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.account.UserRS;
import org.springframework.stereotype.Service;

@Service
public interface UserSV {

    StructureRS userListing();

    StructureRS userReferrals();

    StructureRS userProfile(String userCode);

    StructureRS lotteryType(String userCode);

    UserRS prepareUser(String userCode);

    StructureRS createUserLevel(CreateUserRQ createUserRQ);

    StructureRS cloneUserLevel(CloneUserRQ cloneUserRQ);

    StructureRS createSuperSenior(CreateEmployeeRQ employeeRQ);

    StructureRS createSubAccount(CreateEmployeeRQ employeeRQ);

    StructureRS createEmployee(CreateEmployeeRQ employeeRQ);

    StructureRS employeeListing();

    StructureRS update(String userCode, UpdateUserRQ request);

    StructureRS changePassword(String userCode, ChangeUserPasswordRQ request);

    StructureRS checkUsername(String username);

    StructureRS listLimitBet(String userCode);

    StructureRS updateLimitBet(UpdateLimitBetRQ updateLimitBetRQ);

}
