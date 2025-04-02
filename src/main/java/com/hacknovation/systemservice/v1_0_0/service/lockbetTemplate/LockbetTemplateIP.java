package com.hacknovation.systemservice.v1_0_0.service.lockbetTemplate;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.LockbetTemplateEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.ShiftsEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.LockbetTemplateRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.ShiftsRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.lockbetTemplate.ListLockbetRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.lockbetTemplate.UpdateLockbetRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.lockbetTemplate.LockbetItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.lockbetTemplate.LockbetListRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 17/03/2022
 * time: 14:01
 */
@Service
@RequiredArgsConstructor
public class LockbetTemplateIP extends BaseServiceIP implements LockbetTemplateSV {

    private final ShiftsRP shiftsRP;
    private final TempDrawingNQ tempDrawingNQ;
    private final LockbetTemplateRP lockbetTemplateRP;
    private final JwtToken jwtToken;
    private final HttpServletRequest request;
    private final GeneralUtility generalUtility;
    private final ActivityLogUtility activityLogUtility;

    @Override
    public StructureRS listLockbet() {
        ListLockbetRQ lockbetRQ = new ListLockbetRQ(request);
        if (lockbetRQ.getFilterByLotteryType() == null || lockbetRQ.getFilterByShiftCode() == null) {
            return responseBodyWithBadRequest(MessageConstant.REQUEST_PARAM_MISSING, MessageConstant.REQUEST_PARAM_MISSING_KEY);
        }
        UserToken userToken = jwtToken.getUserToken();
        String userCode = generalUtility.getUserCodeFromToken(userToken);
        if (lockbetRQ.getFilterByUserCode() == null) {
            lockbetRQ.setFilterByUserCode(userCode);
        }

        ShiftsEntity shiftsEntity = shiftsRP.findShiftByLotteryTypeAndShiftCode(lockbetRQ.getFilterByLotteryType(), lockbetRQ.getFilterByShiftCode());
        if (shiftsEntity == null) {
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
        }

        List<LockbetTemplateEntity> lockbetTemplateEntities = lockbetTemplateRP.findAllByLotteryTypeAndUserCodeAndShiftCode(lockbetRQ.getFilterByLotteryType(), lockbetRQ.getFilterByUserCode(), lockbetRQ.getFilterByShiftCode());
        Map<String, LockbetTemplateEntity> lockbetTemplateEntityMapByDayOfWeek = new HashMap<>();
        if (lockbetTemplateEntities.size() > 0) {
            lockbetTemplateEntityMapByDayOfWeek = lockbetTemplateEntities.stream().collect(Collectors.toMap(LockbetTemplateEntity::getDayOfWeek, Function.identity()));
        }

        LockbetListRS lockbetListRS = new LockbetListRS();
        List<LockbetItemRS> lockbetItemRSList = new ArrayList<>();
        lockbetListRS.setShiftCode(shiftsEntity.getCode());
        lockbetListRS.setIsNight(shiftsEntity.getIsNight());
        lockbetListRS.setDrawAt(shiftsEntity.getResultedPostAt());
        lockbetListRS.setColumns(shiftsEntity.getIsNight() ? LotteryConstant.COLUMNS_NIGHT : LotteryConstant.COLUMNS_DAY);
        if (LotteryConstant.LEAP.equalsIgnoreCase(lockbetRQ.getFilterByLotteryType())) {
            lockbetListRS.setColumns(LotteryConstant.COLUMNS_LEAP);
        }
        if (LotteryConstant.SC.equalsIgnoreCase(lockbetRQ.getFilterByLotteryType())) {
            lockbetListRS.setColumns(LotteryConstant.COLUMNS_SC);
        }
        if (LotteryConstant.KH.equalsIgnoreCase(lockbetRQ.getFilterByLotteryType())) {
            lockbetListRS.setColumns(LotteryConstant.COLUMNS_KH);
        }
        if (LotteryConstant.TN.equals(lockbetRQ.getFilterByLotteryType()) && !shiftsEntity.getIsNight()) {
            lockbetListRS.setColumns(LotteryConstant.COLUMNS_TN);
        }
        if (LotteryConstant.SC.equalsIgnoreCase(lockbetRQ.getFilterByLotteryType())) {
            lockbetListRS.setColumns(LotteryConstant.COLUMNS_SABAY);
        }
        for(String day : LotteryConstant.STRING_DAYS) {
            LockbetItemRS lockbetItemRS = new LockbetItemRS();
            lockbetItemRS.setDayOfWeek(day);
            lockbetItemRS.setStopLoAt(shiftsEntity.getStopedLoAt());
            lockbetItemRS.setStopAAt(shiftsEntity.getStopedAAt());
            lockbetItemRS.setStopPostAt(shiftsEntity.getStopedPostAt());
            if (lockbetTemplateEntityMapByDayOfWeek.containsKey(day)) {
                LockbetTemplateEntity entity = lockbetTemplateEntityMapByDayOfWeek.get(day);
                lockbetItemRS.setItemId(entity.getId().intValue());
                lockbetItemRS.setStopPostAt(entity.getStopPostAt());
                lockbetItemRS.setStopDeleteAt(entity.getStopDeleteAt());
                if (!LotteryConstant.KH.equalsIgnoreCase(lockbetRQ.getFilterByLotteryType())) {
                    lockbetItemRS.setStopLoAt(entity.getStopLoAt());
                    lockbetItemRS.setStopAAt(entity.getStopAAt());
                }
            }
            lockbetItemRSList.add(lockbetItemRS);
        }
         lockbetListRS.setItems(lockbetItemRSList);

        return responseBodyWithSuccessMessage(lockbetListRS);
    }

