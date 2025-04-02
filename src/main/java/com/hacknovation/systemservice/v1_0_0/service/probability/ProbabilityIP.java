package com.hacknovation.systemservice.v1_0_0.service.probability;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.ProbabilityEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.ProbabilityHasDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.ProbabilityTypeEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.ShiftsEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.ProbabilityHasDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.ProbabilityRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.ProbabilityTypeRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.ShiftsRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.probability.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.probability.ProbabilityItemDrawRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.probability.ProbabilityItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.probability.ProbabilityKeyRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 22:39
 */
@Service
@RequiredArgsConstructor
public class ProbabilityIP extends BaseServiceIP implements ProbabilitySV {

    private final ProbabilityTypeRP probabilityTypeRP;
    private final ProbabilityRP probabilityRP;
    private final ProbabilityHasDrawingRP probabilityHasDrawingRP;
    private final ShiftsRP shiftsRP;
    private final HttpServletRequest servletRequest;
    private final ActivityLogUtility activityLogUtility;
    private final JwtToken jwtToken;

    @Override
    public StructureRS probabilityRule() {
        return responseBodyWithSuccessMessage(probabilityTypeRP.getAllProbabilityRules());
    }

    @Override
    public StructureRS updateProbabilityRule(UpdateProbabilityRuleRQ updateProbabilityRuleRQ) {

        ProbabilityTypeEntity probabilityTypeEntity = probabilityTypeRP.getByLotteryType(updateProbabilityRuleRQ.getLotteryType());
        if (probabilityTypeEntity == null)
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);

        UserToken token = jwtToken.getUserToken();
        probabilityTypeEntity.setIsPercentageAllDrawing(updateProbabilityRuleRQ.getIsPercentageAllDraws());
        probabilityTypeEntity.setUpdatedBy(token.getUsername());

        if (LotteryConstant.LEAP.equals(probabilityTypeEntity.getLotteryType()))
            probabilityTypeRP.updateLeapDrawingTableIsHasDrawing(!probabilityTypeEntity.getIsPercentageAllDrawing());
        if (LotteryConstant.VN2.equals(probabilityTypeEntity.getLotteryType()))
            probabilityTypeRP.updateVN2DrawingTableIsHasDrawing(!probabilityTypeEntity.getIsPercentageAllDrawing());
        if (LotteryConstant.SC.equals(probabilityTypeEntity.getLotteryType()))
            probabilityTypeRP.updateSCDrawingTableIsHasDrawing(!probabilityTypeEntity.getIsPercentageAllDrawing());
        if (LotteryConstant.TN.equals(probabilityTypeEntity.getLotteryType()))
            probabilityTypeRP.updateTNDrawingTableIsHasDrawing(!probabilityTypeEntity.getIsPercentageAllDrawing());

        System.out.println("ProbabilityIP.updateProbabilityRule lottery " + probabilityTypeEntity.getLotteryType());

        probabilityTypeRP.save(probabilityTypeEntity);


        activityLogUtility.addActivityLog(
                probabilityTypeEntity.getLotteryType(),
                ActivityLogConstant.MODULE_PROBABILITY_TYPE,
                token.getUserType(),
                ActivityLogConstant.ACTION_UPDATE,
                token.getUserCode(),
                probabilityTypeEntity);

        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS probabilityKeyList() {
        List<ProbabilityKeyRS> configs = new ArrayList<>();
        configs.add(new ProbabilityKeyRS(LotteryConstant.IS_PRO_RANDOM, LotteryConstant.IS_PRO_RANDOM_KEY));
        configs.add(new ProbabilityKeyRS(LotteryConstant.IS_PRO_HIGH, LotteryConstant.IS_PRO_HIGH_KEY));
        configs.add(new ProbabilityKeyRS(LotteryConstant.IS_PRO_LOW, LotteryConstant.IS_PRO_LOW_KEY));
        return responseBodyWithSuccessMessage(configs);
    }

    @Override
    public StructureRS probabilityList() {
        ProbabilityListRQ probabilityListRQ = new ProbabilityListRQ(servletRequest);

        List<ProbabilityEntity> probabilityEntities = probabilityRP.findAllByLotteryType(probabilityListRQ.getLotteryType());
        List<ProbabilityItemRS> probabilityItemRSList = probabilityEntities.stream().map(ProbabilityItemRS::new).collect(Collectors.toList());

        return responseBodyWithSuccessMessage(probabilityItemRSList);
    }

