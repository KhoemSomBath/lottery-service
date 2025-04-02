package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.summaryDaily;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * author: kangto
 * createdAt: 03/02/2022
 * time: 22:06
 */
@NativeQueryFolder("summaryDaily")
@Component
public interface SummaryDailyNQ extends NativeQuery {

    @Transactional
    List<SummaryDailyTO> listSummaryDaily(String filterDate);

    @Transactional
    List<SummaryDailyTO> listSummaryDailyGroupByDate(String lotteryType, List<String> userCodes, String filterByStartDate, String filterByEndDate);

}
