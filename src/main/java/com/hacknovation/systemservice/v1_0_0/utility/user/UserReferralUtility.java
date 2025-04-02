package com.hacknovation.systemservice.v1_0_0.utility.user;

import com.hacknovation.systemservice.constant.BettingConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserReferralTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserHasLotteryRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.CommissionLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.CommissionRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.CreateUserRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.UserReferralRS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserReferralUtility {

    private final UserRP userRP;
    private final UserHasLotteryRP userHasLotteryRP;
    private final UserNQ userNQ;

    public UserReferralRS agentReferral(UserTO userTO, List<UserReferralTO> memberReferrals) {

        List<UserReferralTO> memberLists = memberReferrals.stream()
                .filter(member -> member.getAgentCode().equalsIgnoreCase(userTO.getUserCode()))
                .collect(Collectors.toList());

        UserReferralRS userReferralRS = new UserReferralRS();
        userReferralRS.setKey(userTO.getUserCode());
        userReferralRS.setLabel(userTO.getUsername());
        userReferralRS.setNickName(userTO.getNickname());
        userReferralRS.setRoleCode(userTO.getRoleCode());
        userReferralRS.setNodes(memberLists(memberLists));
        return userReferralRS;
    }

    public UserReferralRS masterReferral(UserTO userTO, List<UserReferralTO> agentReferrals, List<UserReferralTO> memberReferrals) {

        List<UserReferralTO> agentLists = agentReferrals.stream()
                .filter(agent -> agent.getMasterCode().equalsIgnoreCase(userTO.getUserCode()))
                .collect(Collectors.toList());

        UserReferralRS userReferralRS = new UserReferralRS();
        userReferralRS.setKey(userTO.getUserCode());
        userReferralRS.setLabel(userTO.getNickname());
        userReferralRS.setNickName(userTO.getUsername());
        userReferralRS.setRoleName(userTO.getRoleCode());
        userReferralRS.setRoleCode(userTO.getRoleCode());
        userReferralRS.setNodes(agentLists(agentLists, memberReferrals));
        return userReferralRS;
    }

    public UserReferralRS seniorReferral(UserTO userTO, List<UserReferralTO> masterReferrals, List<UserReferralTO> agentReferrals, List<UserReferralTO> memberReferrals) {

        // Master referral
        List<UserReferralTO> masterLists = masterReferrals.stream()
                .filter(master -> master.getSeniorCode().equalsIgnoreCase(userTO.getUserCode()))
                .collect(Collectors.toList());


        UserReferralRS userReferralRS = new UserReferralRS();
        userReferralRS.setKey(userTO.getUserCode());
        userReferralRS.setNickName(userTO.getNickname());
        userReferralRS.setLabel(userTO.getUsername());
        userReferralRS.setRoleName(userTO.getRoleCode());
        userReferralRS.setRoleCode(userTO.getRoleCode());
        userReferralRS.setNodes(masterLists(masterLists, agentReferrals, memberReferrals));
        return userReferralRS;
    }


    public UserReferralRS superSeniorReferral(UserTO userTO, List<UserReferralTO> seniorReferrals, List<UserReferralTO> masterReferrals, List<UserReferralTO> agentReferrals, List<UserReferralTO> memberReferrals) {
        // Senior referral
        List<UserReferralRS> seniorReferralRS = new ArrayList<>();
        seniorReferrals.forEach(item -> {
            UserReferralRS seniorReferralRS1  = new UserReferralRS();
            seniorReferralRS1.setKey(item.getCode());
            seniorReferralRS1.setLabel(item.getUsername());
            seniorReferralRS1.setRoleName(item.getRoleName());
            seniorReferralRS1.setNickName(item.getNickname());
            seniorReferralRS1.setRoleCode(item.getRoleCode());

            // Master referral
            if (masterReferrals.size() > 0) {
                List<UserReferralTO> masterLists = masterReferrals.stream()
                        .filter(master -> master.getSeniorCode().equalsIgnoreCase(item.getCode()))
                        .collect(Collectors.toList());
                seniorReferralRS1.setNodes(masterLists(masterLists, agentReferrals, memberReferrals));
            }
            seniorReferralRS.add(seniorReferralRS1);
        });

        UserReferralRS userReferralRS = new UserReferralRS();
        userReferralRS.setKey(userTO.getUserCode());
        userReferralRS.setNickName(userTO.getNickname());
        userReferralRS.setLabel(userTO.getUsername());
        userReferralRS.setRoleName(userTO.getRoleCode());
        userReferralRS.setRoleCode(userTO.getRoleCode());
        userReferralRS.setNodes(seniorReferralRS);
        return userReferralRS;
    }

    private List<UserReferralRS> masterLists(List<UserReferralTO> seniorLists, List<UserReferralTO> agentReferrals, List<UserReferralTO> memberReferrals) {

        List<UserReferralRS> masterReferralRS = new ArrayList<>();
        seniorLists.forEach(item -> {
            UserReferralRS masterReferralRS1 = new UserReferralRS();
            masterReferralRS1.setKey(item.getCode());
            masterReferralRS1.setNickName(item.getNickname());
            masterReferralRS1.setLabel(item.getUsername());
            masterReferralRS1.setRoleName(item.getRoleName());
            masterReferralRS1.setRoleCode(item.getRoleCode());

            // Agent referral
            if (agentReferrals.size() > 0) {
                List<UserReferralTO> agentLists = agentReferrals.stream()
                        .filter(agent -> agent.getMasterCode().equalsIgnoreCase(item.getCode()))
                        .collect(Collectors.toList());
                masterReferralRS1.setNodes(agentLists(agentLists, memberReferrals));
            }
            masterReferralRS.add(masterReferralRS1);
        });

        return masterReferralRS;
    }

    private List<UserReferralRS> agentLists(List<UserReferralTO> agentLists, List<UserReferralTO> memberReferrals) {
        List<UserReferralRS> agentReferralRS = new ArrayList<>();
        agentLists.forEach(agent -> {
            UserReferralRS agentReferralRS1 = new UserReferralRS();
            agentReferralRS1.setKey(agent.getCode());
            agentReferralRS1.setNickName(agent.getNickname());
            agentReferralRS1.setLabel(agent.getUsername());
            agentReferralRS1.setRoleName(agent.getRoleName());
            agentReferralRS1.setRoleCode(agent.getRoleCode());

            // Member referral
            if (memberReferrals.size() > 0) {
                List<UserReferralTO> memberLists = memberReferrals.stream()
                        .filter(member -> member.getAgentCode().equalsIgnoreCase(agent.getCode()))
                        .collect(Collectors.toList());
                agentReferralRS1.setNodes(memberLists(memberLists));
            }

            agentReferralRS.add(agentReferralRS1);

        });
        return agentReferralRS;
    }

    private List<UserReferralRS> memberLists(List<UserReferralTO> memberLists) {
        List<UserReferralRS> memberReferralRS = new ArrayList<>();
        memberLists.forEach(member -> {
            UserReferralRS memberReferralRS1 = new UserReferralRS();
            memberReferralRS1.setKey(member.getCode());
            memberReferralRS1.setNickName(member.getNickname());
            memberReferralRS1.setLabel(member.getUsername());
            memberReferralRS1.setRoleName(member.getRoleName());
            memberReferralRS1.setRoleCode(member.getRoleCode());
            memberReferralRS.add(memberReferralRS1);
        });
        return memberReferralRS;
    }


    public List<String> userReferralByLevel(String userCode, String userRole) {

        List<UserReferralTO> userReferralTOS = userNQ.userByReferral(userRole, userCode, "all" );

        return userReferralTOS.stream()
                .map(UserReferralTO::getCode)
                .distinct()
                .collect(Collectors.toList());

    }



    public List<String> userReferralCodes(String userCode, String userRole) {

        String userLevel = userRole;
        if (userRole.equalsIgnoreCase("member-order")) {
            userLevel = "member";
        }

        List<UserReferralTO> userReferralTOS = userNQ.userByReferral(userLevel, userCode, "all" );

        if (userRole.equalsIgnoreCase("super-senior")) {
            return userReferralTOS.stream()
                    .map(UserReferralTO::getCode)
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (userRole.equalsIgnoreCase("senior")) {
            return userReferralTOS.stream()
                    .map(UserReferralTO::getSuperSeniorCode)
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (userRole.equalsIgnoreCase("master")) {
            return userReferralTOS.stream()
                    .map(UserReferralTO::getSeniorCode)
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (userRole.equalsIgnoreCase("agent")) {
            return userReferralTOS.stream()
                    .map(UserReferralTO::getMasterCode)
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (userRole.equalsIgnoreCase("member-order")) {
            return userReferralTOS.stream()
                    .map(UserReferralTO::getCode)
                    .distinct()
                    .collect(Collectors.toList());
        }

        return userReferralTOS.stream()
                .map(UserReferralTO::getAgentCode)
                .distinct()
                .collect(Collectors.toList());

    }

    public String userType(String userRole) {
        if (userRole.equalsIgnoreCase("super-senior") || userRole.equalsIgnoreCase("senior") || userRole.equalsIgnoreCase("master") || userRole.equalsIgnoreCase("agent")) {
            return "front";
        }
        return "system";
    }

    public String getCurrency(String superSeniorCode) {
        UserTO userTO = userNQ.userByCode(superSeniorCode);
        return userTO.getCurrencyCode();
    }

    /**
     * check commission and rebate rate is valid with parent
     * @param createUserRQ CreateUserRQ
     * @return boolean
     */
    public boolean isCommissionValid(CreateUserRQ createUserRQ) {
        UserEntity userEntity = userRP.findByCode(createUserRQ.getLevelUnderUserCode());
        if (userEntity != null) {
            List<UserHasLotteryEntity> hasLotteryEntities = userHasLotteryRP.userHasLotteries(userEntity.getCode());
            Map<String, List<UserHasLotteryEntity>> mapUserHasLottery = hasLotteryEntities.stream().collect(Collectors.groupingBy(UserHasLotteryEntity::getLotteryCode));
            List<CommissionLotteryRQ> commissions = createUserRQ.getCommissions();
            if (commissions.isEmpty())
                return false;
            Map<String, CommissionLotteryRQ>  mapCommissionList = commissions.stream().collect(Collectors.toMap(CommissionLotteryRQ::getLotteryType, Function.identity()));
            for (String lotteryType : mapCommissionList.keySet()) {
                if (mapCommissionList.containsKey(lotteryType)) {
                    List<CommissionRQ> commissionRQList = mapCommissionList.get(lotteryType).getItems();
                    Map<String, CommissionRQ> mapCommissionRebateCode = commissionRQList.stream().collect(Collectors.toMap(CommissionRQ::getRebateCode, Function.identity()));
                    Map<String, UserHasLotteryEntity> mapUserHasLotteryRebateCode = mapUserHasLottery.get(lotteryType).stream().collect(Collectors.toMap(UserHasLotteryEntity::getRebateCode, Function.identity()));
                    for (String rebateCode : mapCommissionRebateCode.keySet()) {
                        boolean isValid = isValidCompare(mapUserHasLotteryRebateCode.get(rebateCode), mapCommissionRebateCode.get(rebateCode));
                        if (!isValid)
                            return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * compare commission is >= and rebate <=
     * compare maxBetFirst & maxBetSecond <=
     * compare maxBetSecondAt >=
     * @param userHasLotteryEntity UserHasLotteryEntity
     * @param commissionRQ CommissionRQ
     * @return boolean
     */
    public boolean isValidCompare(UserHasLotteryEntity userHasLotteryEntity, CommissionRQ commissionRQ) {
        boolean isCommission = commissionRQ.getCommission().compareTo(userHasLotteryEntity.getCommission()) >= 0;
        boolean isRebateRate = commissionRQ.getRebateRate().compareTo(userHasLotteryEntity.getRebateRate()) <= 0;
        boolean isMaxBetFirst = commissionRQ.getMaxBetFirst().compareTo(userHasLotteryEntity.getMaxBetFirst()) <= 0;
        boolean isMaxBetSecond = commissionRQ.getMaxBetSecond().compareTo(userHasLotteryEntity.getMaxBetSecond()) <= 0;
//        boolean isMaxBetSecondMin = commissionRQ.getMaxBetSecondAt().compareTo(userHasLotteryEntity.getMaxBetSecondMin()) >= 0;

        boolean isUpdateAble = true;

        if(commissionRQ.getRebateCode().equals(BettingConstant.TWOD)){
            isUpdateAble = commissionRQ.getRebateRate().compareTo(commissionRQ.getCommission()) <= 0;
        }

        if(commissionRQ.getRebateCode().equals(BettingConstant.THREED)){
            isUpdateAble = commissionRQ.getRebateRate().divide(BigDecimal.valueOf(10), RoundingMode.CEILING).compareTo(commissionRQ.getCommission()) <= 0;
        }

        if(commissionRQ.getRebateCode().equals(BettingConstant.FOURD)){
            isUpdateAble = commissionRQ.getRebateRate().divide(BigDecimal.valueOf(100), RoundingMode.CEILING).compareTo(commissionRQ.getCommission()) <= 0;
        }

        return isCommission && isRebateRate && isMaxBetFirst && isMaxBetSecond && isUpdateAble;
    }

}
