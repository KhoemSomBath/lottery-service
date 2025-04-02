package com.hacknovation.systemservice.v1_0_0.service.manageresult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.*;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.PostReleaseConfigEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.PostReleaseConfigRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.analyze.AnalyzeSV;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.PostReleaseConfigTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.manageresult.ManageResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyzelog.AnalyseLogItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyzelog.AnalyzeLogRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.ResultRenderRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hacknovation.systemservice.v1_0_0.utility.ResultUtility.*;

/**
 * @author Sombath
 * create at 5/9/22 4:56 PM
 */

@Service
@RequiredArgsConstructor
public class ManageResultIP extends BaseServiceIP implements ManageResultSV {

    private final AnalyzeSV analyzeSV;
    private final JwtToken jwtToken;
    private final ActivityLogUtility activityLogUtility;

    private final PostReleaseConfigRP postReleaseConfigRP;

    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final VNTwoTempDrawingItemsRP vnTwoTempDrawingItemsRP;

    private final LeapTempDrawingRP leapTempDrawingRP;
    private final LeapTempDrawingItemsRP leapTempDrawingItemsRP;

    private final TNTempDrawingRP tnTempDrawingRP;
    private final TNTempDrawingItemsRP tnTempDrawingItemsRP;

    private final SCTempDrawingRP scTempDrawingRP;
    private final SCTempDrawingItemRP scTempDrawingItemRP;

    private final HazelcastInstance hazelcastInstance;

    private final static String MT_COLLECTION_NAME = "mt_manage_result";
    private final static String TN_COLLECTION_NAME = "tn_manage_result";
    private final static String LEAP_COLLECTION_NAME = "leap_manage_result";
    private final static String SC_COLLECTION_NAME = "sc_manage_result";
    private final THTempDrawingRP tHTempDrawingRP;
    //    private final THTempDrawingItemRP tHTempDrawingItemRP;

