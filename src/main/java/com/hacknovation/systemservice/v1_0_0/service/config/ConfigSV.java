package com.hacknovation.systemservice.v1_0_0.service.config;


import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.RebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.CheckHasLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.UpdateRebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.UpdateUserHasLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.CommissionLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConfigSV {

    /**
     * Has Lottery
     */
    StructureRS userHasLottery();
    StructureRS getUserHasLotteryCreate(String userCode);
    StructureRS updateUserHasLottery(UpdateUserHasLotteryRQ updateUserHasLotteryRQ);
    StructureRS checkShareCommission(CheckHasLotteryRQ checkHasLotteryRQ);
    StructureRS updateRebateSixD(UpdateRebateRQ updateRebateRQ);
    void copyUserHasLottery(UserEntity userEntity);
    void createUserHasLotteryFromCommissionList(UserEntity userEntity, List<CommissionLotteryRQ> commissionRQList, UserToken userToken);
    StructureRS fetchUserHasLotteryList(String userCode);

    /**
     * Manage Lottery
     * @param userCode
     * @param type
     */
    StructureRS lottery(String userCode, String type);
    StructureRS listRebate();
    StructureRS filterAbleRebate(String lotteryType, String filterByLevel);
    StructureRS updateRebate(Integer id, RebateRQ rebateRQ);

    /*
    * shift
    * */

    StructureRS shift(String lotteryType);
    StructureRS getLotteryType();
}
