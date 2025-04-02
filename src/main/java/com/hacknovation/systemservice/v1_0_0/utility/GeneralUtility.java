package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.LockbetTemplateEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.daily.DailyReportTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.LockbetTemplateRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.CommissionDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.drawing.DrawRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.HasLotteryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SummaryReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.DrawItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.full.FullDrawItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.fullinput.FullResultInputItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.result.fullinput.FullResultInputRowRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GeneralUtility {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Integer TEMP_TABLE = 2;
    private final LockbetTemplateRP lockbetTemplateRP;

    public String getDayOfWeek(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
        return dateFormat.format(date);
    }

    public Date parseDate(String date) {
        Date parseDate = new Date();
        try {
            parseDate = dateFormat.parse(date);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return parseDate;
    }

    public Integer getHour(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        return Integer.parseInt(dateFormat.format(DateUtils.addHours(date, 7)));
    }


    public String getHourKey(Date date) {
        Integer hour = getHour(date);
        if (hour >= 9 && hour < 16) {
            return "ថ្ងៃ";
        } else if (hour >= 16 && hour < 18) {
            return "ល្ងាច";
        } else if (hour >= 18 && hour < 23) {
            return "យប់";
        }
        return "ព្រឹក";
    }

    public String formatDateYYYYMMDD(Date date) {
        return dateFormat.format(date);
    }

    public BigDecimal commissionAmount(BigDecimal betAmount, BigDecimal commission) {
        return betAmount.multiply(commission.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN)).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getDefaultRebateRate(String rebateCode) {
        if (LotteryConstant.REBATE1D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.REBATE_RATE_1D;
        if (LotteryConstant.REBATE2D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.REBATE_RATE_2D;
        if (LotteryConstant.REBATE3D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.REBATE_RATE_3D;
        if (LotteryConstant.REBATE4D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.REBATE_RATE_4D;
        return BigDecimal.ZERO;
    }

    public BigDecimal getDefaultMaxBetFirst(String rebateCode) {
        if (LotteryConstant.REBATE1D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.MAX_BET_FIRST_1D;
        if (LotteryConstant.REBATE2D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.MAX_BET_FIRST_2D;
        if (LotteryConstant.REBATE3D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.MAX_BET_FIRST_3D;
        if (LotteryConstant.REBATE4D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.MAX_BET_FIRST_4D;
        return BigDecimal.ZERO;
    }

    public BigDecimal getDefaultMaxBetSecond(String rebateCode) {
        if (LotteryConstant.REBATE1D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.MAX_BET_SECOND_1D;
        if (LotteryConstant.REBATE2D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.MAX_BET_SECOND_2D;
        if (LotteryConstant.REBATE3D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.MAX_BET_SECOND_3D;
        if (LotteryConstant.REBATE4D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.MAX_BET_SECOND_4D;
        return BigDecimal.ZERO;
    }

    public BigDecimal getDefaultLimitDigit(String rebateCode) {
        if (LotteryConstant.REBATE1D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.LIMIT_DIGIT_1D;
        if (LotteryConstant.REBATE2D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.LIMIT_DIGIT_2D;
        if (LotteryConstant.REBATE3D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.LIMIT_DIGIT_3D;
        if (LotteryConstant.REBATE4D.equalsIgnoreCase(rebateCode))
            return LotteryConstant.LIMIT_DIGIT_4D;
        return BigDecimal.ZERO;
    }


    public boolean isTempTable(String date) {
        try {
            if (date == null || date.equals(""))
                return true;
            if (formatDateYYYYMMDD(new Date()).equals(date)) {
                return true;
            }
            Date requestDate = DateUtils.addDays(dateFormat.parse(date), TEMP_TABLE);
            if (requestDate.getTime() >= new Date().getTime()) {
                return true;
            }
        } catch (ParseException exception) {
            System.out.println("ParseException date : " + date);
            return false;
        }

        return false;
    }

    public List<DrawRS> getListDraw(List<DrawingDTO> drawingList) {
        List<DrawRS> drawRSList = new ArrayList<>();
        for (DrawingDTO item : drawingList) {
            DrawRS drawRS = new DrawRS();
            BeanUtils.copyProperties(item, drawRS);
            drawRS.setDrawAt(item.getResultedPostAt());
            drawRSList.add(drawRS);
        }
        return drawRSList;
    }

    public BigDecimal commissionRate(List<CommissionDTO> commissionLottery, String rebateCode) {
        BigDecimal commRate = BigDecimal.ZERO;
        Optional<CommissionDTO> commission = commissionLottery.stream().filter(item -> item.getRebateCode().equalsIgnoreCase(rebateCode)).findFirst();
        if (commission.isPresent()) {
            commRate = commission.get().getCommission() != null ? commission.get().getCommission() : BigDecimal.ZERO;
        }
        return commRate;
    }

    public BigDecimal commission(List<CommissionDTO> commissionLottery, DailyReportTO item) {
        BigDecimal comRate = BigDecimal.ZERO;
        if (commissionLottery.size() > 0) {
            commissionLottery = commissionLottery.stream().filter(com -> com.getLotteryType().equalsIgnoreCase(item.getLotteryType())).collect(Collectors.toList());
            if (commissionLottery.size() > 0) {
                comRate = commissionRate(commissionLottery, item.getRebateCode());
            }
        }
        return comRate;
    }

    /**
     * get default company has lottery rs
     *
     * @param hasLotteryRS HasLotteryRS
     */
    public void getDefaultCompanyHasLotteryRS(HasLotteryRS hasLotteryRS) {
        hasLotteryRS.setCom2D(LotteryConstant.COMMISSION_RATE_2D);
        hasLotteryRS.setCom3D(LotteryConstant.COMMISSION_RATE_3D);
        hasLotteryRS.setCom4D(LotteryConstant.COMMISSION_RATE_4D);

        hasLotteryRS.setRebateRate2D(LotteryConstant.REBATE_RATE_2D);
        hasLotteryRS.setRebateRate3D(LotteryConstant.REBATE_RATE_3D);
        hasLotteryRS.setRebateRate4D(LotteryConstant.REBATE_RATE_4D);
    }

    public void setUserHasLottery(List<CommissionDTO> commissionDTOList, String lotteryType, HasLotteryRS hasLotteryRS) {
        if (commissionDTOList != null) {
            List<CommissionDTO> hasLottery = commissionDTOList.stream().filter(item -> item.getLotteryType().equalsIgnoreCase(lotteryType)).collect(Collectors.toList());

            Optional<CommissionDTO> hasLottery2D = hasLottery.stream().filter(item -> item.getRebateCode().equalsIgnoreCase(LotteryConstant.REBATE2D)).findFirst();
            if (hasLottery2D.isPresent()) {
                hasLotteryRS.setCom2D(hasLottery2D.get().getCommission());
                hasLotteryRS.setRebateRate2D(hasLottery2D.get().getRebateRate().setScale(2, RoundingMode.HALF_EVEN));
            }

            Optional<CommissionDTO> hasLottery3D = hasLottery.stream().filter(item -> item.getRebateCode().equalsIgnoreCase(LotteryConstant.REBATE3D)).findFirst();
            if (hasLottery3D.isPresent()) {
                hasLotteryRS.setCom3D(hasLottery3D.get().getCommission());
                hasLotteryRS.setRebateRate3D(hasLottery3D.get().getRebateRate().setScale(2, RoundingMode.HALF_EVEN));
            }

            Optional<CommissionDTO> hasLottery4D = hasLottery.stream().filter(item -> item.getRebateCode().equalsIgnoreCase(LotteryConstant.REBATE4D)).findFirst();
            if (hasLottery4D.isPresent()) {
                hasLotteryRS.setCom4D(hasLottery4D.get().getCommission());
                hasLotteryRS.setRebateRate4D(hasLottery4D.get().getRebateRate());
            }
        }

    }

    public void prepareNumber(DrawItemRS drawItemRS, DrawingItemsDTO itemsDTO, boolean isNight, String lotteryType) {

        int length = lotteryNumber(itemsDTO, isNight, lotteryType);
        switch (length) {
            case 2:
                drawItemRS.setNumber(itemsDTO.getTwoDigits());
                break;
            case 3:
                drawItemRS.setNumber(itemsDTO.getThreeDigits());
                break;
            case 4:
                drawItemRS.setNumber(itemsDTO.getFourDigits());
                break;
            case 5:
                drawItemRS.setNumber(itemsDTO.getFiveDigits());
                break;
            default:
                drawItemRS.setNumber(itemsDTO.getSixDigits());
                break;

        }

    }

    public void prepareNumber(FullDrawItemRS drawItemRS, DrawingItemsDTO itemsDTO, boolean isNight, String lotteryType) {

        int length = lotteryNumber(itemsDTO, isNight, lotteryType);
        switch (length) {
            case 2:
                drawItemRS.setNumber(itemsDTO.getTwoDigits());
                break;
            case 3:
                drawItemRS.setNumber(itemsDTO.getThreeDigits());
                break;
            case 4:
                drawItemRS.setNumber(itemsDTO.getFourDigits());
                break;
            case 5:
                drawItemRS.setNumber(itemsDTO.getFiveDigits());
                break;
            default:
                drawItemRS.setNumber(itemsDTO.getSixDigits());
                break;
        }

    }

    public void prepareNumber(FullResultInputItemRS drawItemRS, DrawingItemsDTO itemsDTO, boolean isNight, String lotteryType) {

        int length = lotteryNumber(itemsDTO, isNight, lotteryType);
        switch (length) {
            case 2:
                drawItemRS.setNumber(itemsDTO.getTwoDigits());
                break;
            case 3:
                drawItemRS.setNumber(itemsDTO.getThreeDigits());
                break;
            case 4:
                drawItemRS.setNumber(itemsDTO.getFourDigits());
                break;
            case 5:
                drawItemRS.setNumber(itemsDTO.getFiveDigits());
                break;
            default:
                drawItemRS.setNumber(itemsDTO.getSixDigits());
                break;
        }

    }

    public int lotteryNumber(DrawingItemsDTO itemsDTO, boolean isNight, String lotteryType) {

        int length = 0;
        switch (lotteryType) {
            case LotteryConstant.VN1:
            case LotteryConstant.VN2:
            case LotteryConstant.TN:
                if (isNight) {
                    length = 6;

                    /*
                     * Show 5 digits
                     */
                    if (PostConstant.LO_1_TO_9.contains(itemsDTO.getPostCode())) {
                        length = 5;
                    }

                    /*
                     * Show 4 digits
                     */
                    if (PostConstant.LO_10_TO_19.contains(itemsDTO.getPostCode())) {
                        length = 4;
                    }

                } else {
                    length = 5;
                    if (PostConstant.LO_1_TO_4.contains(itemsDTO.getPostCode())) {
                        length = 4;
                    }
                    if (PostConstant.POST_K.equals(itemsDTO.getPostCode()))
                        length = 4;
                    if (LotteryConstant.TN.equals(lotteryType)) {
                        if (PostConstant.LO4.equals(itemsDTO.getPostCode()))
                            length = 5;
                    }
                }
                break;
            case LotteryConstant.LEAP:
                length = 5;
                if (PostConstant.POST_GROUP.equals(itemsDTO.getPostGroup()))
                    length = 6;
                break;
        }

        return length;
    }

    /**
     * transform sale report rs for upper level
     *
     * @param upperLineReport        SaleReportRS
     * @param upperLineHasCommission Map<String, List<CommissionDTO>>
     * @param mapUnderAndUpperCode   Map<String, String>
     */
    public void transformSaleReportRSForUpperLevel(SaleReportRS upperLineReport, Map<String, List<CommissionDTO>> upperLineHasCommission, Map<String, String> mapUnderAndUpperCode) {
        SummaryReportRS upperSummary = new SummaryReportRS();
        copyBetAndWinAmount(upperLineReport.getSummary(), upperSummary);
        upperLineReport.getItems().forEach(saleItemsRS -> {
            HasLotteryRS hasLotteryRS = new HasLotteryRS();
            List<CommissionDTO> commissionDTOList = upperLineHasCommission.get(mapUnderAndUpperCode.get(saleItemsRS.getUserCode()));
            setUserHasLottery(commissionDTOList, upperLineReport.getLotteryType(), hasLotteryRS);
            upperSummary.setCom2DKhr(upperSummary.getCom2DKhr().add(commissionAmount(saleItemsRS.getBetAmount2DKhr(), hasLotteryRS.getCom2D())));
            upperSummary.setCom3DKhr(upperSummary.getCom3DKhr().add(commissionAmount(saleItemsRS.getBetAmount3DKhr(), hasLotteryRS.getCom3D())));
            upperSummary.setCom4DKhr(upperSummary.getCom4DKhr().add(commissionAmount(saleItemsRS.getBetAmount4DKhr(), hasLotteryRS.getCom4D())));
            upperSummary.setCom2DUsd(upperSummary.getCom2DUsd().add(commissionAmount(saleItemsRS.getBetAmount2DUsd(), hasLotteryRS.getCom2D())));
            upperSummary.setCom3DUsd(upperSummary.getCom3DUsd().add(commissionAmount(saleItemsRS.getBetAmount3DUsd(), hasLotteryRS.getCom3D())));
            upperSummary.setCom4DUsd(upperSummary.getCom4DUsd().add(commissionAmount(saleItemsRS.getBetAmount4DUsd(), hasLotteryRS.getCom4D())));
            upperSummary.setRewardAmount2DKhr(upperSummary.getRewardAmount2DKhr().add(saleItemsRS.getWinAmount2DKhr().multiply(hasLotteryRS.getRebateRate2D())));
            upperSummary.setRewardAmount3DKhr(upperSummary.getRewardAmount3DKhr().add(saleItemsRS.getWinAmount3DKhr().multiply(hasLotteryRS.getRebateRate3D())));
            upperSummary.setRewardAmount4DKhr(upperSummary.getRewardAmount4DKhr().add(saleItemsRS.getWinAmount4DKhr().multiply(hasLotteryRS.getRebateRate4D())));
            upperSummary.setRewardAmount2DUsd(upperSummary.getRewardAmount2DUsd().add(saleItemsRS.getWinAmount2DUsd().multiply(hasLotteryRS.getRebateRate2D())));
            upperSummary.setRewardAmount3DUsd(upperSummary.getRewardAmount3DUsd().add(saleItemsRS.getWinAmount3DUsd().multiply(hasLotteryRS.getRebateRate3D())));
            upperSummary.setRewardAmount4DUsd(upperSummary.getRewardAmount4DUsd().add(saleItemsRS.getWinAmount4DUsd().multiply(hasLotteryRS.getRebateRate4D())));
        });

        upperSummary.setWinLoseAmountKhr(upperSummary.getCommissionKhr().subtract(upperSummary.getRewardAmountKhr()));
        upperSummary.setWinLoseAmountUsd(upperSummary.getCommissionUsd().subtract(upperSummary.getRewardAmountUsd()));

        upperLineReport.setSummary(upperSummary);
    }

    private void copyBetAndWinAmount(SummaryReportRS source, SummaryReportRS target) {
        target.setBetAmount2DKhr(source.getBetAmount2DKhr());
        target.setBetAmount3DKhr(source.getBetAmount3DKhr());
        target.setBetAmount4DKhr(source.getBetAmount4DKhr());
        target.setBetAmount2DUsd(source.getBetAmount2DUsd());
        target.setBetAmount3DUsd(source.getBetAmount3DUsd());
        target.setBetAmount4DUsd(source.getBetAmount4DUsd());

        target.setWinAmount2DKhr(source.getWinAmount2DKhr());
        target.setWinAmount3DKhr(source.getWinAmount3DKhr());
        target.setWinAmount4DKhr(source.getWinAmount4DKhr());
        target.setWinAmount2DUsd(source.getWinAmount2DUsd());
        target.setWinAmount3DUsd(source.getWinAmount3DUsd());
        target.setWinAmount4DUsd(source.getWinAmount4DUsd());
    }

    public <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

    /**
     * get user by check is sub-account login get parent
     *
     * @param userToken UserToken
     * @return String
     */
    public String getUserCodeFromToken(UserToken userToken) {
        String userCode = userToken.getUserCode();
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
        }

        return userCode;
    }

    /**
     * update stop bet hour, and minute
     *
     * @param drawingDTO  DrawingDTO
     * @param lotteryType String
     * @param userCode    String
     */
    public void updateStopBetBaseOnUserCode(DrawingDTO drawingDTO, String lotteryType, String userCode) {
        String dayOfWeek = getDayOfWeek(drawingDTO.getResultedPostAt());
        LockbetTemplateEntity lockbetTemplateEntity = lockbetTemplateRP.findByLotteryTypeAndDayOfWeekAndShiftCodeAndUserCode(lotteryType, dayOfWeek, drawingDTO.getShiftCode(), userCode);
        if (lockbetTemplateEntity != null) {
            drawingDTO.setStoppedLoAt(getStopDate(drawingDTO.getStoppedLoAt(), lockbetTemplateEntity.getStopLoAt()));
            drawingDTO.setStoppedPostAt(getStopDate(drawingDTO.getStoppedPostAt(), lockbetTemplateEntity.getStopPostAt()));
            if (drawingDTO.getIsNight()) {
                drawingDTO.setStoppedAAt(getStopDate(drawingDTO.getStoppedAAt(), lockbetTemplateEntity.getStopAAt()));
            }
        }
    }

    /**
     * set stop date from string
     *
     * @param stopDate   Date
     * @param stopString String
     * @return Date
     */
    public Date getStopDate(Date stopDate, String stopString) {
        Date newDate = new Date();
        BeanUtils.copyProperties(stopDate, newDate);
        String[] hourMinute = stopString.split(":");
        if (hourMinute.length > 1) {
            // minus 7 for time zone from Phnom_Penh
            newDate.setHours(Integer.parseInt(hourMinute[0]) - 7);
            newDate.setMinutes(Integer.parseInt(hourMinute[1]));
        }

        return newDate;
    }

    public BigDecimal setScaleAndHALFEVEN(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.HALF_EVEN);
    }

    public <T extends BaseDrawingItemsEntity> Map<String, T> getMapByKeyPostCode(List<T> drawingItemsEntities) {
        return drawingItemsEntities.stream().collect(Collectors.toMap(BaseDrawingItemsEntity::getPostCode, Function.identity()));
    }


    public String getRoleCode(UserToken userToken) {
        String roleCode = userToken.getUserRole();
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            roleCode = userToken.getParentRole();
        }
        return roleCode;
    }

    public String getUserCode(UserToken userToken) {
        String userCode = userToken.getUserCode();
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
        }
        return userCode;
    }

}