    @Override
    public StructureRS updateLockbet(UpdateLockbetRQ updateLockbetRQ) {
        UserToken userToken = jwtToken.getUserToken();
        String userCode = generalUtility.getUserCodeFromToken(userToken);

        List<DrawingDTO> drawingDTOList = getDrawingByLotteryAndDrawAt(updateLockbetRQ.getLotteryType(), generalUtility.formatDateYYYYMMDD(new Date()));

        Optional<DrawingDTO> optDrawingDTO = drawingDTOList.stream().filter(it-> it.getShiftCode().equals(updateLockbetRQ.getShiftCode())).findFirst();
        if (optDrawingDTO.isPresent()) {
            DrawingDTO drawingDTO = optDrawingDTO.get();
            DrawingDTO drawingDTORQ = new DrawingDTO();
            BeanUtils.copyProperties(drawingDTO, drawingDTORQ);
            drawingDTORQ.setStoppedLoAt(generalUtility.getStopDate(drawingDTORQ.getStoppedLoAt(), updateLockbetRQ.getStopLoAt()));
            drawingDTORQ.setStoppedPostAt(generalUtility.getStopDate(drawingDTORQ.getStoppedPostAt(), updateLockbetRQ.getStopPostAt()));

            if (LotteryConstant.VN1.equals(updateLockbetRQ.getLotteryType())) {
                setMinuteLockBet(drawingDTO, updateLockbetRQ.getDayOfWeek());
            }

            boolean isInvalid = drawingDTORQ.getStoppedLoAt().getTime() > drawingDTO.getStoppedLoAt().getTime();
            isInvalid = isInvalid || drawingDTORQ.getStoppedPostAt().getTime() > drawingDTO.getStoppedPostAt().getTime();

            if (drawingDTO.getIsNight() || LotteryConstant.TN.equals(updateLockbetRQ.getLotteryType())) {
                drawingDTORQ.setStoppedAAt(generalUtility.getStopDate(drawingDTORQ.getStoppedAAt(), updateLockbetRQ.getStopAAt()));
                isInvalid = isInvalid || drawingDTORQ.getStoppedAAt().getTime() > drawingDTO.getStoppedAAt().getTime();
            }

            if (isInvalid)
                return responseBodyWithBadRequest(MessageConstant.INVALID_DATA, MessageConstant.INVALID_DATA_KEY);

        } else {
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
        }


        if (updateLockbetRQ.getUserCode() == null) {
            updateLockbetRQ.setUserCode(userCode);
        }
        LockbetTemplateEntity lockbetTemplateEntity = null;
        if (updateLockbetRQ.getItemId() != null) {
            lockbetTemplateEntity = lockbetTemplateRP.getOne(updateLockbetRQ.getItemId().longValue());
        }

        if (lockbetTemplateEntity == null) {
            lockbetTemplateEntity = lockbetTemplateRP.findByLotteryTypeAndDayOfWeekAndShiftCodeAndUserCode(updateLockbetRQ.getLotteryType(), updateLockbetRQ.getDayOfWeek(), updateLockbetRQ.getShiftCode(), updateLockbetRQ.getUserCode());
        }

        if (lockbetTemplateEntity == null) {
            lockbetTemplateEntity = new LockbetTemplateEntity();
        }
        try {
            lockbetTemplateEntity.setLotteryType(updateLockbetRQ.getLotteryType());
            lockbetTemplateEntity.setShiftCode(updateLockbetRQ.getShiftCode());
            lockbetTemplateEntity.setDayOfWeek(updateLockbetRQ.getDayOfWeek());
            lockbetTemplateEntity.setUserCode(updateLockbetRQ.getUserCode());
            lockbetTemplateEntity.setStopLoAt(updateLockbetRQ.getStopLoAt());
            lockbetTemplateEntity.setStopAAt(updateLockbetRQ.getStopAAt());
            lockbetTemplateEntity.setStopPostAt(updateLockbetRQ.getStopPostAt());
            lockbetTemplateEntity.setUpdatedBy(userToken.getUserCode());

            if(updateLockbetRQ.getStopDeleteAt() != null && !updateLockbetRQ.getStopDeleteAt().isEmpty())
                lockbetTemplateEntity.setStopDeleteAt(updateLockbetRQ.getStopDeleteAt());

            lockbetTemplateEntity = lockbetTemplateRP.save(lockbetTemplateEntity);
            System.out.println("LockbetTemplateIP.updateLockbet => By " + userToken.getUsername());
            System.out.println(lockbetTemplateEntity);

            /*
             * add log activity
             */
            activityLogUtility.addActivityLog(updateLockbetRQ.getLotteryType(), "LOCKBET_TEMPLATE", userToken.getUserType(), "ADD_EDIT", userToken.getUserCode(), lockbetTemplateEntity);
        } catch (DataIntegrityViolationException exception) {
            System.out.println("===============Exception unique==========");
            System.out.println(exception.getMessage());
            return responseBodyWithBadRequest(MessageConstant.DATA_EXIST, MessageConstant.DATA_NOT_FOUND_KEY);
        }

        return responseBodyWithSuccessMessage();
    }


