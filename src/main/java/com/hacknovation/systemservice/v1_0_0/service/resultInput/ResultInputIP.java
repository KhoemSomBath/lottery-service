package com.hacknovation.systemservice.v1_0_0.service.resultInput;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.*;
import com.hacknovation.systemservice.enums.DrawingStatus;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.DrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.service.drawing.DrawingSV;
import com.hacknovation.systemservice.v1_0_0.service.result.ResultFull;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.resultInput.RenderNumberRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.resultInput.SetResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.ResultRenderRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.fullinput.FullResultInputRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.PublishResultUtility;
import com.hacknovation.systemservice.v1_0_0.utility.ResultBeforeStopUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.number.MapResultItemUtility;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 29/10/2022
 * time: 15:40
 */
@Service
@RequiredArgsConstructor
public class ResultInputIP extends BaseServiceIP implements ResultInputSV {

    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final VNOneTempDrawingItemsRP vnOneTempDrawingItemsRP;

    private final TNTempDrawingRP tnTempDrawingRP;
    private final TNTempDrawingItemsRP tnTempDrawingItemsRP;

    private final HazelcastInstance hazelcastInstance;
    private final MapResultItemUtility mapResultItemUtility;
    private final PublishResultUtility publishResultUtility;
    private final ActivityLogUtility activityLogUtility;
    private final ResultBeforeStopUtility resultBeforeStopUtility;

    private final DrawingNQ drawingNQ;
    private final TempDrawingNQ tempDrawingNQ;

    private final ResultFull resultFull;
    private final JwtToken jwtToken;


    @Override
    public StructureRS listResultItem(String lotteryType) {

        FullResultInputRS respond = new FullResultInputRS();

        List<DrawingItemsDTO> drawingItems;

        switch (lotteryType.toUpperCase()) {
            case LotteryConstant.VN1:
                VNOneTempDrawingEntity vnOneTempDrawing = vnOneTempDrawingRP.recentDrawing();
                if(vnOneTempDrawing == null) {
                    return responseBodyWithBadRequest(MessageConstant.NO_DRAW_LEFT, MessageConstant.NO_DRAW_LEFT_KEY);
                }

                respond.setDrawAt(vnOneTempDrawing.getResultedPostAt());
                respond.setIsNight(vnOneTempDrawing.getIsNight());

                List<VNOneTempDrawingItemsEntity> vnOneTempDrawingItemsEntities = vnOneTempDrawingItemsRP.getAllByDrawId(vnOneTempDrawing.getId());
                drawingItems = vnOneTempDrawingItemsEntities.stream().map(DrawingItemsDTO::new).collect(Collectors.toList());
                respond.setResults(resultFull.vnFullResultInput(vnOneTempDrawing, drawingItems));
                break;
            case LotteryConstant.TN:
                TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.recentDrawing();
                if(tnTempDrawingEntity == null) {
                    return responseBodyWithBadRequest(MessageConstant.NO_DRAW_LEFT, MessageConstant.NO_DRAW_LEFT_KEY);
                }

                respond.setDrawAt(tnTempDrawingEntity.getResultedPostAt());
                respond.setIsNight(tnTempDrawingEntity.getIsNight());

                List<TNTempDrawingItemsEntity> tnTempDrawingItemsEntities = tnTempDrawingItemsRP.getAllByDrawId(tnTempDrawingEntity.getId());
                drawingItems = tnTempDrawingItemsEntities.stream().map(DrawingItemsDTO::new).collect(Collectors.toList());
                respond.setResults(resultFull.tnFullResultInput(tnTempDrawingEntity, drawingItems));
                break;
        }

        respond.setLotteryType(lotteryType.toUpperCase());
        return responseBodyWithSuccessMessage(respond);
    }