    @Override
    public StructureRS updateProbability(List<UpdateProbabilityRQ> updateProbabilityRQList) {
        for (UpdateProbabilityRQ updateProbabilityRQ : updateProbabilityRQList) {
            StructureRS data = updateProbability(updateProbabilityRQ);
            if (data.getStatus() != HttpStatus.OK.value()) {
                return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
            }
        }
        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS probabilityDrawList() {
        ProbabilityListRQ probabilityListRQ = new ProbabilityListRQ(servletRequest);
        List<ProbabilityHasDrawingEntity> probabilityHasDrawingEntities = probabilityHasDrawingRP.findAllByLotteryTypeAndShiftCode(probabilityListRQ.getLotteryType(), probabilityListRQ.getShiftCode());
        List<ProbabilityItemDrawRS> probabilityItemDrawRSList = probabilityHasDrawingEntities
                .stream().map(ProbabilityItemDrawRS::new).collect(Collectors.toList());

        ShiftsEntity shiftsEntity = shiftsRP.findShiftByLotteryTypeAndShiftCode(probabilityListRQ.getLotteryType(), probabilityListRQ.getShiftCode());

        if (LotteryConstant.VN2.equals(probabilityListRQ.getLotteryType()) && shiftsEntity != null) {
            if (!shiftsEntity.getIsNight()) {
                probabilityItemDrawRSList.removeIf(it -> List.of("A2", "A3", "A4").contains(it.getPostCode()));
            } else {
                probabilityItemDrawRSList.removeIf(it -> List.of("F", "I", "N", "K").contains(it.getPostCode()));
            }
        }

        if (LotteryConstant.TN.equals(probabilityListRQ.getLotteryType()) && shiftsEntity != null) {
            if (shiftsEntity.getIsNight()) {
                probabilityItemDrawRSList.removeIf(it -> List.of("F", "I", "N", "K", "Z", "P").contains(it.getPostCode()));
            } else {
                probabilityItemDrawRSList.removeIf(it -> List.of("A2", "A3", "A4").contains(it.getPostCode()));
            }
        }

        return responseBodyWithSuccessMessage(probabilityItemDrawRSList);
    }

    @Override
    public StructureRS updateProbabilityDraw(List<UpdateProbabilityDrawRQ> updateProbabilityDrawRQList) {
        for (UpdateProbabilityDrawRQ updateProbabilityDrawRQ : updateProbabilityDrawRQList) {
            StructureRS data = updateProbabilityDraw(updateProbabilityDrawRQ);
            if (data.getStatus() != HttpStatus.OK.value()) {
                return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
            }
        }
        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS updateAllProbability(UpdateAllProbabilityRQ updateAllProbabilityRQ) {

        List<ProbabilityEntity> probabilityEntities = probabilityRP.findAllByLotteryType(updateAllProbabilityRQ.getLotteryType());
        if (probabilityEntities.isEmpty())
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);

        UserToken userToken = jwtToken.getUserToken();
        if (updateAllProbabilityRQ.getProKey() != null && !updateAllProbabilityRQ.getProKey().isBlank()) {
            probabilityEntities.forEach(it -> {
                it.setProbabilityKey(updateAllProbabilityRQ.getProKey());
                it.setUpdatedBy(userToken.getUsername());
            });
        }

        if (updateAllProbabilityRQ.getPercentage() != null && updateAllProbabilityRQ.getPercentage().compareTo(BigDecimal.ZERO) > 0) {
            probabilityEntities.forEach(it -> {
                it.setPercentage(updateAllProbabilityRQ.getPercentage());
                it.setUpdatedBy(userToken.getUsername());
            });
        }

        probabilityRP.saveAll(probabilityEntities);

        activityLogUtility.addActivityLog(
                updateAllProbabilityRQ.getLotteryType(),
                ActivityLogConstant.MODULE_PROBABILITY_ALL,
                userToken.getUserType(),
                ActivityLogConstant.ACTION_UPDATE,
                userToken.getUserCode(),
                updateAllProbabilityRQ
        );

        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS updateAllProbabilityDraw(UpdateAllProbabilityDrawRQ probabilityDrawRQ) {
        List<ProbabilityHasDrawingEntity> probabilityHasDrawingEntities = probabilityHasDrawingRP.findAllByLotteryTypeAndShiftCode(probabilityDrawRQ.getLotteryType(), probabilityDrawRQ.getShiftCode());
        if (probabilityHasDrawingEntities.isEmpty())
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);

        UserToken userToken = jwtToken.getUserToken();
        if (probabilityDrawRQ.getProKey() == null || probabilityDrawRQ.getProKey().isBlank()) {
            return responseBodyWithBadRequest(MessageConstant.PROBABILITY_KEY_REQUIRED, MessageConstant.PROBABILITY_KEY_REQUIRED_KEY);
        }
        probabilityHasDrawingEntities.forEach(it -> {
            it.setProbabilityKey(probabilityDrawRQ.getProKey());
            it.setUpdatedBy(userToken.getUsername());
        });

        if (probabilityDrawRQ.getPercentage() == null || probabilityDrawRQ.getPercentage().compareTo(BigDecimal.ZERO) < 0) {
            return responseBodyWithBadRequest(MessageConstant.PROBABILITY_PERCENTAGE_REQUIRED, MessageConstant.PROBABILITY_PERCENTAGE_REQUIRED_KEY);
        }
        probabilityHasDrawingEntities.forEach(it -> {
            it.setPercentage(probabilityDrawRQ.getPercentage());
            it.setUpdatedBy(userToken.getUsername());
        });

        probabilityHasDrawingRP.saveAll(probabilityHasDrawingEntities);

        activityLogUtility.addActivityLog(
                probabilityDrawRQ.getLotteryType(),
                ActivityLogConstant.MODULE_PROBABILITY_HAS_DRAWING_ALL,
                userToken.getUserType(),
                ActivityLogConstant.ACTION_UPDATE,
                userToken.getUserCode(),
                probabilityDrawRQ
        );

        return responseBodyWithSuccessMessage();
    }

    private StructureRS updateProbability(UpdateProbabilityRQ updateProbabilityRQ) {

        UserToken token = jwtToken.getUserToken();

        List<ProbabilityEntity> probabilityEntities = probabilityRP.findAllByLotteryTypeAndPostCode(updateProbabilityRQ.getLotteryType(), updateProbabilityRQ.getPostCode());
        if (probabilityEntities.size() > 0) {
            probabilityEntities
                    .stream()
                    .filter(it -> it.getRebateCode().equals(updateProbabilityRQ.getRebateCode()))
                    .findFirst()
                    .ifPresent(probability -> {
                        probability.setIsProbability(updateProbabilityRQ.getIsProbability());
                        probability.setProbabilityKey(updateProbabilityRQ.getProKey());
                        probability.setPercentage(updateProbabilityRQ.getPercentage());
                        probability.setUpdatedBy(token.getUsername());
                    });
            if (LotteryConstant.LEAP.equalsIgnoreCase(updateProbabilityRQ.getLotteryType())) {
                probabilityEntities.forEach(probability -> {
                    probability.setProbabilityKey(updateProbabilityRQ.getProKey());
                    probability.setUpdatedBy(token.getUsername());
                });
            }

            if (LotteryConstant.VN2.equalsIgnoreCase(updateProbabilityRQ.getLotteryType())) {
                if (List.of("LO", "K").contains(updateProbabilityRQ.getPostCode())) {
                    probabilityEntities.forEach(probability -> {
                        probability.setProbabilityKey(updateProbabilityRQ.getProKey());
                        probability.setUpdatedBy(token.getUsername());
                    });
                }
            }

            probabilityRP.saveAll(probabilityEntities);

            activityLogUtility.addActivityLog(
                    updateProbabilityRQ.getLotteryType(),
                    ActivityLogConstant.MODULE_PROBABILITY,
                    token.getUserType(),
                    ActivityLogConstant.ACTION_UPDATE,
                    token.getUserCode(),
                    updateProbabilityRQ
            );

            return responseBodyWithSuccessMessage();
        }

        return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
    }

    private StructureRS updateProbabilityDraw(UpdateProbabilityDrawRQ updateProbabilityDrawRQ) {

        UserToken token = jwtToken.getUserToken();

        List<ProbabilityHasDrawingEntity> probabilityHasDrawingEntities = probabilityHasDrawingRP.findAllByLotteryTypeAndShiftCodeAndPostCode(
                updateProbabilityDrawRQ.getLotteryType(),
                updateProbabilityDrawRQ.getShiftCode(),
                updateProbabilityDrawRQ.getPostCode());

        if (probabilityHasDrawingEntities.size() > 0) {
            probabilityHasDrawingEntities
                    .stream()
                    .filter(it -> it.getRebateCode().equals(updateProbabilityDrawRQ.getRebateCode()))
                    .findFirst()
                    .ifPresent(probability -> {
                        probability.setIsProbability(updateProbabilityDrawRQ.getIsProbability());
                        probability.setProbabilityKey(updateProbabilityDrawRQ.getProKey());
                        probability.setPercentage(updateProbabilityDrawRQ.getPercentage());
                        probability.setUpdatedBy(token.getUsername());
                    });
            if (LotteryConstant.LEAP.equalsIgnoreCase(updateProbabilityDrawRQ.getLotteryType())) {
                probabilityHasDrawingEntities.forEach(probability -> {
                    probability.setProbabilityKey(updateProbabilityDrawRQ.getProKey());
                    probability.setUpdatedBy(token.getUsername());
                });
            }

            if (LotteryConstant.VN2.equalsIgnoreCase(updateProbabilityDrawRQ.getLotteryType())) {
                if (List.of("LO", "K").contains(updateProbabilityDrawRQ.getPostCode())) {
                    probabilityHasDrawingEntities.forEach(probability -> {
                        probability.setProbabilityKey(updateProbabilityDrawRQ.getProKey());
                        probability.setUpdatedBy(token.getUsername());
                    });
                }
            }

            probabilityHasDrawingRP.saveAll(probabilityHasDrawingEntities);

            activityLogUtility.addActivityLog(
                    updateProbabilityDrawRQ.getLotteryType(),
                    ActivityLogConstant.MODULE_PROBABILITY_HAS_DRAWING,
                    token.getUserType(),
                    ActivityLogConstant.ACTION_UPDATE,
                    token.getUserCode(),
                    updateProbabilityDrawRQ
            );

            return responseBodyWithSuccessMessage();
        }

        return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
    }
}
