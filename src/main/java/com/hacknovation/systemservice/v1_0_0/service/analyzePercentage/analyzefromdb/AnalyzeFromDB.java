package com.hacknovation.systemservice.v1_0_0.service.analyzePercentage.analyzefromdb;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.analyzenumber.AnalyzeItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.RebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.MainAnalyseRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Sombath
 * create at 3/3/23 8:32 PM
 */

@Service
@RequiredArgsConstructor
public class AnalyzeFromDB {

    private final DataService dataService;

    public void setAnalyze(MainAnalyseRS mainAnalyseRS, AnalyzeRQ analyzeRQ, List<String> memberCodes, UserToken userToken, DrawingDTO drawingDTO) {

        String roleCode = userToken.getUserRole();
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            roleCode = userToken.getParentRole();
        }

        List<AnalyzeItemsDTO> orderItems = dataService.getData(analyzeRQ, memberCodes, drawingDTO);

        Map<String, List<AnalyzeItemsDTO>> orderItemGroupByPost = dataService.groupOrderItemByPost(orderItems, analyzeRQ.getFilterPosts());
        Map<String, Map<String, BigDecimal>> totalBetPerPostPerRebate = dataService.totalBetPerPostPerRebate(orderItemGroupByPost, roleCode);

        System.out.println(orderItemGroupByPost.keySet());
        BigDecimal totalSale = BigDecimal.ZERO;

        for (RebateRQ rebateRQ : analyzeRQ.getFilterPosts()) {

            String post = rebateRQ.getPostCode();

            Map<String, BigDecimal> totalByRebate = totalBetPerPostPerRebate.get(post);

            AnalyseRS analyseRS = new AnalyseRS();
            analyseRS.setPostCode(post);

            for (AnalyzeItemsDTO orderItem : orderItemGroupByPost.getOrDefault(post, Collections.emptyList())) {

                List<String> numberDetails = Stream.of(orderItem.getNumberDetail().split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());

                for (String number : numberDetails) {

                    AnalyseItemsRS itemsRS = new AnalyseItemsRS();

                    BigDecimal totalBet = BigDecimal.ZERO;
                    BigDecimal rebateRate = BigDecimal.ZERO;
                    BigDecimal filteredPercentage = BigDecimal.ZERO;

                    RebateRQ rebateTransfer = analyzeRQ.getRebateTransfer();

                    switch (orderItem.getRebateCode()) {

                        case LotteryConstant.REBATE1D:
                            totalBet = totalByRebate.get(LotteryConstant.REBATE1D);
                            filteredPercentage = rebateRQ.getOneD();
                            rebateRate = rebateTransfer.getOneD();
                            break;
                        case LotteryConstant.REBATE2D:
                            totalBet = totalByRebate.get(LotteryConstant.REBATE2D);
                            filteredPercentage = rebateRQ.getTwoD();
                            rebateRate = rebateTransfer.getTwoD();
                            break;
                        case LotteryConstant.REBATE3D:
                            totalBet = totalByRebate.get(LotteryConstant.REBATE3D);
                            filteredPercentage = rebateRQ.getThreeD();
                            rebateRate = rebateTransfer.getThreeD();
                            break;
                        case LotteryConstant.REBATE4D:
                            totalBet = totalByRebate.get(LotteryConstant.REBATE4D);
                            filteredPercentage = rebateRQ.getFourD();
                            rebateRate = rebateTransfer.getFourD();
                            break;
                        case LotteryConstant.REBATE5D:
                            totalBet = totalByRebate.get(LotteryConstant.REBATE5D);
                            filteredPercentage = rebateRQ.getFiveD();
                            rebateRate = rebateTransfer.getFiveD();
                            break;
                    }

                    // set filter percent for future filter percentage
                    itemsRS.setFilterPercentage(filteredPercentage);

                    BigDecimal reward = this.getRewardByRole(orderItem, roleCode);
                    BigDecimal winLost = totalBet.subtract(reward);
                    BigDecimal percentage = this.getPercentage(reward, totalBet);
                    BigDecimal transferAmount = filteredPercentage.multiply(winLost).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).divide(rebateRate, RoundingMode.HALF_UP);

                    itemsRS.setBetAmount(transferAmount.abs());
                    itemsRS.setTotalSale(orderItem.getBetAmount());
                    itemsRS.setNumber(number);
                    itemsRS.setRebateCode(orderItem.getRebateCode());
                    itemsRS.setTotalReward(reward);
                    itemsRS.setPercentage(percentage);

                    OptionalInt indexOpt = IntStream.range(0, analyseRS.getItems().size())
                            .filter(i -> number.equals(analyseRS.getItems().get(i).getNumber()))
                            .findFirst();

                    if (indexOpt.isEmpty())
                        analyseRS.getItems().add(itemsRS);
                    else {

                        // re calculate - bet amount
                        AnalyseItemsRS _itemRs = analyseRS.getItems().get(indexOpt.getAsInt());
                        _itemRs.setTotalReward(_itemRs.getTotalReward().add(itemsRS.getTotalReward()));
                        _itemRs.setTotalSale(_itemRs.getTotalSale().add(itemsRS.getTotalSale()));
                        _itemRs.setBetAmount((totalBet.subtract(_itemRs.getTotalReward()).divide(rebateRate, RoundingMode.HALF_UP)).abs());

                        _itemRs.setPercentage(this.getPercentage(_itemRs.getTotalReward(), totalBet));

                        analyseRS.getItems().set(indexOpt.getAsInt(), _itemRs);

                    }
                }
            }

            // get total sale

            if(totalByRebate != null) {
                Optional<BigDecimal> _totalByPost = totalByRebate.values().stream().reduce(BigDecimal::add);

                if (_totalByPost.isPresent()) {
                    totalSale = totalSale.add(_totalByPost.get());
                    analyseRS.setTotalSale(_totalByPost.get());
                }
            }


//            if (!LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType()))
//                analyseRS.getItems().removeIf(it -> it.getFilterPercentage().compareTo(it.getPercentage()) > 0);

            analyseRS.getItems().sort(Comparator.comparing(AnalyseItemsRS::getRebateCode).thenComparing(AnalyseItemsRS::getTotalSale, Comparator.reverseOrder()));

            mainAnalyseRS.setTotalSale(totalSale);
            if (!analyseRS.getItems().isEmpty())
                mainAnalyseRS.getItems().add(analyseRS);

        }

    }

    private BigDecimal getRewardByRole(AnalyzeItemsDTO it, String roleCode) {

        BigDecimal rebateRate = it.getSuperSeniorRebateRate();

        switch (roleCode.toUpperCase()) {
            case UserConstant.SUPER_SENIOR:
                rebateRate = it.getRebateRate();
                break;
            case UserConstant.SENIOR:
                rebateRate = it.getMasterRebateRate();
                break;
        }

        return it.getBetAmount().multiply(rebateRate);
    }

    private BigDecimal getPercentage(BigDecimal reward, BigDecimal totalBet) {
        if (totalBet.compareTo(BigDecimal.ZERO) > 0) {
            return reward.multiply(BigDecimal.valueOf(100)).divide(totalBet, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
