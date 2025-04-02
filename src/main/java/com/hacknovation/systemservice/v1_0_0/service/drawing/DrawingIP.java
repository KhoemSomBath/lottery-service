package com.hacknovation.systemservice.v1_0_0.service.drawing;

import com.hacknovation.systemservice.constant.HazelcastConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.DrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.draw.UpdateDrawingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.drawing.DrawRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrawingIP extends BaseServiceIP implements DrawingSV {

    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final LeapTempDrawingRP leapTempDrawingRP;
    private final SCTempDrawingRP scTempDrawingRP;
    private final KHTempDrawingRP khTempDrawingRP;
    private final TNTempDrawingRP tnTempDrawingRP;
    private final DrawingNQ drawingNQ;
    private final TempDrawingNQ tempDrawingNQ;
    private final GeneralUtility generalUtility;
    private final HazelcastInstance hazelcastInstance;
    private final THTempDrawingRP tHTempDrawingRP;

    @Override
    public StructureRS drawShifts(String lotteryType, String filterByDate) {

        if ("".equals(filterByDate))
            filterByDate = generalUtility.formatDateYYYYMMDD(new Date());

        List<DrawRS> drawRSList;
        List<DrawingDTO> drawingDTOS;

        boolean isTemp = generalUtility.isTempTable(filterByDate);

        switch (lotteryType.toUpperCase()) {
            case LotteryConstant.VN1:
                if (isTemp) {
                    drawingDTOS = tempDrawingNQ.vnOneTempDrawingByDate(filterByDate, filterByDate);
                } else {
                    drawingDTOS = drawingNQ.vnOneDrawingByDate(filterByDate, filterByDate);
                }
                drawRSList = generalUtility.getListDraw(drawingDTOS);
                break;
            case LotteryConstant.VN2:
            case LotteryConstant.MT:
                if (isTemp) {
                    drawingDTOS = tempDrawingNQ.vnTwoTempDrawingByDate(filterByDate, filterByDate);
                } else {
                    drawingDTOS = drawingNQ.vnTwoDrawingByDate(filterByDate, filterByDate);
                }
                drawRSList = generalUtility.getListDraw(drawingDTOS);
                break;
            case LotteryConstant.TN:
                if (isTemp) {
                    drawingDTOS = tempDrawingNQ.tnTempDrawingByDate(filterByDate, filterByDate);
                } else {
                    drawingDTOS = drawingNQ.tnDrawingByDate(filterByDate, filterByDate);
                }
                drawRSList = generalUtility.getListDraw(drawingDTOS);
                break;
            case LotteryConstant.LEAP:
                if (isTemp) {
                    drawingDTOS = tempDrawingNQ.leapTempDrawingByDate(filterByDate, filterByDate);
                } else {
                    drawingDTOS = drawingNQ.leapDrawingByDate(filterByDate, filterByDate);
                }
                drawRSList = generalUtility.getListDraw(drawingDTOS);
                break;
            case LotteryConstant.SC:
                if (isTemp) {
                    drawingDTOS = tempDrawingNQ.scTempDrawingByDate(filterByDate, filterByDate);
                } else {
                    drawingDTOS = drawingNQ.scDrawingByDate(filterByDate, filterByDate);
                }
                drawRSList = generalUtility.getListDraw(drawingDTOS);
                break;
            case LotteryConstant.TH:
                if (isTemp) {
                    drawingDTOS = tempDrawingNQ.thTempDrawingByDate(filterByDate, filterByDate);
                } else {
                    drawingDTOS = drawingNQ.thDrawingByDate(filterByDate, filterByDate);
                }
                drawRSList = generalUtility.getListDraw(drawingDTOS);
                break;
            case LotteryConstant.KH:
                if (isTemp) {
                    drawingDTOS = tempDrawingNQ.khTempDrawingByDate(filterByDate, filterByDate);
                } else {
                    drawingDTOS = drawingNQ.khDrawingByDate(filterByDate, filterByDate);
                }
                drawRSList = generalUtility.getListDraw(drawingDTOS);
                break;
            default:
                drawRSList = new ArrayList<>();
                break;
        }
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, drawRSList);

    }

    @Override
    public StructureRS updateDraw(UpdateDrawingRQ updateDrawingRQ) {
        try {
            DrawingDTO drawingDTO = new DrawingDTO();

            switch (updateDrawingRQ.getLotteryType().toUpperCase()) {
                case LotteryConstant.VN1:
                    drawingDTO = tempDrawingNQ.vnOneTempDrawing(updateDrawingRQ.getDrawCode());
                    break;
                case LotteryConstant.VN2:
                case LotteryConstant.MT:
                    drawingDTO = tempDrawingNQ.vnTwoTempDrawing(updateDrawingRQ.getDrawCode());
                    break;
                case LotteryConstant.LEAP:
                    drawingDTO = tempDrawingNQ.leapTempDrawing(updateDrawingRQ.getDrawCode());
                    break;
                case LotteryConstant.KH:
                    drawingDTO = tempDrawingNQ.khTempDrawing(updateDrawingRQ.getDrawCode());
                    break;
                case LotteryConstant.TN:
                    drawingDTO = tempDrawingNQ.tnTempDrawing(updateDrawingRQ.getDrawCode());
                    break;
                case LotteryConstant.SC:
                    drawingDTO = tempDrawingNQ.scTempDrawing(updateDrawingRQ.getDrawCode());
                    break;
                case LotteryConstant.TH:
                    drawingDTO = tempDrawingNQ.thTempDrawing(updateDrawingRQ.getDrawCode());
                    break;
            }

            int minuteDrawAt = drawingDTO.getResultedPostAt().getMinutes();

            boolean isCanUpdate = minuteDrawAt >= updateDrawingRQ.getStoppedLoAt() &&
                    minuteDrawAt >= updateDrawingRQ.getStoppedPostAt();

            if (!updateDrawingRQ.getLotteryType().equals(LotteryConstant.LEAP) && !updateDrawingRQ.getLotteryType().equals(LotteryConstant.KH))
                isCanUpdate = isCanUpdate && minuteDrawAt >= updateDrawingRQ.getStoppedAAt();

            if (isCanUpdate) {
                drawingDTO.getStoppedLoAt().setMinutes(updateDrawingRQ.getStoppedLoAt());
                if (drawingDTO.getIsNight())
                    drawingDTO.getStoppedAAt().setMinutes(updateDrawingRQ.getStoppedAAt());
                drawingDTO.getStoppedPostAt().setMinutes(updateDrawingRQ.getStoppedPostAt());
                drawingDTO.setPostponeNumber(updateDrawingRQ.getPostponeNumber());
                String lotteryType = updateDrawingRQ.getLotteryType().toUpperCase();
                switch (lotteryType) {
                    case LotteryConstant.VN1:
                        VNOneTempDrawingEntity tempDrawingEntity = vnOneTempDrawingRP.findByCode(updateDrawingRQ.getDrawCode());
                        tempDrawingEntity.updateStopAt(drawingDTO);
                        vnOneTempDrawingRP.save(tempDrawingEntity);
                        break;
                    case LotteryConstant.VN2:
                        VNTwoTempDrawingEntity vnTwoTempDrawingEntity = vnTwoTempDrawingRP.findByCode(updateDrawingRQ.getDrawCode());
                        vnTwoTempDrawingEntity.updateStopAt(drawingDTO);
                        vnTwoTempDrawingRP.save(vnTwoTempDrawingEntity);
                        break;
                    case LotteryConstant.LEAP:
                        LeapTempDrawingEntity leapTempDrawingEntity = leapTempDrawingRP.findByCode(updateDrawingRQ.getDrawCode());
                        leapTempDrawingEntity.updateStopAt(drawingDTO);
                        leapTempDrawingRP.save(leapTempDrawingEntity);
                        break;
                    case LotteryConstant.SC:
                        SCTempDrawingEntity scTempDrawingEntity = scTempDrawingRP.findByCode(updateDrawingRQ.getDrawCode());
                        scTempDrawingEntity.updateStopAt(drawingDTO);
                        scTempDrawingRP.save(scTempDrawingEntity);
                        break;
                    case LotteryConstant.TH:
                        THTempDrawingEntity thTempDrawingEntity = tHTempDrawingRP.findByCode(updateDrawingRQ.getDrawCode());
                        thTempDrawingEntity.updateStopAt(drawingDTO);
                        tHTempDrawingRP.save(thTempDrawingEntity);
                        break;
                    case LotteryConstant.KH:
                        KHTempDrawingEntity khTempDrawingEntity = khTempDrawingRP.findByCode(updateDrawingRQ.getDrawCode());
                        khTempDrawingEntity.updateStopAtKH(drawingDTO);
                        khTempDrawingRP.save(khTempDrawingEntity);
                        break;
                    case LotteryConstant.TN:
                        TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.findByCode(updateDrawingRQ.getDrawCode());
                        tnTempDrawingEntity.updateStopAt(drawingDTO);
                        tnTempDrawingRP.save(tnTempDrawingEntity);
                        break;
                }
                removeCache(lotteryType, generalUtility.formatDateYYYYMMDD(drawingDTO.getResultedPostAt()));

                return responseBodyWithSuccessMessage();
            }
            return responseBodyWithBadRequest(MessageConstant.INVALID_DATA, MessageConstant.INVALID_DATA_KEY);
        } catch (NoResultException exception) {
            exception.printStackTrace();
            return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
        }
    }

    @Override
    public void removeCache(String lotteryType, String drawAt) {
        String key = lotteryType.concat("_").concat(drawAt);
        hazelcastInstance.getMap(HazelcastConstant.DRAWS_COLLECTION).delete(key);
    }

}
