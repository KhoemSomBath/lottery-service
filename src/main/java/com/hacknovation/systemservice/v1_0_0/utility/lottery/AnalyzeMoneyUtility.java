package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.RebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 23/12/2022
 * time: 18:07
 */
@Component
public class AnalyzeMoneyUtility {

    public void analyzeMoney(MainAnalyseRS mainAnalyseRS) {
        List<RebateRQ> filterPosts = mainAnalyseRS.getFilter().getFilterPosts();
        List<AnalyseRS> analyseRSList = mainAnalyseRS.getItems();
        List<AnalyseRS> analyseRSMoneyList = new ArrayList<>();
        for (RebateRQ rebateRQ : filterPosts) {
            AnalyseRS analyseRS = new AnalyseRS();
            analyseRS.setPostCode(rebateRQ.getPostCode());

            List<String> posts = List.of(rebateRQ.getPostCode().split(""));
            if (PostConstant.LO_GROUP.equals(rebateRQ.getPostCode())) {
                posts = List.of(PostConstant.LO_GROUP);
            }
            List<SubAnalyseItemsRS> validAnalyseItemsRSList = new ArrayList<>();
            List<PostSelectRS> postSelectRSList = new ArrayList<>();
            for (String post : posts) {
                PostSelectRS postSelectRS = new PostSelectRS();
                postSelectRS.setPostCode(post);
                if (PostConstant.LO_GROUP.equals(post)) {
                    postSelectRS.setPostCode(PostConstant.LO_GROUP);
                }
                postSelectRSList.add(postSelectRS);
                Optional<AnalyseRS> optAnalyseRS = analyseRSList.stream().filter(it->it.getPostCode().equals(post)).findFirst();
                if (optAnalyseRS.isPresent()) {
                    AnalyseRS item = optAnalyseRS.get();
                    validAnalyseItemsRSList.addAll(getFilterAnalyzeItemRS(item.getItems(), post, LotteryConstant.REBATE1D, rebateRQ.getOneD()));
                    validAnalyseItemsRSList.addAll(getFilterAnalyzeItemRS(item.getItems(), post, LotteryConstant.REBATE2D, rebateRQ.getTwoD()));
                    validAnalyseItemsRSList.addAll(getFilterAnalyzeItemRS(item.getItems(), post, LotteryConstant.REBATE3D, rebateRQ.getThreeD()));
                    validAnalyseItemsRSList.addAll(getFilterAnalyzeItemRS(item.getItems(), post, LotteryConstant.REBATE4D, rebateRQ.getFourD()));
                }
            }
            Map<String, List<SubAnalyseItemsRS>> analyzeItemGroupByNumber = validAnalyseItemsRSList.stream().collect(Collectors.groupingBy(AnalyseItemsRS::getNumber));

            List<AnalyseItemsRS> analyseItemsRSList = new ArrayList<>();
            for (String number : analyzeItemGroupByNumber.keySet()) {
                List<PostSelectRS> postSelectRSNumberList = new ArrayList<>();
                for (PostSelectRS postSelectRS : postSelectRSList) {
                    PostSelectRS numberPost = new PostSelectRS();
                    BeanUtils.copyProperties(postSelectRS, numberPost);
                    postSelectRSNumberList.add(numberPost);
                }
                List<SubAnalyseItemsRS> numberAllPost = analyzeItemGroupByNumber.get(number);

                SubAnalyseItemsRS maxObject = numberAllPost.stream().max(Comparator.comparing(AnalyseItemsRS::getTotalSale)).orElseThrow(NoSuchElementException::new);
                AnalyseItemsRS analyseItemsRS = new AnalyseItemsRS();
                analyseItemsRS.setNumber(number);
                analyseItemsRS.setRebateCode(String.valueOf(number.length()).concat("D"));
                analyseItemsRS.setTotalSale(maxObject.getTotalSale());
                for (PostSelectRS postSelectRS : postSelectRSNumberList) {
                    numberAllPost.stream().filter(it-> it.getPostCode().equals(postSelectRS.getPostCode()))
                            .findFirst()
                            .ifPresent(p-> postSelectRS.setIsHighlight(true));
                }

                analyseItemsRS.setHighlightPosts(postSelectRSNumberList);
                analyseItemsRSList.add(analyseItemsRS);
            }

            analyseRS.setItems(analyseItemsRSList);

            analyseRSMoneyList.add(analyseRS);
        }
        AnalyseRS analyseRSLO = analyseRSMoneyList.stream()
                .filter(it-> LotteryConstant.LO_GROUP.equals(it.getPostCode()))
                .findFirst().orElse(null);
        AnalyseRS analyseRSABCD = analyseRSMoneyList.stream()
                .filter(it-> "ABCD".equals(it.getPostCode()))
                .findFirst().orElse(null);
        if (analyseRSLO != null && analyseRSABCD != null) {
            for (AnalyseItemsRS item : analyseRSLO.getItems()) {
                Optional<AnalyseItemsRS> optAnalyze = analyseRSABCD.getItems().stream().filter(it->it.getNumber().equals(item.getNumber())).findFirst();
                if (optAnalyze.isPresent()) {
                    item.setIsHighlightSale(true);
                }
            }
        }
        mainAnalyseRS.setItems(analyseRSMoneyList);

    }


    private List<SubAnalyseItemsRS> getFilterAnalyzeItemRS(List<AnalyseItemsRS> analyseItemsRSList, String postCode, String rebateCode, BigDecimal filterAmount) {
        return analyseItemsRSList.stream()
                .filter(it-> rebateCode.equals(it.getRebateCode()) && it.getTotalSale().doubleValue() >= filterAmount.doubleValue())
                .map(it -> {
                    SubAnalyseItemsRS subAnalyseItemsRS = new SubAnalyseItemsRS();
                    BeanUtils.copyProperties(it, subAnalyseItemsRS);
                    subAnalyseItemsRS.setPostCode(postCode);
                    return subAnalyseItemsRS;
                }).collect(Collectors.toList());
    }
}
