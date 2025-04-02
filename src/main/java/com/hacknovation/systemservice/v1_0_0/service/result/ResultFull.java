package com.hacknovation.systemservice.v1_0_0.service.result;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.DrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.result.ResultNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.result.ResultTempNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.PublishResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full.FullDrawItemRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full.FullResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full.FullResultRowRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.full.FullDrawItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.full.FullResultRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.full.FullResultRowRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.fullinput.FullResultInputItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.fullinput.FullResultInputRowRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.PublishResultUtility;
import com.hacknovation.systemservice.v1_0_0.utility.ResultFullUtility;
import com.hacknovation.systemservice.v1_0_0.utility.number.MapResultItemUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hacknovation.systemservice.constant.PostConstant.*;

/**
 * @author Sombath
 * create at 13/10/22 10:07 PM
 */

@Service
@RequiredArgsConstructor
public class ResultFull extends BaseServiceIP {

    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final LeapTempDrawingRP leapTempDrawingRP;
    private final TNTempDrawingRP tnTempDrawingRP;
    private final VNOneTempDrawingItemsRP vnOneTempDrawingItemsRP;
    private final VNTwoTempDrawingItemsRP vnTwoTempDrawingItemsRP;
    private final TNTempDrawingItemsRP tnTempDrawingItemsRP;
    private final LeapTempDrawingItemsRP leapTempDrawingItemsRP;
    private final ResultNQ resultNQ;
    private final ResultTempNQ resultTempNQ;
    private final DrawingNQ drawingNQ;
    private final TempDrawingNQ tempDrawingNQ;
    private final GeneralUtility generalUtility;
    private final MapResultItemUtility mapResultItemUtility;
    private final PublishResultUtility publishResultUtility;
    private final ResultFullUtility resultFullUtility;
    private final SCTempDrawingItemRP sCTempDrawingItemRP;
    private final SCTempDrawingRP sCTempDrawingRP;

    public StructureRS getResult(String drawCode, String filterByDate, String lotteryType) {
        FullResultRS respond = new FullResultRS();

        boolean isTemp = generalUtility.isTempTable(filterByDate);

        DrawingDTO drawingDTO = new DrawingDTO();
        List<DrawingItemsDTO> drawingItems;

        switch (lotteryType.toUpperCase()) {
            case LotteryConstant.VN1:
                drawingDTO = isTemp ? tempDrawingNQ.vnOneTempDrawing(drawCode) : drawingNQ.vnOneDrawing(drawCode);
                drawingItems = isTemp ? resultTempNQ.getVNOneTempResult(drawingDTO.getId().intValue()) : resultNQ.getVNOneResult(drawingDTO.getId().intValue());
                respond.setResults(this.vnFullResult(drawingDTO, drawingItems));
                break;
            case LotteryConstant.VN2:
                drawingDTO = isTemp ? tempDrawingNQ.vnTwoTempDrawing(drawCode) : drawingNQ.vnTwoDrawing(drawCode);
                drawingItems = isTemp ? resultTempNQ.getVNTwoTempResult(drawingDTO.getId().intValue()) : resultNQ.getVNTwoResult(drawingDTO.getId().intValue());
                respond.setResults(this.vnFullResult(drawingDTO, drawingItems));
                break;
            case LotteryConstant.TN:
                drawingDTO = isTemp ? tempDrawingNQ.tnTempDrawing(drawCode) : drawingNQ.tnDrawing(drawCode);
                drawingItems = isTemp ? resultTempNQ.getTNResult(drawingDTO.getId().intValue()) : resultNQ.getTNResult(drawingDTO.getId().intValue());
                respond.setResults(this.tnFullResult(drawingDTO, drawingItems));
                break;
            case LotteryConstant.LEAP:
                drawingDTO = isTemp ? tempDrawingNQ.leapTempDrawing(drawCode) : drawingNQ.leapDrawing(drawCode);
                drawingItems = isTemp ? resultTempNQ.getLeapTempResult(drawingDTO.getId().intValue()) : resultNQ.getLeapResult(drawingDTO.getId().intValue());
                respond.setResults(this.leapFullResult(drawingItems));
                break;
            case LotteryConstant.SC:
                drawingDTO = isTemp ? tempDrawingNQ.scTempDrawing(drawCode) : drawingNQ.scDrawing(drawCode);
                drawingItems = isTemp ? resultTempNQ.getSCTempResult(drawingDTO.getId().intValue()) : resultNQ.getSCResult(drawingDTO.getId().intValue());
                respond.setResults(this.scFullResult(drawingItems));
                break;
            case LotteryConstant.TH:
                drawingDTO = isTemp ? tempDrawingNQ.thTempDrawing(drawCode) : drawingNQ.thDrawing(drawCode);
                drawingItems = isTemp ? resultTempNQ.getTHTempResult(drawingDTO.getId().intValue()) : resultNQ.getTHResult(drawingDTO.getId().intValue());
                respond.setResults(this.thFullResult(drawingItems));
                break;
        }

        respond.setLotteryType(lotteryType.toUpperCase());
        respond.setDrawAt(drawingDTO.getResultedPostAt());
        respond.setIsNight(drawingDTO.getIsNight());
        return responseBodyWithSuccessMessage(respond);
    }

