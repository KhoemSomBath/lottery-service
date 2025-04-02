package com.hacknovation.systemservice.v1_0_0.service.analyzeV2;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.AnalyzeDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeV2RQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.RebateV2RQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analysev2.AnalyseItemV2RS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analysev2.AnalyseV2RS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analysev2.MainAnalyseV2RS;
import com.hacknovation.systemservice.v1_0_0.utility.SqlUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 06/04/2023
 * time: 15:57
 */
@Service
@RequiredArgsConstructor
public class AnalyzeV2IP extends BaseServiceIP implements AnalyzeV2SV {

    private final SCTempDrawingRP scTempDrawingRP;
    private final SqlUtility sqlUtility;

    @Value("classpath:query/analyze/list-analyze.sql")
    Resource analyzeResource;
    private final THTempDrawingRP tHTempDrawingRP;

    @Override
    public StructureRS getFilter(String lotteryType, String drawCode) {
        if (!List.of(LotteryConstant.SC, LotteryConstant.TH).contains(lotteryType))
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);

        List<String> posts = PostConstant.POST_SC;
        if (LotteryConstant.TH.equals(lotteryType))
            posts = PostConstant.POST_TH;

        MainAnalyseV2RS mainAnalyseRS = new MainAnalyseV2RS();
        AnalyzeV2RQ analyzeRQ = mainAnalyseRS.getFilter();
        for (String postCode : posts) {
            RebateV2RQ rebateRQ = new RebateV2RQ();
            rebateRQ.setPostCode(postCode);
            analyzeRQ.getFilterPosts().add(rebateRQ);
        }

        BaseDrawingEntity drawingEntity = new BaseDrawingEntity();

        switch (lotteryType) {
            case LotteryConstant.SC:
                drawingEntity = drawCode.equals(LotteryConstant.ALL) ? scTempDrawingRP.recentDrawing() : scTempDrawingRP.findByCode(drawCode);
                if (drawingEntity == null)
                    drawingEntity = scTempDrawingRP.lastDraw();
                break;
            case LotteryConstant.TH:
                THTempDrawingEntity thTempDrawingEntity = drawCode.equals(LotteryConstant.ALL) ? tHTempDrawingRP.recentDrawing() : tHTempDrawingRP.findByCode(drawCode);
                if (thTempDrawingEntity == null)
                    thTempDrawingEntity = tHTempDrawingRP.lastDraw();
                BeanUtils.copyProperties(thTempDrawingEntity, drawingEntity);
                break;
        }

        analyzeRQ.setLotteryType(lotteryType);
        analyzeRQ.setDrawCode(drawingEntity.getCode());
        analyzeRQ.setDrawAt(drawingEntity.getResultedPostAt());

