package com.hacknovation.systemservice.v1_0_0.utility.user;

import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.enums.UserStatusEnum;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserReferralTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.UpdateUserRQ;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserStatusUtility {

    private final UserRP userRP;
    private final UserNQ userNQ;

    @Async
    public void changeStatus(UserEntity userEntity, UpdateUserRQ request) {

        userEntity.setIsLockedBetting(false);
        userEntity.setStatus(UserStatusEnum.ACTIVATE);
        if (UserConstant.DEACTIVATE.equalsIgnoreCase(request.getStatus())) {
            userEntity.setIsLockedBetting(true);
            userEntity.setStatus(UserStatusEnum.DEACTIVATE);
        }
        userRP.save(userEntity);

        // This deactivates under user
        if (UserConstant.AGENT.equalsIgnoreCase(userEntity.getRoleCode())) {
            agentStatus(userEntity, request);
        }

        if (UserConstant.MASTER.equalsIgnoreCase(userEntity.getRoleCode())) {
            masterStatus(userEntity, request);
        }

        if (UserConstant.SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            seniorStatus(userEntity, request);
        }

        if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            superSeniorStatus(userEntity, request);
        }


    }

    private void agentStatus(UserEntity userEntity, UpdateUserRQ request) {
        memberCodes(userEntity, request);
    }

    private void masterStatus(UserEntity userEntity, UpdateUserRQ request) {
        memberCodes(userEntity, request);
        agentCodes(userEntity, request);
    }

    private void seniorStatus(UserEntity userEntity, UpdateUserRQ request) {
        memberCodes(userEntity, request);
        agentCodes(userEntity, request);
        masterCodes(userEntity, request);
    }

    private void superSeniorStatus(UserEntity userEntity, UpdateUserRQ request) {
        memberCodes(userEntity, request);
        agentCodes(userEntity, request);
        masterCodes(userEntity, request);
        seniorCodes(userEntity, request);
    }


    private void memberCodes(UserEntity userEntity, UpdateUserRQ request) {
        List<UserReferralTO> userReferralTOS = userNQ.userByReferral("member", userEntity.getCode(), "all" );
        List<String> memberCodes = userReferralTOS.stream()
                .map(UserReferralTO::getCode)
                .collect(Collectors.toList());
        updateUsers(memberCodes, request);
    }

    private void agentCodes(UserEntity userEntity, UpdateUserRQ request) {
        List<UserReferralTO> userReferralTOS = userNQ.userByReferral("agent", userEntity.getCode(), "all" );
        List<String> agentCodes = userReferralTOS.stream()
                                .map(UserReferralTO::getCode)
                                .collect(Collectors.toList());
        updateUsers(agentCodes, request);
    }

    private void masterCodes(UserEntity userEntity, UpdateUserRQ request) {
        List<UserReferralTO> userReferralTOS = userNQ.userByReferral("master", userEntity.getCode(), "all" );
        List<String> masterCodes = userReferralTOS.stream()
                        .map(UserReferralTO::getCode)
                        .collect(Collectors.toList());
        updateUsers(masterCodes, request);
    }

    private void seniorCodes(UserEntity userEntity, UpdateUserRQ request) {
        List<UserReferralTO> userReferralTOS = userNQ.userByReferral("senior", userEntity.getCode(), "all" );
        List<String> seniorCodes = userReferralTOS.stream()
                                    .map(UserReferralTO::getCode)
                                    .collect(Collectors.toList());
        updateUsers(seniorCodes, request);
    }

    private void updateUsers(List<String> userCodes, UpdateUserRQ request) {
        List<UserEntity> userEntity1 = userRP.getUserByCodeIn(userCodes);
        for (UserEntity item : userEntity1) {
            item.setIsLockedBetting(false);
            item.setStatus(UserStatusEnum.ACTIVATE);
            if (UserConstant.DEACTIVATE.equalsIgnoreCase(request.getStatus())) {
                item.setIsLockedBetting(true);
                item.setStatus(UserStatusEnum.DEACTIVATE);
            }
            userRP.save(item);
        }

    }

}
