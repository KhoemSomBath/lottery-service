package com.hacknovation.systemservice.v1_0_0.service.config;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.*;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.userlottery.UserHasLotteryNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.userlottery.UserHasLotteryTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.*;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.service.cache.UserCacheSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.RebateDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.CommissionLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.CommissionRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.config.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.shift.ShiftRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.Commission;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.CommissionConfigRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.CommissionLotteryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.ShiftMaxBetRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.UserHasLotteryJsonUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility.staticUpperUserCode;

@Service
@RequiredArgsConstructor
public class ConfigIP extends BaseServiceIP implements ConfigSV {

    private final UserHasLotteryRP userHasLotteryRP;
    private final UserRP userRP;
    private final ShiftsRP shiftsRP;
    private final LotteryRP lotteryRP;
    private final RebatesRP rebatesRP;

    private final UserHasLotteryNQ userHasLotteryNQ;
    private final TempDrawingNQ tempDrawingNQ;

    private final UpperUserCodeUtility upperUserCodeUtility;
    private final GeneralUtility generalUtility;
    private final ActivityLogUtility activityLogUtility;
    private final UserHasLotteryJsonUtility userHasLotteryJsonUtility;

    private final JwtToken jwtToken;
    private final HttpServletRequest request;

    private final UserCacheSV userCacheSV;

