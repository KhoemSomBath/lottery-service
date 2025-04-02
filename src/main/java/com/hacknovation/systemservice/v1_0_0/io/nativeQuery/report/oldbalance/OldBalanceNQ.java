package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.oldbalance;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;

/**
 * @author Sombath
 * create at 20/8/21 5:42 PM
 */

@NativeQueryFolder("report/banhji")
@Component
public interface OldBalanceNQ extends NativeQuery {

    OldBanhjiTO oldBanhji(String lotteryType, String userCode, String filterDate);
    OldBanhjiAgentTO oldBanhjiAgent(String lotteryType, String userCode, String filterDate);
}
