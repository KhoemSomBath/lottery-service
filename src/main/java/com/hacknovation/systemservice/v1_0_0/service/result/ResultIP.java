package com.hacknovation.systemservice.v1_0_0.service.result;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.ActivityLogEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.DrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.result.ResultNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.result.ResultTempNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.ActivityLogRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCDrawingItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.DrawItemRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.GetResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.PublishResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.UpdateDrawItemsRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.DrawItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.DrawItemsListRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.PublishResultUtility;
import com.hacknovation.systemservice.v1_0_0.utility.WinItemTHUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultIP extends BaseServiceIP implements ResultSV {

    private final VNOneTempDrawingItemsRP vnOneTempDrawingItemsRP;
    private final VNTwoTempDrawingItemsRP vnTwoTempDrawingItemsRP;
    private final TNTempDrawingItemsRP tnTempDrawingItemsRP;
    private final LeapTempDrawingItemsRP leapTempDrawingItemsRP;
    private final LeapDrawingItemsRP leapDrawingItemsRP;
    private final KHTempDrawingItemsRP khTempDrawingItemsRP;
    private final KHDrawingItemsRP khDrawingItemsRP;


    private final ActivityLogRP activityLogRP;
    private final ResultNQ resultNQ;
    private final ResultTempNQ resultTempNQ;
    private final DrawingNQ drawingNQ;
    private final TempDrawingNQ tempDrawingNQ;
    private final JwtToken jwtToken;
    private final HttpServletRequest request;
    private final GeneralUtility generalUtility;
    private final ActivityLogUtility activityLogUtility;
    private final PublishResultUtility publishResultUtility;
    private final WinItemTHUtility winItemTHUtility;

    private final KHTempDrawingRP khTempDrawingRP;

    private final LeapTempDrawingRP leapTempDrawingRP;
    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final TNTempDrawingRP tnTempDrawingRP;
    private final SCTempDrawingItemRP sCTempDrawingItemRP;
    private final SCDrawingItemRP sCDrawingItemRP;
    private final SCTempDrawingRP sCTempDrawingRP;
    private final THTempDrawingRP tHTempDrawingRP;
    private final THTempDrawingItemsRP tHTempDrawingItemsRP;
    private final THDrawingItemsRP tHDrawingItemsRP;

    @Override
    public StructureRS getAllResult(String lotteryType) {
        GetResultRQ getResultRQ = new GetResultRQ(request);
        getResultRQ.setLotteryType(lotteryType.toUpperCase());
        List<DrawItemsListRS> drawItemsListRSList = new ArrayList<>();
        boolean isTemp = generalUtility.isTempTable(getResultRQ.getFilterByDate());
        List<DrawingDTO> drawingDTOList = new ArrayList<>();
        switch (getResultRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:
                drawingDTOList = isTemp ? tempDrawingNQ.vnOneTempDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate()) : drawingNQ.vnOneDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate());
                break;
            case LotteryConstant.VN2:
                drawingDTOList = isTemp ? tempDrawingNQ.vnTwoTempDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate()) : drawingNQ.vnTwoDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate());
                break;
            case LotteryConstant.TN:
                drawingDTOList = isTemp ? tempDrawingNQ.tnTempDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate()) : drawingNQ.tnDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate());
                break;
            case LotteryConstant.LEAP:
                drawingDTOList = isTemp ? tempDrawingNQ.leapTempDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate()) : drawingNQ.leapDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate());
                break;
            case LotteryConstant.KH:
                drawingDTOList = isTemp ? tempDrawingNQ.khTempDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate()) : drawingNQ.khDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate());
                break;
            case LotteryConstant.SC:
                drawingDTOList = isTemp ? tempDrawingNQ.scTempDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate()) : drawingNQ.scDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate());
                break;
            case LotteryConstant.TH:
                drawingDTOList = isTemp ? tempDrawingNQ.thTempDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate()) : drawingNQ.thDrawingByDate(getResultRQ.getFilterByDate(), getResultRQ.getFilterByDate());
                break;
        }

        drawingDTOList.forEach(draw -> {
            DrawItemsListRS drawItemsListRS = getResultByDraw(draw, isTemp, getResultRQ.getLotteryType());
            drawItemsListRS.setHourKey(generalUtility.getHourKey(drawItemsListRS.getDrawAt()));
            if (draw.getIsRecent()) {
                drawItemsListRS.getResults().forEach(item -> {
                    item.setNumber(null);
                    item.setTwoDigits(null);
                    item.setThreeDigits(null);
                    item.setFourDigits(null);
                });
            }
            drawItemsListRSList.add(drawItemsListRS);
        });

        return responseBodyWithSuccessMessage(drawItemsListRSList);
    }

    @Override
    public StructureRS getResult(String lotteryType, String drawCode, String filterByDate) {
        GetResultRQ getResultRQ = new GetResultRQ(request);
        getResultRQ.setLotteryType(lotteryType.toUpperCase());
        DrawItemsListRS drawItemsListRS = new DrawItemsListRS();
        DrawingDTO drawingDTO = null;
        boolean isTemp = generalUtility.isTempTable(getResultRQ.getFilterByDate());
        switch (getResultRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:
                drawingDTO = isTemp ? tempDrawingNQ.vnOneTempDrawing(getResultRQ.getDrawCode()) : drawingNQ.vnOneDrawing(getResultRQ.getDrawCode());
                drawItemsListRS = getResultByDraw(drawingDTO, isTemp, LotteryConstant.VN1);
                break;
            case LotteryConstant.VN2:
                drawingDTO = isTemp ? tempDrawingNQ.vnTwoTempDrawing(getResultRQ.getDrawCode()) : drawingNQ.vnTwoDrawing(getResultRQ.getDrawCode());
                drawItemsListRS = getResultByDraw(drawingDTO, isTemp, LotteryConstant.VN2);
                break;
            case LotteryConstant.LEAP:
                drawingDTO = isTemp ? tempDrawingNQ.leapTempDrawing(getResultRQ.getDrawCode()) : drawingNQ.leapDrawing(getResultRQ.getDrawCode());
                drawItemsListRS = getResultByDraw(drawingDTO, isTemp, LotteryConstant.LEAP);
                break;
            case LotteryConstant.TN:
                drawingDTO = isTemp ? tempDrawingNQ.tnTempDrawing(getResultRQ.getDrawCode()) : drawingNQ.tnDrawing(getResultRQ.getDrawCode());
                drawItemsListRS = getResultByDraw(drawingDTO, isTemp, LotteryConstant.TN);
                break;
            case LotteryConstant.KH:
                drawingDTO = isTemp ? tempDrawingNQ.khTempDrawing(getResultRQ.getDrawCode()) : drawingNQ.khDrawing(getResultRQ.getDrawCode());
                drawItemsListRS = getResultByDraw(drawingDTO, isTemp, LotteryConstant.KH);
                break;
            case LotteryConstant.SC:
                drawingDTO = isTemp ? tempDrawingNQ.scTempDrawing(getResultRQ.getDrawCode()) : drawingNQ.scDrawing(getResultRQ.getDrawCode());
                drawItemsListRS = getResultByDraw(drawingDTO, isTemp, LotteryConstant.SC);
                break;
            case LotteryConstant.TH:
                drawingDTO = isTemp ? tempDrawingNQ.thTempDrawing(getResultRQ.getDrawCode()) : drawingNQ.thDrawing(getResultRQ.getDrawCode());
                drawItemsListRS = getResultByDraw(drawingDTO, isTemp, LotteryConstant.TH);
                break;
        }
        if (drawingDTO == null)
            return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);

        return responseBodyWithSuccessMessage(drawItemsListRS);
    }

    @Override
    public StructureRS setResult(UpdateDrawItemsRQ updateDrawItemsRQ) {
        var userToken = jwtToken.getUserToken();
        List<String> permissions = userToken.getPermissions();

        if (updateDrawItemsRQ.getResults().size() == 0)
            return responseBodyWithBadRequest(MessageConstant.DONT_HAVE_DRAW_ITEM, MessageConstant.DONT_HAVE_DRAW_ITEM_KEY);

        boolean isOkay = false;
        switch (updateDrawItemsRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:

                if (!permissions.contains("create-input-result-vnone"))
                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.YOU_DONT_HAVE_PERMISSION);

                DrawingDTO drawingDTO = tempDrawingNQ.vnOneTempDrawing(updateDrawItemsRQ.getDrawCode());
                if (drawingDTO == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
                if (drawingDTO.getResultedPostAt().getTime() > new Date().getTime())
                    return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_RELEASE, MessageConstant.RESULT_NOT_FULLY_RELEASE_KEY);
                isOkay = isSetVNOneOkay(drawingDTO, updateDrawItemsRQ);
                drawingNQ.vnOneUpdateIsRecent(drawingDTO.getDrawCode(), true);
                break;
            case LotteryConstant.VN2:

                if (!permissions.contains("create-input-result-mt"))
                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.YOU_DONT_HAVE_PERMISSION);

                DrawingDTO drawingDTO1 = tempDrawingNQ.vnTwoTempDrawing(updateDrawItemsRQ.getDrawCode());
                if (drawingDTO1 == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
                if (drawingDTO1.getResultedPostAt().getTime() > new Date().getTime())
                    return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_RELEASE, MessageConstant.RESULT_NOT_FULLY_RELEASE_KEY);
                isOkay = isSetVNTwoOkay(drawingDTO1, updateDrawItemsRQ);
                drawingNQ.vnTwoUpdateIsRecent(drawingDTO1.getDrawCode(), true);
                break;
            case LotteryConstant.TN:

                if (!permissions.contains("create-input-result-tn"))
                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.YOU_DONT_HAVE_PERMISSION);

                DrawingDTO drawingDTO3 = tempDrawingNQ.tnTempDrawing(updateDrawItemsRQ.getDrawCode());
                if (drawingDTO3 == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
                if (drawingDTO3.getResultedPostAt().getTime() > new Date().getTime())
                    return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_RELEASE, MessageConstant.RESULT_NOT_FULLY_RELEASE_KEY);
                isOkay = isSetTNOkay(drawingDTO3, updateDrawItemsRQ);
                drawingNQ.tnUpdateIsRecent(drawingDTO3.getDrawCode(), true);
                break;
            case LotteryConstant.LEAP:

                if (!permissions.contains("create-input-result-leap"))
                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.YOU_DONT_HAVE_PERMISSION);

                DrawingDTO drawingDTO2 = null;

                boolean isTemp = true;
                if (leapTempDrawingRP.existsByCode(updateDrawItemsRQ.getDrawCode()))
                    drawingDTO2 = tempDrawingNQ.leapTempDrawing(updateDrawItemsRQ.getDrawCode());

                if (drawingDTO2 == null) {
                    isTemp = false;
                    drawingDTO2 = drawingNQ.leapDrawing(updateDrawItemsRQ.getDrawCode());
                }

                if (drawingDTO2 == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);

                if (drawingDTO2.getResultedPostAt().getTime() > new Date().getTime())
                    return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_RELEASE, MessageConstant.RESULT_NOT_FULLY_RELEASE_KEY);

                isOkay = isSetLeapOkay(drawingDTO2, updateDrawItemsRQ, isTemp);
                drawingNQ.leapUpdateIsRecent(drawingDTO2.getDrawCode(), true);
                break;
            case LotteryConstant.SC:

//                if (!permissions.contains("create-input-result-sc"))
//                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.YOU_DONT_HAVE_PERMISSION);

                DrawingDTO drawingDTOSC = null;

                if (sCTempDrawingRP.existsByCode(updateDrawItemsRQ.getDrawCode()))
                    drawingDTOSC = tempDrawingNQ.scTempDrawing(updateDrawItemsRQ.getDrawCode());

                boolean isTempSc = true;
                if (drawingDTOSC == null) {
                    isTempSc = false;
                    try {
                        drawingDTOSC = drawingNQ.scDrawing(updateDrawItemsRQ.getDrawCode());
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
                    }
                }

                if (drawingDTOSC == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);

                if (drawingDTOSC.getResultedPostAt().getTime() > new Date().getTime())
                    return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_RELEASE, MessageConstant.RESULT_NOT_FULLY_RELEASE_KEY);

                isOkay = isSetSCOkay(drawingDTOSC, updateDrawItemsRQ, isTempSc);
                drawingNQ.scUpdateIsRecent(drawingDTOSC.getDrawCode(), true);
                break;
            case LotteryConstant.TH:

                DrawingDTO drawingDTOTH = null;

                if (tHTempDrawingRP.existsByCode(updateDrawItemsRQ.getDrawCode()))
                    drawingDTOTH = tempDrawingNQ.thTempDrawing(updateDrawItemsRQ.getDrawCode());

                boolean isTempTH = true;
                if (drawingDTOTH == null) {
                    isTempTH = false;
                    try {
                        drawingDTOTH = drawingNQ.scDrawing(updateDrawItemsRQ.getDrawCode());
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
                    }
                }

                if (drawingDTOTH == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);

                if (drawingDTOTH.getResultedPostAt().getTime() > new Date().getTime())
                    return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_RELEASE, MessageConstant.RESULT_NOT_FULLY_RELEASE_KEY);

                isOkay = isSetTHOkay(drawingDTOTH, updateDrawItemsRQ, isTempTH);
                tHTempDrawingRP.updateIsRecent(drawingDTOTH.getDrawCode(), true);
                break;
            case LotteryConstant.KH:

                if (!permissions.contains("create-input-result-kh"))
                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.YOU_DONT_HAVE_PERMISSION);

                DrawingDTO drawingDTO4 = null;
                if (khTempDrawingRP.existsByCode(updateDrawItemsRQ.getDrawCode()))
                    drawingDTO4 = tempDrawingNQ.khTempDrawing(updateDrawItemsRQ.getDrawCode());

                boolean isTemp1 = true;

                if (drawingDTO4 == null) {
                    isTemp1 = false;
                    drawingDTO4 = drawingNQ.khDrawing(updateDrawItemsRQ.getDrawCode());
                }

                if (drawingDTO4 == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);

                if (drawingDTO4.getResultedPostAt().getTime() > new Date().getTime())
                    return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_RELEASE, MessageConstant.RESULT_NOT_FULLY_RELEASE_KEY);

                isOkay = isSetKhOkay(drawingDTO4, updateDrawItemsRQ, isTemp1);
                drawingNQ.khUpdateIsRecent(drawingDTO4.getDrawCode(), true);
                break;
        }
        System.out.println("ResultIP.setResult of " + updateDrawItemsRQ.getLotteryType() + " At " + new Date());
        System.out.println(updateDrawItemsRQ);
        if (isOkay)
            return responseBodyWithSuccessMessage();
        return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_UPDATE, MessageConstant.RESULT_NOT_FULLY_UPDATE_KEY);
    }

    @Override
    public StructureRS publishResult(PublishResultRQ publishResultRQ) {
        DrawingDTO drawingDTO = null;
        String lotteryType = publishResultRQ.getLotteryType().toUpperCase();
        // get draw first
        try {
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
                case LotteryConstant.KH:
                    drawingDTO = tempDrawingNQ.khTempDrawing(publishResultRQ.getDrawCode());
                    break;
                case LotteryConstant.TN:
                    drawingDTO = tempDrawingNQ.tnTempDrawing(publishResultRQ.getDrawCode());
                    break;
                case LotteryConstant.TH:
                    drawingDTO = tempDrawingNQ.thTempDrawing(publishResultRQ.getDrawCode());
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
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

                    System.out.println("ResultIP.publishResult => VN1 set is_recent to false, date = " + new Date());
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

                    System.out.println("ResultIP.publishResult => VN2 set is_recent to false, date = " + new Date());
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

                    System.out.println("ResultIP.publishResult => TN set is_recent to false, date = " + new Date());
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

                    System.out.println("ResultIP.publishResult => LEAP set is_recent to false, date = " + new Date());
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

                    System.out.println("ResultIP.publishResult => SC set is_recent to false, date = " + new Date());
                    System.out.println(publishResultRQ);
                    return responseBodyWithSuccessMessage();
                }
                break;
            case LotteryConstant.TH:
                size = tHTempDrawingItemsRP.countByDrawingIdAndTwoDigitsNull(drawId);
                if (size == 0) {
                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    tHTempDrawingRP.resetWinOrderItems(drawingDTO.getDrawCode());
                    tHTempDrawingRP.updateIsRecent(drawingDTO.getDrawCode(), false);
                    tHTempDrawingRP.resetIsCalTempOrder(generalUtility.formatDateYYYYMMDD(drawingDTO.getResultedPostAt()));
                    drawingNQ.resetSummaryDaily(LotteryConstant.TH, drawingDTO.getResultedPostAt());
                    tHTempDrawingRP.updateDrawAwarded(drawingDTO.getDrawCode());

                    winItemTHUtility.setWinLoseByDraw((long) drawId, drawingDTO.getDrawCode(), true);

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.TH, drawingDTO);

                    System.out.println("ResultIP.publishResult => TH set is_recent to false, date = " + new Date());
                    System.out.println(publishResultRQ);
                    return responseBodyWithSuccessMessage();
                }
                break;
            case LotteryConstant.KH:
                size = khTempDrawingItemsRP.countAllByDrawingIdAndTwoDigitsIsNull(drawId);
                if (size == 0) {
                    publishResultUtility.resetWinBalanceUserOnline(lotteryType, drawingDTO);

                    /*
                     * reset win order item to be re-calculate
                     */
                    drawingNQ.khResetWinOrderItems(drawingDTO.getDrawCode());
                    drawingNQ.khUpdateIsRecent(drawingDTO.getDrawCode(), false);
                    drawingNQ.khResetIsCalTempOrder(drawingDTO.getResultedPostAt());
                    drawingNQ.resetSummaryDaily(LotteryConstant.KH, drawingDTO.getResultedPostAt());
                    khTempDrawingRP.updateDrawAwarded(drawingDTO.getDrawCode());

                    // remove cache
                    publishResultUtility.removeDrawDTOCache(LotteryConstant.KH, drawingDTO);

                    System.out.println("ResultIP.publishResult => KH set is_recent to false, date = " + new Date());
                    System.out.println(publishResultRQ);
                    return responseBodyWithSuccessMessage();
                }
                break;
        }

        return responseBodyWithBadRequest(MessageConstant.RESULT_NOT_FULLY_UPDATE, MessageConstant.RESULT_NOT_FULLY_UPDATE_KEY);

    }

    /**
     * update drawing item and check is complement update
     *
     * @param drawingDTO        DrawingDTO
     * @param updateDrawItemsRQ UpdateDrawItemsRQ
     * @return boolean
     */
    private boolean isSetVNOneOkay(DrawingDTO drawingDTO, UpdateDrawItemsRQ updateDrawItemsRQ) {
        List<DrawingItemsDTO> drawingItemsDTOList = resultTempNQ.getVNOneTempResult(drawingDTO.getId().intValue());

        drawingItemsDTOList = drawingItemsDTOList.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());

        Map<String, DrawingItemsDTO> mapDrawItems = drawingItemsDTOList.stream().collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));

        Map<String, DrawItemRQ> mapDrawItemRQ = updateDrawItemsRQ.getResults().stream().collect(Collectors.toMap(DrawItemRQ::getPostCode, Function.identity()));

        List<ActivityLogEntity> activityLogEntities = new ArrayList<>();

        for (String post : mapDrawItemRQ.keySet()) {
            DrawItemRQ drawItemRQ = mapDrawItemRQ.get(post);
            DrawingItemsDTO itemsDTO = mapDrawItems.get(post);

            itemsDTO.setDigits(LotteryConstant.VN1, drawItemRQ, drawingDTO.getIsNight(), drawingDTO);
            /*
             * set activity log to a list
             */
            activityLogUtility.logSetResult(LotteryConstant.VN1, drawingDTO, jwtToken.getUserToken(), drawItemRQ, activityLogEntities);
        }

        // save log
        activityLogRP.saveAll(activityLogEntities);

        List<VNOneTempDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(VNOneTempDrawingItemsEntity::new).collect(Collectors.toList());
        vnOneTempDrawingItemsRP.saveAll(drawingItemsEntities);


        long size;

        if (drawingDTO.getIsNight())
            size = vnOneTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndPostCodeNotLikeAndTwoDigitsIsNull(drawingDTO.getId().intValue(), 1, "K");
        else
            size = vnOneTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(drawingDTO.getId().intValue(), 1);

        return size == 0;
    }

    private boolean isSetVNTwoOkay(DrawingDTO drawingDTO, UpdateDrawItemsRQ updateDrawItemsRQ) {
        List<DrawingItemsDTO> drawingItemsDTOList = resultTempNQ.getVNTwoTempResult(drawingDTO.getId().intValue());
        drawingItemsDTOList = drawingItemsDTOList.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());

        Map<String, DrawingItemsDTO> mapDrawItems = drawingItemsDTOList.stream().collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));

        Map<String, DrawItemRQ> mapDrawItemRQ = updateDrawItemsRQ.getResults().stream().collect(Collectors.toMap(DrawItemRQ::getPostCode, Function.identity()));

        List<ActivityLogEntity> activityLogEntities = new ArrayList<>();

        for (String post : mapDrawItemRQ.keySet()) {
            DrawItemRQ drawItemRQ = mapDrawItemRQ.get(post);
            DrawingItemsDTO itemsDTO = mapDrawItems.get(post);

            itemsDTO.setDigits(LotteryConstant.VN2, drawItemRQ, drawingDTO.getIsNight(), drawingDTO);
            /*
             * set activity log to a list
             */
            activityLogUtility.logSetResult(LotteryConstant.VN2, drawingDTO, jwtToken.getUserToken(), drawItemRQ, activityLogEntities);
        }

        // save log
        activityLogRP.saveAll(activityLogEntities);

        List<VNTwoTempDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(VNTwoTempDrawingItemsEntity::new).collect(Collectors.toList());
        vnTwoTempDrawingItemsRP.saveAll(drawingItemsEntities);


        long size;

        if (drawingDTO.getIsNight())
            size = vnTwoTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndPostCodeNotLikeAndTwoDigitsIsNull(drawingDTO.getId().intValue(), 1, "K");
        else
            size = vnTwoTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(drawingDTO.getId().intValue(), 1);

        return size == 0;
    }

    private boolean isSetLeapOkay(DrawingDTO drawingDTO, UpdateDrawItemsRQ updateDrawItemsRQ, Boolean isTemp) {
        List<DrawingItemsDTO> drawingItemsDTOList = new ArrayList<>();

        if (isTemp)
            drawingItemsDTOList = resultTempNQ.getLeapTempResult(drawingDTO.getId().intValue());
        else
            drawingItemsDTOList = resultNQ.getLeapResult(drawingDTO.getId().intValue());

        Map<String, DrawingItemsDTO> mapDrawItems = drawingItemsDTOList.stream().collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));

        Map<String, DrawItemRQ> mapDrawItemRQ = updateDrawItemsRQ.getResults().stream().collect(Collectors.toMap(DrawItemRQ::getPostCode, Function.identity()));

        List<ActivityLogEntity> activityLogEntities = new ArrayList<>();

        for (String post : mapDrawItemRQ.keySet()) {
            DrawItemRQ drawItemRQ = mapDrawItemRQ.get(post);
            DrawingItemsDTO itemsDTO = mapDrawItems.get(post);

            itemsDTO.setDigits(LotteryConstant.LEAP, drawItemRQ, drawingDTO.getIsNight(), drawingDTO);
            /*
             * set activity log to a list
             */
            activityLogUtility.logSetResult(LotteryConstant.LEAP, drawingDTO, jwtToken.getUserToken(), drawItemRQ, activityLogEntities);
        }

        // save log
        activityLogRP.saveAll(activityLogEntities);

        long size;
        if (isTemp) {
            List<LeapTempDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(LeapTempDrawingItemsEntity::new).collect(Collectors.toList());
            leapTempDrawingItemsRP.saveAll(drawingItemsEntities);
            size = leapTempDrawingItemsRP.countAllByDrawingIdAndTwoDigitsIsNull(drawingDTO.getId().intValue());
        } else {
            List<LeapDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(LeapDrawingItemsEntity::new).collect(Collectors.toList());
            leapDrawingItemsRP.saveAll(drawingItemsEntities);
            size = leapDrawingItemsRP.countAllByDrawingIdAndTwoDigitsIsNull(drawingDTO.getId().intValue());
        }

        return size == 0;
    }

    private boolean isSetSCOkay(DrawingDTO drawingDTO, UpdateDrawItemsRQ updateDrawItemsRQ, Boolean isTemp) {
        List<DrawingItemsDTO> drawingItemsDTOList = new ArrayList<>();

        if (isTemp)
            drawingItemsDTOList = resultTempNQ.getSCTempResult(drawingDTO.getId().intValue());
        else
            drawingItemsDTOList = resultNQ.getSCResult(drawingDTO.getId().intValue());

        Map<String, DrawingItemsDTO> mapDrawItems = drawingItemsDTOList.stream().collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));

        Map<String, DrawItemRQ> mapDrawItemRQ = updateDrawItemsRQ.getResults().stream().collect(Collectors.toMap(DrawItemRQ::getPostCode, Function.identity()));

        List<ActivityLogEntity> activityLogEntities = new ArrayList<>();

        for (String post : mapDrawItemRQ.keySet()) {
            DrawItemRQ drawItemRQ = mapDrawItemRQ.get(post);
            DrawingItemsDTO itemsDTO = mapDrawItems.get(post);

            itemsDTO.setDigits(LotteryConstant.SC, drawItemRQ, drawingDTO.getIsNight(), drawingDTO);
            /*
             * set activity log to a list
             */
            activityLogUtility.logSetResult(LotteryConstant.SC, drawingDTO, jwtToken.getUserToken(), drawItemRQ, activityLogEntities);
        }

        // save log
        activityLogRP.saveAll(activityLogEntities);

        long size;
        if (isTemp) {
            List<SCTempDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(SCTempDrawingItemsEntity::new).collect(Collectors.toList());
            sCTempDrawingItemRP.saveAll(drawingItemsEntities);
            size = sCTempDrawingItemRP.countByDrawingIdAndTwoDigitsNull(drawingDTO.getId().intValue());
        } else {
            List<SCDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(SCDrawingItemsEntity::new).collect(Collectors.toList());
            sCDrawingItemRP.saveAll(drawingItemsEntities);
            size = sCDrawingItemRP.countByDrawingIdAndTwoDigitsNull(drawingDTO.getId().intValue());
        }

        return size == 0;
    }

    private boolean isSetTHOkay(DrawingDTO drawingDTO, UpdateDrawItemsRQ updateDrawItemsRQ, Boolean isTemp) {
        List<DrawingItemsDTO> drawingItemsDTOList = new ArrayList<>();

        if (isTemp)
            drawingItemsDTOList = resultTempNQ.getTHTempResult(drawingDTO.getId().intValue());
        else
            drawingItemsDTOList = resultNQ.getTHResult(drawingDTO.getId().intValue());

        Map<String, DrawingItemsDTO> mapDrawItems = drawingItemsDTOList.stream().collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));

        Map<String, DrawItemRQ> mapDrawItemRQ = updateDrawItemsRQ.getResults().stream().collect(Collectors.toMap(DrawItemRQ::getPostCode, Function.identity()));

        List<ActivityLogEntity> activityLogEntities = new ArrayList<>();

        for (String post : mapDrawItemRQ.keySet()) {
            DrawItemRQ drawItemRQ = mapDrawItemRQ.get(post);
            DrawingItemsDTO itemsDTO = mapDrawItems.get(post);

            itemsDTO.setDigits(LotteryConstant.TH, drawItemRQ, drawingDTO.getIsNight(), drawingDTO);
            /*
             * set activity log to a list
             */
            activityLogUtility.logSetResult(LotteryConstant.TH, drawingDTO, jwtToken.getUserToken(), drawItemRQ, activityLogEntities);
        }

        // save log
        activityLogRP.saveAll(activityLogEntities);

        long size;
        if (isTemp) {
            List<THTempDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(THTempDrawingItemsEntity::new).collect(Collectors.toList());
            tHTempDrawingItemsRP.saveAll(drawingItemsEntities);
            size = tHTempDrawingItemsRP.countByDrawingIdAndTwoDigitsNull(drawingDTO.getId().intValue());
        } else {
            List<THDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(THDrawingItemsEntity::new).collect(Collectors.toList());
            tHDrawingItemsRP.saveAll(drawingItemsEntities);
            size = tHDrawingItemsRP.countByDrawingIdAndTwoDigitsNull(drawingDTO.getId().intValue());
        }

        return size == 0;
    }


    private boolean isSetKhOkay(DrawingDTO drawingDTO, UpdateDrawItemsRQ updateDrawItemsRQ, Boolean isTemp) {
        List<DrawingItemsDTO> drawingItemsDTOList = new ArrayList<>();

        if (isTemp)
            drawingItemsDTOList = resultTempNQ.getKhTempResult(drawingDTO.getId().intValue());
        else
            drawingItemsDTOList = resultNQ.getKhResult(drawingDTO.getId().intValue());

        Map<String, DrawingItemsDTO> mapDrawItems = drawingItemsDTOList.stream().collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));

        Map<String, DrawItemRQ> mapDrawItemRQ = updateDrawItemsRQ.getResults().stream().collect(Collectors.toMap(DrawItemRQ::getPostCode, Function.identity()));

        List<ActivityLogEntity> activityLogEntities = new ArrayList<>();

        for (String post : mapDrawItemRQ.keySet()) {
            DrawItemRQ drawItemRQ = mapDrawItemRQ.get(post);
            DrawingItemsDTO itemsDTO = mapDrawItems.get(post);

            itemsDTO.setDigits(LotteryConstant.KH, drawItemRQ, drawingDTO.getIsNight(), drawingDTO);
            /*
             * set activity log to a list
             */
            activityLogUtility.logSetResult(LotteryConstant.KH, drawingDTO, jwtToken.getUserToken(), drawItemRQ, activityLogEntities);
        }

        // save log
        activityLogRP.saveAll(activityLogEntities);

        long size;
        if (isTemp) {
            List<KHTempDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(KHTempDrawingItemsEntity::new).collect(Collectors.toList());
            khTempDrawingItemsRP.saveAll(drawingItemsEntities);
            size = khTempDrawingItemsRP.countAllByDrawingIdAndTwoDigitsIsNull(drawingDTO.getId().intValue());
        } else {
            List<KHDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(KHDrawingItemsEntity::new).collect(Collectors.toList());
            khDrawingItemsRP.saveAll(drawingItemsEntities);
            size = khDrawingItemsRP.countAllByDrawingIdAndTwoDigitsIsNull(drawingDTO.getId().intValue());
        }

        return size == 0;
    }

    private Boolean isSetTNOkay(DrawingDTO drawingDTO, UpdateDrawItemsRQ updateDrawItemsRQ) {
        List<DrawingItemsDTO> drawingItemsDTOList = resultTempNQ.getTNResult(drawingDTO.getId().intValue());
        drawingItemsDTOList = drawingItemsDTOList.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());

        Map<String, DrawingItemsDTO> mapDrawItems = drawingItemsDTOList.stream().collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));

        Map<String, DrawItemRQ> mapDrawItemRQ = updateDrawItemsRQ.getResults().stream().collect(Collectors.toMap(DrawItemRQ::getPostCode, Function.identity()));

        List<ActivityLogEntity> activityLogEntities = new ArrayList<>();

        for (String post : mapDrawItemRQ.keySet()) {
            DrawItemRQ drawItemRQ = mapDrawItemRQ.get(post);
            DrawingItemsDTO itemsDTO = mapDrawItems.get(post);

            itemsDTO.setDigits(LotteryConstant.TN, drawItemRQ, drawingDTO.getIsNight(), drawingDTO);

            if (!drawingDTO.getIsNight()) {
                String fiveDigits = drawItemRQ.getTwoDigits().concat(drawItemRQ.getThreeDigits());
                String fourDigits = StringUtils.right(fiveDigits, 4);
                switch (post) {
                    case PostConstant.LO1:
                        DrawingItemsDTO itemPostA = mapDrawItems.get(PostConstant.POST_A);
                        itemPostA.setFourDigits(drawItemRQ.getNumber());
                        break;
                    case PostConstant.POST_A:
                        DrawingItemsDTO itemPostO = mapDrawItems.get(PostConstant.POST_O);
                        itemPostO.setTwoDigits(StringUtils.right(drawItemRQ.getThreeDigits(), 2));
                        itemPostO.setThreeDigits(drawItemRQ.getThreeDigits());
                        break;
                    case PostConstant.POST_K:
                        itemsDTO.setTwoDigits(StringUtils.right(drawItemRQ.getNumber(), 2));
                        itemsDTO.setThreeDigits(StringUtils.right(drawItemRQ.getNumber(), 3));
                        itemsDTO.setFourDigits(drawItemRQ.getNumber());
                        itemsDTO.setFiveDigits("*".concat(drawItemRQ.getNumber()));
                        itemsDTO.setSixDigits("**".concat(drawItemRQ.getNumber()));

                        DrawingItemsDTO itemPostF = mapDrawItems.get(PostConstant.POST_F);
                        itemPostF.setFourDigits(drawItemRQ.getNumber());
                        break;
                    case PostConstant.POST_I:
                        if (drawItemRQ.getFourDigits() != null)
                            itemsDTO.setFourDigits(drawItemRQ.getFourDigits());
                        break;
                    case PostConstant.POST_N:
                    case PostConstant.POST_B:
                        itemsDTO.setFourDigits(StringUtils.right(fourDigits, 4));
                        break;
                }
            } else {
                switch (post) {
                    case PostConstant.POST_A:
                        DrawingItemsDTO itemPostO = mapDrawItems.get(PostConstant.POST_O);
                        if (itemPostO != null) {
                            itemPostO.setTwoDigits(StringUtils.right(itemsDTO.getThreeDigits(), 2));
                            itemPostO.setThreeDigits(itemsDTO.getThreeDigits());
                        }
                    case PostConstant.POST_A2:
                        DrawingItemsDTO itemPostO2 = mapDrawItems.get(PostConstant.POST_O2);
                        if (itemPostO2 != null) {
                            itemPostO2.setTwoDigits(StringUtils.right(itemsDTO.getThreeDigits(), 2));
                            itemPostO2.setThreeDigits(itemsDTO.getThreeDigits());
                        }
                        break;
                    case PostConstant.POST_A3:
                        DrawingItemsDTO itemPostO3 = mapDrawItems.get(PostConstant.POST_O3);
                        if (itemPostO3 != null) {
                            itemPostO3.setTwoDigits(StringUtils.right(itemsDTO.getThreeDigits(), 2));
                            itemPostO3.setThreeDigits(itemsDTO.getThreeDigits());
                        }
                        break;
                }
            }

            /*
             * set activity log to a list
             */
            activityLogUtility.logSetResult(LotteryConstant.TN, drawingDTO, jwtToken.getUserToken(), drawItemRQ, activityLogEntities);
        }

        // save log
        activityLogRP.saveAll(activityLogEntities);


        List<TNTempDrawingItemsEntity> drawingItemsEntities = drawingItemsDTOList.stream().map(TNTempDrawingItemsEntity::new).collect(Collectors.toList());
        tnTempDrawingItemsRP.saveAll(drawingItemsEntities);

        long size;

        if (drawingDTO.getIsNight())
            size = tnTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndPostCodeNotInAndTwoDigitsIsNull(drawingDTO.getId().intValue(), 1, List.of("F", "I", "N", "K", "P", "Z"));
        else
            size = tnTempDrawingItemsRP.countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(drawingDTO.getId().intValue(), 1);

        return size == 0;
    }


    /**
     * get result set for LEAP
     *
     * @param drawItemsListRS     DrawItemsListRS
     * @param drawingItemsDTOList List<DrawingItemsDTO>
     */
    private void getResultSetLeap(DrawItemsListRS drawItemsListRS, List<DrawingItemsDTO> drawingItemsDTOList) {
        List<DrawItemRS> drawItemRSList = new ArrayList<>();

        for (DrawingItemsDTO drawingItem : drawingItemsDTOList) {
            DrawItemRS item = new DrawItemRS();

            item.setTwoDigits(drawingItem.getTwoDigits());
            item.setThreeDigits(drawingItem.getThreeDigits());
            item.setFourDigits(drawingItem.getFourDigits());
            item.setNumber(drawingItem.getFiveDigits());
            item.setId(drawingItem.getId().intValue());
            item.setPostCode(drawingItem.getPostCode());
            item.setPostGroup(drawingItem.getPostGroup());

            if (LotteryConstant.POST.equalsIgnoreCase(drawingItem.getPostGroup()))
                item.setNumber(drawingItem.getSixDigits());

            drawItemRSList.add(item);
        }

        drawItemsListRS.setResults(drawItemRSList);

    }


    private void getResultSetTH(DrawItemsListRS drawItemsListRS, List<DrawingItemsDTO> drawingItemsDTOList) {
        List<DrawItemRS> drawItemRSList = new ArrayList<>();

        for (DrawingItemsDTO drawingItem : drawingItemsDTOList) {
            DrawItemRS item = new DrawItemRS();

            item.setTwoDigits(drawingItem.getTwoDigits());
            item.setThreeDigits(drawingItem.getThreeDigits());
            item.setFourDigits(drawingItem.getFourDigits());
            item.setNumber(drawingItem.getSixDigits());
            item.setId(drawingItem.getId().intValue());
            item.setPostCode(drawingItem.getPostCode());
            item.setPostGroup(drawingItem.getPostGroup());

            if (PostConstant.POST_A.equals(drawingItem.getPostCode()))
                item.setThNumber(drawingItem.getThreeDigits());

            if (PostConstant.POST_B.equals(drawingItem.getPostCode())) {
                item.setThNumber(drawingItem.getTwoDigits());
                item.setNumber(drawingItem.getTwoDigits());
            }

            drawItemRSList.add(item);
        }

        drawItemsListRS.setResults(drawItemRSList);

    }

    /**
     * get result set for day shift
     *
     * @param drawItemsListRS     DrawItemsListRS
     * @param drawingItemsDTOList List<DrawingItemsDTO>
     * @param drawingDTO
     * @param lotteryType
     */
    private void getResultSetDay(DrawItemsListRS drawItemsListRS, List<DrawingItemsDTO> drawingItemsDTOList, DrawingDTO drawingDTO, String lotteryType) {
        Map<String, DrawingItemsDTO> mapDrawItem = drawingItemsDTOList.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));
        List<DrawItemRS> drawItemRSList = new ArrayList<>();

        List<String> posts = PostConstant.POST_RESULT_DAY;

        if (LotteryConstant.TN.equalsIgnoreCase(lotteryType)) {
            posts = PostConstant.GET_TN_POST_BY_SHIFT_CODE(drawingDTO.getShiftCode());
        }

        for (String post : posts) {
            DrawItemRS drawItemRS = new DrawItemRS();
            DrawingItemsDTO itemsDTO = mapDrawItem.get(post);

            if (itemsDTO != null) {
                BeanUtils.copyProperties(itemsDTO, drawItemRS);

                drawItemRS.setId(itemsDTO.getId().intValue());
                generalUtility.prepareNumber(drawItemRS, mapDrawItem.get(post), false, lotteryType);
            }

            if (LotteryConstant.TN.equalsIgnoreCase(lotteryType)) {

                if (drawingDTO.getShiftCode().equals(PostConstant.TN_SHIFT_2_CODE))
                    if (List.of(PostConstant.POST_B, PostConstant.POST_C, PostConstant.POST_D, PostConstant.POST_N, PostConstant.POST_P).contains(post)) {
                        drawItemRS.setNumber(itemsDTO.getSixDigits());
                    }
            }
            drawItemRSList.add(drawItemRS);
        }
        drawItemsListRS.setResults(drawItemRSList);

    }

    /**
     * get result set for night shift
     *
     * @param drawItemsListRS     DrawItemsListRS
     * @param drawingItemsDTOList List<DrawingItemsDTO>
     */
    private void getResultSetNight(DrawItemsListRS drawItemsListRS, List<DrawingItemsDTO> drawingItemsDTOList) {
        Map<String, DrawingItemsDTO> mapDrawItem = drawingItemsDTOList.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toMap(DrawingItemsDTO::getPostCode, Function.identity()));
        List<DrawItemRS> drawItemRSList = new ArrayList<>();
        for (String post : PostConstant.POST_RESULT_NIGHT) {
            DrawItemRS drawItemRS = new DrawItemRS();
            BeanUtils.copyProperties(mapDrawItem.get(post), drawItemRS);
            generalUtility.prepareNumber(drawItemRS, mapDrawItem.get(post), true, LotteryConstant.VN1);
            drawItemRSList.add(drawItemRS);
        }
        drawItemsListRS.setIsNight(Boolean.TRUE);
        drawItemsListRS.setResults(drawItemRSList);
    }


    /**
     * get result vnOne by draw
     *
     * @param drawingDTO DrawingDTO
     * @param isTemp     boolean
     * @return DrawItemsListRS
     */
    private DrawItemsListRS getResultByDraw(DrawingDTO drawingDTO, boolean isTemp, String lotteryType) {
        DrawItemsListRS drawItemsListRS = new DrawItemsListRS();
        drawItemsListRS.setLotteryType(lotteryType);
        drawItemsListRS.setDrawAt(drawingDTO.getResultedPostAt());
        drawItemsListRS.setIsNight(drawingDTO.getIsNight());

        List<DrawingItemsDTO> drawingItemsDTOList = new ArrayList<>();

        switch (lotteryType.toUpperCase()) {

            case LotteryConstant.VN1:
                drawingItemsDTOList = isTemp ? resultTempNQ.getVNOneTempResult(drawingDTO.getId().intValue()) : resultNQ.getVNOneResult(drawingDTO.getId().intValue());
                break;

            case LotteryConstant.VN2:
                drawingItemsDTOList = isTemp ? resultTempNQ.getVNTwoTempResult(drawingDTO.getId().intValue()) : resultNQ.getVNTwoResult(drawingDTO.getId().intValue());
                break;

            case LotteryConstant.TN:
                drawingItemsDTOList = isTemp ? resultTempNQ.getTNResult(drawingDTO.getId().intValue()) : resultNQ.getTNResult(drawingDTO.getId().intValue());
                break;

            case LotteryConstant.LEAP:
                drawingItemsDTOList = isTemp ? resultTempNQ.getLeapTempResult(drawingDTO.getId().intValue()) : resultNQ.getLeapResult(drawingDTO.getId().intValue());
                break;

            case LotteryConstant.KH:
                drawingItemsDTOList = isTemp ? resultTempNQ.getKhTempResult(drawingDTO.getId().intValue()) : resultNQ.getKhResult(drawingDTO.getId().intValue());
                break;

            case LotteryConstant.SC:
                drawingItemsDTOList = isTemp ? resultTempNQ.getSCTempResult(drawingDTO.getId().intValue()) : resultNQ.getSCResult(drawingDTO.getId().intValue());
                break;

            case LotteryConstant.TH:
                drawingItemsDTOList = isTemp ? resultTempNQ.getTHTempResult(drawingDTO.getId().intValue()) : resultNQ.getTHResult(drawingDTO.getId().intValue());
                break;
        }

        if (List.of(LotteryConstant.LEAP, LotteryConstant.KH, LotteryConstant.SC).contains(lotteryType.toUpperCase())) {
            getResultSetLeap(drawItemsListRS, drawingItemsDTOList);
        } else if (LotteryConstant.TH.equalsIgnoreCase(lotteryType)) {
            getResultSetTH(drawItemsListRS, drawingItemsDTOList);
        } else if (drawingDTO.getIsNight()) {
            getResultSetNight(drawItemsListRS, drawingItemsDTOList);
        } else {
            getResultSetDay(drawItemsListRS, drawingItemsDTOList, drawingDTO, lotteryType);
        }

        return drawItemsListRS;
    }

}