    /**
     * Has Lottery
     */
    public StructureRS userHasLottery() {
        UserLotteryListRQ userLotteryListRQ = new UserLotteryListRQ(request);
        System.out.println("===================================================");
        System.out.println("UserLotteryIP.userHasLottery => userLotteryListRQ = " + userLotteryListRQ);
        System.out.println("===================================================");
        UserEntity userEntity = userRP.findByCode(userLotteryListRQ.getUserCode());
        if (userEntity == null) {
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.BAD_REQUEST);
        }
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, getUserLotteryRS(userLotteryListRQ, userEntity));
    }

    @Override
    public StructureRS getUserHasLotteryCreate(String userCode) {
        String lastUserCode = userCode;
        UserEntity userEntity = userRP.findByCode(userCode);
        if (userEntity != null && userHasLotteryRP.existsByUserCodeAndLotteryCode(lastUserCode, LotteryConstant.VN1)) {
            lastUserCode = upperUserCodeUtility.getLastUserCodeOfUserHasLottery(userEntity);
        }

        CommissionConfigRS configRS = getCommissionConfigRS(lastUserCode);
        if (userEntity != null && userEntity.getLotteryType() != null) {
            configRS.setLotteries(getLotteryRSByLottery(userEntity.getLotteryType()));
        }

        return responseBodyWithSuccessMessage(configRS);
    }

    @Override
    public StructureRS fetchUserHasLotteryList(String userCode) {
        return responseBodyWithSuccessMessage(getCommissionConfigRS(userCode));
    }

    private CommissionConfigRS getCommissionConfigRS(String userCode) {
        CommissionConfigRS configRS = new CommissionConfigRS();
        UserEntity userEntity = userRP.findByCode(userCode);
        String lastUserCode = userCode;
        String userLotteryType = LotteryConstant.VN1;
        String superSeniorCode = userCode;
        if (userEntity != null) {
            if (!UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
                superSeniorCode = userEntity.getSuperSeniorCode();
            }
            if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userEntity.getRoleCode())) {
                UserEntity parent = userRP.getUserByParentId(userEntity.getParentId());
                superSeniorCode = parent.getSuperSeniorCode();
                userEntity.setRoleCode(parent.getRoleCode());
                lastUserCode = parent.getCode();
            }
            configRS.setNickname(userEntity.getNickname());
            configRS.setUsername(userEntity.getUsername());
            configRS.setRoleCode(userEntity.getRoleCode());
            UserEntity creator = userRP.findByCode(userEntity.getCreatedBy());
            if (creator != null) {
                configRS.setCreator(creator.getUsername());
            }
            userLotteryType = upperUserCodeUtility.getLotteryTypeFromSuperSenior(superSeniorCode);
        }

        List<String> lotteryTypes = List.of(userLotteryType.split(","));
        List<UserHasLotteryTO> userHasLotteryTOList = userHasLotteryNQ.userHasLotteryList(LotteryConstant.ALL, lastUserCode);

        String today = generalUtility.formatDateYYYYMMDD(new Date());

        Map<String, List<UserHasLotteryTO>> userHasLotteryGroupByLotteryType = userHasLotteryTOList.stream().collect(Collectors.groupingBy(UserHasLotteryTO::getOriginLotteryCode));
        List<CommissionLotteryRS> commissionLotteryRSList = new ArrayList<>();
        for (String lotteryType : userHasLotteryGroupByLotteryType.keySet()) {

            if (!LotteryConstant.ALL.equalsIgnoreCase(userCode) && !lotteryTypes.contains(lotteryType)) continue;
            List<UserHasLotteryTO> userTOList = userHasLotteryGroupByLotteryType.get(lotteryType);
            CommissionLotteryRS commissionLotteryRS = new CommissionLotteryRS();
            List<Commission> commissionList = new ArrayList<>();
            commissionLotteryRS.setLotteryType(lotteryType);

            // set default share 100 for create super-senior
            userTOList.forEach(item -> {
                if(UserConstant.ALL.equalsIgnoreCase(userCode))
                    item.setShare(BigDecimal.valueOf(100));
                commissionList.add(new Commission(item));
            });

            List<DrawingDTO> drawingDTOList = new ArrayList<>();

            switch (lotteryType) {
                case LotteryConstant.VN1:
                    drawingDTOList = tempDrawingNQ.vnOneTempDrawingByDate(today, today);
                    break;
                case LotteryConstant.VN2:
                    drawingDTOList = tempDrawingNQ.vnTwoTempDrawingByDate(today, today);
                    break;
                case LotteryConstant.LEAP:
                    drawingDTOList = tempDrawingNQ.leapTempDrawingByDate(today, today);
                    break;
                case LotteryConstant.KH:
                    drawingDTOList = tempDrawingNQ.khTempDrawingByDate(today, today);
                    break;
                case LotteryConstant.TN:
                    drawingDTOList = tempDrawingNQ.tnTempDrawingByDate(today, today);
                    break;
                case LotteryConstant.SC:
                    drawingDTOList = tempDrawingNQ.scTempDrawingByDate(today, today);
                    break;
                case LotteryConstant.TH:
                    drawingDTOList = tempDrawingNQ.thTempDrawingByDate(today, today);
                    break;
            }
            setShiftsToUserHasLottery(commissionList, drawingDTOList, userEntity, lotteryType);

            commissionLotteryRS.setItems(commissionList);
            commissionLotteryRSList.add(commissionLotteryRS);
        }
        configRS.setItems(commissionLotteryRSList);

        return configRS;
    }

    private List<UserHasLotteryRS> getUserLotteryRS(UserLotteryListRQ userLotteryListRQ, UserEntity userEntity) {
        List<UserHasLotteryRS> userHasLotteryRSList = new ArrayList<>();
        String userLotteryType = userEntity.getLotteryType();
        if (!UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
            userLotteryType = upperUserCodeUtility.getLotteryTypeFromSuperSenior(userEntity.getSuperSeniorCode());
        }
        List<UserHasLotteryTO> userHasLotteryTOList = userHasLotteryNQ.userHasLottery(userLotteryListRQ.getLotteryType(), userLotteryListRQ.getUserCode());
        Map<String, List<UserHasLotteryTO>> mapUserHasLotteryByLottery = userHasLotteryTOList.stream().collect(Collectors.groupingBy(UserHasLotteryTO::getOriginLotteryCode, Collectors.mapping(item -> item, Collectors.toList())));
        for (String lotteryType : mapUserHasLotteryByLottery.keySet()) {
            if (LotteryConstant.SIXD.equals(lotteryType)) continue;
            if (!userLotteryType.contains(lotteryType)) continue;
            List<UserHasLotteryTO> userHasLotteryTOS = mapUserHasLotteryByLottery.get(lotteryType);
            List<UserLotteryRS> userLotteryRSList = new ArrayList<>();
            Map<String, BigDecimal> mapRebate = new HashMap<>();
            Map<String, String> mapCode = new HashMap<>();
            mapCode.put("2D", "twoD");
            mapCode.put("3D", "threeD");
            mapCode.put("4D", "fourD");
            mapCode.put("5D", "fiveD");
            mapCode.put("6D", "sixD");
            userHasLotteryTOS.forEach(item -> {
                UserLotteryRS userLotteryRS1 = new UserLotteryRS();
                BeanUtils.copyProperties(item, userLotteryRS1);
                mapRebate.put(mapCode.get(item.getOriginRebateCode()), item.getRebateRate());
                userLotteryRSList.add(userLotteryRS1);
                if (LotteryConstant.SIXD.equalsIgnoreCase(userLotteryListRQ.getLotteryType()) && !LotteryConstant.REBATE6D.equalsIgnoreCase(item.getOriginRebateCode()))
                    userLotteryRSList.remove(userLotteryRS1);
            });

            if (userLotteryListRQ.getLotteryType().equalsIgnoreCase(LotteryConstant.SIXD)) {
                userLotteryRSList.forEach(item -> item.setRebates(mapRebate));
            }

            UserHasLotteryRS userHasLotteryRS = new UserHasLotteryRS();
            userHasLotteryRS.setLotteryType(lotteryType);
            userHasLotteryRS.setItems(userLotteryRSList);
            userHasLotteryRSList.add(userHasLotteryRS);
        }
        return userHasLotteryRSList;
    }

    private List<UserLotteryRS> getUserLotteryRSCreate(UserLotteryListRQ userLotteryListRQ) {
        List<UserHasLotteryTO> userHasLotteryTOS = userHasLotteryNQ.userHasLottery(userLotteryListRQ.getLotteryType(), userLotteryListRQ.getUserCode());
        List<UserLotteryRS> userLotteryRS = new ArrayList<>();
        userHasLotteryTOS.forEach(item -> {
            UserLotteryRS userLotteryRS1 = new UserLotteryRS();
            BeanUtils.copyProperties(item, userLotteryRS1);
            userLotteryRS.add(userLotteryRS1);
        });
        return userLotteryRS;
    }

    public StructureRS updateUserHasLottery(UpdateUserHasLotteryRQ updateUserHasLotteryRQ) {

        UserToken token = jwtToken.getUserToken();
        UserEntity userEntity = userRP.findByCode(updateUserHasLotteryRQ.getUserCode());
        UserHasLotteryEntity userHasLotteryEntity = userHasLotteryRP.userLotteryByCodeAndRebateCode(updateUserHasLotteryRQ.getLotteryCode(), updateUserHasLotteryRQ.getUserCode(), updateUserHasLotteryRQ.getRebateCode());
        UserHasLotteryEntity parent = userHasLotteryRP.userLotteryByCodeAndRebateCode(updateUserHasLotteryRQ.getLotteryCode(), staticUpperUserCode(userEntity), updateUserHasLotteryRQ.getRebateCode());

        LocalTime start = LocalTime.parse("14:00:00"); // 9pm utc
        LocalTime stop = LocalTime.parse("16:59:59"); // 11:59 utc
        LocalTime target = LocalTime.now();

        boolean isTargetAfterStartAndBeforeStop = (target.isAfter(start) && target.isBefore(stop));
/*        boolean hasOrder = false;

        switch (updateUserHasLotteryRQ.getLotteryCode().toUpperCase()) {
            case LotteryConstant.LEAP:
                hasOrder = lotteryRP.isHasLeapOrderByUserAndDate(updateUserHasLotteryRQ.getUserCode()) > 0;
                break;
            case LotteryConstant.TN:
                hasOrder = lotteryRP.isHasTnOrderByUserAndDate(updateUserHasLotteryRQ.getUserCode()) > 0;
                break;
            case LotteryConstant.VN1:
                hasOrder = lotteryRP.isHasVn1OrderByUserAndDate(updateUserHasLotteryRQ.getUserCode()) > 0;
                break;
            case LotteryConstant.VN2:
            case LotteryConstant.MT:
                hasOrder = lotteryRP.isHasVn2OrderByUserAndDate(updateUserHasLotteryRQ.getUserCode()) > 0;
                break;
            case LotteryConstant.KH:
                hasOrder = lotteryRP.isHasKhOrderByUserAndDate(updateUserHasLotteryRQ.getUserCode()) > 0;
                break;
        }*/

        if ((!isTargetAfterStartAndBeforeStop && userHasLotteryEntity != null)) {

            updateHasLotteryExceptCommissionAndRebateRate(userHasLotteryEntity, updateUserHasLotteryRQ);

            if (UserConstant.MEMBER.equalsIgnoreCase(userEntity.getRoleCode())) {
                /*
                 * update track user has lottery
                 */
                userHasLotteryJsonUtility.updateTrackUserHasLotteryExceptCommissionAndReward(userHasLotteryEntity);
            } else {
                // if user is senior then update user has lotteries for all user below his senior, master, agent, member
                userHasLotteryRP.copyUserHasLotteriesFromUpperLevel(userEntity.getCode(), userEntity.getRoleCode(), userHasLotteryEntity.getLotteryCode(), userHasLotteryEntity.getRebateCode());
                userHasLotteryJsonUtility.updateTrackAllUserHasLotteryExceptCommissionAndReward(userHasLotteryEntity.getLotteryCode(), userEntity.getCode(), userHasLotteryEntity.getRebateCode(), token);
            }

            return responseBody(HttpStatus.OK, "Update success except commission & rebate");

        }

        if (userHasLotteryEntity != null) {

            BigDecimal oldShared = userHasLotteryEntity.getShare();

            /*
             * Update
             */
            setPayload(updateUserHasLotteryRQ, userHasLotteryEntity);

            // if upper has no share keep old share then.
            if(parent.getShare() != null && parent.getShare().compareTo(BigDecimal.ZERO) == 0){
                userHasLotteryEntity.setShare(oldShared);
            }

            CommissionRQ commissionRQ = new CommissionRQ();
            BeanUtils.copyProperties(updateUserHasLotteryRQ, commissionRQ);
            userHasLotteryRP.save(userHasLotteryEntity);

            // if user is senior then update user has lotteries for all user below his senior, master, agent, member
            if (!UserConstant.MEMBER.equalsIgnoreCase(userEntity.getRoleCode())) {
                userHasLotteryRP.copyUserHasLotteriesFromUpperLevel(userEntity.getCode(), userEntity.getRoleCode(), userHasLotteryEntity.getLotteryCode(), userHasLotteryEntity.getRebateCode());
            }

            /*
             * add log user has lottery after changed
             */
            activityLogUtility.addActivityLog(updateUserHasLotteryRQ.getLotteryCode(), ActivityLogConstant.USER_HAS_LOTTERY, token.getUserType(), ActivityLogConstant.ACTION_UPDATE, token.getUserCode(), userHasLotteryEntity);

            /*
             * auto log out when change user has lottery
             */
            Date now = new Date();
            userEntity.setLastLoginApp(now);
            userEntity.setLastLoginWeb(now);
            userRP.save(userEntity);
            userCacheSV.removeUserCacheByUsername(userEntity.getUsername());
        } else {
            /*
             * Create
             */
            UserHasLotteryEntity userHasLotteryEntity1 = new UserHasLotteryEntity();
            userHasLotteryEntity1.setUserCode(updateUserHasLotteryRQ.getUserCode());
            userHasLotteryEntity1.setLotteryCode(updateUserHasLotteryRQ.getLotteryCode());
            userHasLotteryEntity1.setRebateCode(updateUserHasLotteryRQ.getRebateCode());
            userHasLotteryEntity1.setRebateRate(generalUtility.getDefaultRebateRate(updateUserHasLotteryRQ.getRebateCode()));
            userHasLotteryEntity1.setWaterRate(updateUserHasLotteryRQ.getWaterRate());
            userHasLotteryEntity1.setShare(updateUserHasLotteryRQ.getShare());
            userHasLotteryEntity1.setCommission(updateUserHasLotteryRQ.getCommission());
            userHasLotteryEntity1.setMaxBetFirst(generalUtility.getDefaultMaxBetFirst(updateUserHasLotteryRQ.getRebateCode()));
            userHasLotteryEntity1.setMaxBetSecond(generalUtility.getDefaultMaxBetSecond(updateUserHasLotteryRQ.getRebateCode()));
            userHasLotteryEntity1.setLimitDigit(generalUtility.getDefaultLimitDigit(updateUserHasLotteryRQ.getRebateCode()));
            userHasLotteryEntity1.setUpdatedBy(jwtToken.getUserToken().getUserCode());

            setPayload(updateUserHasLotteryRQ, userHasLotteryEntity1);

            activityLogUtility.addActivityLog(updateUserHasLotteryRQ.getLotteryCode(), ActivityLogConstant.USER_HAS_LOTTERY, token.getUserType(), ActivityLogConstant.ACTION_ADD, token.getUserCode(), userHasLotteryRP.save(userHasLotteryEntity1));

        }
        /*
         * add user has lottery json for member
         */
        userHasLotteryJsonUtility.trackUserHasLottery(updateUserHasLotteryRQ.getLotteryCode(), userEntity.getCode(), token);

        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
    }


    /**
     * @param userHasLotteryEntity   UserHasLotteryEntity
     * @param updateUserHasLotteryRQ UpdateUserHasLotteryRQ
     */
    private void updateHasLotteryExceptCommissionAndRebateRate(UserHasLotteryEntity userHasLotteryEntity, UpdateUserHasLotteryRQ updateUserHasLotteryRQ) {

        UserToken token = jwtToken.getUserToken();

        UserHasLotteryEntity backup = new UserHasLotteryEntity();
        BeanUtils.copyProperties(userHasLotteryEntity, backup);

        setPayload(updateUserHasLotteryRQ, userHasLotteryEntity);
        userHasLotteryEntity.setCommission(backup.getCommission());
        userHasLotteryEntity.setRebateRate(backup.getRebateRate());
        userHasLotteryEntity.setShare(backup.getShare());

        userHasLotteryRP.save(userHasLotteryEntity);

        /*
         * add log user has lottery after changed
         */
        activityLogUtility.addActivityLog(updateUserHasLotteryRQ.getLotteryCode(), ActivityLogConstant.USER_HAS_LOTTERY, token.getUserType(), ActivityLogConstant.ACTION_UPDATE, token.getUserCode(), userHasLotteryEntity);

        System.out.println("ConfigIP.updateHasLotteryExceptCommissionAndRebateRate");
        System.out.println("Updated By " + token.getUsername());

    }

    private void setPayload(UpdateUserHasLotteryRQ updateUserHasLotteryRQ, UserHasLotteryEntity userHasLotteryEntity) {
        userHasLotteryEntity.setRebateRate(updateUserHasLotteryRQ.getRebateRate());
        userHasLotteryEntity.setWaterRate(updateUserHasLotteryRQ.getWaterRate());
        userHasLotteryEntity.setShare(updateUserHasLotteryRQ.getShare());
        userHasLotteryEntity.setCommission(updateUserHasLotteryRQ.getCommission());
        userHasLotteryEntity.setMaxBetFirst(updateUserHasLotteryRQ.getMaxBetFirst());
        userHasLotteryEntity.setMaxBetSecond(updateUserHasLotteryRQ.getMaxBetSecond());
        userHasLotteryEntity.setLimitDigit(updateUserHasLotteryRQ.getLimitDigit());
        userHasLotteryEntity.setMaxBetSecondMin(updateUserHasLotteryRQ.getMaxBetSecondAt());
        userHasLotteryEntity.setUpdatedBy(jwtToken.getUserToken().getUserCode());
        userHasLotteryEntity.setStatus(updateUserHasLotteryRQ.getStatus());
        if (updateUserHasLotteryRQ.getMaxBetRange() != null) {
            userHasLotteryEntity.setMaxBetRange(updateUserHasLotteryRQ.getMaxBetRange());
        }
        if (updateUserHasLotteryRQ.getMaxBetItemRange() != null) {
            userHasLotteryEntity.setMaxBetItemRange(updateUserHasLotteryRQ.getMaxBetItemRange());
        }
    }

    public StructureRS checkShareCommission(CheckHasLotteryRQ checkHasLotteryRQ) {
        UserEntity userEntity = userRP.findByCode(checkHasLotteryRQ.getUserCode());
        if (userEntity == null) {
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.BAD_REQUEST);
        }
        UserHasLotteryEntity userHasLotteryEntity = userHasLotteryRP.checkUserHasLottery(checkHasLotteryRQ.getLotteryCode(), upperUserCodeUtility.upperUserCode(userEntity), checkHasLotteryRQ.getRebateCode());
        if (userHasLotteryEntity == null) {
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.BAD_REQUEST);
        }
        LotteryConfigRS lotteryConfigRS = new LotteryConfigRS();
        BeanUtils.copyProperties(userHasLotteryEntity, lotteryConfigRS);
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, lotteryConfigRS);
    }

    @Override
    public StructureRS updateRebateSixD(UpdateRebateRQ updateRebateRQ) {
        List<UserEntity> userEntities = userRP.findAllByCodeOrSuperSeniorCode(updateRebateRQ.getUserCode(), updateRebateRQ.getUserCode());
        if (userEntities.size() == 0)
            return responseBody(HttpStatus.NOT_FOUND, MessageConstant.COULD_NOT_FIND_USER);
        List<String> userCodes = userEntities.stream().map(UserEntity::getCode).collect(Collectors.toList());
        List<UserHasLotteryEntity> userHasLotteryEntities = userHasLotteryRP.findByLotteryCodeAndUserCodeIn(LotteryConstant.SIXD, userCodes);
        for (UserHasLotteryEntity item : userHasLotteryEntities) {
            if (LotteryConstant.REBATE6D.equalsIgnoreCase(item.getRebateCode()))
                item.setRebateRate(updateRebateRQ.getSixD());
            if (LotteryConstant.REBATE5D.equalsIgnoreCase(item.getRebateCode()))
                item.setRebateRate(updateRebateRQ.getFiveD());
            if (LotteryConstant.REBATE4D.equalsIgnoreCase(item.getRebateCode()))
                item.setRebateRate(updateRebateRQ.getFourD());
            if (LotteryConstant.REBATE3D.equalsIgnoreCase(item.getRebateCode()))
                item.setRebateRate(updateRebateRQ.getThreeD());
            if (LotteryConstant.REBATE2D.equalsIgnoreCase(item.getRebateCode()))
                item.setRebateRate(updateRebateRQ.getTwoD());
        }

        userHasLotteryRP.saveAll(userHasLotteryEntities);

        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
    }

    @Async
    public void copyUserHasLottery(UserEntity userEntity) {
        String userCode = upperUserCodeUtility.upperUserCode(userEntity);
        List<UserHasLotteryEntity> hasLotteryEntities = userHasLotteryRP.userHasLotteries(userCode);
        List<UserHasLotteryEntity> hasLotteryEntityList = new ArrayList<>();
        if (hasLotteryEntities.size() > 0) {
            for (UserHasLotteryEntity item : hasLotteryEntities) {
                UserHasLotteryEntity entity = new UserHasLotteryEntity();
                BeanUtils.copyProperties(item, entity);
                entity.setId(null);
                entity.setUserCode(userEntity.getCode());
                hasLotteryEntityList.add(entity);
            }
            userHasLotteryRP.saveAll(hasLotteryEntityList);
        }
    }

    @Override
    public void createUserHasLotteryFromCommissionList(UserEntity userEntity, List<CommissionLotteryRQ> commissionRQList, UserToken userToken) {
        try {
            List<UserHasLotteryEntity> userHasLotteryEntities = new ArrayList<>();
            commissionRQList.forEach(item -> item.getItems().forEach(commission -> {
                UserHasLotteryEntity userHasLotteryEntity = new UserHasLotteryEntity();
                userHasLotteryEntity.setUserCode(userEntity.getCode());
                userHasLotteryEntity.setLotteryCode(item.getLotteryType());
                userHasLotteryEntity.setRebateCode(commission.getRebateCode());
                userHasLotteryEntity.setWaterRate(commission.getWaterRate());
                userHasLotteryEntity.setShare(commission.getShare());
                userHasLotteryEntity.setCommission(commission.getCommission());
                userHasLotteryEntity.setRebateRate(commission.getRebateRate());
                userHasLotteryEntity.setMaxBetFirst(commission.getMaxBetFirst());
                userHasLotteryEntity.setMaxBetSecond(commission.getMaxBetSecond());
                userHasLotteryEntity.setMaxBetSecondMin(commission.getMaxBetSecondAt());
                userHasLotteryEntity.setLimitDigit(commission.getLimitDigit());
                userHasLotteryEntity.setMaxBetRange(commission.getMaxBetRange());
                userHasLotteryEntity.setMaxBetItemRange(commission.getMaxBetItemRange());
                userHasLotteryEntities.add(userHasLotteryEntity);
            }));

            userHasLotteryRP.saveAll(userHasLotteryEntities);
            /*
             * add track user has lottery
             */
            commissionRQList.forEach(item -> {
                userHasLotteryJsonUtility.trackUserHasLottery(item.getLotteryType(), userEntity.getCode(), userToken);
            });
        } catch (Exception ex) {
            System.out.println("ConfigIP.createUserHasLotteryFromCommissionList Exception");
            ex.printStackTrace();
        }

    }


    /**
     * Manage lottery
     *
     * @param userCode
     * @param type
     */
    @Override
    public StructureRS lottery(String userCode, String type) {
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, getLotteryRSList(userCode, type));
    }

    public List<LotteryRS> getLotteryRSList(String userCode, String type) {
        List<LotteryRS> lotteryRSList = new ArrayList<>();
        if (userCode != null) {
            UserEntity child = userRP.findByCode(userCode);
            String lottery = child.getLotteryType();
            if (!"child".equalsIgnoreCase(type)) {
                UserEntity parent = userRP.findByCode(staticUpperUserCode(child));
                lottery = parent.getLotteryType();
            }
            if (lottery == null)
                return lotteryRSList;
            return getLotteryRSByLottery(lottery);
        }

        List<LotteryEntity> lotteryEntities = lotteryRP.listing();
        lotteryEntities.forEach(item -> {
            LotteryRS lotteryRS = new LotteryRS();
            BeanUtils.copyProperties(item, lotteryRS);
            lotteryRSList.add(lotteryRS);
        });

        return lotteryRSList;
    }

    private List<LotteryRS> getLotteryRSByLottery(String lotteryType) {
        List<LotteryRS> lotteryRSList = new ArrayList<>();
        List<String> lotteries = Arrays.stream(lotteryType.split(",")).distinct().collect(Collectors.toList());
        for (String lottery : lotteries) {
            LotteryRS lotteryRS = new LotteryRS();
            lotteryRS.setCode(lottery);
            lotteryRS.setName(lottery);

            if (lottery.equals(LotteryConstant.VN2))
                lotteryRS.setName(LotteryConstant.MT);
            lotteryRSList.add(lotteryRS);
        }
        return lotteryRSList;
    }

    @Override
    public StructureRS listRebate() {
        RebateListRQ rebateListRQ = new RebateListRQ(request);
        List<RebatesEntity> rebatesEntities = rebatesRP.listing(rebateListRQ.getLotteryType());
        List<RebateRS> rebateRS = new ArrayList<>();
        rebatesEntities.forEach(item -> {
            RebateRS rebateRS1 = new RebateRS();
            BeanUtils.copyProperties(item, rebateRS1);
            rebateRS.add(rebateRS1);
        });
        return responseBody(HttpStatus.OK,
                MessageConstant.SUCCESSFULLY,
                rebateRS);
    }

    @Override
    public StructureRS filterAbleRebate(String lotteryType, String filterByLevel) {
        UserToken userToken = jwtToken.getUserToken();
        List<RebateDTO> rebateList = rebatesRP.getRebate(lotteryType, filterByLevel, userToken.getUserType(), userToken.getUserCode(), userToken.getUserRole());
        return responseBodyWithSuccessMessage(rebateList);
    }

    @Override
    public StructureRS updateRebate(Integer id, RebateRQ rebateRQ) {
        RebatesEntity rebatesEntity = rebatesRP.getOne(id.longValue());
        rebatesEntity.setRebateRate(rebateRQ.getRebateRate());
        rebatesEntity.setWaterRate(rebateRQ.getWaterRate());
        rebatesEntity.setStatus(rebateRQ.getStatus());
        rebatesEntity.setUpdatedBy(jwtToken.getUserToken().getUserCode());
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, rebatesRP.save(rebatesEntity));
    }

    @Override
    public StructureRS shift(String lotteryType) {
        List<ShiftsEntity> shiftsEntities = shiftsRP.shifts(lotteryType);
        List<ShiftRS> shiftRS = shiftsEntities.stream().map(ShiftRS::new).collect(Collectors.toList());
        return responseBodyWithSuccessMessage(shiftRS);
    }

    /**
     * set shifts to user has lottery
     *
     * @param commissionList List<Commission>
     * @param drawingDTOList List<DrawingDTO>
     * @param userEntity     UserEntity
     */
    private void setShiftsToUserHasLottery(List<Commission> commissionList, List<DrawingDTO> drawingDTOList, UserEntity userEntity, String lotteryType) {
        String superSeniorCode = "";
        if (userEntity != null) {
            superSeniorCode = userEntity.getSuperSeniorCode();
            if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode())) {
                superSeniorCode = userEntity.getCode();
            }
        }
        String finalSuperSeniorCode = superSeniorCode;
        List<String> defaultMinutes = new ArrayList<>();
        for (DrawingDTO dw : drawingDTOList) {
            defaultMinutes.add("30");
        }

        commissionList.forEach(commission -> {
            List<ShiftMaxBetRS> shifts = new ArrayList<>();
            List<String> minutes = new ArrayList<>(defaultMinutes);
            if (commission.getMaxBetSecondAt() != null) {
                minutes = List.of(commission.getMaxBetSecondAt().split(","));
            }
            commission.setMaxBetSecondAt(String.join(",", minutes));
            for (int index = 0; index < drawingDTOList.size(); index++) {
                DrawingDTO drawingDTO = drawingDTOList.get(index);

                /*
                 * update stop lo and post base on super-senior code
                 */
                generalUtility.updateStopBetBaseOnUserCode(drawingDTO, lotteryType, finalSuperSeniorCode);

                ShiftMaxBetRS shiftMaxBetRS = new ShiftMaxBetRS();
                shiftMaxBetRS.setDrawAt(drawingDTO.getResultedPostAt());
                shiftMaxBetRS.setStopLoAt(drawingDTO.getStoppedLoAt());
                shiftMaxBetRS.setStopPostAt(drawingDTO.getStoppedPostAt());
                shiftMaxBetRS.setMaxBetSecondAt(DateUtils.addMinutes(drawingDTO.getResultedPostAt(), -30));

                if (minutes.size() -1 > index) {
                    shiftMaxBetRS.setMaxBetSecondAt(DateUtils.addMinutes(drawingDTO.getResultedPostAt(), Integer.parseInt(minutes.get(index)) * (-1)));
                }
                shifts.add(shiftMaxBetRS);
            }
            commission.setShifts(shifts);
        });
    }

    @Override
    public StructureRS getLotteryType() {
        UserToken userToken = jwtToken.getUserToken();
        String userCode = userToken.getUserCode();
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
        }
        String lotteryTypeSuper = upperUserCodeUtility.getLotteryTypeFromSuperSeniorByCode(userCode);
        List<String> lotteries = new ArrayList<>(List.of(lotteryTypeSuper.split(",")));
        return responseBodyWithSuccessMessage(lotteries);
    }
}