    @Override
    public StructureRS getManageResultForm(String lottery) {
        switch (lottery.toUpperCase()) {
            case LotteryConstant.VN2:
            case LotteryConstant.MT:
                VNTwoTempDrawingEntity vnTwoTempDrawingEntity = vnTwoTempDrawingRP.recentDrawing();
                if (vnTwoTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.NO_DRAW_LEFT);
                return mtManageResultList(vnTwoTempDrawingEntity);
            case LotteryConstant.TN:
                TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.recentDrawing();
                if (tnTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.NO_DRAW_LEFT);
                return tnManageResultList(tnTempDrawingEntity);
            case LotteryConstant.LEAP:
                LeapTempDrawingEntity leapTempDrawingEntity = leapTempDrawingRP.recentDrawing();
                if (leapTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.NO_DRAW_LEFT);
                return leapManageResultList(leapTempDrawingEntity);
            case LotteryConstant.SC:
                SCTempDrawingEntity scTempDrawingEntity = scTempDrawingRP.recentDrawing();
                if (scTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.NO_DRAW_LEFT);
                return scManageResultList(scTempDrawingEntity);
            case LotteryConstant.TH:
                THTempDrawingEntity thTempDrawingEntity = tHTempDrawingRP.recentDrawing();
                if (thTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.NO_DRAW_LEFT);
                return responseBodyWithSuccessMessage();
        }
        return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.UNKNOWN_TYPE);
    }

    @Override
    public StructureRS setManageResultForm(ManageResultRQ manageResultRQ) {
        String collectionName;
        String postCode;
        Map<String, Map<String, Integer>> mapPostTimer;
        boolean isEditAble;

        switch (manageResultRQ.getLotteryType().toUpperCase()){
            case LotteryConstant.LEAP:
                collectionName = LEAP_COLLECTION_NAME;

                LeapTempDrawingEntity leapTempDrawingEntity = leapTempDrawingRP.findByCode(manageResultRQ.getDrawCode());
                if (leapTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);

                mapPostTimer = getLeapPostMinute();
                isEditAble = isEditable(leapTempDrawingEntity.getResultedPostAt(), mapPostTimer.get(manageResultRQ.getPostCode()));

                if (!isEditAble)
                    throw new BadRequestException(MessageConstant.UNABLE_TO_EDIT_RIGHT_NOW);
                break;
            case LotteryConstant.MT:
            case LotteryConstant.VN2:
                collectionName = MT_COLLECTION_NAME;
                postCode = manageResultRQ.getPostCode();

                if (List.of(PostConstant.POST_A, PostConstant.POST_F, PostConstant.POST_I, PostConstant.POST_A2, PostConstant.POST_A3, PostConstant.POST_A4).contains(postCode))
                    postCode = postCode.concat("_").concat(manageResultRQ.getRebateCode());

                VNTwoTempDrawingEntity vnTwoTempDrawingEntity = vnTwoTempDrawingRP.findByCode(manageResultRQ.getDrawCode());
                if (vnTwoTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);

                mapPostTimer = getMTPostMinute(vnTwoTempDrawingEntity.getIsNight());
                isEditAble = isEditable(vnTwoTempDrawingEntity.getResultedPostAt(), mapPostTimer.get(postCode));


                if (vnTwoTempDrawingEntity.isDay() && PostConstant.POST_B_N.contains(manageResultRQ.getPostCode())) {
                    PostReleaseConfigEntity postReleaseConfigEntity = postReleaseConfigRP.findByLotteryTypeAndPostCode(LotteryConstant.VN2, manageResultRQ.getPostCode());
                    Boolean isPostCanRelease = true;
                    if (postReleaseConfigEntity != null)
                        isPostCanRelease = postReleaseConfigEntity.getIsCanRelease();

                    if (isPostCanRelease && !isEditAble)
                        throw new BadRequestException(MessageConstant.UNABLE_TO_EDIT_RIGHT_NOW);

                } else if (!isEditAble)
                    throw new BadRequestException(MessageConstant.UNABLE_TO_EDIT_RIGHT_NOW);
                break;
            case LotteryConstant.TN:
                collectionName = TN_COLLECTION_NAME;
                postCode = manageResultRQ.getPostCode();

                if (List.of(PostConstant.POST_A, PostConstant.POST_F, PostConstant.POST_I, PostConstant.POST_A2, PostConstant.POST_A3, PostConstant.POST_A4).contains(postCode))
                    postCode = postCode.concat("_").concat(manageResultRQ.getRebateCode());

                TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.findByCode(manageResultRQ.getDrawCode());
                if (tnTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);

                mapPostTimer = getTNPostMinute(tnTempDrawingEntity.getShiftCode());
                isEditAble = isEditable(tnTempDrawingEntity.getResultedPostAt(), mapPostTimer.get(postCode));

                if (tnTempDrawingEntity.isDay() && PostConstant.POST_B_N_P.contains(manageResultRQ.getPostCode())) {
                    PostReleaseConfigEntity postReleaseConfigEntity = postReleaseConfigRP.findByLotteryTypeAndPostCode(LotteryConstant.TN, manageResultRQ.getPostCode());
                    Boolean isPostCanRelease = true;
                    if (postReleaseConfigEntity != null)
                        isPostCanRelease = postReleaseConfigEntity.getIsCanRelease();

                    if (isPostCanRelease && !isEditAble)
                        throw new BadRequestException(MessageConstant.UNABLE_TO_EDIT_RIGHT_NOW);

                } else if (!isEditAble)
                    throw new BadRequestException(MessageConstant.UNABLE_TO_EDIT_RIGHT_NOW);
                break;
            case LotteryConstant.SC:
                collectionName = SC_COLLECTION_NAME;

                SCTempDrawingEntity scTempDrawingEntity = scTempDrawingRP.findByCode(manageResultRQ.getDrawCode());
                if (scTempDrawingEntity == null)
                    throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);

                mapPostTimer = getSCPostMinute();
                isEditAble = isEditable(scTempDrawingEntity.getResultedPostAt(), mapPostTimer.get(manageResultRQ.getPostCode()));

                if (!isEditAble)
                    throw new BadRequestException(MessageConstant.UNABLE_TO_EDIT_RIGHT_NOW);
                break;

            default:
                throw new BadRequestException(MessageConstant.UNKNOWN_TYPE);
        }

        try {
            manageResultRQ.setUserCode(jwtToken.getUserToken().getUserCode());
            long time = Instant.now().getEpochSecond();
            hazelcastInstance.getMap(collectionName).put(Long.toString(time), new ObjectMapper().writeValueAsString(manageResultRQ), 0, TimeUnit.SECONDS, 10, TimeUnit.MINUTES);
            activityLogUtility.addActivityLog(manageResultRQ.getLotteryType(), ActivityLogConstant.SET_RESULT, jwtToken.getUserToken().getUserType(), ActivityLogConstant.ACTION_UPDATE, jwtToken.getUserToken().getUserCode(), manageResultRQ);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return responseBody(HttpStatus.BAD_REQUEST, "Could not convert object request to string");
        }
        return responseBodyWithSuccessMessage();
    }

    private StructureRS leapManageResultList(LeapTempDrawingEntity drawingEntity) {
        AnalyzeLogRS analyzeLogRS = analyzeSV.leapAnalyzeLog(drawingEntity.getCode());
        List<LeapTempDrawingItemsEntity> leapTempDrawingItemsEntities = leapTempDrawingItemsRP.getAllByDrawId(drawingEntity.getId());
        Map<String, Map<String, Integer>> mapPostTimer = getLeapPostMinute();

        for (AnalyseLogItemRS item : analyzeLogRS.getItems()) {
            Optional<LeapTempDrawingItemsEntity> optionalLeapTempDrawingItemsEntity = leapTempDrawingItemsEntities
                    .stream()
                    .filter(it -> it.getPostCode().equals(item.getPostCode()))
                    .findFirst();

            if (optionalLeapTempDrawingItemsEntity.isPresent()) {
                LeapTempDrawingItemsEntity drawingItemsEntity = optionalLeapTempDrawingItemsEntity.get();
                item.setFullNumber(drawingItemsEntity.getPostGroup().equals(PostConstant.POST_GROUP) ? drawingItemsEntity.getSixDigits() : drawingItemsEntity.getFiveDigits());
                item.setIsEditable(drawingItemsEntity.getTwoDigits() != null && isEditable(drawingEntity.getResultedPostAt(), mapPostTimer.get(item.getPostCode())));
            }
        }

        return responseBodyWithSuccessMessage(analyzeLogRS);
    }

    private StructureRS scManageResultList(SCTempDrawingEntity drawingEntity) {
        AnalyzeLogRS analyzeLogRS = analyzeSV.scAnalyzeLog(drawingEntity.getCode());
        List<SCTempDrawingItemsEntity> scTempDrawingItemsEntities = scTempDrawingItemRP.getAllByDrawingId(drawingEntity.getId().intValue());
        Map<String, Map<String, Integer>> mapPostTimer = getSCPostMinute();

        for (AnalyseLogItemRS item : analyzeLogRS.getItems()) {
            Optional<SCTempDrawingItemsEntity> optTempDrawingItemsEntity = scTempDrawingItemsEntities
                    .stream()
                    .filter(it -> it.getPostCode().equals(item.getPostCode()))
                    .findFirst();

            if (optTempDrawingItemsEntity.isPresent()) {
                SCTempDrawingItemsEntity drawingItemsEntity = optTempDrawingItemsEntity.get();
                item.setFullNumber(drawingItemsEntity.getSixDigits());
                item.setIsEditable(drawingItemsEntity.getTwoDigits() != null && isEditable(drawingEntity.getResultedPostAt(), mapPostTimer.get(item.getPostCode())));
            }
        }

        return responseBodyWithSuccessMessage(analyzeLogRS);
    }


    private StructureRS mtManageResultList(VNTwoTempDrawingEntity drawingEntity) {

        List<PostReleaseConfigEntity> postReleaseConfigEntities = postReleaseConfigRP.findByLotteryTypeAndPostCodeIn(LotteryConstant.VN2, List.of(PostConstant.POST_B, PostConstant.POST_N));
        Map<String, PostReleaseConfigEntity> postReleaseConfigEntityMap = postReleaseConfigEntities
                .stream()
                .collect(Collectors.toMap(PostReleaseConfigEntity::getPostCode, Function.identity()));

        AnalyzeLogRS analyzeLogRS = analyzeSV.vnTwoAnalyzeLog(drawingEntity);
        List<VNTwoTempDrawingItemsEntity> vnTwoTempDrawingItemsEntities = vnTwoTempDrawingItemsRP.getAllByDrawId(drawingEntity.getId());
        Map<String, Map<String, Integer>> mapPostTimer = getMTPostMinute(drawingEntity.getIsNight());

        for (AnalyseLogItemRS item : analyzeLogRS.getItems()) {
            Optional<VNTwoTempDrawingItemsEntity> optionalLeapTempDrawingItemsEntity = vnTwoTempDrawingItemsEntities
                    .stream()
                    .filter(it -> it.getPostCode().equals(item.getPostCode()))
                    .findFirst();

            if (optionalLeapTempDrawingItemsEntity.isPresent()) {
                VNTwoTempDrawingItemsEntity drawingItemsEntity = optionalLeapTempDrawingItemsEntity.get();
                String postCode = item.getPostCode();
                if (drawingEntity.isDay()) {
                    if (drawingItemsEntity.getPostGroup().equals(PostConstant.LO_GROUP) && PostConstant.LO_1_TO_4.contains(item.getPostCode()))
                        item.setFullNumber(drawingItemsEntity.getFourDigits());
                    else {
                        switch (item.getPostCode()) {
                            case PostConstant.POST_K:
                                item.setFullNumber(drawingItemsEntity.getFourDigits());
                                break;
                            case PostConstant.POST_A:
                            case PostConstant.POST_F:
                            case PostConstant.POST_I:
                                postCode = postCode.concat("_").concat(item.getRebateCode());
                                if (item.getRebateCode().equals(LotteryConstant.REBATE2D))
                                    item.setFullNumber(drawingItemsEntity.getTwoDigits());
                                else
                                    item.setFullNumber(drawingItemsEntity.getThreeDigits());
                                break;
                            default:
                                if (PostConstant.POST_B_N.contains(item.getPostCode())) {
                                    item.setFullNumber(drawingItemsEntity.getSixDigits());
                                    PostReleaseConfigEntity configEntity = postReleaseConfigEntityMap.get(item.getPostCode());
                                    if (configEntity != null)
                                        item.setIsCanRelease(configEntity.getIsCanRelease());
                                } else
                                    item.setFullNumber(drawingItemsEntity.getFiveDigits());
                                break;
                        }
                    }
                } else {
                    switch (item.getPostCode()) {
                        case PostConstant.POST_A:
                        case PostConstant.POST_A2:
                        case PostConstant.POST_A3:
                        case PostConstant.POST_A4:
                            postCode = postCode.concat("_").concat(item.getRebateCode());
                            if (item.getRebateCode().equals(LotteryConstant.REBATE2D))
                                item.setFullNumber(drawingItemsEntity.getTwoDigits());
                            else
                                item.setFullNumber(drawingItemsEntity.getThreeDigits());
                            break;
                        case PostConstant.POST_B:
                            PostReleaseConfigEntity configEntity = postReleaseConfigEntityMap.get(item.getPostCode());
                            if (configEntity != null)
                                item.setIsCanRelease(configEntity.getIsCanRelease());
                            item.setFullNumber(drawingItemsEntity.getFiveDigits());
                            break;
                        default:
                            if (PostConstant.LO_1_TO_9.contains(item.getPostCode()))
                                item.setFullNumber(drawingItemsEntity.getFiveDigits());
                            else
                                item.setFullNumber(drawingItemsEntity.getFourDigits());
                            break;
                    }
                }

                if (!PostConstant.COMPOUND_POST.contains(item.getPostCode())) {
                    PostReleaseConfigEntity postReleaseConfigEntity = postReleaseConfigEntityMap.get(item.getPostCode());
                    if (PostConstant.POST_B_N.contains(item.getPostCode()) && postReleaseConfigEntity != null && !postReleaseConfigEntity.getIsCanRelease())
                        item.setIsEditable(true);
                    else
                        item.setIsEditable(drawingItemsEntity.getTwoDigits() != null && isEditable(drawingEntity.getResultedPostAt(), mapPostTimer.get(postCode)));
                }
            }
        }

        return responseBodyWithSuccessMessage(analyzeLogRS);
    }

    private StructureRS tnManageResultList(TNTempDrawingEntity drawingEntity) {

        List<PostReleaseConfigEntity> postReleaseConfigEntities = postReleaseConfigRP.findByLotteryTypeAndPostCodeIn(LotteryConstant.VN2, List.of(PostConstant.POST_B, PostConstant.POST_N));
        Map<String, PostReleaseConfigEntity> postReleaseConfigEntityMap = postReleaseConfigEntities
                .stream()
                .collect(Collectors.toMap(PostReleaseConfigEntity::getPostCode, Function.identity()));

        AnalyzeLogRS analyzeLogRS = analyzeSV.tnAnalyzeLog(drawingEntity);
        List<TNTempDrawingItemsEntity> vnTwoTempDrawingItemsEntities = tnTempDrawingItemsRP.getAllByDrawId(drawingEntity.getId());
        Map<String, Map<String, Integer>> mapPostTimer = getTNPostMinute(drawingEntity.getShiftCode());

        for (AnalyseLogItemRS item : analyzeLogRS.getItems()) {
            Optional<TNTempDrawingItemsEntity> optionalLeapTempDrawingItemsEntity = vnTwoTempDrawingItemsEntities
                    .stream()
                    .filter(it -> it.getPostCode().equals(item.getPostCode()))
                    .findFirst();

            if (optionalLeapTempDrawingItemsEntity.isPresent()) {
                TNTempDrawingItemsEntity drawingItemsEntity = optionalLeapTempDrawingItemsEntity.get();
                String postCode = item.getPostCode();
                if (drawingEntity.isDay()) {
                    if (drawingItemsEntity.getPostGroup().equals(PostConstant.LO_GROUP) && PostConstant.LO_1_TO_4.contains(item.getPostCode()))
                        item.setFullNumber(drawingItemsEntity.getFourDigits());
                    else {
                        switch (item.getPostCode()) {
                            case PostConstant.POST_O:
                                item.setFullNumber(drawingItemsEntity.getThreeDigits());
                                break;
                            case PostConstant.POST_K:
                            case PostConstant.POST_Z:
                                item.setFullNumber(drawingItemsEntity.getFourDigits());
                                break;
                            case PostConstant.POST_A:
                            case PostConstant.POST_F:
                            case PostConstant.POST_I:
                                postCode = postCode.concat("_").concat(item.getRebateCode());
                                if (item.getRebateCode().equals(LotteryConstant.REBATE2D))
                                    item.setFullNumber(drawingItemsEntity.getTwoDigits());
                                else
                                    item.setFullNumber(drawingItemsEntity.getThreeDigits());
                                break;
                            default:
                                if (PostConstant.POST_B_N_P.contains(item.getPostCode())) {
                                    String full = drawingItemsEntity.getFiveDigits();
                                    if (drawingEntity.getShiftCode().equals(PostConstant.TN_SHIFT_2_CODE))
                                        full = drawingItemsEntity.getSixDigits();

                                    item.setFullNumber(full);
                                    PostReleaseConfigEntity configEntity = postReleaseConfigEntityMap.get(item.getPostCode());
                                    if (configEntity != null)
                                        item.setIsCanRelease(configEntity.getIsCanRelease());
                                } else
                                    item.setFullNumber(drawingItemsEntity.getFiveDigits());
                                break;
                        }
                    }
                } else {
                    switch (item.getPostCode()) {
                        case PostConstant.POST_A:
                        case PostConstant.POST_A2:
                        case PostConstant.POST_A3:
                        case PostConstant.POST_A4:
                            postCode = postCode.concat("_").concat(item.getRebateCode());
                            if (item.getRebateCode().equals(LotteryConstant.REBATE2D))
                                item.setFullNumber(drawingItemsEntity.getTwoDigits());
                            else
                                item.setFullNumber(drawingItemsEntity.getThreeDigits());
                            break;
                        case PostConstant.POST_B:
                            PostReleaseConfigEntity configEntity = postReleaseConfigEntityMap.get(item.getPostCode());
                            if (configEntity != null)
                                item.setIsCanRelease(configEntity.getIsCanRelease());
                            item.setFullNumber(drawingItemsEntity.getFiveDigits());
                            break;
                        default:
                            if (PostConstant.LO_1_TO_9.contains(item.getPostCode()))
                                item.setFullNumber(drawingItemsEntity.getFiveDigits());
                            else
                                item.setFullNumber(drawingItemsEntity.getFourDigits());
                            break;
                    }
                }

                if (!PostConstant.COMPOUND_POST.contains(item.getPostCode())) {
                    PostReleaseConfigEntity postReleaseConfigEntity = postReleaseConfigEntityMap.get(item.getPostCode());
                    if (PostConstant.POST_B_N_P.contains(item.getPostCode()) && postReleaseConfigEntity != null && !postReleaseConfigEntity.getIsCanRelease())
                        item.setIsEditable(true);
                    else if (mapPostTimer.get(postCode) != null)
                        item.setIsEditable(drawingItemsEntity.getTwoDigits() != null && isEditable(drawingEntity.getResultedPostAt(), mapPostTimer.get(postCode)));
                }

            }
        }

        return responseBodyWithSuccessMessage(analyzeLogRS);
    }

    @Override
    public StructureRS lockPost(String postCode) {
        PostReleaseConfigEntity postReleaseConfigEntity = postReleaseConfigRP.findByLotteryTypeAndPostCode(LotteryConstant.VN2, postCode);
        if (postReleaseConfigEntity == null)
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);

        postReleaseConfigEntity.setIsCanRelease(!postReleaseConfigEntity.getIsCanRelease());
        postReleaseConfigRP.save(postReleaseConfigEntity);
        activityLogUtility.addActivityLog(LotteryConstant.VN2, ActivityLogConstant.LOCK_POST, jwtToken.getUserToken().getUserType(), ActivityLogConstant.ACTION_UPDATE, jwtToken.getUserToken().getUserCode(), postReleaseConfigEntity);
        updateRenderResultCache(postReleaseConfigEntity);

        return responseBodyWithSuccessMessage();
    }


    private void updateRenderResultCache(PostReleaseConfigEntity postReleaseConfigEntity) {
        VNTwoTempDrawingEntity drawingEntity = vnTwoTempDrawingRP.recentDrawing();
        if (drawingEntity != null) {
            int minuteStart = 12;
            if (drawingEntity.getIsNight())
                minuteStart = 2;

            Date startDate = new Date();
            BeanUtils.copyProperties(drawingEntity.getStopedLoAt(), startDate);
            startDate.setMinutes(minuteStart);

            Date endDate = new Date();
            BeanUtils.copyProperties(drawingEntity.getStopedLoAt(), endDate);
            endDate.setMinutes(35);
            if (!postReleaseConfigEntity.getIsCanRelease()) {
                endDate.setMinutes(55);
            }

            List<PostReleaseConfigTO> postReleaseConfigTOList = postReleaseConfigRP.findAllByLotteryType(LotteryConstant.VN2);
            boolean isCanRelease = true;
            for (PostReleaseConfigTO release : postReleaseConfigTOList) {
                if (drawingEntity.getIsNight() && PostConstant.POST_N.equals(release.getPostCode())) continue;
                isCanRelease = isCanRelease && release.getIsCanRelease();
            }

            Date now = new Date();
            if (now.getTime() > startDate.getTime()) {
                if (isCanRelease) {
                    endDate.setMinutes(35);
                    if (now.getTime() > endDate.getTime()) {
                        endDate = DateUtils.addSeconds(now, 20);
                    }
                } else {
                    endDate.setMinutes(55);
                }

                ResultRenderRS renderRS = new ResultRenderRS();
                renderRS.setDrawAt(drawingEntity.getResultedPostAt());
                renderRS.setDrawCode(drawingEntity.getCode());
                renderRS.setIsNight(drawingEntity.getIsNight());
                renderRS.setStart(startDate);
                renderRS.setEnd(endDate);

                try {
                    IMap<String, String> mapRenderResult = hazelcastInstance.getMap(HazelcastConstant.MT_RESULT_RENDER_COLLECTION);
                    mapRenderResult.put(
                            renderRS.getDrawCode(),
                            new ObjectMapper().writeValueAsString(renderRS),
                            0,
                            TimeUnit.SECONDS,
                            2,
                            TimeUnit.HOURS
                    );
                    System.out.println("ManageResultIP.updateRenderResultCache renderRS = " + renderRS);
                } catch (JsonProcessingException jsonProcessingException) {
                    jsonProcessingException.printStackTrace();
                }
            }
        }
    }

}