    @Transactional
    public StructureRS publishResult(PublishResultRQ publishResultRQ) {
        DrawingDTO drawingDTO = null;
        String lotteryType = publishResultRQ.getLotteryType().toUpperCase();
        // get draw first
        switch (lotteryType) {
            case LotteryConstant.VN1:
                drawingDTO = tempDrawingNQ.vnOneTempDrawing(publishResultRQ.getDrawCode());
                break;
            case LotteryConstant.VN2:
                drawingDTO = tempDrawingNQ.vnTwoTempDrawing(publishResultRQ.getDrawCode());
                break;
            case LotteryConstant.LEAP:
                drawingDTO = tempDrawingNQ.leapTempDrawing(publishResultRQ.getDrawCode());
                break;
            case LotteryConstant.SC:
                drawingDTO = tempDrawingNQ.scTempDrawing(publishResultRQ.getDrawCode());
                break;
            case LotteryConstant.TN:
                drawingDTO = tempDrawingNQ.tnTempDrawing(publishResultRQ.getDrawCode());
                break;
        }
        if (drawingDTO == null)
            return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);

        if (!drawingDTO.getIsRecent()) {
            return responseBodyWithBadRequest(MessageConstant.RESULT_ALREADY_PUBLISHED, MessageConstant.RESULT_ALREADY_PUBLISHED_KEY);
        }
        long size;
        int drawId = drawingDTO.getId().intValue();
        switch (lotteryType) {
            case LotteryConstant.VN1:
                if (drawingDTO.getIsNight())
                    size = vnOneTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndPostCodeNotLikeAndTwoDigitsIsNull(drawId, 1, "K");
                else
                    size = vnOneTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(drawId, 1);
                if (size == 0) {
                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    drawingNQ.vnOneResetWinOrderItems(drawingDTO.getDrawCode());
                    drawingNQ.vnOneUpdateIsRecent(drawingDTO.getDrawCode(), false);
                    drawingNQ.vnOneResetIsCalTempOrder(drawingDTO.getResultedPostAt());
                    drawingNQ.resetSummaryDaily(LotteryConstant.VN1, drawingDTO.getResultedPostAt());
                    vnOneTempDrawingRP.updateDrawAwarded(drawingDTO.getDrawCode());

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.VN1, drawingDTO);

                    System.out.println("ResultFull.publishResult => VN1 set is_recent to false, date = " + new Date());
                    System.out.println(publishResultRQ);
                    return responseBodyWithSuccessMessage();
                }
                break;
            case LotteryConstant.VN2:
                if (drawingDTO.getIsNight())
                    size = vnTwoTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndPostCodeNotLikeAndTwoDigitsIsNull(drawId, 1, "K");
                else
                    size = vnTwoTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(drawId, 1);
                if (size == 0) {
                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    drawingNQ.vnTwoResetWinOrderItems(drawingDTO.getDrawCode());
                    drawingNQ.vnTwoUpdateIsRecent(drawingDTO.getDrawCode(), false);
                    drawingNQ.vnTwoResetIsCalTempOrder(drawingDTO.getResultedPostAt());
                    drawingNQ.resetSummaryDaily(LotteryConstant.VN2, drawingDTO.getResultedPostAt());
                    vnTwoTempDrawingRP.updateDrawAwarded(drawingDTO.getDrawCode());

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.VN2, drawingDTO);

