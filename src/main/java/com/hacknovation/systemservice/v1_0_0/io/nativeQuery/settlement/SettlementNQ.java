package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.settlement;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * author: kangto
 * createdAt: 03/02/2022
 * time: 21:50
 */
@NativeQueryFolder("settlement")
@Component
public interface SettlementNQ extends NativeQuery {

    @Transactional
    List<SettlementTO> sumSettlementItemsLessThanDate(String lotteryType, List<String> userCodes, String filterByDate);

    @Transactional
    List<SettlementTO> sumSettlementItemsLessThanDateMix(List<String> userCodes, String filterByDate);

    @Transactional
    List<SettlementTO> sumSettlementItemEqualDateGroupByType(String lotteryType, List<String> userCodes, String filterByDate);

}
