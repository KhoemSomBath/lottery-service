package com.hacknovation.systemservice.v1_0_0.service.betting;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.DrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempOrdersRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.betting.BettingListRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.betting.BettingListRS;
import com.hacknovation.systemservice.v1_0_0.utility.BettingUtility;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.MonitorTicketUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class BettingIP extends BaseServiceIP implements BettingSV {

    private final DrawingNQ drawingNQ;
    private final TempDrawingNQ tempDrawingNQ;
    private final GeneralUtility generalUtility;
    private final BettingUtility bettingUtility;
    private final HttpServletRequest request;
    private final MonitorTicketUtility monitorTicketUtility;
    private final VNOneTempOrderRP vNOneTempOrderRP;
    private final VNTwoTempOrderRP vNTwoTempOrderRP;
    private final LeapTempOrderRP leapTempOrderRP;
    private final KHTempOrderRP kHTempOrderRP;
    private final TNTempOrderRP tNTempOrderRP;
    private final SCTempOrderRP scTempOrderRP;
    private final THTempOrdersRP tHTempOrdersRP;

    @Override
    public StructureRS listing() {
        BettingListRQ bettingListRQ = new BettingListRQ(request);
        BettingListRS bettingListRS = new BettingListRS();
        DrawingDTO drawingDTO = null;

        boolean isTemp = generalUtility.isTempTable(bettingListRQ.getFilterByDate());
        switch (bettingListRQ.getLotteryType()) {
            case LotteryConstant.VN1:
                drawingDTO = isTemp ? tempDrawingNQ.vnOneTempDrawing(bettingListRQ.getDrawCode()) : drawingNQ.vnOneDrawing(bettingListRQ.getDrawCode());
                break;
            case LotteryConstant.VN2:
                drawingDTO = isTemp ? tempDrawingNQ.vnTwoTempDrawing(bettingListRQ.getDrawCode()) : drawingNQ.vnTwoDrawing(bettingListRQ.getDrawCode());
                break;
            case LotteryConstant.LEAP:
                drawingDTO = isTemp ? tempDrawingNQ.leapTempDrawing(bettingListRQ.getDrawCode()) : drawingNQ.leapDrawing(bettingListRQ.getDrawCode());
                break;
            case LotteryConstant.KH:
                drawingDTO = isTemp ? tempDrawingNQ.khTempDrawing(bettingListRQ.getDrawCode()) : drawingNQ.khDrawing(bettingListRQ.getDrawCode());
                break;
            case LotteryConstant.TN:
                drawingDTO = isTemp ? tempDrawingNQ.tnTempDrawing(bettingListRQ.getDrawCode()) : drawingNQ.tnDrawing(bettingListRQ.getDrawCode());
                break;
            case LotteryConstant.SC:
                drawingDTO = isTemp ? tempDrawingNQ.scTempDrawing(bettingListRQ.getDrawCode()) : drawingNQ.scDrawing(bettingListRQ.getDrawCode());
                break;
            case LotteryConstant.TH:
                drawingDTO = isTemp ? tempDrawingNQ.thTempDrawing(bettingListRQ.getDrawCode()) : drawingNQ.thDrawing(bettingListRQ.getDrawCode());
                break;
        }


        if (drawingDTO == null)
            return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);

        bettingUtility.bettingList(bettingListRS, bettingListRQ);

        bettingListRS.setPageNumber(bettingListRQ.getPageNumber());

        return responseBodyWithSuccessMessage(bettingListRS);
    }

    @Override
    public StructureRS updateIsSeen(String lottery, Long orderId) {
        switch (lottery) {
            case LotteryConstant.VN1:
                VNOneTempOrdersEntity vnOneTempOrdersEntity = vNOneTempOrderRP.getById(orderId);
                vnOneTempOrdersEntity.setIsSeen(!vnOneTempOrdersEntity.getIsSeen());
                vNOneTempOrderRP.save(vnOneTempOrdersEntity);
                break;
            case LotteryConstant.VN2:
                VNTwoTempOrdersEntity vnTwoTempOrdersEntity = vNTwoTempOrderRP.getById(orderId);
                vnTwoTempOrdersEntity.setIsSeen(!vnTwoTempOrdersEntity.getIsSeen());
                vNTwoTempOrderRP.save(vnTwoTempOrdersEntity);
                break;
            case LotteryConstant.LEAP:
                LeapTempOrdersEntity leapTempOrdersEntity = leapTempOrderRP.getById(orderId);
                leapTempOrdersEntity.setIsSeen(!leapTempOrdersEntity.getIsSeen());
                leapTempOrderRP.save(leapTempOrdersEntity);
                break;
            case LotteryConstant.KH:
                KHTempOrdersEntity khTempOrdersEntity = kHTempOrderRP.getById(orderId);
                khTempOrdersEntity.setIsSeen(!khTempOrdersEntity.getIsSeen());
                kHTempOrderRP.save(khTempOrdersEntity);
                break;
            case LotteryConstant.TN:
                TNTempOrdersEntity tnTempOrdersEntity = tNTempOrderRP.getById(orderId);
                tnTempOrdersEntity.setIsSeen(!tnTempOrdersEntity.getIsSeen());
                tNTempOrderRP.save(tnTempOrdersEntity);
                break;
            case LotteryConstant.SC:
                SCTempOrdersEntity scTempOrdersEntity = scTempOrderRP.getById(orderId);
                scTempOrdersEntity.setIsSeen(!scTempOrdersEntity.getIsSeen());
                scTempOrderRP.save(scTempOrdersEntity);
                break;
            case LotteryConstant.TH:
                THTempOrdersEntity thTempOrdersEntity = tHTempOrdersRP.getById(orderId);
                thTempOrdersEntity.setIsSeen(!thTempOrdersEntity.getIsSeen());
                tHTempOrdersRP.save(thTempOrdersEntity);
                break;
        }
        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS updateIsMark(String lottery, Long orderId) {
        switch (lottery) {
            case LotteryConstant.VN1:
                VNOneTempOrdersEntity vnOneTempOrdersEntity = vNOneTempOrderRP.getById(orderId);
                vnOneTempOrdersEntity.setIsMark(!vnOneTempOrdersEntity.getIsMark());
                vNOneTempOrderRP.save(vnOneTempOrdersEntity);
                break;
            case LotteryConstant.VN2:
                VNTwoTempOrdersEntity vnTwoTempOrdersEntity = vNTwoTempOrderRP.getById(orderId);
                vnTwoTempOrdersEntity.setIsMark(!vnTwoTempOrdersEntity.getIsMark());
                vNTwoTempOrderRP.save(vnTwoTempOrdersEntity);
                break;
            case LotteryConstant.LEAP:
                LeapTempOrdersEntity leapTempOrdersEntity = leapTempOrderRP.getById(orderId);
                leapTempOrdersEntity.setIsMark(!leapTempOrdersEntity.getIsMark());
                leapTempOrderRP.save(leapTempOrdersEntity);
                break;
            case LotteryConstant.KH:
                KHTempOrdersEntity khTempOrdersEntity = kHTempOrderRP.getById(orderId);
                khTempOrdersEntity.setIsMark(!khTempOrdersEntity.getIsMark());
                kHTempOrderRP.save(khTempOrdersEntity);
                break;
            case LotteryConstant.TN:
                TNTempOrdersEntity tnTempOrdersEntity = tNTempOrderRP.getById(orderId);
                tnTempOrdersEntity.setIsMark(!tnTempOrdersEntity.getIsMark());
                tNTempOrderRP.save(tnTempOrdersEntity);
                break;
            case LotteryConstant.SC:
                SCTempOrdersEntity scTempOrdersEntity = scTempOrderRP.getById(orderId);
                scTempOrdersEntity.setIsMark(!scTempOrdersEntity.getIsMark());
                scTempOrderRP.save(scTempOrdersEntity);
                break;
            case LotteryConstant.TH:
                THTempOrdersEntity thTempOrdersEntity = tHTempOrdersRP.getById(orderId);
                thTempOrdersEntity.setIsMark(!thTempOrdersEntity.getIsMark());
                tHTempOrdersRP.save(thTempOrdersEntity);
                break;
        }
        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS monitorTicket(String lottery, String drawCode, Integer size, Integer isMark, Integer page) {
        return monitorTicketUtility.getTicket(lottery, drawCode, size, isMark, page);
    }

}
