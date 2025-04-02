package com.hacknovation.systemservice.v1_0_0.io.repo.vntwo;

import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoAnalyzeEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.AnalyzeTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * author: kangto
 * createdAt: 28/04/2022
 * time: 10:59
 */
@Repository
public interface VNTwoAnalyzeRP extends JpaRepository<VNTwoAnalyzeEntity, Long> {

    @Query(value = "SELECT va.pc      PostCode,\n" +
            "       va.pg      PostGroup,\n" +
            "       va.rc      RebateCode,\n" +
            "       sum(va.ba * va.rebate_rate)      RewardAmount,\n" +
            "       va.nu      Number,\n" +
            "       SUM(va.ba) BetAmount,\n" +
            "       SUM(va.wa) WaterAmount\n" +
            "FROM vntwo_analyzing va\n" +
            "WHERE va.dc = :drawCode\n" +
            "  AND va.uc IN :memberCodes\n" +
            "  AND va.pg = 'POST'\n" +
            "GROUP BY va.pc, va.rc, va.nu", nativeQuery = true)
    List<AnalyzeTO> sumBetAmountGroupByPostAndNumber(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT va.pc      PostCode,\n" +
            "       va.pg      PostGroup,\n" +
            "       va.rc      RebateCode,\n" +
            "       sum(va.ba * va.rebate_rate)      RewardAmount,\n" +
            "       va.nu      Number,\n" +
            "       SUM(va.ba) BetAmount,\n" +
            "       SUM(va.wa) WaterAmount\n" +
            "FROM vntwo_analyzing va\n" +
            "WHERE va.dc = :drawCode\n" +
            "  AND va.uc IN :memberCodes\n" +
            "  AND va.pc IN :posts\n" +
            "GROUP BY va.pc, va.rc, va.nu", nativeQuery = true)
    List<AnalyzeTO> sumBetAmountGroupByPostAndNumberWherePostIn(String drawCode, List<String> memberCodes, List<String> posts);


    @Query(value = "SELECT 'LO'      PostCode,\n" +
            "       va.pg      PostGroup,\n" +
            "       va.rc      RebateCode,\n" +
            "       sum(va.ba * va.rebate_rate)      RewardAmount,\n" +
            "       va.nu      Number,\n" +
            "       SUM(va.ba) BetAmount,\n" +
            "       SUM(va.wa) WaterAmount\n" +
            "FROM vntwo_analyzing va\n" +
            "WHERE va.dc = :drawCode\n" +
            "  AND va.uc IN :memberCodes\n" +
            "  AND va.pg = 'LO'\n" +
            "GROUP BY va.rc, va.nu", nativeQuery = true)
    List<AnalyzeTO> sumBetAmountGroupByLOAndNumber(String drawCode, List<String> memberCodes);

}
