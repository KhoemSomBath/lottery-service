package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.v1_0_0.io.entity.SummeryDailyEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.summaryDaily.SummaryDailyNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.summaryDaily.SummaryDailyTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.SummeryDailyRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.summary.SummaryItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.summary.SummaryRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 03/02/2022
 * time: 22:04
 */
@Component
@RequiredArgsConstructor
public class SummaryDailyUtility {

    private final SummaryDailyNQ summaryDailyNQ;
    private final GeneralUtility generalUtility;
    private final ObjectMapper objectMapper;
    private final SummeryDailyRP summeryDailyRP;

    /**
     * get summary daily by lottery type, user code and less than filter date
     * @param lotteryType String
     * @param userCodes List<String>
     * @param filterByDate String
     * @return Map<String, CurrencyRS>
     */
    public Map<String, CurrencyRS> getSummaryDailyLessThanDateMap(String lotteryType, List<String> userCodes, String filterByDate) {
        Map<String, CurrencyRS> currencyRSMap = new HashMap<>();

        List<SummeryDailyEntity> summeryDailyEntities = summeryDailyRP.findAllByLotteryUserCodeInLessThanThat(lotteryType, userCodes, filterByDate);
        Map<String, List<SummeryDailyEntity>> summeryDailyEntitiesMap = summeryDailyEntities.stream().collect(Collectors.groupingBy(SummeryDailyEntity::getUserCode));

        for(String userCode: summeryDailyEntitiesMap.keySet()){
            CurrencyRS currencyRS = new CurrencyRS();

            for(SummeryDailyEntity item: summeryDailyEntitiesMap.get(userCode)){
                if(item.getDetail() != null) {

                    SummaryRS summaryRS = this.getSummaryRSFromEntity(item);
                    List<BigDecimal> shared = this.getSharedBalanceFromSummeryDaily(summaryRS);

                    currencyRS.setAmountKhr(currencyRS.getAmountKhr().add(shared.get(0)));
                    currencyRS.setAmountUsd(currencyRS.getAmountUsd().add(shared.get(1)));

                } else {

                    BigDecimal amountKhr = item.getTotalTurnoverKhr().subtract(item.getTotalRewardKhr());
                    BigDecimal amountUsd = item.getTotalTurnoverUsd().subtract(item.getTotalRewardUsd());

                    currencyRS.setAmountKhr(currencyRS.getAmountKhr().add(amountKhr));
                    currencyRS.setAmountUsd(currencyRS.getAmountUsd().add(amountUsd));

                }
            }

            currencyRSMap.put(userCode, currencyRS);
        }

        return currencyRSMap;
    }


    public Map<String, CurrencyRS> getSummaryDailyLessThanDateMapMix(List<String> userCodes, String filterByDate) {
        Map<String, CurrencyRS> currencyRSMap = new HashMap<>();

        List<SummeryDailyEntity> summeryDailyEntities = summeryDailyRP.findAllUserCodeInLessThanThatMix(userCodes, filterByDate);
        Map<String, List<SummeryDailyEntity>> summeryDailyEntitiesMap = summeryDailyEntities.stream().collect(Collectors.groupingBy(SummeryDailyEntity::getUserCode));

        for(String userCode: summeryDailyEntitiesMap.keySet()){
            CurrencyRS currencyRS = new CurrencyRS();

            for(SummeryDailyEntity item: summeryDailyEntitiesMap.get(userCode)){
                if(item.getDetail() != null) {

                    SummaryRS summaryRS = this.getSummaryRSFromEntity(item);
                    List<BigDecimal> shared = this.getSharedBalanceFromSummeryDaily(summaryRS);

                    currencyRS.setAmountKhr(currencyRS.getAmountKhr().add(shared.get(0)));
                    currencyRS.setAmountUsd(currencyRS.getAmountUsd().add(shared.get(1)));

                } else {

                    BigDecimal amountKhr = item.getTotalTurnoverKhr().subtract(item.getTotalRewardKhr());
                    BigDecimal amountUsd = item.getTotalTurnoverUsd().subtract(item.getTotalRewardUsd());

                    currencyRS.setAmountKhr(currencyRS.getAmountKhr().add(amountKhr));
                    currencyRS.setAmountUsd(currencyRS.getAmountUsd().add(amountUsd));

                }
            }

            currencyRSMap.put(userCode, currencyRS);
        }

        return currencyRSMap;
    }

