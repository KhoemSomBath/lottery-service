package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.InitialBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InitialBalanceRP extends JpaRepository<InitialBalanceEntity, Long> {
    @Query(value = "SELECT * FROM initial_balance ib WHERE ib.uc IN :userCodes and ib.lottery_type in :lottery", nativeQuery = true)
    List<InitialBalanceEntity> listing(List<String> userCodes, List<String> lottery);

    @Query(value = "SELECT * FROM initial_balance ib WHERE ib.uc IN :userCodes", nativeQuery = true)
    List<InitialBalanceEntity> listingByUserCode(List<String> userCodes);

    @Query(value = "SELECT * FROM initial_balance ib WHERE ib.lottery_type = :lotteryType AND ib.uc IN :userCodes", nativeQuery = true)
    List<InitialBalanceEntity> listingByLotteryType(List<String> userCodes, String lotteryType);
}