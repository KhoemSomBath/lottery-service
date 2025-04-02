package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.drawing.DrawingSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.resultInput.SetResultRQ;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/*
 * author: kangto
 * createdAt: 06/01/2023
 * time: 14:45
 */
@Component
@RequiredArgsConstructor
public class ResultBeforeStopUtility {

    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final TNTempDrawingRP tnTempDrawingRP;
    private final DrawingSV drawingSV;
    private final GeneralUtility generalUtility;
    private final ActivityLogUtility activityLogUtility;
    private final JwtToken jwtToken;

    public void updateVNOneStopAtIfResultBeforeStop(VNOneTempDrawingEntity drawingEntity, SetResultRQ setResultRQ) {

        boolean isStep1 = isUpdateStopLoAt(drawingEntity.getStopedLoAt(), drawingEntity.getIsNight(), setResultRQ);
        if (isStep1) {
            updateVNOneStopLoAOrPostAt(drawingEntity, "STEP1");
            return;
        }

        boolean isStep2 = isUpdateStopAAt(LotteryConstant.VN1, drawingEntity.getIsNight(), drawingEntity.getStopedAAt(), setResultRQ);
        if (isStep2) {
            updateVNOneStopLoAOrPostAt(drawingEntity, "STEP2");
            return;
        }

        boolean isStep3 = isUpdateStopPostAt(drawingEntity.getStopedPostAt(), setResultRQ);
        if (isStep3) {
            updateVNOneStopLoAOrPostAt(drawingEntity, "STEP3");
        }

    }

    public void updateTNStopAtIfResultBeforeStop(TNTempDrawingEntity drawingEntity, SetResultRQ setResultRQ) {

        boolean isStep1 = isUpdateStopLoAt(drawingEntity.getStopedLoAt(), drawingEntity.getIsNight(), setResultRQ);
        if (isStep1) {
            updateTNStopLoAOrPostAt(drawingEntity, "STEP1");
            return;
        }

        boolean isStep2 = isUpdateStopAAt(LotteryConstant.TN, drawingEntity.getIsNight(), drawingEntity.getStopedAAt(), setResultRQ);
        if (isStep2) {
            updateTNStopLoAOrPostAt(drawingEntity, "STEP2");
            return;
        }

        boolean isStep3 = isUpdateStopPostAt(drawingEntity.getStopedPostAt(), setResultRQ);
        if (isStep3) {
            updateTNStopLoAOrPostAt(drawingEntity, "STEP3");
        }

    }

    private boolean isResultBeforeStopBet(Date stopBet) {
        return new Date().getTime() < stopBet.getTime();
    }

    private boolean isUpdateStopLoAt(Date stopValue, boolean isNight, SetResultRQ setResultRQ) {

        if (setResultRQ.getColumnNumber() == 1 && isResultBeforeStopBet(stopValue)) {
            if (isNight) {
                return PostConstant.LO1.equals(setResultRQ.getPostCode());
            } else {
                return PostConstant.POST_A.equals(setResultRQ.getPostCode()) && LotteryConstant.REBATE2D.equals(setResultRQ.getRebateCode());
            }
        }

        return false;
    }

    private boolean isUpdateStopAAt(String lottery, boolean isNight, Date stopValue, SetResultRQ setResultRQ) {
        if (isResultBeforeStopBet(stopValue)) {
            if (isNight && PostConstant.POST_A.equals(setResultRQ.getPostCode()) && LotteryConstant.REBATE3D.equals(setResultRQ.getRebateCode())){
                return true;
            }

            if (!isNight && LotteryConstant.TN.equals(lottery)) {
                if (PostConstant.POST_B.equals(setResultRQ.getPostCode()) && setResultRQ.getColumnNumber() == 2) {
                    return true;
                }
                return PostConstant.POST_B.equals(setResultRQ.getPostCode()) && setResultRQ.getColumnNumber() == 3;
            }

        }

        return false;
    }

    private boolean isUpdateStopPostAt(Date stopValue, SetResultRQ setResultRQ) {
        if (setResultRQ.getColumnNumber() == 1 && isResultBeforeStopBet(stopValue)) {
            return PostConstant.POST_B.equals(setResultRQ.getPostCode());
        }

        return false;
    }

    private void updateVNOneStopLoAOrPostAt(VNOneTempDrawingEntity vnOneTempDrawing, String step) {
        try {
            if ("STEP1".equals(step))
                vnOneTempDrawing.setStopedLoAt(new Date());
            if ("STEP2".equals(step))
                vnOneTempDrawing.setStopedAAt(new Date());
            if ("STEP3".equals(step))
                vnOneTempDrawing.setStopedPostAt(new Date());
            vnOneTempDrawingRP.save(vnOneTempDrawing);

            drawingSV.removeCache(LotteryConstant.VN1, generalUtility.formatDateYYYYMMDD(vnOneTempDrawing.getResultedPostAt()));

            var token = jwtToken.getUserToken();

            String stopAt = "Stop step1=".concat(vnOneTempDrawing.getStopedLoAt().toString()).concat(", step2=")
                    .concat(vnOneTempDrawing.getStopedAAt().toString()).concat(", step3=")
                    .concat(vnOneTempDrawing.getStopedPostAt().toString());
            activityLogUtility.addActivityLog(LotteryConstant.VN1, ActivityLogConstant.MODULE_UPDATE_DRAW, token.getUserType(), ActivityLogConstant.ACTION_UPDATE, token.getUserCode(), stopAt);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void updateTNStopLoAOrPostAt(TNTempDrawingEntity tnTempDrawing, String step) {
        try {
            if ("STEP1".equals(step))
                tnTempDrawing.setStopedLoAt(new Date());
            if ("STEP2".equals(step))
                tnTempDrawing.setStopedAAt(new Date());
            if ("STEP3".equals(step))
                tnTempDrawing.setStopedPostAt(new Date());
            tnTempDrawingRP.save(tnTempDrawing);

            drawingSV.removeCache(LotteryConstant.TN, generalUtility.formatDateYYYYMMDD(tnTempDrawing.getResultedPostAt()));

            var token = jwtToken.getUserToken();

            String stopAt = "Stop step1=".concat(tnTempDrawing.getStopedLoAt().toString()).concat(", step2=")
                    .concat(tnTempDrawing.getStopedAAt().toString()).concat(", step3=")
                    .concat(tnTempDrawing.getStopedPostAt().toString());
            activityLogUtility.addActivityLog(LotteryConstant.TN, ActivityLogConstant.MODULE_UPDATE_DRAW, token.getUserType(), ActivityLogConstant.ACTION_UPDATE, token.getUserCode(), stopAt);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
