package com.hacknovation.systemservice.v1_0_0.service.analyze;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapAnalyzeLogEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCAnalyzeLogEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNAnalyzeLogEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoAnalyzeLogEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapAnalyzeLogRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCAnalyzeLogRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNAnalyzeLogRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoAnalyzeLogRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.SellingLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyzelog.AnalyseLogItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyzelog.AnalyzeLogRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.AnalyzeUtility;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyzeIP extends BaseServiceIP implements AnalyzeSV {

    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final JwtToken jwtToken;
    private final HttpServletRequest request;
    private final AnalyzeUtility analyzeUtility;
    private final UserNQ userNQ;

    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final LeapTempDrawingRP leapTempDrawingRP;
    private final VNTwoAnalyzeLogRP vnTwoAnalyzeLogRP;
    private final LeapAnalyzeLogRP leapAnalyzeLogRP;
    private final TNAnalyzeLogRP tnAnalyzeLogRP;
    private final TNTempDrawingRP tnTempDrawingRP;
    private final SCTempDrawingRP scTempDrawingRP;
    private final SCAnalyzeLogRP scAnalyzeLogRP;

    private final HazelcastInstance hazelcastInstance;
    private final static String MT_COLLECTION_NAME = "mt_analyzing_result";
    private final static String TN_COLLECTION_NAME = "tn_analyzing_result";
    private final static String LEAP_COLLECTION_NAME = "leap_analyzing_result";
    private final static String SC_COLLECTION_NAME = "sc_analyzing_result";

    @Override
    public StructureRS analyseSelling() {
        UserToken userToken = jwtToken.getUserToken();
        SellingLotteryRQ analyseRQ = new SellingLotteryRQ(request);

        String userCode = userToken.getUserCode();
        String userType = userToken.getUserType().toLowerCase();

        boolean isCanSeeUserOnline = true;
        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
            isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                isCanSeeUserOnline = true;
        }

        /*
         * Get member codes
         */
        List<UserLevelReportTO> filterMemberTOS = userNQ.userLevelFilter(userCode, userType, LotteryConstant.ALL, UserConstant.MEMBER.toLowerCase(), userCode, isCanSeeUserOnline);
        List<String> memberCodes = filterMemberTOS.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());


        List<AnalyseRS> analyseRS = new ArrayList<>();
        switch (analyseRQ.getFilterByLotteryType().toUpperCase()) {

            case LotteryConstant.VN1:
                if (LotteryConstant.ALL.equals(analyseRQ.getFilterByDrawCode())) {
                    VNOneTempDrawingEntity drawingEntity = vnOneTempDrawingRP.recentDrawing();
                    if (drawingEntity != null) {
                        analyseRQ.setFilterByDrawCode(drawingEntity.getCode());
                    }
                }
                analyzeUtility.analyseVn1(analyseRS, memberCodes, analyseRQ);
                break;
            case LotteryConstant.VN2:
                analyzeUtility.analyseVn2(analyseRS, memberCodes, analyseRQ);
                break;
            case LotteryConstant.LEAP:
                analyzeUtility.analyseLeap(analyseRS, memberCodes, analyseRQ);
                break;
        }

        return responseBodyWithSuccessMessage(analyseRS);
    }

    @Override
    public StructureRS analyzeLog(String lottery, String drawCode) {
        switch (lottery.toUpperCase()) {
            case LotteryConstant.VN2:
            case LotteryConstant.MT:
                VNTwoTempDrawingEntity drawingEntity = vnTwoTempDrawingRP.findByCode(drawCode);
                if (drawingEntity == null)
                    throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);
                return responseBodyWithSuccessMessage(vnTwoAnalyzeLog(drawingEntity));
            case LotteryConstant.LEAP:
                return responseBodyWithSuccessMessage(leapAnalyzeLog(drawCode));
            case LotteryConstant.TN:
                TNTempDrawingEntity tnDrawing = tnTempDrawingRP.findByCode(drawCode);
                if (tnDrawing == null)
                    throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);
                return responseBodyWithSuccessMessage(tnAnalyzeLog(tnDrawing));
            case LotteryConstant.SC:
                return responseBodyWithSuccessMessage(scAnalyzeLog(drawCode));
        }
        return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.UNKNOWN_TYPE);
    }

    public AnalyzeLogRS tnAnalyzeLog(TNTempDrawingEntity drawingEntity) {
        AnalyzeLogRS analyzeLogRS = new AnalyzeLogRS();
        analyzeLogRS.setIsNight(drawingEntity.getIsNight());
        analyzeLogRS.setDrawCode(drawingEntity.getCode());
        analyzeLogRS.setDrawAt(drawingEntity.getResultedPostAt());

        List<TNAnalyzeLogEntity> analyzeLogEntities = tnAnalyzeLogRP.getAllByDrawCode(drawingEntity.getCode());
        IMap<String, String> isLoadings = hazelcastInstance.getMap(TN_COLLECTION_NAME);

        for (TNAnalyzeLogEntity item : analyzeLogEntities) {
            AnalyseLogItemRS itemLog = new AnalyseLogItemRS();

            List<TNAnalyzeLogEntity> subAnalyze = analyzeLogEntities.stream().filter(it -> it.getPostCode().equals(item.getPostCode())).collect(Collectors.toList());
            BigDecimal totalCom = subAnalyze.stream().map(TNAnalyzeLogEntity::getTotalCommission).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalPrize = subAnalyze.stream().map(TNAnalyzeLogEntity::getPrize).reduce(BigDecimal.ZERO, BigDecimal::add);
            itemLog.setTotalAmount(totalCom);
            itemLog.setWinLoseAmount(totalCom.subtract(totalPrize));

            itemLog.setBetAmount(item.getBetAmount());
            itemLog.setNumber(item.getNumber());
            itemLog.setCommissionAmount(item.getCommission());
            itemLog.setPostCode(item.getPostCode());
            itemLog.setRewardAmount(item.getPrize());
            itemLog.setProtestPercentage(item.getProtestPercentage());
            itemLog.setRealPercentage(item.getRealPercentage());
            itemLog.setRebateAmount(item.getTotalSale());
            itemLog.setTotalCommissionAmount(item.getTotalCommission());
            itemLog.setRebateCode(item.getRebateCode());
            itemLog.setIsLoading(isLoadings.containsKey(item.getPostCode()));

            analyzeLogRS.getItems().add(itemLog);

            analyzeLogRS.addTotalPostAmount(item.getTotalCommission());
            analyzeLogRS.addTotalRewardAmount(item.getPrize());
            analyzeLogRS.addWinLoseAmount(item.getTotalCommission().subtract(item.getPrize()));
        }

        return analyzeLogRS;
    }

    public AnalyzeLogRS vnTwoAnalyzeLog(VNTwoTempDrawingEntity drawingEntity) {
        AnalyzeLogRS analyzeLogRS = new AnalyzeLogRS();
        analyzeLogRS.setIsNight(drawingEntity.getIsNight());
        analyzeLogRS.setDrawCode(drawingEntity.getCode());
        analyzeLogRS.setDrawAt(drawingEntity.getResultedPostAt());

        List<VNTwoAnalyzeLogEntity> analyzeLogEntities = vnTwoAnalyzeLogRP.getAllByDrawCode(drawingEntity.getCode());
        IMap<String, String> isLoadings = hazelcastInstance.getMap(MT_COLLECTION_NAME);

        for (VNTwoAnalyzeLogEntity item : analyzeLogEntities) {
            AnalyseLogItemRS itemLog = new AnalyseLogItemRS();

            List<VNTwoAnalyzeLogEntity> subAnalyze = analyzeLogEntities.stream().filter(it -> it.getPostCode().equals(item.getPostCode())).collect(Collectors.toList());
            BigDecimal totalCom = subAnalyze.stream().map(VNTwoAnalyzeLogEntity::getTotalCommission).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalPrize = subAnalyze.stream().map(VNTwoAnalyzeLogEntity::getPrize).reduce(BigDecimal.ZERO, BigDecimal::add);
            itemLog.setTotalAmount(totalCom);
            itemLog.setWinLoseAmount(totalCom.subtract(totalPrize));

            itemLog.setBetAmount(item.getBetAmount());
            itemLog.setNumber(item.getNumber());
            itemLog.setCommissionAmount(item.getCommission());
            itemLog.setPostCode(item.getPostCode());
            itemLog.setRewardAmount(item.getPrize());
            itemLog.setProtestPercentage(item.getProtestPercentage());
            itemLog.setRealPercentage(item.getRealPercentage());
            itemLog.setRebateAmount(item.getTotalSale());
            itemLog.setTotalCommissionAmount(item.getTotalCommission());
            itemLog.setRebateCode(item.getRebateCode());
            itemLog.setIsLoading(isLoadings.containsKey(item.getPostCode()));

            analyzeLogRS.getItems().add(itemLog);

            analyzeLogRS.addTotalPostAmount(item.getTotalCommission());
            analyzeLogRS.addTotalRewardAmount(item.getPrize());
            analyzeLogRS.addWinLoseAmount(item.getTotalCommission().subtract(item.getPrize()));
        }

        return analyzeLogRS;
    }

    public AnalyzeLogRS leapAnalyzeLog(String drawCode) {
        AnalyzeLogRS analyzeLogRS = new AnalyzeLogRS();
        LeapTempDrawingEntity leapTempDrawingEntity = leapTempDrawingRP.findByCode(drawCode);
        if (leapTempDrawingEntity == null)
            throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);

        analyzeLogRS.setDrawAt(leapTempDrawingEntity.getResultedPostAt());
        analyzeLogRS.setDrawCode(leapTempDrawingEntity.getCode());

        List<LeapAnalyzeLogEntity> analyzeLogEntities = leapAnalyzeLogRP.getAllByDrawCode(drawCode);
        IMap<String, String> isLoadings = hazelcastInstance.getMap(LEAP_COLLECTION_NAME);

        for (LeapAnalyzeLogEntity item : analyzeLogEntities) {
            AnalyseLogItemRS itemLog = new AnalyseLogItemRS();

            List<LeapAnalyzeLogEntity> subAnalyze = analyzeLogEntities.stream().filter(it -> it.getPostCode().equals(item.getPostCode())).collect(Collectors.toList());
            BigDecimal totalCom = subAnalyze.stream().map(LeapAnalyzeLogEntity::getTotalCommission).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalPrize = subAnalyze.stream().map(LeapAnalyzeLogEntity::getPrize).reduce(BigDecimal.ZERO, BigDecimal::add);
            itemLog.setTotalAmount(totalCom);
            itemLog.setWinLoseAmount(totalCom.subtract(totalPrize));

            itemLog.setBetAmount(item.getBetAmount());
            itemLog.setNumber(item.getNumber());
            itemLog.setCommissionAmount(item.getCommission());
            itemLog.setPostCode(item.getPostCode());
            itemLog.setRewardAmount(item.getPrize());
            itemLog.setProtestPercentage(item.getProtestPercentage());
            itemLog.setRealPercentage(item.getRealPercentage());
            itemLog.setRebateAmount(item.getTotalSale());
            itemLog.setTotalCommissionAmount(item.getTotalCommission());
            itemLog.setRebateCode(item.getRebateCode());
            itemLog.setIsLoading(isLoadings.containsKey(item.getPostCode()));

            analyzeLogRS.getItems().add(itemLog);

            analyzeLogRS.addTotalPostAmount(item.getTotalCommission());
            analyzeLogRS.addTotalRewardAmount(item.getPrize());
            analyzeLogRS.addWinLoseAmount(item.getTotalCommission().subtract(item.getPrize()));
        }

        return analyzeLogRS;
    }


    public AnalyzeLogRS scAnalyzeLog(String drawCode) {
        AnalyzeLogRS analyzeLogRS = new AnalyzeLogRS();
        SCTempDrawingEntity scTempDrawingEntity = scTempDrawingRP.findByCode(drawCode);
        if (scTempDrawingEntity == null)
            throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);

        analyzeLogRS.setDrawAt(scTempDrawingEntity.getResultedPostAt());
        analyzeLogRS.setDrawCode(scTempDrawingEntity.getCode());

        List<SCAnalyzeLogEntity> analyzeLogEntities = scAnalyzeLogRP.getAllByDrawCode(drawCode);
        IMap<String, String> isLoadings = hazelcastInstance.getMap(SC_COLLECTION_NAME);

        for (SCAnalyzeLogEntity item : analyzeLogEntities) {
            AnalyseLogItemRS itemLog = new AnalyseLogItemRS();

            List<SCAnalyzeLogEntity> subAnalyze = analyzeLogEntities.stream().filter(it -> it.getPostCode().equals(item.getPostCode())).collect(Collectors.toList());
            BigDecimal totalCom = subAnalyze.stream().map(SCAnalyzeLogEntity::getTotalCommission).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalPrize = subAnalyze.stream().map(SCAnalyzeLogEntity::getPrize).reduce(BigDecimal.ZERO, BigDecimal::add);
            itemLog.setTotalAmount(totalCom);
            itemLog.setWinLoseAmount(totalCom.subtract(totalPrize));

            itemLog.setBetAmount(item.getBetAmount());
            itemLog.setNumber(item.getNumber());
            itemLog.setCommissionAmount(item.getCommission());
            itemLog.setPostCode(item.getPostCode());
            itemLog.setRewardAmount(item.getPrize());
            itemLog.setProtestPercentage(item.getProtestPercentage());
            itemLog.setRealPercentage(item.getRealPercentage());
            itemLog.setRebateAmount(item.getTotalSale());
            itemLog.setTotalCommissionAmount(item.getTotalCommission());
            itemLog.setRebateCode(item.getRebateCode());
            itemLog.setIsLoading(isLoadings.containsKey(item.getPostCode()));

            analyzeLogRS.getItems().add(itemLog);

            analyzeLogRS.addTotalPostAmount(item.getTotalCommission());
            analyzeLogRS.addTotalRewardAmount(item.getPrize());
            analyzeLogRS.addWinLoseAmount(item.getTotalCommission().subtract(item.getPrize()));
        }

        return analyzeLogRS;
    }

}