    @Override
    public StructureRS showForm(String lotteryType) {
        String collectionName;
        ResultRenderRS resultRenderRS = new ResultRenderRS();
        resultRenderRS.setStart(new Date());
        resultRenderRS.setEnd(new Date());
        resultRenderRS.setEnd(DateUtils.addHours(resultRenderRS.getEnd(), 1));
        switch (lotteryType) {
            case LotteryConstant.VN1:
                collectionName = HazelcastConstant.VN1_RESULT_RENDER_COLLECTION;
                VNOneTempDrawingEntity drawingEntity = vnOneTempDrawingRP.recentDrawing();
                if (drawingEntity != null) {
                    resultRenderRS.setDrawCode(drawingEntity.getCode());
                    resultRenderRS.setDrawAt(drawingEntity.getResultedPostAt());
                    resultRenderRS.setIsNight(drawingEntity.getIsNight());

                    List<VNOneTempDrawingItemsEntity> vnOneTempDrawingItemsEntities = vnOneTempDrawingItemsRP.getAllByDrawId(drawingEntity.getId());
                    if (vnOneTempDrawingItemsEntities.size() > 0) {
                        vnOneTempDrawingItemsEntities.forEach(BaseDrawingItemsEntity::setRenderingStar);
                        vnOneTempDrawingItemsRP.saveAll(vnOneTempDrawingItemsEntities);
                    }

                }
                break;
            case LotteryConstant.TN:
                collectionName = HazelcastConstant.TN_RESULT_RENDER_COLLECTION;
                TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.recentDrawing();

                if(PostConstant.TN_SHIFT_5_CODE.equals(tnTempDrawingEntity.getShiftCode()))
                    throw new BadRequestException("Result could not be start");

                if (tnTempDrawingEntity != null) {
                    resultRenderRS.setDrawCode(tnTempDrawingEntity.getCode());
                    resultRenderRS.setDrawAt(tnTempDrawingEntity.getResultedPostAt());
                    resultRenderRS.setIsNight(tnTempDrawingEntity.getIsNight());

                    List<TNTempDrawingItemsEntity> tnTempDrawingItemsEntities = tnTempDrawingItemsRP.getAllByDrawId(tnTempDrawingEntity.getId());
                    if (tnTempDrawingItemsEntities.size() > 0) {
                        tnTempDrawingItemsEntities.forEach(BaseDrawingItemsEntity::setRenderingStar);
                        tnTempDrawingItemsRP.saveAll(tnTempDrawingItemsEntities);
                    }
                }
                break;
            default:
                return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
        }

        updateRenderResult(resultRenderRS, collectionName);

        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS startRenderNumber(RenderNumberRQ renderNumberRQ) {
        String lotteryType = renderNumberRQ.getLotteryType().toUpperCase();
        switch (lotteryType) {
            case LotteryConstant.VN1:
                VNOneTempDrawingEntity vnOneTempDrawing = vnOneTempDrawingRP.recentDrawing();
                VNOneTempDrawingItemsEntity vnOneTempDrawingItemsEntity = vnOneTempDrawingItemsRP.getOneByDrawIdColumnAndPost(vnOneTempDrawing.getId(),
                        renderNumberRQ.getColumnNumber(),
                        renderNumberRQ.getPostCode());
                if (vnOneTempDrawingItemsEntity != null) {
                    if (PostConstant.POST_A.equals(renderNumberRQ.getPostCode())) {
                        vnOneTempDrawingItemsEntity.setTwoDigits("??");
                    } else {
                        vnOneTempDrawingItemsEntity.setRenderingNumber();
                    }
                    vnOneTempDrawingItemsRP.save(vnOneTempDrawingItemsEntity);

                    return responseBodyWithSuccessMessage();
                }

                break;
            case LotteryConstant.TN:
                TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.recentDrawing();
                TNTempDrawingItemsEntity tnTempDrawingItemsEntity = tnTempDrawingItemsRP.getOneByDrawIdColumnAndPost(tnTempDrawingEntity.getId(),
                        renderNumberRQ.getColumnNumber(),
                        renderNumberRQ.getPostCode());
                if (tnTempDrawingItemsEntity != null) {
                    if (PostConstant.POST_A.equals(renderNumberRQ.getPostCode())) {
                        tnTempDrawingItemsEntity.setTwoDigits("??");
                    } else {
                        tnTempDrawingItemsEntity.setRenderingNumber();
                    }
                    tnTempDrawingItemsRP.save(tnTempDrawingItemsEntity);

                    return responseBodyWithSuccessMessage();
                }
                break;
        }

        return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
    }

    @Override
    public StructureRS setResultRender(SetResultRQ setResultRQ) {
        UserToken userToken = jwtToken.getUserToken();
        String lotteryType = setResultRQ.getLotteryType().toUpperCase();
        boolean isSetNext = true;
        switch (lotteryType) {
            case LotteryConstant.VN1:
                VNOneTempDrawingEntity vnOneTempDrawing = vnOneTempDrawingRP.recentDrawing();
                VNOneTempDrawingItemsEntity vnOneTempDrawingItemsEntity = vnOneTempDrawingItemsRP.getOneByDrawIdColumnAndPost(vnOneTempDrawing.getId(),
                        setResultRQ.getColumnNumber(),
                        setResultRQ.getPostCode());
                if (vnOneTempDrawingItemsEntity != null) {
                    switch (setResultRQ.getRebateCode()) {
                        case LotteryConstant.REBATE2D:
                            isSetNext = !isHasResult(vnOneTempDrawingItemsEntity.getTwoDigits());
                            vnOneTempDrawingItemsEntity.setTwoDigits(setResultRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE3D:
                            isSetNext = !isHasResult(vnOneTempDrawingItemsEntity.getThreeDigits());
                            vnOneTempDrawingItemsEntity.setThreeDigits(setResultRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE4D:
                            isSetNext = !isHasResult(vnOneTempDrawingItemsEntity.getFourDigits());
                            vnOneTempDrawingItemsEntity.setFourDigits(setResultRQ.getNumber());
                            vnOneTempDrawingItemsEntity.setTwoDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getFourDigits(), 2));
                            vnOneTempDrawingItemsEntity.setThreeDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getFourDigits(), 3));
                            break;
                        case LotteryConstant.REBATE5D:
                            isSetNext = !isHasResult(vnOneTempDrawingItemsEntity.getFiveDigits());
                            vnOneTempDrawingItemsEntity.setFiveDigits(setResultRQ.getNumber());
                            vnOneTempDrawingItemsEntity.setTwoDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getFiveDigits(), 2));
                            vnOneTempDrawingItemsEntity.setThreeDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getFiveDigits(), 3));
                            vnOneTempDrawingItemsEntity.setFourDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getFiveDigits(), 4));
                            break;
                        case LotteryConstant.REBATE6D:
                            isSetNext = !isHasResult(vnOneTempDrawingItemsEntity.getSixDigits());
                            vnOneTempDrawingItemsEntity.setSixDigits(setResultRQ.getNumber());
                            vnOneTempDrawingItemsEntity.setTwoDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getSixDigits(), 2));
                            vnOneTempDrawingItemsEntity.setThreeDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getSixDigits(), 3));
                            vnOneTempDrawingItemsEntity.setFourDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getSixDigits(), 4));
                            vnOneTempDrawingItemsEntity.setFiveDigits(StringUtils.right(vnOneTempDrawingItemsEntity.getSixDigits(), 5));
                            break;
                    }
                    vnOneTempDrawingItemsRP.save(vnOneTempDrawingItemsEntity);
                    resultBeforeStopUtility.updateVNOneStopAtIfResultBeforeStop(vnOneTempDrawing, setResultRQ);

                    SetResultRQ findNextRender = getNextPost(setResultRQ, vnOneTempDrawing.getShiftCode(), vnOneTempDrawing.getIsNight());
                    if (findNextRender != null && isSetNext) {
                        List<String> posts = List.of(findNextRender.getPostCode().split(","));
                        List<VNOneTempDrawingItemsEntity> nextDrawingItems = vnOneTempDrawingItemsRP.getAllByDrawIdAndColumnAndPostIn(vnOneTempDrawing.getId(), setResultRQ.getColumnNumber(), posts);
                        if (findNextRender.getPostCode().contains("A")) {
                            if (LotteryConstant.REBATE2D.equals(findNextRender.getRebateCode())) {
                                nextDrawingItems.forEach(it-> it.setTwoDigits("??"));
                            }
                            if (LotteryConstant.REBATE3D.equals(findNextRender.getRebateCode())) {
                                nextDrawingItems.forEach(it-> it.setThreeDigits("???"));
                            }
                        } else {
                            nextDrawingItems.forEach(BaseDrawingItemsEntity::setRenderingNumber);
                        }
                        if (nextDrawingItems.size() > 0) {
                            vnOneTempDrawingItemsRP.saveAll(nextDrawingItems);
                        }
                    }

                    activityLogUtility.addActivityLog(lotteryType,
                            ActivityLogConstant.MODULE_RENDER_RESULT,
                            userToken.getUserType(),
                            ActivityLogConstant.ACTION_UPDATE,
                            userToken.getUserCode(),
                            setResultRQ);

                    return responseBodyWithSuccessMessage();
                }
                break;

            case LotteryConstant.TN:
                TNTempDrawingEntity tnTempDrawing = tnTempDrawingRP.recentDrawing();
                TNTempDrawingItemsEntity tnTempDrawingItemsEntity = tnTempDrawingItemsRP.getOneByDrawIdColumnAndPost(tnTempDrawing.getId(),
                        setResultRQ.getColumnNumber(),
                        setResultRQ.getPostCode());
                if (tnTempDrawingItemsEntity != null) {
                    switch (setResultRQ.getRebateCode()) {
                        case LotteryConstant.REBATE2D:
                            isSetNext = !isHasResult(tnTempDrawingItemsEntity.getTwoDigits());
                            tnTempDrawingItemsEntity.setTwoDigits(setResultRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE3D:
                            isSetNext = !isHasResult(tnTempDrawingItemsEntity.getThreeDigits());
                            tnTempDrawingItemsEntity.setThreeDigits(setResultRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE4D:
                            isSetNext = !isHasResult(tnTempDrawingItemsEntity.getFourDigits());
                            tnTempDrawingItemsEntity.setFourDigits(setResultRQ.getNumber());
                            tnTempDrawingItemsEntity.setTwoDigits(StringUtils.right(tnTempDrawingItemsEntity.getFourDigits(), 2));
                            tnTempDrawingItemsEntity.setThreeDigits(StringUtils.right(tnTempDrawingItemsEntity.getFourDigits(), 3));
                            break;
                        case LotteryConstant.REBATE5D:
                            isSetNext = !isHasResult(tnTempDrawingItemsEntity.getFiveDigits());
                            tnTempDrawingItemsEntity.setFiveDigits(setResultRQ.getNumber());
                            tnTempDrawingItemsEntity.setTwoDigits(StringUtils.right(tnTempDrawingItemsEntity.getFiveDigits(), 2));
                            tnTempDrawingItemsEntity.setThreeDigits(StringUtils.right(tnTempDrawingItemsEntity.getFiveDigits(), 3));
                            tnTempDrawingItemsEntity.setFourDigits(StringUtils.right(tnTempDrawingItemsEntity.getFiveDigits(), 4));
                            break;
                        case LotteryConstant.REBATE6D:
                            isSetNext = !isHasResult(tnTempDrawingItemsEntity.getSixDigits());
                            tnTempDrawingItemsEntity.setSixDigits(setResultRQ.getNumber());
                            tnTempDrawingItemsEntity.setTwoDigits(StringUtils.right(tnTempDrawingItemsEntity.getSixDigits(), 2));
                            tnTempDrawingItemsEntity.setThreeDigits(StringUtils.right(tnTempDrawingItemsEntity.getSixDigits(), 3));
                            tnTempDrawingItemsEntity.setFourDigits(StringUtils.right(tnTempDrawingItemsEntity.getSixDigits(), 4));
                            tnTempDrawingItemsEntity.setFiveDigits(StringUtils.right(tnTempDrawingItemsEntity.getSixDigits(), 5));
                            break;
                    }
                    tnTempDrawingItemsRP.save(tnTempDrawingItemsEntity);
                    resultBeforeStopUtility.updateTNStopAtIfResultBeforeStop(tnTempDrawing, setResultRQ);

                    SetResultRQ findNextRender = getNextPost(setResultRQ, tnTempDrawing.getShiftCode(), tnTempDrawing.getIsNight());
                    if (findNextRender != null && isSetNext) {
                        List<String> posts = List.of(findNextRender.getPostCode().split(","));
                        List<TNTempDrawingItemsEntity> nextDrawingItems = tnTempDrawingItemsRP.getAllByDrawIdAndColumnAndPostIn(tnTempDrawing.getId(), setResultRQ.getColumnNumber(), posts);
                        if (findNextRender.getPostCode().contains("A")) {
                            if (LotteryConstant.REBATE2D.equals(findNextRender.getRebateCode())) {
                                nextDrawingItems.forEach(it-> it.setTwoDigits("??"));
                            }
                            if (LotteryConstant.REBATE3D.equals(findNextRender.getRebateCode())) {
                                nextDrawingItems.forEach(it-> it.setThreeDigits("???"));
                            }
                        } else {
                            nextDrawingItems.forEach(BaseDrawingItemsEntity::setRenderingNumber);
                        }
                        if (nextDrawingItems.size() > 0) {
                            tnTempDrawingItemsRP.saveAll(nextDrawingItems);
                        }
                    }

                    activityLogUtility.addActivityLog(lotteryType,
                            ActivityLogConstant.MODULE_RENDER_RESULT,
                            userToken.getUserType(),
                            ActivityLogConstant.ACTION_UPDATE,
                            userToken.getUserCode(),
                            setResultRQ);

                    return responseBodyWithSuccessMessage();
                }
                break;
        }

        return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
    }

    @Override
    public StructureRS closeDraw(String lotteryType) {
        String collectionName;
        ResultRenderRS resultRenderRS = new ResultRenderRS();
        resultRenderRS.setStart(DateUtils.addSeconds(new Date(), -5));
        resultRenderRS.setEnd(DateUtils.addSeconds(new Date(), 5));
        UserToken userToken = jwtToken.getUserToken();
        switch (lotteryType) {
            case LotteryConstant.VN1:
                collectionName = HazelcastConstant.VN1_RESULT_RENDER_COLLECTION;
                VNOneTempDrawingEntity vnOneTempDrawing = vnOneTempDrawingRP.recentDrawing();
                if (vnOneTempDrawing != null) {
                    List<VNOneTempDrawingItemsEntity> vnOneTempDrawingItemsEntities = vnOneTempDrawingItemsRP.getAllByDrawId(vnOneTempDrawing.getId());
                    mapResultItemUtility.mappingVN1Result(vnOneTempDrawingItemsEntities, vnOneTempDrawing.getResultedPostAt(), vnOneTempDrawing.getIsNight());

                    boolean isFullResult = isFullyResultVN1(vnOneTempDrawing.getIsNight() ?
                            vnOneTempDrawingItemsEntities.stream().filter(it-> !it.getPostCode().equals(PostConstant.POST_K)).collect(Collectors.toList()) :
                            vnOneTempDrawingItemsEntities);

                    if (!isFullResult) {
                        return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_COMPLETE,MessageConstant.RESULT_NOT_COMPLETE_KEY);
                    }

                    vnOneTempDrawingItemsRP.saveAll(vnOneTempDrawingItemsEntities);

                    vnOneTempDrawing.setIsRecent(false);
                    vnOneTempDrawing.setStatus(DrawingStatus.AWARDED);
                    vnOneTempDrawing.setIsReleasedLo(true);
                    vnOneTempDrawing.setIsReleasedPostA(true);
                    vnOneTempDrawing.setIsReleasedPost(true);
                    vnOneTempDrawingRP.save(vnOneTempDrawing);

                    resultRenderRS.setDrawCode(vnOneTempDrawing.getCode());
                    resultRenderRS.setDrawAt(vnOneTempDrawing.getResultedPostAt());
                    resultRenderRS.setIsNight(vnOneTempDrawing.getIsNight());

                    updateRenderResult(resultRenderRS, collectionName);

                    DrawingDTO drawingDTO = tempDrawingNQ.vnOneTempDrawing(vnOneTempDrawing.getCode());

                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    drawingNQ.vnOneResetWinOrderItems(drawingDTO.getDrawCode());
                    drawingNQ.vnOneUpdateIsRecent(drawingDTO.getDrawCode(), false);
                    drawingNQ.vnOneResetIsCalTempOrder(drawingDTO.getResultedPostAt());
                    drawingNQ.resetSummaryDaily(LotteryConstant.VN1, drawingDTO.getResultedPostAt());

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.VN1, drawingDTO);

                    activityLogUtility.addActivityLog(lotteryType,
                            ActivityLogConstant.MODULE_RENDER_RESULT,
                            userToken.getUserType(),
                            ActivityLogConstant.ACTION_FINISH,
                            userToken.getUserCode(),
                            "CLOSE DRAW "+ lotteryType);

                    return responseBodyWithSuccessMessage();
                }
                break;
            case LotteryConstant.TN:
                collectionName = HazelcastConstant.TN_RESULT_RENDER_COLLECTION;
                TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.recentDrawing();
                if (tnTempDrawingEntity != null) {
                    List<TNTempDrawingItemsEntity> tnTempDrawingItemsEntities = tnTempDrawingItemsRP.getAllByDrawId(tnTempDrawingEntity.getId());
                    mapResultItemUtility.mappingTNResult(tnTempDrawingItemsEntities, tnTempDrawingEntity.getIsNight());

                    boolean isFullResult = isFullyResultTN(tnTempDrawingEntity.getIsNight() ?
                            tnTempDrawingItemsEntities.stream().filter(it-> !it.getPostCode().equals(PostConstant.POST_K)).collect(Collectors.toList()) :
                            tnTempDrawingItemsEntities);

                    if (!isFullResult) {
                        return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_COMPLETE,MessageConstant.RESULT_NOT_COMPLETE_KEY);
                    }

                    tnTempDrawingItemsRP.saveAll(tnTempDrawingItemsEntities);

                    tnTempDrawingEntity.setStatus(DrawingStatus.AWARDED);
                    tnTempDrawingEntity.setIsRecent(false);
                    tnTempDrawingEntity.setIsReleasedLo(true);
                    tnTempDrawingEntity.setIsReleasedPostA(true);
                    tnTempDrawingEntity.setIsReleasedPost(true);
                    tnTempDrawingRP.save(tnTempDrawingEntity);

                    resultRenderRS.setDrawCode(tnTempDrawingEntity.getCode());
                    resultRenderRS.setDrawAt(tnTempDrawingEntity.getResultedPostAt());
                    resultRenderRS.setIsNight(tnTempDrawingEntity.getIsNight());

                    updateRenderResult(resultRenderRS, collectionName);

                    DrawingDTO drawingDTO = tempDrawingNQ.tnTempDrawing(tnTempDrawingEntity.getCode());

                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    drawingNQ.tnResetWinOrderItems(drawingDTO.getDrawCode());
                    drawingNQ.tnUpdateIsRecent(drawingDTO.getDrawCode(), false);
                    drawingNQ.tnResetIsCalTempOrder(drawingDTO.getResultedPostAt());
                    drawingNQ.resetSummaryDaily(LotteryConstant.TN, drawingDTO.getResultedPostAt());

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.TN, drawingDTO);

                    activityLogUtility.addActivityLog(lotteryType,
                            ActivityLogConstant.MODULE_RENDER_RESULT,
                            userToken.getUserType(),
                            ActivityLogConstant.ACTION_FINISH,
                            userToken.getUserCode(),
                            "CLOSE DRAW "+ lotteryType);

                    return responseBodyWithSuccessMessage();
                }
                break;
        }

        return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
    }

    private SetResultRQ getNextPost(SetResultRQ recentSetResultRQ, String shiftCode, boolean isNight) {
        SetResultRQ setResultRQ = new SetResultRQ();
        BeanUtils.copyProperties(recentSetResultRQ, setResultRQ);
        int maxLo = isNight ? 19: 15;

        if (recentSetResultRQ.getPostCode().contains("LO")) {
            String number = recentSetResultRQ.getPostCode().replace("LO","");
            if (LotteryConstant.TN.equals(recentSetResultRQ.getLotteryType()) && "020".equals(shiftCode)) {
                maxLo = 12;
            }
            int recentLo = Integer.parseInt(number);
            if (!isNight) {
                if (recentLo == maxLo) {
                    setResultRQ.setPostCode(PostConstant.POST_B);
                    return setResultRQ;
                }
            } else {
                if (recentLo == maxLo) {
                    setResultRQ.setPostCode("A,A2,A3");
                    setResultRQ.setRebateCode(LotteryConstant.REBATE3D);
                    return setResultRQ;
                }
            }

            recentLo+=1;
            setResultRQ.setPostCode("LO".concat(String.valueOf(recentLo)));
            return setResultRQ;
        } else {
            if (isNight) {
                if (PostConstant.POST_A3.equals(recentSetResultRQ.getPostCode()) && LotteryConstant.REBATE3D.equals(recentSetResultRQ.getRebateCode())) {
                    setResultRQ.setPostCode("A,A2,A3,A4");
                    setResultRQ.setRebateCode(LotteryConstant.REBATE2D);
                    return setResultRQ;
                }
                if (PostConstant.POST_A4.equals(recentSetResultRQ.getPostCode())) {
                    setResultRQ.setPostCode(PostConstant.POST_B);
                    return setResultRQ;
                }
            } else {
                if (PostConstant.POST_A.equals(recentSetResultRQ.getPostCode())) {
                    if (LotteryConstant.REBATE2D.equals(recentSetResultRQ.getRebateCode()))
                        setResultRQ.setRebateCode(LotteryConstant.REBATE3D);
                    else {
                        setResultRQ.setPostCode(PostConstant.LO1);
                    }
                    return setResultRQ;
                }
            }
        }

        return null;
    }

    private void updateRenderResult(ResultRenderRS renderRS, String collection) {

        try {
            IMap<String, String> mapRenderResult = hazelcastInstance.getMap(collection);
            mapRenderResult.put(
                    renderRS.getDrawCode(),
                    new ObjectMapper().writeValueAsString(renderRS),
                    0,
                    TimeUnit.SECONDS,
                    2,
                    TimeUnit.HOURS
            );
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
    }

    private boolean isFullyResultVN1(List<VNOneTempDrawingItemsEntity> drawingItemsEntities) {
        List<VNOneTempDrawingItemsEntity> drawingItemsEntityList = drawingItemsEntities.stream().filter(it-> it.getColumnNumber() == 1).collect(Collectors.toList());
        Optional<VNOneTempDrawingItemsEntity> optDrawItem = drawingItemsEntityList.stream().filter(it -> (it.getTwoDigits() == null) || it.getTwoDigits().contains("?") || it.getTwoDigits().contains("*")).findFirst();
        return optDrawItem.isEmpty();
    }

    private boolean isFullyResultTN(List<TNTempDrawingItemsEntity> drawingItemsEntities) {
        List<TNTempDrawingItemsEntity> drawingItemsEntityList = drawingItemsEntities.stream().filter(it-> it.getColumnNumber() == 1).collect(Collectors.toList());
        Optional<TNTempDrawingItemsEntity> optDrawItem = drawingItemsEntityList.stream().filter(it -> (it.getTwoDigits() == null) || it.getTwoDigits().contains("?") || it.getTwoDigits().contains("*")).findFirst();
        return optDrawItem.isEmpty();
    }

    private boolean isHasResult(String result) {
        if (result != null) {
            return !(result.contains("?") || result.contains("*"));
        }
        return false;
    }
}