    private List<DrawingDTO> getDrawingByLotteryAndDrawAt(String lotteryType, String drawAt) {
        if (LotteryConstant.VN1.equals(lotteryType)) {
            return tempDrawingNQ.vnOneTempDrawingByDate(drawAt, drawAt);
        }
        if (List.of(LotteryConstant.VN2, LotteryConstant.MT).contains(lotteryType)) {
            return tempDrawingNQ.vnTwoTempDrawingByDate(drawAt, drawAt);
        }
        if (LotteryConstant.LEAP.equals(lotteryType)) {
            return tempDrawingNQ.leapTempDrawingByDate(drawAt, drawAt);
        }
        if (LotteryConstant.KH.equals(lotteryType)) {
            return tempDrawingNQ.khTempDrawingByDate(drawAt, drawAt);
        }
        if (LotteryConstant.TN.equals(lotteryType)) {
            return tempDrawingNQ.tnTempDrawingByDate(drawAt, drawAt);
        }
        return new ArrayList<>();
    }


    public void setMinuteLockBet(DrawingDTO drawingDTO, String dayOfWeek) {
        Map<String, String> mapLimit = drawingDTO.getIsNight() ? getMapPostNightLockBet() : getMapPostDayLockBet(dayOfWeek);
        if (drawingDTO.getIsNight()) {
            drawingDTO.setStoppedAAt(DateUtils.setMinutes(drawingDTO.getStoppedAAt(), Integer.parseInt(mapLimit.get("A"))));
        }
        drawingDTO.setStoppedLoAt(DateUtils.setMinutes(drawingDTO.getStoppedLoAt(), Integer.parseInt(mapLimit.get("Lo"))));
        drawingDTO.setStoppedPostAt(DateUtils.setMinutes(drawingDTO.getStoppedPostAt(), Integer.parseInt(mapLimit.get("Post"))));
    }

    private Map<String, String> getMapPostDayLockBet(String dayOfWeek) {
        Map<String, String> mapLimit = new HashMap<>();
        String minuteLo = "10";
        String minutePost = "25";

        switch (dayOfWeek) {
            case LotteryConstant.MON:
                minuteLo = "15";
                minutePost ="31";
                break;
            case LotteryConstant.TUE:
                minuteLo = "12";
                minutePost = "27";
                break;
            case LotteryConstant.WED:
                minuteLo = "13";
                minutePost = "27";
                break;
            case LotteryConstant.THU:
                minuteLo = "08";
                minutePost = "20";
                break;
            case LotteryConstant.FRI:
                minuteLo = "11";
                minutePost = "32";
                break;
            case LotteryConstant.SAT:
            case LotteryConstant.SUN:
                minuteLo = "15";
                minutePost="32";
                break;
        }

        mapLimit.put("Lo", minuteLo);
        mapLimit.put("Post", minutePost);

        return mapLimit;
    }

    private Map<String, String> getMapPostNightLockBet() {
        Map<String, String> mapLimit = new HashMap<>();
        String minuteLo = "11";
        String minuteA = "19";
        String minutePost = "26";

        mapLimit.put("Lo", minuteLo);
        mapLimit.put("A", minuteA);
        mapLimit.put("Post", minutePost);

        return mapLimit;
    }

}