                    System.out.println("ResultFull.publishResult => VN2 set is_recent to false, date = " + new Date());
                    System.out.println(publishResultRQ);
                    return responseBodyWithSuccessMessage();
                }
                break;
            case LotteryConstant.TN:
                if (drawingDTO.getIsNight())
                    size = tnTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndPostCodeNotInAndTwoDigitsIsNull(drawingDTO.getId().intValue(), 1, List.of("F", "I", "N", "K", "P", "Z"));
                else
                    size = tnTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(drawId, 1);
                if (size == 0) {
                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    drawingNQ.tnResetWinOrderItems(drawingDTO.getDrawCode());
                    drawingNQ.tnUpdateIsRecent(drawingDTO.getDrawCode(), false);
                    drawingNQ.tnResetIsCalTempOrder(drawingDTO.getResultedPostAt());
                    drawingNQ.resetSummaryDaily(LotteryConstant.TN, drawingDTO.getResultedPostAt());
                    tnTempDrawingRP.updateDrawAwarded(drawingDTO.getDrawCode());

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.TN, drawingDTO);

                    System.out.println("ResultFull.publishResult => TN set is_recent to false, date = " + new Date());
                    System.out.println(publishResultRQ);
                    return responseBodyWithSuccessMessage();
                }
                break;
            case LotteryConstant.LEAP:
                size = leapTempDrawingItemsRP.countAllByDrawingIdAndTwoDigitsIsNull(drawId);
                if (size == 0) {
                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    drawingNQ.leapResetWinOrderItems(drawingDTO.getDrawCode());
                    drawingNQ.leapUpdateIsRecent(drawingDTO.getDrawCode(), false);
                    drawingNQ.leapResetIsCalTempOrder(drawingDTO.getResultedPostAt());
                    drawingNQ.resetSummaryDaily(LotteryConstant.LEAP, drawingDTO.getResultedPostAt());
                    leapTempDrawingRP.updateDrawAwarded(drawingDTO.getDrawCode());

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.LEAP, drawingDTO);

                    System.out.println("ResultFull.publishResult => LEAP set is_recent to false, date = " + new Date());
                    System.out.println(publishResultRQ);
                    return responseBodyWithSuccessMessage();
                }
                break;
            case LotteryConstant.SC:
                size = sCTempDrawingItemRP.countByDrawingIdAndTwoDigitsNull(drawId);
                if (size == 0) {
                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    sCTempDrawingRP.resetWinOrderItems(drawingDTO.getDrawCode());
                    sCTempDrawingRP.updateIsRecent(drawingDTO.getDrawCode(), false);
                    sCTempDrawingRP.resetIsCalTempOrder(generalUtility.formatDateYYYYMMDD(drawingDTO.getResultedPostAt()));
                    drawingNQ.resetSummaryDaily(LotteryConstant.SC, drawingDTO.getResultedPostAt());
                    sCTempDrawingRP.updateDrawAwarded(drawingDTO.getDrawCode());

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.SC, drawingDTO);

                    System.out.println("ResultFull.publishResult => SC set is_recent to false, date = " + new Date());
                    System.out.println(publishResultRQ);
                    return responseBodyWithSuccessMessage();
                }
                break;
        }

        return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_UPDATE, MessageConstant.RESULT_NOT_FULLY_UPDATE_KEY);

    }

    private List<FullResultRowRS> vnFullResult(DrawingDTO drawingDTO, List<DrawingItemsDTO> drawingItems) {
        List<FullResultRowRS> items = new ArrayList<>();

        Boolean isNight = drawingDTO.getIsNight();
        List<String> loFiveDigits = isNight ? PostConstant.LO_1_TO_9 : PostConstant.LO_6_TO_15;
        List<String> loFourDigits = isNight ? PostConstant.LO_10_TO_19 : PostConstant.LO_1_TO_4;

        List<String> posts = new ArrayList<>(PostConstant.getVNPostByIsNight(isNight));

        posts.removeAll(PostConstant.VN_REMOVAL_POST);

        // add A 3D
        if (!isNight)
            posts.add(1, POST_A3);

        for (String post : posts) {
            FullResultRowRS row = new FullResultRowRS();
            List<DrawingItemsDTO> subItemByPost = drawingItems.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            // add A 3D
            if (!isNight && POST_A3.equals(post)) {
                row.setRebateCode(LotteryConstant.REBATE3D);
                FullResultRowRS rowA = items.stream().filter(it -> it.getPostCode().equals(POST_A)).findFirst().orElseThrow(BadRequestException::new);
                FullResultRowRS rowA3D = new FullResultRowRS();

                rowA3D.setRebateCode(LotteryConstant.REBATE3D);
                rowA3D.setPostCode(rowA.getPostCode());
                rowA3D.setPostGroup(rowA.getPostGroup());

                for (FullDrawItemRS columnItem : rowA.getItems()) {
                    FullDrawItemRS item = new FullDrawItemRS();
                    item.setNumber(columnItem.getThreeDigits());
                    item.setId(columnItem.getId());
                    item.setColumnNumber(columnItem.getColumnNumber());
                    item.setRebateCode(LotteryConstant.REBATE3D);
                    rowA3D.getItems().add(item);
                }

                items.add(rowA3D);
            }

            for (DrawingItemsDTO drawItem : subItemByPost) {

                row.setPostCode(drawItem.getPostCode());
                row.setPostGroup(drawItem.getPostGroup());

                FullDrawItemRS item = new FullDrawItemRS();
                BeanUtils.copyProperties(drawItem, item);
                generalUtility.prepareNumber(item, drawItem, drawingDTO.getIsNight(), LotteryConstant.VN1);

                if (loFiveDigits.contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE5D);
                    item.setRebateCode(LotteryConstant.REBATE5D);
                    item.setNumber(drawItem.getFiveDigits());
                }

                if (loFourDigits.contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE4D);
                    item.setRebateCode(LotteryConstant.REBATE4D);
                    item.setNumber(drawItem.getFourDigits());
                }

                if (PostConstant.POST_B.equals(drawItem.getPostCode())) {
                    row.setRebateCode(isNight ? LotteryConstant.REBATE5D : LotteryConstant.REBATE6D);
                    item.setRebateCode(isNight ? LotteryConstant.REBATE5D : LotteryConstant.REBATE6D);
                    item.setNumber(isNight ? drawItem.getFiveDigits() : drawItem.getSixDigits());
                }

                if (PostConstant.getPostANight().contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE2D);
                    item.setRebateCode(LotteryConstant.REBATE2D);
                    item.setNumber(drawItem.getTwoDigits());
                }

                row.getItems().add(item);

                // add 3D for post A night
                if (isNight && PostConstant.getPostANight().contains(drawItem.getPostCode()) && !PostConstant.POST_A4.equals(drawItem.getPostCode())) {
                    FullDrawItemRS item3D = new FullDrawItemRS();
                    BeanUtils.copyProperties(item, item3D);
                    item3D.setNumber(item.getThreeDigits());
                    item3D.setRebateCode(LotteryConstant.REBATE3D);
                    row.getItems().add(item3D);
                }
            }

            if (!subItemByPost.isEmpty())
                items.add(row);
        }

        return items;
    }

    private List<FullResultRowRS> tnFullResult(DrawingDTO drawingDTO, List<DrawingItemsDTO> drawingItems) {
        List<FullResultRowRS> items = new ArrayList<>();

        Boolean isNight = drawingDTO.getIsNight();
        List<String> loFiveDigits = isNight ? PostConstant.LO_1_TO_9 : PostConstant.LO_4_TO_15;
        List<String> loFourDigits = isNight ? PostConstant.LO_10_TO_19 : PostConstant.LO_1_TO_3;

        List<String> posts = new ArrayList<>(PostConstant.GET_TN_POST_BY_SHIFT_CODE(drawingDTO.getShiftCode()));

        posts.removeAll(PostConstant.TN_REMOVAL_POST);

        // add A 3D
        if (!isNight)
            posts.add(1, POST_A3);

        for (String post : posts) {
            FullResultRowRS row = new FullResultRowRS();
            List<DrawingItemsDTO> subItemByPost = drawingItems.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            // add A 3D
            if (!isNight && POST_A3.equals(post)) {
                row.setRebateCode(LotteryConstant.REBATE3D);
                FullResultRowRS rowA = items.stream().filter(it -> it.getPostCode().equals(POST_A)).findFirst().orElseThrow(BadRequestException::new);
                FullResultRowRS rowA3D = new FullResultRowRS();

                rowA3D.setRebateCode(LotteryConstant.REBATE3D);
                rowA3D.setPostCode(rowA.getPostCode());
                rowA3D.setPostGroup(rowA.getPostGroup());

                for (FullDrawItemRS columnItem : rowA.getItems()) {
                    FullDrawItemRS item = new FullDrawItemRS();
                    item.setNumber(columnItem.getThreeDigits());
                    item.setId(columnItem.getId());
                    item.setRebateCode(LotteryConstant.REBATE3D);
                    item.setColumnNumber(columnItem.getColumnNumber());
                    rowA3D.getItems().add(item);
                }

                items.add(rowA3D);
            }

            for (DrawingItemsDTO drawItem : subItemByPost) {

                row.setPostCode(drawItem.getPostCode());
                row.setPostGroup(drawItem.getPostGroup());

                FullDrawItemRS item = new FullDrawItemRS();
                BeanUtils.copyProperties(drawItem, item);
                generalUtility.prepareNumber(item, drawItem, drawingDTO.getIsNight(), LotteryConstant.TN);

                if (loFiveDigits.contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE5D);
                    item.setRebateCode(LotteryConstant.REBATE5D);
                    item.setNumber(drawItem.getFiveDigits());
                }

                if (loFourDigits.contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE4D);
                    item.setRebateCode(LotteryConstant.REBATE4D);
                    item.setNumber(drawItem.getFourDigits());
                }

                if (PostConstant.POST_B.equals(drawItem.getPostCode())) {
                    switch (drawingDTO.getShiftCode()) {
                        case TN_SHIFT_1_CODE:
                        case TN_SHIFT_3_CODE:
                        case TN_SHIFT_5_CODE:
                            row.setRebateCode(LotteryConstant.REBATE5D);
                            item.setRebateCode(LotteryConstant.REBATE5D);
                            item.setNumber(drawItem.getFiveDigits());
                            break;
                        case TN_SHIFT_2_CODE:
                        case TN_SHIFT_4_CODE:
                            row.setRebateCode(LotteryConstant.REBATE6D);
                            item.setRebateCode(LotteryConstant.REBATE6D);
                            item.setNumber(drawItem.getSixDigits());
                            break;
                    }
                }

                if (PostConstant.getPostANight().contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE2D);
                    item.setRebateCode(LotteryConstant.REBATE2D);
                    item.setNumber(drawItem.getTwoDigits());
                }

                row.getItems().add(item);

                // add 3D for post A night
                if (isNight && PostConstant.getPostANight().contains(drawItem.getPostCode()) && !PostConstant.POST_A4.equals(drawItem.getPostCode())) {
                    FullDrawItemRS item3D = new FullDrawItemRS();
                    BeanUtils.copyProperties(item, item3D);
                    item3D.setNumber(item.getThreeDigits());
                    item3D.setRebateCode(LotteryConstant.REBATE3D);
                    row.getItems().add(item3D);
                }
            }

            if (!subItemByPost.isEmpty())
                items.add(row);
        }

        return items;
    }

    private List<FullResultRowRS> leapFullResult(List<DrawingItemsDTO> drawingItems) {
        List<FullResultRowRS> items = new ArrayList<>();

        for (DrawingItemsDTO drawingItem : drawingItems) {
            FullResultRowRS row = new FullResultRowRS();
            BeanUtils.copyProperties(drawingItem, row);
            row.setRebateCode(LotteryConstant.REBATE6D);

            FullDrawItemRS item = new FullDrawItemRS();
            BeanUtils.copyProperties(drawingItem, item);
            item.setColumnNumber(1);
            item.setNumber(drawingItem.getSixDigits());
            item.setRebateCode(LotteryConstant.REBATE6D);

            row.getItems().add(item);
            items.add(row);
        }

        return  items;
    }

    private List<FullResultRowRS> scFullResult(List<DrawingItemsDTO> drawingItems) {
        List<FullResultRowRS> items = new ArrayList<>();

        for (DrawingItemsDTO drawingItem : drawingItems) {
            FullResultRowRS row = new FullResultRowRS();
            BeanUtils.copyProperties(drawingItem, row);
            row.setRebateCode(LotteryConstant.REBATE6D);

            FullDrawItemRS item = new FullDrawItemRS();
            BeanUtils.copyProperties(drawingItem, item);
            item.setColumnNumber(1);
            item.setNumber(drawingItem.getSixDigits());
            item.setRebateCode(LotteryConstant.REBATE6D);

            row.getItems().add(item);
            items.add(row);
        }

        return  items;
    }

    private List<FullResultRowRS> thFullResult(List<DrawingItemsDTO> drawingItems) {
        List<FullResultRowRS> items = new ArrayList<>();

        for (DrawingItemsDTO drawingItem : drawingItems) {
            FullResultRowRS row = new FullResultRowRS();
            BeanUtils.copyProperties(drawingItem, row);
            row.setRebateCode(LotteryConstant.REBATE6D);

            FullDrawItemRS item = new FullDrawItemRS();
            BeanUtils.copyProperties(drawingItem, item);
            item.setColumnNumber(1);
            item.setNumber(drawingItem.getSixDigits());
            item.setRebateCode(LotteryConstant.REBATE6D);

            row.getItems().add(item);
            items.add(row);
        }

        return  items;
    }

    @Transactional
    public StructureRS setResult(FullResultRQ resultRQ, boolean isFullResult) {
        switch (resultRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.TN:
                this.setResultTN(resultRQ, isFullResult);
                break;
            case LotteryConstant.VN1:
                this.setResultVN1(resultRQ, isFullResult);
                break;
            case LotteryConstant.VN2:
                this.setResultVN2(resultRQ, isFullResult);
                break;
            case LotteryConstant.LEAP:
                this.setResultLeap(resultRQ, isFullResult);
                break;
            case LotteryConstant.SC:
                this.setResultSC(resultRQ, isFullResult);
                break;
            default:
                throw new BadRequestException(MessageConstant.UNKNOWN_TYPE);
        }
        return responseBodyWithSuccessMessage();
    }

    private void setResultTN(FullResultRQ resultRQ, boolean isFullResult) {
        try {
            DrawingDTO drawingDTO = tempDrawingNQ.tnTempDrawing(resultRQ.getDrawCode());
            List<TNTempDrawingItemsEntity> tnTempDrawingItemsEntities = tnTempDrawingItemsRP.getAllByDrawId(drawingDTO.getId().longValue());
            List<String> posts = new ArrayList<>(PostConstant.GET_TN_POST_BY_SHIFT_CODE(drawingDTO.getShiftCode()));
            posts.removeAll(PostConstant.TN_REMOVAL_POST);

            Boolean isNight = drawingDTO.getIsNight();

            for (String post : posts) {
                List<TNTempDrawingItemsEntity> _tnTempDrawingItemsEntities = tnTempDrawingItemsEntities.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());
                List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

                for (FullResultRowRQ postItemRQ : postItems)
                    for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {

                        TNTempDrawingItemsEntity drawingItemsEntity = _tnTempDrawingItemsEntities.stream()
                                .filter(it -> it.getPostCode().equals(post) && it.getColumnNumber().equals(columnItemRQ.getColumnNumber()))
                                .findAny().orElseThrow(BadRequestException::new);


                        if (columnItemRQ.getNumber().length() >= 4)
                            drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));


                        switch (columnItemRQ.getRebateCode()) {
                            case LotteryConstant.REBATE2D:
                                drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE3D:
                                drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE4D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE5D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                                drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                                break;
                            case LotteryConstant.REBATE6D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                                drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                                break;
                        }

                    }

            }

            mapResultItemUtility.mappingTNResult(tnTempDrawingItemsEntities, isNight);
            tnTempDrawingItemsRP.saveAll(tnTempDrawingItemsEntities);
            if (isFullResult)
                drawingNQ.tnUpdateIsRecent(resultRQ.getDrawCode(), true);
            else {
                // reset draw, draw item and update from temp again
                tnTempDrawingRP.resetDrawing(drawingDTO.getDrawCode());
                tnTempDrawingRP.resetDrawingItems(drawingDTO.getDrawCode());
                tnTempDrawingRP.saveDrawing(drawingDTO.getDrawCode());
                tnTempDrawingRP.saveDrawingItems(drawingDTO.getDrawCode());
            }

            publishResultUtility.removeDrawDTOCache(LotteryConstant.TN, drawingDTO);
        } catch (NoResultException noResultException) {
            DrawingDTO drawingDTO = drawingNQ.tnDrawing(resultRQ.getDrawCode());
            resultFullUtility.setTNOriginalOrderItem(resultRQ, drawingDTO);
            publishResultUtility.removeDrawDTOCache(LotteryConstant.TN, drawingDTO);
        }

    }

    private void setResultVN1(FullResultRQ resultRQ, boolean isFullResult) {
        try {
            DrawingDTO drawingDTO = tempDrawingNQ.vnOneTempDrawing(resultRQ.getDrawCode());

            List<VNOneTempDrawingItemsEntity> vnOneTempDrawingItemsEntities = vnOneTempDrawingItemsRP.getAllByDrawId(drawingDTO.getId().longValue());

            Boolean isNight = drawingDTO.getIsNight();

            List<String> posts = new ArrayList<>(PostConstant.getVNPostByIsNight(isNight));
            posts.removeAll(PostConstant.VN_REMOVAL_POST);

            for (String post : posts) {
                List<VNOneTempDrawingItemsEntity> _vnOneTempDrawingItemsEntities = vnOneTempDrawingItemsEntities.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());
                List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

                for (FullResultRowRQ postItemRQ : postItems)
                    for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {

                        if (columnItemRQ.getNumber() == null) continue;

                        VNOneTempDrawingItemsEntity drawingItemsEntity = _vnOneTempDrawingItemsEntities.stream()
                                .filter(it -> it.getPostCode().equals(post) && it.getColumnNumber().equals(columnItemRQ.getColumnNumber()))
                                .findAny().orElseThrow(BadRequestException::new);


                        if (columnItemRQ.getNumber().length() >= 4)
                            drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));


                        switch (columnItemRQ.getRebateCode()) {
                            case LotteryConstant.REBATE2D:
                                drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE3D:
                                drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE4D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE5D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                                drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                                break;
                            case LotteryConstant.REBATE6D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                                drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                                break;
                        }

                    }

            }

            mapResultItemUtility.mappingVN1Result(vnOneTempDrawingItemsEntities, drawingDTO.getResultedPostAt(), isNight);
            vnOneTempDrawingItemsRP.saveAll(vnOneTempDrawingItemsEntities);

            if (isFullResult)
                drawingNQ.vnOneUpdateIsRecent(resultRQ.getDrawCode(), true);
            else {
                // reset draw, draw item and update from temp again
                vnOneTempDrawingRP.resetDrawing(drawingDTO.getDrawCode());
                vnOneTempDrawingRP.resetDrawingItems(drawingDTO.getDrawCode());
                vnOneTempDrawingRP.saveDrawing(drawingDTO.getDrawCode());
                vnOneTempDrawingRP.saveDrawingItems(drawingDTO.getDrawCode());
            }

            publishResultUtility.removeDrawDTOCache(LotteryConstant.VN1, drawingDTO);
        } catch (NoResultException noResultException) {
            DrawingDTO drawingDTO = drawingNQ.vnOneDrawing(resultRQ.getDrawCode());
            resultFullUtility.setVN1OriginalOrderItem(resultRQ, drawingDTO);
            publishResultUtility.removeDrawDTOCache(LotteryConstant.VN1, drawingDTO);
        }
    }

    private void setResultVN2(FullResultRQ resultRQ, boolean isFullResult) {
        try {
            DrawingDTO drawingDTO = tempDrawingNQ.vnTwoTempDrawing(resultRQ.getDrawCode());
            List<VNTwoTempDrawingItemsEntity> vnTwoTempDrawingItemsEntities = vnTwoTempDrawingItemsRP.getAllByDrawId(drawingDTO.getId().longValue());

            Boolean isNight = drawingDTO.getIsNight();

            List<String> posts = new ArrayList<>(PostConstant.getVNPostByIsNight(isNight));

            posts.removeAll(PostConstant.VN_REMOVAL_POST);

            for (String post : posts) {
                List<VNTwoTempDrawingItemsEntity> _vnTwoTempDrawingItemsEntities = vnTwoTempDrawingItemsEntities.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());
                List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

                for (FullResultRowRQ postItemRQ : postItems)
                    for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {

                        VNTwoTempDrawingItemsEntity drawingItemsEntity = _vnTwoTempDrawingItemsEntities.stream()
                                .filter(it -> it.getPostCode().equals(post) && it.getColumnNumber().equals(columnItemRQ.getColumnNumber()))
                                .findAny().orElseThrow(BadRequestException::new);


                        if (columnItemRQ.getNumber().length() >= 4)
                            drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));


                        switch (columnItemRQ.getRebateCode()) {
                            case LotteryConstant.REBATE2D:
                                drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE3D:
                                drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE4D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE5D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                                drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                                break;
                            case LotteryConstant.REBATE6D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                                drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                                break;
                        }

                    }

            }

            mapResultItemUtility.mappingVN2Result(vnTwoTempDrawingItemsEntities, isNight);
            vnTwoTempDrawingItemsRP.saveAll(vnTwoTempDrawingItemsEntities);

            if (isFullResult)
                drawingNQ.vnTwoUpdateIsRecent(resultRQ.getDrawCode(), true);
            else {
                // reset draw, draw item and update from temp again
                vnTwoTempDrawingRP.resetDrawing(drawingDTO.getDrawCode());
                vnTwoTempDrawingRP.resetDrawingItems(drawingDTO.getDrawCode());
                vnTwoTempDrawingRP.saveDrawing(drawingDTO.getDrawCode());
                vnTwoTempDrawingRP.saveDrawingItems(drawingDTO.getDrawCode());
            }

            publishResultUtility.removeDrawDTOCache(LotteryConstant.VN2, drawingDTO);
        } catch (NoResultException noResultException) {
            System.out.println("ResultFull.setResultVN2 set to original table");
            DrawingDTO drawingDTO = drawingNQ.vnTwoDrawing(resultRQ.getDrawCode());
            resultFullUtility.setVN2OriginalOrderItem(resultRQ, drawingDTO);
            publishResultUtility.removeDrawDTOCache(LotteryConstant.VN2, drawingDTO);
        }
    }

    private void setResultLeap(FullResultRQ resultRQ, boolean isFullResult) {
        try {
            DrawingDTO drawingDTO = tempDrawingNQ.leapTempDrawing(resultRQ.getDrawCode());
            List<LeapTempDrawingItemsEntity> leapTempDrawingItemsEntities = leapTempDrawingItemsRP.getAllByDrawId(drawingDTO.getId().longValue());
            Map<String, LeapTempDrawingItemsEntity> mapDrawingItemByPost = generalUtility.getMapByKeyPostCode(leapTempDrawingItemsEntities);

            for (String post : mapDrawingItemByPost.keySet()) {

                LeapTempDrawingItemsEntity drawingItemsEntity = mapDrawingItemByPost.get(post);

                List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

                for (FullResultRowRQ postItemRQ : postItems)
                    for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {
                        switch (columnItemRQ.getRebateCode()) {
                            case LotteryConstant.REBATE2D:
                                drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE3D:
                                drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE4D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE5D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));
                                drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                                drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                                break;
                            case LotteryConstant.REBATE6D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));
                                drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                                drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                                break;
                        }

                    }

            }
            leapTempDrawingItemsRP.saveAll(leapTempDrawingItemsEntities);
            if (isFullResult)
                drawingNQ.leapUpdateIsRecent(resultRQ.getDrawCode(), true);
            else {
                // reset draw, draw item and update from temp again
                leapTempDrawingRP.resetDrawing(drawingDTO.getDrawCode());
                leapTempDrawingRP.resetDrawingItems(drawingDTO.getDrawCode());
                leapTempDrawingRP.saveDrawing(drawingDTO.getDrawCode());
                leapTempDrawingRP.saveDrawingItems(drawingDTO.getDrawCode());
            }

            publishResultUtility.removeDrawDTOCache(LotteryConstant.LEAP, drawingDTO);
        } catch (NoResultException noResultException) {
            System.out.println("ResultFull.setResultLeap set to original table");
            DrawingDTO drawingDTO = drawingNQ.leapDrawing(resultRQ.getDrawCode());
            resultFullUtility.setLeapOriginalOrderItem(resultRQ, drawingDTO);
            publishResultUtility.removeDrawDTOCache(LotteryConstant.LEAP, drawingDTO);
        }
    }

    private void setResultSC(FullResultRQ resultRQ, boolean isFullResult) {
        try {
            DrawingDTO drawingDTO = tempDrawingNQ.scTempDrawing(resultRQ.getDrawCode());
            List<SCTempDrawingItemsEntity> scTempDrawingItemsEntities = sCTempDrawingItemRP.getAllByDrawingId(drawingDTO.getId().intValue());
            Map<String, SCTempDrawingItemsEntity> mapDrawingItemByPost = generalUtility.getMapByKeyPostCode(scTempDrawingItemsEntities);

            for (String post : mapDrawingItemByPost.keySet()) {

                SCTempDrawingItemsEntity drawingItemsEntity = mapDrawingItemByPost.get(post);

                List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

                for (FullResultRowRQ postItemRQ : postItems)
                    for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {
                        switch (columnItemRQ.getRebateCode()) {
                            case LotteryConstant.REBATE2D:
                                drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE3D:
                                drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE4D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                                break;
                            case LotteryConstant.REBATE5D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));
                                drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                                drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                                break;
                            case LotteryConstant.REBATE6D:
                                drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                                drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                                drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));
                                drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                                drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                                break;
                        }

                    }

            }
            sCTempDrawingItemRP.saveAll(scTempDrawingItemsEntities);
            if (isFullResult)
                sCTempDrawingRP.updateIsRecent(resultRQ.getDrawCode(), true);
            else {
                // reset draw, draw item and update from temp again
                sCTempDrawingRP.resetDrawing(drawingDTO.getDrawCode());
                sCTempDrawingRP.resetDrawingItems(drawingDTO.getDrawCode());
                sCTempDrawingRP.saveDrawing(drawingDTO.getDrawCode());
                sCTempDrawingRP.saveDrawingItems(drawingDTO.getDrawCode());
            }

            publishResultUtility.removeDrawDTOCache(LotteryConstant.SC, drawingDTO);
        } catch (NoResultException noResultException) {
            System.out.println("ResultFull.setResultLeap set to original table");
            DrawingDTO drawingDTO = drawingNQ.scDrawing(resultRQ.getDrawCode());
            resultFullUtility.setSCOriginalOrderItem(resultRQ, drawingDTO);
            publishResultUtility.removeDrawDTOCache(LotteryConstant.SC, drawingDTO);
        }
    }

    public List<FullResultInputRowRS> vnFullResultInput(VNOneTempDrawingEntity drawingDTO, List<DrawingItemsDTO> drawingItems) {
        List<FullResultInputRowRS> items = new ArrayList<>();

        Boolean isNight = drawingDTO.getIsNight();
        List<String> loFiveDigits = isNight ? PostConstant.LO_1_TO_9 : PostConstant.LO_6_TO_15;
        List<String> loFourDigits = isNight ? PostConstant.LO_10_TO_19 : PostConstant.LO_1_TO_4;

        List<String> posts = new ArrayList<>(PostConstant.getVNPostByIsNight(isNight));

        posts.removeAll(PostConstant.VN_REMOVAL_POST);

        // add A 3D
        if (!isNight)
            posts.add(1, POST_A3);

        for (String post : posts) {
            FullResultInputRowRS row = new FullResultInputRowRS();
            List<DrawingItemsDTO> subItemByPost = drawingItems.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            // add A 3D
            if (!isNight && POST_A3.equals(post)) {
                row.setRebateCode(LotteryConstant.REBATE3D);
                FullResultInputRowRS rowA = items.stream().filter(it -> it.getPostCode().equals(POST_A)).findFirst().orElseThrow(BadRequestException::new);
                FullResultInputRowRS rowA3D = new FullResultInputRowRS();

                rowA3D.setRebateCode(LotteryConstant.REBATE3D);
                rowA3D.setPostCode(rowA.getPostCode());
                rowA3D.setPostGroup(rowA.getPostGroup());

                for (FullResultInputItemRS columnItem : rowA.getItems()) {
                    FullResultInputItemRS item = new FullResultInputItemRS();
                    item.setNumber(columnItem.getThreeDigits());
                    item.setPostCode(POST_A);
                    item.setLotteryType(LotteryConstant.VN1);
                    item.setId(columnItem.getId());
                    item.setColumnNumber(columnItem.getColumnNumber());
                    item.setRebateCode(LotteryConstant.REBATE3D);
                    rowA3D.getItems().add(item);
                }

                items.add(rowA3D);
            }

            for (DrawingItemsDTO drawItem : subItemByPost) {

                row.setPostCode(drawItem.getPostCode());
                row.setPostGroup(drawItem.getPostGroup());

                FullResultInputItemRS item = new FullResultInputItemRS();
                BeanUtils.copyProperties(drawItem, item);
                generalUtility.prepareNumber(item, drawItem, drawingDTO.getIsNight(), LotteryConstant.VN1);

                item.setPostCode(post);
                item.setLotteryType(LotteryConstant.VN1);

                if (loFiveDigits.contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE5D);
                    item.setRebateCode(LotteryConstant.REBATE5D);
                    item.setNumber(drawItem.getFiveDigits());
                }

                if (loFourDigits.contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE4D);
                    item.setRebateCode(LotteryConstant.REBATE4D);
                    item.setNumber(drawItem.getFourDigits());
                }

                if (PostConstant.POST_B.equals(drawItem.getPostCode())) {
                    row.setRebateCode(isNight ? LotteryConstant.REBATE5D : LotteryConstant.REBATE6D);
                    item.setRebateCode(isNight ? LotteryConstant.REBATE5D : LotteryConstant.REBATE6D);
                    item.setNumber(isNight ? drawItem.getFiveDigits() : drawItem.getSixDigits());
                }

                if (PostConstant.getPostANight().contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE2D);
                    item.setRebateCode(LotteryConstant.REBATE2D);
                    item.setNumber(drawItem.getTwoDigits());
                }

                if (!isNight && post.equals(POST_A) && LotteryConstant.REBATE2D.equals(item.getRebateCode()))
                    if (drawItem.getTwoDigits() != null && drawItem.getTwoDigits().contains("*"))
                        item.setIsStartRender(Boolean.TRUE);

                if (isNight && post.equals(LO1))
                    if (drawItem.getFourDigits() != null && drawItem.getFourDigits().contains("*"))
                        item.setIsStartRender(Boolean.TRUE);

                row.getItems().add(item);

                // add 3D for post A night
                if (isNight && PostConstant.getPostANight().contains(drawItem.getPostCode()) && !PostConstant.POST_A4.equals(drawItem.getPostCode())) {
                    FullResultInputItemRS item3D = new FullResultInputItemRS();
                    BeanUtils.copyProperties(item, item3D);
                    item3D.setNumber(item.getThreeDigits());
                    item3D.setRebateCode(LotteryConstant.REBATE3D);
                    row.getItems().add(item3D);
                }
            }

            if (!subItemByPost.isEmpty())
                items.add(row);
        }

        return items;
    }

    public List<FullResultInputRowRS> tnFullResultInput(TNTempDrawingEntity drawingDTO, List<DrawingItemsDTO> drawingItems) {
        List<FullResultInputRowRS> items = new ArrayList<>();

        Boolean isNight = drawingDTO.getIsNight();
        List<String> loFiveDigits = isNight ? PostConstant.LO_1_TO_9 : PostConstant.LO_4_TO_15;
        List<String> loFourDigits = isNight ? PostConstant.LO_10_TO_19 : PostConstant.LO_1_TO_3;

        List<String> posts = new ArrayList<>(PostConstant.GET_TN_POST_BY_SHIFT_CODE(drawingDTO.getShiftCode()));

        posts.removeAll(PostConstant.TN_REMOVAL_POST);

        // add A 3D
        if (!isNight)
            posts.add(1, POST_A3);

        for (String post : posts) {
            FullResultInputRowRS row = new FullResultInputRowRS();
            List<DrawingItemsDTO> subItemByPost = drawingItems.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            // add A 3D
            if (!isNight && POST_A3.equals(post)) {
                row.setRebateCode(LotteryConstant.REBATE3D);
                FullResultInputRowRS rowA = items.stream().filter(it -> it.getPostCode().equals(POST_A)).findFirst().orElseThrow(BadRequestException::new);
                FullResultInputRowRS rowA3D = new FullResultInputRowRS();

                rowA3D.setRebateCode(LotteryConstant.REBATE3D);
                rowA3D.setPostCode(rowA.getPostCode());
                rowA3D.setPostGroup(rowA.getPostGroup());

                for (FullResultInputItemRS columnItem : rowA.getItems()) {
                    FullResultInputItemRS item = new FullResultInputItemRS();
                    item.setPostCode(POST_A);
                    item.setLotteryType(LotteryConstant.TN);
                    item.setNumber(columnItem.getThreeDigits());
                    item.setId(columnItem.getId());
                    item.setRebateCode(LotteryConstant.REBATE3D);
                    item.setColumnNumber(columnItem.getColumnNumber());
                    rowA3D.getItems().add(item);
                }

                items.add(rowA3D);
            }

            for (DrawingItemsDTO drawItem : subItemByPost) {

                row.setPostCode(drawItem.getPostCode());
                row.setPostGroup(drawItem.getPostGroup());

                FullResultInputItemRS item = new FullResultInputItemRS();
                BeanUtils.copyProperties(drawItem, item);
                generalUtility.prepareNumber(item, drawItem, drawingDTO.getIsNight(), LotteryConstant.TN);

                item.setPostCode(post);
                item.setLotteryType(LotteryConstant.TN);

                if (loFiveDigits.contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE5D);
                    item.setRebateCode(LotteryConstant.REBATE5D);
                    item.setNumber(drawItem.getFiveDigits());
                }

                if (loFourDigits.contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE4D);
                    item.setRebateCode(LotteryConstant.REBATE4D);
                    item.setNumber(drawItem.getFourDigits());
                }

                if (PostConstant.POST_B.equals(drawItem.getPostCode())) {
                    switch (drawingDTO.getShiftCode()) {
                        case "020":
                        case "022":
                            row.setRebateCode(LotteryConstant.REBATE5D);
                            item.setRebateCode(LotteryConstant.REBATE5D);
                            item.setNumber(drawItem.getFiveDigits());
                            break;
                        case "021":
                        case "024":
                            row.setRebateCode(LotteryConstant.REBATE6D);
                            item.setRebateCode(LotteryConstant.REBATE6D);
                            item.setNumber(drawItem.getSixDigits());
                            break;
                    }
                }

                if (PostConstant.getPostANight().contains(drawItem.getPostCode())) {
                    row.setRebateCode(LotteryConstant.REBATE2D);
                    item.setRebateCode(LotteryConstant.REBATE2D);
                    item.setNumber(drawItem.getTwoDigits());
                }

                if (!isNight && post.equals(POST_A) && LotteryConstant.REBATE2D.equals(item.getRebateCode()))
                    if (drawItem.getTwoDigits() != null && drawItem.getTwoDigits().contains("*"))
                        item.setIsStartRender(Boolean.TRUE);

                if (isNight && post.equals(LO1))
                    if (drawItem.getFourDigits() != null && drawItem.getFourDigits().contains("*"))
                        item.setIsStartRender(Boolean.TRUE);

                row.getItems().add(item);

                // add 3D for post A night
                if (isNight && PostConstant.getPostANight().contains(drawItem.getPostCode()) && !PostConstant.POST_A4.equals(drawItem.getPostCode())) {
                    FullResultInputItemRS item3D = new FullResultInputItemRS();
                    BeanUtils.copyProperties(item, item3D);
                    item3D.setNumber(item.getThreeDigits());
                    item3D.setRebateCode(LotteryConstant.REBATE3D);
                    row.getItems().add(item3D);
                }
            }

            if (!subItemByPost.isEmpty())
                items.add(row);
        }

        return items;
    }

}
