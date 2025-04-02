package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.TrackUserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.TrackUserHasLotteryRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserHasLotteryRP;
import com.hacknovation.systemservice.v1_0_0.service.cache.UserCacheSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.HasLotteryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.userhaslottery.HasLotteryJsonRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.userhaslottery.RateRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.userhaslottery.UserHasLotteryRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v2_0_0.component.dailyreport.DailyReportTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 28/03/2022
 * time: 16:19
 */
@Component
@RequiredArgsConstructor
public class UserHasLotteryJsonUtility {

    private final TrackUserHasLotteryRP trackUserHasLotteryRP;
    private final UserNQ userNQ;
    private final ObjectMapper objectMapper;
    private final UserHasLotteryUtility userHasLotteryUtility;
    private final ActivityLogUtility activityLogUtility;
    private final UserHasLotteryRP userHasLotteryRP;
    private final UserCacheSV userCacheSV;

    /**
     * add user has lottery json member
     * @param lotteryType String
     * @param userCode String
     * @param userToken UserToken
     */
    public void trackUserHasLottery(String lotteryType, String userCode, UserToken userToken) {
        boolean isCanSeeUserOnline = true;

        String userType = "SYSTEM";
        String tokenUserCode = "RUNNER";
        if (userToken.getId() != null) {
            userType = userToken.getUserType();
            tokenUserCode = userToken.getUserCode();
        }

        List<TrackUserHasLotteryEntity> trackUserHasLotteryEntities = new ArrayList<>();
        List<UserLevelReportTO> memberTOs = userNQ.userLevelFilter(userCode, UserConstant.LEVEL, LotteryConstant.ALL, UserConstant.MEMBER, userCode, isCanSeeUserOnline);
        List<String> userCodes = new ArrayList<>();
        for (UserLevelReportTO to : memberTOs) {
            userCodes.addAll(userHasLotteryUtility.getUpperCodes(to));
        }
        userCodes = userCodes.stream().distinct().collect(Collectors.toList());
        Map<String, List<UserHasLotteryEntity>> userHasLotteryGroupByUser =  userHasLotteryUtility.getUserHasLotteryGroupByUser(lotteryType, userCodes);
        Date now = new Date();
        for (UserLevelReportTO userTO : memberTOs) {
            UserHasLotteryRS userHasLotteryRS = new UserHasLotteryRS();

            if (userHasLotteryGroupByUser.containsKey(userTO.getSuperSeniorCode())) {
                HasLotteryJsonRS superSenior = new HasLotteryJsonRS();
                updateHasLotteryRS(superSenior, userHasLotteryGroupByUser.get(userTO.getSuperSeniorCode()));
                userHasLotteryRS.setSuperSenior(superSenior);
            }

            if (userHasLotteryGroupByUser.containsKey(userTO.getSeniorCode())) {
                HasLotteryJsonRS senior = new HasLotteryJsonRS();
                updateHasLotteryRS(senior, userHasLotteryGroupByUser.get(userTO.getSeniorCode()));
                userHasLotteryRS.setSenior(senior);
            }

            if (userHasLotteryGroupByUser.containsKey(userTO.getMasterCode())) {
                HasLotteryJsonRS master = new HasLotteryJsonRS();
                updateHasLotteryRS(master, userHasLotteryGroupByUser.get(userTO.getMasterCode()));
                userHasLotteryRS.setMaster(master);
            }

            if (userHasLotteryGroupByUser.containsKey(userTO.getAgentCode())) {
                HasLotteryJsonRS agent = new HasLotteryJsonRS();
                updateHasLotteryRS(agent, userHasLotteryGroupByUser.get(userTO.getAgentCode()));
                userHasLotteryRS.setAgent(agent);
            }

            if (userHasLotteryGroupByUser.containsKey(userTO.getUserCode())) {
                HasLotteryJsonRS member = new HasLotteryJsonRS();
                updateHasLotteryRS(member, userHasLotteryGroupByUser.get(userTO.getUserCode()));
                userHasLotteryRS.setMember(member);
            }

            try {
                TrackUserHasLotteryEntity trackUserHasLotteryEntity = new TrackUserHasLotteryEntity();
                trackUserHasLotteryEntity.setUserCode(userTO.getUserCode());
                trackUserHasLotteryEntity.setLotteryType(lotteryType);
                trackUserHasLotteryEntity.setIssuedAt(now);
                trackUserHasLotteryEntity.setCreatedBy(tokenUserCode);
                trackUserHasLotteryEntity.setHasLotteryJson(objectMapper.writeValueAsString(userHasLotteryRS));
                trackUserHasLotteryEntities.add(trackUserHasLotteryEntity);
            } catch (JsonProcessingException exception) {
                System.out.println("UserHasLotteryJsonUtility.trackUserHasLottery >>>>> JsonProcessingException");
                exception.printStackTrace();
            }
        }

        if (!trackUserHasLotteryEntities.isEmpty()) {
            /*
             * remove track user has lottery before save that > that now + 9PM
             */
            Date firstEditByDay = new Date();
            firstEditByDay.setHours(14); // 9PM At KH
            firstEditByDay.setMinutes(0);
            firstEditByDay.setSeconds(0);
            trackUserHasLotteryRP.deleteDuplicateByDate(lotteryType, firstEditByDay, userCodes);
            trackUserHasLotteryRP.saveAll(trackUserHasLotteryEntities);

            /*
             * add activity log
             */
            activityLogUtility.addActivityLog(lotteryType, ActivityLogConstant.MODULE_USER_HAS_LOTTERY_JSON, userType, ActivityLogConstant.ACTION_ADD, tokenUserCode, trackUserHasLotteryEntities);
            userCacheSV.removeMaxBetItemByUserCodeIn(userCodes);
        }

    }


