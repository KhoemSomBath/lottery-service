package com.hacknovation.systemservice.v1_0_0.ui.controller;


import com.hacknovation.systemservice.enums.UserTypeEnum;
import com.hacknovation.systemservice.v1_0_0.service.user.UserSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/v1.0.0/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserSV userSV;

    @GetMapping("{userCode}")
    public ResponseEntity<StructureRS> userProfile(@PathVariable String userCode) {
        return response(userSV.userProfile(userCode));
    }

    @GetMapping("user/{username}")
    public ResponseEntity<StructureRS> checkUsername(@PathVariable("username") String username) {
        return response(userSV.checkUsername(username));
    }

    @GetMapping("list-user-levels")
    public ResponseEntity<StructureRS> userListing() {
        return response(userSV.userListing());
    }

    @GetMapping("list-user-referrals")
    public ResponseEntity<StructureRS> userReferrals() {
        return response(userSV.userReferrals());
    }

    @GetMapping("employees")
    public  ResponseEntity<StructureRS> employees() {
        return response(userSV.employeeListing());
    }

    @PostMapping("create-user-level")
    public ResponseEntity<StructureRS> createUser(@Valid @RequestBody CreateUserRQ createUserRQ) {
        createUserRQ.setUserType(UserTypeEnum.LEVEL);
        return response(userSV.createUserLevel(createUserRQ));
    }

    @PostMapping("clone-user-level")
    public ResponseEntity<StructureRS> cloneUserLevel(@Valid @RequestBody CloneUserRQ cloneUserRQ) {
        return response(userSV.cloneUserLevel(cloneUserRQ));
    }

    @PostMapping("create-super-senior")
    public ResponseEntity<StructureRS> createSuperSenior(@Valid @RequestBody CreateEmployeeRQ employeeRQ) {
        employeeRQ.setUserType(UserTypeEnum.LEVEL);
        return response(userSV.createSuperSenior(employeeRQ));
    }

    @PostMapping("create-sub-account")
    public ResponseEntity<StructureRS> createSubAccount(@Valid @RequestBody CreateEmployeeRQ employeeRQ) {
        employeeRQ.setUserType(UserTypeEnum.LEVEL);
        return response(userSV.createSubAccount(employeeRQ));
    }

    @PostMapping("create-employee")
    public ResponseEntity<StructureRS> createEmployee(@Valid @RequestBody CreateEmployeeRQ employeeRQ) {
        employeeRQ.setUserType(UserTypeEnum.SYSTEM);
        return response(userSV.createEmployee(employeeRQ));
    }

    @PutMapping("{userCode}")
    public ResponseEntity<StructureRS> update(@PathVariable("userCode") String userCode, @RequestBody UpdateUserRQ request) {
        return response(userSV.update(userCode, request));
    }

    @PutMapping("{userCode}/change-password")
    public ResponseEntity<StructureRS> changePassword(@Validated @RequestBody ChangeUserPasswordRQ request, @PathVariable String userCode) {
        return response(userSV.changePassword(userCode, request));
    }

    @GetMapping("{userCode}/lottery")
    public ResponseEntity<StructureRS> getLotteryType(@PathVariable String userCode) {
        return response(userSV.lotteryType(userCode));
    }


    @GetMapping("/limit-bet/{userCode}")
    public ResponseEntity<StructureRS> listLimitBet(@PathVariable String userCode) {
        return response(userSV.listLimitBet(userCode));
    }

    @PostMapping("/limit-bet")
    public ResponseEntity<StructureRS> updateLimitBet(@RequestBody @Valid UpdateLimitBetRQ updateLimitBetRQ) {
        return response(userSV.updateLimitBet(updateLimitBetRQ));
    }

}