    /**
     * get summary daily by lottery type, user code and group by
     * @param lotteryType String
     * @param userCodes List<String>
     * @param filterByStartDate String
     * @return Map<String, CurrencyRS>
     */
    public Map<String, Map<String, CurrencyRS>> getSummaryDailyGroupByUserCodeAndDateMap(String lotteryType, List<String> userCodes, String filterByStartDate, String filterByEndDate) {
        Map<String, Map<String, CurrencyRS>> currencyRSMapUserCode = new HashMap<>();
        List<SummaryDailyTO> summaryDailyTOList = summaryDailyNQ.listSummaryDailyGroupByDate(lotteryType, userCodes, filterByStartDate, filterByEndDate);
        Map<String, List<SummaryDailyTO>> summaryDailyGroupByUserCode = summaryDailyTOList.stream().collect(Collectors.groupingBy(SummaryDailyTO::getUserCode));
        summaryDailyGroupByUserCode.forEach((userCode,items) -> {
            Map<String, CurrencyRS> currencyRSMapDate = new HashMap<>();
            Map<Date, SummaryDailyTO> mapByIssuedAt = items.stream().collect(Collectors.toMap(SummaryDailyTO::getIssuedAt, Function.identity()));
            mapByIssuedAt.forEach((draw, item) -> {
                CurrencyRS currencyRS = new CurrencyRS();
                BeanUtils.copyProperties(item, currencyRS);
                currencyRSMapDate.put(generalUtility.formatDateYYYYMMDD(draw), currencyRS);
            });
            currencyRSMapUserCode.put(userCode, currencyRSMapDate);
        });

        return currencyRSMapUserCode;
    }


    public SummaryRS getSummaryRSFromEntity(SummeryDailyEntity entity) {
        SummaryRS item = new SummaryRS();
        if (entity.getDetail() != null) {
            try {
                item = objectMapper.readValue(entity.getDetail(), SummaryRS.class);
            } catch (JsonProcessingException e) {
                System.out.println("SummaryDailyUtility.getSummaryRSFromEntity INTERNAL ERROR");
                e.printStackTrace();
            }
        }
        return item;
    }

    public List<BigDecimal> getSharedBalanceFromSummeryDaily(SummaryRS summeryDaily) {

        SummaryItemRS oneDKhr = summeryDaily.getOneD().getKhr();
        SummaryItemRS oneDUsd = summeryDaily.getOneD().getUsd();

        SummaryItemRS twoDKhr = summeryDaily.getTwoD().getKhr();
        SummaryItemRS twoDUsd = summeryDaily.getTwoD().getUsd();

        SummaryItemRS threeDKhr = summeryDaily.getThreeD().getKhr();
        SummaryItemRS threeDUsd = summeryDaily.getThreeD().getUsd();

        SummaryItemRS fourDKhr = summeryDaily.getFourD().getKhr();
        SummaryItemRS fourDUsd = summeryDaily.getFourD().getUsd();


        BigDecimal sharedKhr = oneDKhr.getShareAmount().add(twoDKhr.getShareAmount()).add(threeDKhr.getShareAmount()).add(fourDKhr.getShareAmount());
        BigDecimal sharedUsd = oneDUsd.getShareAmount().add(twoDUsd.getShareAmount()).add(threeDUsd.getShareAmount()).add(fourDUsd.getShareAmount());

        return List.of(sharedKhr, sharedUsd);
    }
}