    /**
     * update track user has lottery
     * @param userHasLotteryEntity UserHasLotteryEntity
     */
    public void updateTrackUserHasLotteryExceptCommissionAndReward(UserHasLotteryEntity userHasLotteryEntity) {
        TrackUserHasLotteryEntity trackUserHasLotteryEntity = trackUserHasLotteryRP.getLastUserHasLottery(userHasLotteryEntity.getLotteryCode(), userHasLotteryEntity.getUserCode());
        if (trackUserHasLotteryEntity != null) {
            UserHasLotteryRS userHasLotteryRS = getUserHasLotteryRSFromTrackEntity(trackUserHasLotteryEntity);
            HasLotteryJsonRS member = userHasLotteryRS.getMember();
            RateRS rateRS = new RateRS();
            BeanUtils.copyProperties(userHasLotteryEntity, rateRS);
            if (LotteryConstant.REBATE1D.equals(userHasLotteryEntity.getRebateCode())) {
                member.setOneD(rateRS);
            }
            if (LotteryConstant.REBATE2D.equals(userHasLotteryEntity.getRebateCode())) {
                member.setTwoD(rateRS);
            }
            if (LotteryConstant.REBATE3D.equals(userHasLotteryEntity.getRebateCode())) {
                member.setThreeD(rateRS);
            }
            if (LotteryConstant.REBATE4D.equals(userHasLotteryEntity.getRebateCode())) {
                member.setFourD(rateRS);
            }
            userHasLotteryRS.setMember(member);

            try {
                trackUserHasLotteryEntity.setHasLotteryJson(objectMapper.writeValueAsString(userHasLotteryRS));
                trackUserHasLotteryRP.save(trackUserHasLotteryEntity);
                userCacheSV.removeMaxBetItemByUserCode(trackUserHasLotteryEntity.getUserCode());
                System.out.println("UserHasLotteryJsonUtility.updateTrackUserHasLotteryExceptCommissionAndReward");
                System.out.println("change member config has lottery before 9PM");
            } catch (JsonProcessingException exception) {
                System.out.println("UserHasLotteryJsonUtility.updateTrackUserHasLotteryExceptCommissionAndReward >>>>> JsonProcessingException");
                exception.printStackTrace();
            }

        }
    }