        return responseBodyWithSuccessMessage(mainAnalyseRS);
    }

    @Override
    public StructureRS postAnalyze(AnalyzeV2RQ analyzeV2RQ) {
        if (!List.of(LotteryConstant.SC, LotteryConstant.TH).contains(analyzeV2RQ.getLotteryType()))
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);

        BaseDrawingEntity drawingEntity = new BaseDrawingEntity();

        switch (analyzeV2RQ.getLotteryType()) {
            case LotteryConstant.SC:
                drawingEntity = scTempDrawingRP.findByCode(analyzeV2RQ.getDrawCode());
                if (drawingEntity == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
                break;
            case LotteryConstant.TH:
                THTempDrawingEntity thTempDrawingEntity = tHTempDrawingRP.findByCode(analyzeV2RQ.getDrawCode());
                if (thTempDrawingEntity == null)
                    return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
                BeanUtils.copyProperties(thTempDrawingEntity, drawingEntity);
                break;
        }

        analyzeV2RQ.setDrawAt(drawingEntity.getResultedPostAt());
        MainAnalyseV2RS mainAnalyseV2RS = new MainAnalyseV2RS();
        BeanUtils.copyProperties(analyzeV2RQ, mainAnalyseV2RS.getFilter());


        List<AnalyzeDTO> analyzeDTOList = sqlUtility.executeQueryForList(sqlUtility.getQuery(analyzeResource, Map.of(":analyze_tbl", analyzeV2RQ.getLotteryType().toLowerCase().concat("_analyze"))), Map.of("drawId", drawingEntity.getId()), AnalyzeDTO.class);

        if (analyzeV2RQ.getLotteryType().equals(LotteryConstant.TH))
            return this.THAnalyze(analyzeDTOList, mainAnalyseV2RS);

        List<AnalyzeDTO> filterAnalyze2D = new ArrayList<>();
        List<AnalyzeDTO> filterAnalyze3D = new ArrayList<>();
        List<AnalyzeDTO> filterAnalyze4D = new ArrayList<>();
        filterRebateAnalyzeTOList(analyzeDTOList, filterAnalyze2D, filterAnalyze3D, filterAnalyze4D, mainAnalyseV2RS);

        for (RebateV2RQ filterPost : mainAnalyseV2RS.getFilter().getFilterPosts()) {
            List<AnalyzeDTO> filterAnalyze2DByPost = filterAnalyze2D.stream().filter(it -> it.getPostCode().equals(filterPost.getPostCode())).collect(Collectors.toList());
            List<AnalyzeDTO> filterAnalyze3DByPost = filterAnalyze3D.stream().filter(it -> it.getPostCode().equals(filterPost.getPostCode())).collect(Collectors.toList());
            List<AnalyzeDTO> filterAnalyze4DByPost = filterAnalyze4D.stream().filter(it -> it.getPostCode().equals(filterPost.getPostCode())).collect(Collectors.toList());
            int two = 0;
            int three = 0;
            AnalyseV2RS analyseV2RS = new AnalyseV2RS();
            analyseV2RS.setPostCode(filterPost.getPostCode());
            List<AnalyseItemV2RS> analyzeRSList = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                AnalyseItemV2RS analyzeRS = new AnalyseItemV2RS();
                analyzeRS.setPostCode(filterPost.getPostCode());

                /**
                 * Set two digits
                 */
                if (two == 100)
                    two = 0;

                String twoDigit = StringUtils.leftPad(String.valueOf(two), 2, "0");
                analyzeRS.setNumber(twoDigit);
                this.updateValueBetAndWaterAndRewardAnalyseRS(analyzeRS, filterAnalyze2DByPost);

                /**
                 * Set three digits
                 */
                if (three == 1000)
                    three = 0;

                String threeDigit = StringUtils.leftPad(String.valueOf(three), 3, "0");
                analyzeRS.setNumber(threeDigit);
                this.updateValueBetAndWaterAndRewardAnalyseRS(analyzeRS, filterAnalyze3DByPost);

                /**
                 * Set four digits
                 */
                String fourDigit = StringUtils.leftPad(String.valueOf(i), 4, "0");
                analyzeRS.setNumber(fourDigit);
                this.updateValueBetAndWaterAndRewardAnalyseRS(analyzeRS, filterAnalyze4DByPost);


                if (mainAnalyseV2RS.getCommissionAmount().compareTo(BigDecimal.ZERO) > 0)
                    analyzeRS.setPercentage(this.calculatePercentage(analyzeRS.getTotalReward(), mainAnalyseV2RS.getCommissionAmount()));


                boolean isAdd = analyzeRS.getPercentage().compareTo(BigDecimal.ZERO) > 0;
                isAdd = isAdd && analyzeRS.getPercentage().compareTo(filterPost.getPercentage()) >= 0;

                if (isAdd)
                    analyzeRSList.add(analyzeRS);

                two++;
                three++;

                analyseV2RS.setBetAmount(analyseV2RS.getBetAmount().add(analyzeRS.getBetAmount()));
                analyseV2RS.setCommissionAmount(analyseV2RS.getCommissionAmount().add(analyzeRS.getCommissionAmount()));
            }

            analyzeRSList.sort(Comparator.comparing(AnalyseItemV2RS::getPercentage, Collections.reverseOrder()));
            analyseV2RS.setItems(analyzeRSList);
            mainAnalyseV2RS.getItems().add(analyseV2RS);

        }


        return responseBodyWithSuccessMessage(mainAnalyseV2RS);
    }


    private StructureRS THAnalyze(List<AnalyzeDTO> analyzeDTOList, MainAnalyseV2RS mainAnalyseV2RS) {

        BigDecimal twoDComm = analyzeDTOList.stream().filter(it->it.getRebateCode().equals(LotteryConstant.REBATE2D)).map(AnalyzeDTO::getWaterAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal threeDComm = analyzeDTOList.stream().filter(it->it.getRebateCode().equals(LotteryConstant.REBATE3D)).map(AnalyzeDTO::getWaterAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal betAmount = analyzeDTOList.stream().map(AnalyzeDTO::getBetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        mainAnalyseV2RS.setCommissionAmount(twoDComm.add(threeDComm));
        mainAnalyseV2RS.setBetAmount(betAmount);

        for (RebateV2RQ filterPost : mainAnalyseV2RS.getFilter().getFilterPosts()) {
            AnalyseV2RS analyseV2RS = new AnalyseV2RS();
            analyseV2RS.setPostCode(filterPost.getPostCode());
            List<AnalyseItemV2RS> analyzeRSList = new ArrayList<>();

            for (AnalyzeDTO item : analyzeDTOList.stream().filter(it -> it.getPostCode().equals(filterPost.getPostCode())).collect(Collectors.toList())) {

                AnalyseItemV2RS analyzeRS = new AnalyseItemV2RS();
                analyzeRS.setPostCode(filterPost.getPostCode());
                analyzeRS.setNumber(item.getNumber());
                analyzeRS.setBetAmount(item.getBetAmount());
                analyzeRS.setTotalReward(item.getRewardAmount());
                analyzeRS.setCommissionAmount(item.getWaterAmount());

                analyzeRS.setPercentage(this.calculatePercentage(analyzeRS.getTotalReward(), item.getRebateCode().equals(LotteryConstant.REBATE2D) ? twoDComm : threeDComm));

                boolean isAdd = analyzeRS.getPercentage().compareTo(BigDecimal.ZERO) > 0;
                isAdd = isAdd && analyzeRS.getPercentage().compareTo(filterPost.getPercentage()) >= 0;

                if (isAdd)
                    analyzeRSList.add(analyzeRS);

                analyseV2RS.setBetAmount(analyseV2RS.getBetAmount().add(item.getBetAmount()));
                analyseV2RS.setCommissionAmount(analyseV2RS.getCommissionAmount().add(item.getWaterAmount()));
            }

            analyzeRSList.sort(Comparator.comparing(AnalyseItemV2RS::getPercentage, Collections.reverseOrder()));
            analyseV2RS.setItems(analyzeRSList);
            mainAnalyseV2RS.getItems().add(analyseV2RS);

        }

        return responseBodyWithSuccessMessage(mainAnalyseV2RS);
    }


    private void filterRebateAnalyzeTOList(List<AnalyzeDTO> analyzeTOList,
                                           List<AnalyzeDTO> filterAnalyze2D,
                                           List<AnalyzeDTO> filterAnalyze3D,
                                           List<AnalyzeDTO> filterAnalyze4D,
                                           MainAnalyseV2RS mainAnalyseV2RS) {
        for (AnalyzeDTO item : analyzeTOList) {
            mainAnalyseV2RS.setBetAmount(mainAnalyseV2RS.getBetAmount().add(item.getBetAmount()));
            mainAnalyseV2RS.setCommissionAmount(mainAnalyseV2RS.getCommissionAmount().add(item.getWaterAmount()));
            if (LotteryConstant.REBATE2D.equals(item.getRebateCode()))
                filterAnalyze2D.add(item);
            if (LotteryConstant.REBATE3D.equals(item.getRebateCode()))
                filterAnalyze3D.add(item);
            if (LotteryConstant.REBATE4D.equals(item.getRebateCode()))
                filterAnalyze4D.add(item);
        }
    }

    public void updateValueBetAndWaterAndRewardAnalyseRS(AnalyseItemV2RS analyzeRS, List<AnalyzeDTO> analyzeTOList) {
        analyzeTOList
                .stream()
                .filter(it -> it.getNumber().equals(analyzeRS.getNumber()))
                .collect(Collectors.toList())
                .forEach(analyzeRS::addAmountBetAndWaterAndReward);
    }

    private BigDecimal calculatePercentage(BigDecimal amount, BigDecimal rate) {
        return amount.divide(rate, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

}
