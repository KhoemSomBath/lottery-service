package com.hacknovation.systemservice.schedule;

import com.hacknovation.systemservice.v1_0_0.io.entity.InitialBalanceEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.SettlementItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.SummeryDailyEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.summaryDaily.SummaryDailyTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.InitialBalanceRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.SettlementItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.SummeryDailyRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.summary.SummaryRS;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.SummaryDailyUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sombath
 * create at 22/2/22 12:06 PM
 */

@EnableScheduling
@RequiredArgsConstructor
@Component
public class InitialBalanceSchedule {

    private static final Integer DELETE_DAY = 60;

    private final SettlementItemRP settlementItemRP;
    private final InitialBalanceRP initialBalanceRP;
    private final SummeryDailyRP summeryDailyRP;
    private final SummaryDailyUtility summaryDailyUtility;

    @Scheduled(cron = "20 20 0 * * ?", zone = "Asia/Phnom_Penh")
    public void reCalculateInitialBalance() {

        LocalDate currentDate = LocalDate.now().minusDays(DELETE_DAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<InitialBalanceEntity> initialBalanceEntities = initialBalanceRP.findAll();
        List<SettlementItemsEntity> settlementItemsEntities = settlementItemRP.getAllByDateLessThan(currentDate);
        List<SummeryDailyEntity> summeryDailyEntities = summeryDailyRP.findAllByUserCodeInLessThanThat(currentDate.format(formatter));

        Map<String, List<SummeryDailyEntity>> summeryDailyEntityMap = summeryDailyEntities.stream().collect(Collectors.groupingBy(SummeryDailyEntity::getUserCode));
        Map<String, List<SummaryDailyTO>> summeryDailyMap = new HashMap<>();

        for (String userCode : summeryDailyEntityMap.keySet()) {

            List<SummaryDailyTO> summaryDailyList = new ArrayList<>();

            for (SummeryDailyEntity item : summeryDailyEntityMap.get(userCode)) {

                SummaryDailyTO summaryDailyTO = new SummaryDailyTO();
                summaryDailyTO.setLotteryType(item.getLotteryType());
                summaryDailyTO.setIssuedAt(item.getIssuedAt());
                summaryDailyTO.setUserCode(userCode);

                if (item.getDetail() != null) {
                    SummaryRS summaryRS = summaryDailyUtility.getSummaryRSFromEntity(item);
                    List<BigDecimal> shared = summaryDailyUtility.getSharedBalanceFromSummeryDaily(summaryRS);
                    summaryDailyTO.setAmountKhr(shared.get(0));
                    summaryDailyTO.setAmountUsd(shared.get(1));
                } else {
                    BigDecimal amountKhr = item.getTotalTurnoverKhr().subtract(item.getTotalRewardKhr());
                    BigDecimal amountUsd = item.getTotalTurnoverUsd().subtract(item.getTotalRewardUsd());
                    summaryDailyTO.setAmountKhr(amountKhr);
                    summaryDailyTO.setAmountUsd(amountUsd);
                }

                summaryDailyList.add(summaryDailyTO);
            }

            summeryDailyMap.put(userCode, summaryDailyList);
        }


        System.out.println("Initial balance " + initialBalanceEntities.size());
        System.out.println("Settlement " + settlementItemsEntities.size());
        System.out.println("SummeryDaily Group by User " + summeryDailyEntities.size());

        try {

            Map<String, List<SettlementItemsEntity>> settlementItemsEntityMap = settlementItemsEntities.stream().collect(Collectors.groupingBy(SettlementItemsEntity::getMemberCode));
            for (InitialBalanceEntity initialBalanceEntity : initialBalanceEntities) {

                BigDecimal balanceKhr = initialBalanceEntity.getBalanceKhr();
                BigDecimal balanceUsd = initialBalanceEntity.getBalanceUsd();

                if (settlementItemsEntityMap.containsKey(initialBalanceEntity.getUserCode())) {
                    System.out.println("InitialBalanceSchedule.reCalculateInitialBalance update for user code = " + initialBalanceEntity.getUserCode());
                    Map<String, List<SettlementItemsEntity>> settlementByLotteries = settlementItemsEntityMap.get(initialBalanceEntity.getUserCode()).stream().collect(Collectors.groupingBy(SettlementItemsEntity::getLotteryCode));
                    if (settlementByLotteries.containsKey(initialBalanceEntity.getLotteryType())) {
                        for (SettlementItemsEntity settlementItemsEntity : settlementByLotteries.get(initialBalanceEntity.getLotteryType())) {
                            balanceKhr = balanceKhr.add(settlementItemsEntity.getAmountKhr());
                            balanceUsd = balanceUsd.add(settlementItemsEntity.getAmountUsd());
                        }
                    }
                }

                if (summeryDailyMap.containsKey(initialBalanceEntity.getUserCode())) {
                    Map<String, List<SummaryDailyTO>> summeryByLotteries = summeryDailyMap.get(initialBalanceEntity.getUserCode()).stream().collect(Collectors.groupingBy(SummaryDailyTO::getLotteryType));
                    if (summeryByLotteries.containsKey(initialBalanceEntity.getLotteryType())) {
                        for (SummaryDailyTO summaryDailyTO : summeryByLotteries.get(initialBalanceEntity.getLotteryType())) {
                            balanceKhr = balanceKhr.add(summaryDailyTO.getAmountKhr());
                            balanceUsd = balanceUsd.add(summaryDailyTO.getAmountUsd());
                        }
                    }
                }

                initialBalanceEntity.setBalanceKhr(balanceKhr);
                initialBalanceEntity.setBalanceUsd(balanceUsd);

            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            initialBalanceRP.saveAll(initialBalanceEntities);
            settlementItemRP.removeAllByDateLessThan(currentDate);
            summeryDailyRP.removeAllByDateLessThan(currentDate);

            System.out.println("Initial balance complete successfully");
        }
    }

}
