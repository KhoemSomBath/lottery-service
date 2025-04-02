package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.enums.UserTypeEnum;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpperUserCodeUtility {

    private final UserRP userRP;

    public String upperUserCode(UserEntity userEntity) {

        if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getCode();
        }

        if (UserConstant.SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getSuperSeniorCode();
        }

        if (UserConstant.MASTER.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getSeniorCode();
        }

        if (UserConstant.AGENT.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getMasterCode();
        }

        /*
         * get agent
         */
        return userEntity.getAgentCode();

    }

    public static String staticUpperUserCode(UserEntity userEntity) {

        if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getCode();
        }

        if (UserConstant.SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getSuperSeniorCode();
        }

        if (UserConstant.MASTER.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getSeniorCode();
        }

        if (UserConstant.AGENT.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getMasterCode();
        }

        /*
         * get agent
         */
        return userEntity.getAgentCode();

    }

    public static String staticUpperUserCode(UserLevelReportTO userEntity) {

        if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getUserCode();
        }

        if (UserConstant.SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getSuperSeniorCode();
        }

        if (UserConstant.MASTER.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getSeniorCode();
        }

        if (UserConstant.AGENT.equalsIgnoreCase(userEntity.getRoleCode())) {
            return userEntity.getMasterCode();
        }

        /*
         * get agent
         */
        return userEntity.getAgentCode();

    }

    /**
     * get under level role code
     * @param roleCode String
     * @return String
     */
    public String underLevelRole(String roleCode) {

        if (UserConstant.COMPANY.equalsIgnoreCase(roleCode)) {
            return UserConstant.SUPER_SENIOR;
        }
        if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(roleCode)) {
            return UserConstant.SENIOR;
        }
        if (UserConstant.SENIOR.equalsIgnoreCase(roleCode)) {
            return UserConstant.MASTER;
        }
        if (UserConstant.MASTER.equalsIgnoreCase(roleCode)) {
            return UserConstant.AGENT;
        }

        return UserConstant.MEMBER;
    }

    /**
     * get upper level role code
     * @param roleCode String
     * @return String
     */
    public String upperLevelRole(String roleCode) {

        if (UserConstant.SENIOR.equalsIgnoreCase(roleCode)) {
            return UserConstant.SUPER_SENIOR;
        }
        if (UserConstant.MASTER.equalsIgnoreCase(roleCode)) {
            return UserConstant.SENIOR;
        }
        if (UserConstant.AGENT.equalsIgnoreCase(roleCode)) {
            return UserConstant.MASTER;
        }
        if (UserConstant.MEMBER.equalsIgnoreCase(roleCode)) {
            return UserConstant.AGENT;
        }

        return UserConstant.SUPER_SENIOR;
    }

    /**
     * get user code for clone user has lottery config
     * @param userEntity UserEntity
     * @return String
     */
    public String getLastUserCodeOfUserHasLottery(UserEntity userEntity) {
        String lastUserCode = userEntity.getCode();
        UserEntity lastLevel = null;
        switch (userEntity.getRoleCode().toUpperCase()) {
            case UserConstant.AGENT:
                lastLevel = userRP.lastMember(lastUserCode);
                break;
            case UserConstant.MASTER:
                lastLevel = userRP.lastAgent(lastUserCode);
                break;
            case UserConstant.SENIOR:
                lastLevel = userRP.lastMaster(lastUserCode);
                break;
            case UserConstant.SUPER_SENIOR:
                lastLevel = userRP.lastSenior(lastUserCode);
                break;
        }
        if (lastLevel != null)
            lastUserCode = lastLevel.getCode();

        return lastUserCode;
    }

    public String getLotteryTypeFromSuperSenior(String superSenior) {
        UserEntity userEntity = userRP.findByCode(superSenior);
        if(!UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            UserEntity user = userRP.findByCode(userEntity.getSuperSeniorCode());
            return user.getLotteryType();
        }

        return userEntity.getLotteryType();
    }

    public String getLotteryTypeFromSuperSenior(UserEntity user) {
        UserEntity userEntity;
        if(user.getUserType().equals(UserTypeEnum.SYSTEM))
           return LotteryConstant.LEAP.concat(",")
                   .concat(LotteryConstant.VN1).concat(",")
                   .concat(LotteryConstant.VN2).concat(",")
                   .concat(LotteryConstant.TN).concat(",")
                   .concat(LotteryConstant.KH).concat(",")
                   .concat(LotteryConstant.SC);
        if(!UserConstant.SUPER_SENIOR.equalsIgnoreCase(user.getRoleCode()))
            userEntity = userRP.findByCode(user.getSuperSeniorCode());
        else
            userEntity = user;
        return userEntity.getLotteryType();
    }

    public String getLotteryTypeFromSuperSeniorByCode(String userCode) {
        UserEntity userEntity = userRP.findByCode(userCode);
        if(userEntity.getUserType().equals(UserTypeEnum.SYSTEM))
            return LotteryConstant.LEAP.concat(",")
                    .concat(LotteryConstant.TN).concat(",")
                    .concat(LotteryConstant.VN1).concat(",")
                    .concat(LotteryConstant.VN2).concat(",")
                    .concat(LotteryConstant.KH).concat(",")
                    .concat(LotteryConstant.SC).concat(",")
                    .concat(LotteryConstant.TH);
        if(!UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode()))
            userEntity = userRP.findByCode(userEntity.getSuperSeniorCode());
        return userEntity.getLotteryType();
    }
}