    public void updateTrackAllUserHasLotteryExceptCommissionAndReward(String lotteryType, String userCode, String rebateCode, UserToken userToken) {
        List<UserLevelReportTO> memberTOs = userNQ.userLevelFilter(userCode, UserConstant.LEVEL, LotteryConstant.ALL, UserConstant.MEMBER, userCode, true);
        List<String> memberCodes = memberTOs.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());
        List<UserHasLotteryEntity> userHasLotteryEntities = userHasLotteryRP.userHasLotteriesByLotteryUserCodeInRebate(lotteryType, memberCodes, rebateCode);
        userHasLotteryEntities.forEach(this::updateTrackUserHasLotteryExceptCommissionAndReward);
        userCacheSV.removeMaxBetItemByUserCodeIn(memberCodes);
    }

    /**
     * update has lottery rs
     * @param hasLotteryJsonRS HasLotteryRS
     * @param hasLotteryEntities List<UserHasLotteryEntity>
     */
    private void updateHasLotteryRS (HasLotteryJsonRS hasLotteryJsonRS, List<UserHasLotteryEntity> hasLotteryEntities) {
        Map<String, UserHasLotteryEntity> userHasLotteryEntityMapByRebate = hasLotteryEntities.stream().collect(Collectors.toMap(UserHasLotteryEntity::getRebateCode, Function.identity()));
        userHasLotteryEntityMapByRebate.forEach((rebateCode, item) -> {
            RateRS rateRS = new RateRS();
            BeanUtils.copyProperties(item, rateRS);
            if (LotteryConstant.REBATE1D.equals(rebateCode)) {
                hasLotteryJsonRS.setOneD(rateRS);
            }
            if (LotteryConstant.REBATE2D.equals(rebateCode)) {
                hasLotteryJsonRS.setTwoD(rateRS);
            }
            if (LotteryConstant.REBATE3D.equals(rebateCode)) {
                hasLotteryJsonRS.setThreeD(rateRS);
            }
            if (LotteryConstant.REBATE4D.equals(rebateCode)) {
                hasLotteryJsonRS.setFourD(rateRS);
            }
        });
    }

    /**
     * get track user has lottery by id in
     * @param trackIds List<Integer>
     * @return List<TrackUserHasLotteryEntity>
     */
    public List<TrackUserHasLotteryEntity> getTrackUserHasLotteryEntitiesByIdIn(List<Long> trackIds) {
        return trackUserHasLotteryRP.getAllByIdIn(trackIds);
    }

    /**
     * get map track user has lottery by order id
     * @param orderDTOMapById Map<BigInteger, OrderDTO>
     * @param trackUserHasLotteryEntities List<TrackUserHasLotteryEntity>
     * @return Map<Integer, TrackUserHasLotteryEntity>
     */
    public Map<Integer, TrackUserHasLotteryEntity> getTrackUserHasLotteryByOrderId(Map<BigInteger, OrderDTO> orderDTOMapById, List<TrackUserHasLotteryEntity> trackUserHasLotteryEntities) {
        Map<Integer, TrackUserHasLotteryEntity> map = new HashMap<>();
        Map<Long, TrackUserHasLotteryEntity> trackUserHasLotteryEntityMapById = trackUserHasLotteryEntities.stream().collect(Collectors.toMap(TrackUserHasLotteryEntity::getId, Function.identity()));
        for (BigInteger orderId : orderDTOMapById.keySet()) {
            OrderDTO orderDTO = orderDTOMapById.get(orderId);
            if (trackUserHasLotteryEntityMapById.containsKey(orderDTO.getHasLotteryId().longValue())) {
                map.put(orderDTO.getId().intValue(), trackUserHasLotteryEntityMapById.get(orderDTO.getHasLotteryId().longValue()));
            }
        }

        return map;
    }


    public Map<Long, TrackUserHasLotteryEntity> _getTrackUserHasLotteryByOrderId(Map<Long, List<DailyReportTO>> orderDTOMapById, List<TrackUserHasLotteryEntity> trackUserHasLotteryEntities) {
        Map<Long, TrackUserHasLotteryEntity> map = new HashMap<>();
        Map<Long, TrackUserHasLotteryEntity> trackUserHasLotteryEntityMapById = trackUserHasLotteryEntities.stream().collect(Collectors.toMap(TrackUserHasLotteryEntity::getId, Function.identity()));
        for (Long orderId : orderDTOMapById.keySet()) {
            List<DailyReportTO> orderDTO = orderDTOMapById.get(orderId);
            for(DailyReportTO to: orderDTO){
                if (trackUserHasLotteryEntityMapById.containsKey(to.getHasLotteryId())) {
                    map.put(to.getOrderId(), trackUserHasLotteryEntityMapById.get(to.getHasLotteryId()));
                }
            }
        }

        return map;
    }


    public UserHasLotteryRS getUserHasLotteryRSFromTrackEntity(TrackUserHasLotteryEntity trackUserHasLotteryEntity) {
        UserHasLotteryRS userHasLotteryRS = new UserHasLotteryRS();
        try {
            userHasLotteryRS = objectMapper.readValue(trackUserHasLotteryEntity.getHasLotteryJson(), UserHasLotteryRS.class);
        } catch (JsonProcessingException e) {
            System.out.println("UserHasLotteryJsonUtility.getUserHasLotteryRSFromTrackEntity  ====>JsonProcessingException");
            e.printStackTrace();
        }

        return userHasLotteryRS;
    }

    /**
     * update user has lottery
     * @param userRole String
     * @param hasLotteryRSByUser HasLotteryRS
     * @param trackUserHasLotteryEntity TrackUserHasLotteryEntity
     */
    public void setUserHasLotteryRS(String userRole, HasLotteryRS hasLotteryRSByUser, TrackUserHasLotteryEntity trackUserHasLotteryEntity) {

        UserHasLotteryRS userHasLotteryRS = getUserHasLotteryRSFromTrackEntity(trackUserHasLotteryEntity);
        HasLotteryJsonRS hasLotteryJsonRS = new HasLotteryJsonRS();
        switch (userRole.toUpperCase()) {
            case UserConstant.MEMBER:
                hasLotteryJsonRS = userHasLotteryRS.getMember();
                break;
            case UserConstant.AGENT:
                hasLotteryJsonRS = userHasLotteryRS.getAgent();
                break;
            case UserConstant.MASTER:
                hasLotteryJsonRS = userHasLotteryRS.getMaster();
                break;
            case UserConstant.SENIOR:
                hasLotteryJsonRS = userHasLotteryRS.getSenior();
                break;
            case UserConstant.SUPER_SENIOR:
                hasLotteryJsonRS = userHasLotteryRS.getSuperSenior();
                break;
        }

        hasLotteryRSByUser.setCom1D(hasLotteryJsonRS.getOneD().getCommission());
        hasLotteryRSByUser.setShare1D(hasLotteryJsonRS.getOneD().getShare());
        hasLotteryRSByUser.setRebateRate1D(hasLotteryJsonRS.getOneD().getRebateRate());

        hasLotteryRSByUser.setCom2D(hasLotteryJsonRS.getTwoD().getCommission());
        hasLotteryRSByUser.setShare2D(hasLotteryJsonRS.getTwoD().getShare());
        hasLotteryRSByUser.setRebateRate2D(hasLotteryJsonRS.getTwoD().getRebateRate());

        hasLotteryRSByUser.setCom3D(hasLotteryJsonRS.getThreeD().getCommission());
        hasLotteryRSByUser.setShare3D(hasLotteryJsonRS.getThreeD().getShare());
        hasLotteryRSByUser.setRebateRate3D(hasLotteryJsonRS.getThreeD().getRebateRate());

        hasLotteryRSByUser.setCom4D(hasLotteryJsonRS.getFourD().getCommission());
        hasLotteryRSByUser.setShare4D(hasLotteryJsonRS.getFourD().getShare());
        hasLotteryRSByUser.setRebateRate4D(hasLotteryJsonRS.getFourD().getRebateRate());
    }

}
