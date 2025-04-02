package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.PagingTempEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PagingTempRP extends JpaRepository<PagingTempEntity, Long> {
    @Query(value = "SELECT * FROM paging_temp WHERE lottery_code = :lotteryCode AND dc = :drawCode AND uc = :userCode ORDER BY pn DESC LIMIT 1", nativeQuery = true)
    PagingTempEntity findOnePageDesc(String lotteryCode, String drawCode, String userCode);

    @Query(value = "SELECT * FROM paging_temp WHERE lottery_code = :lotteryCode AND dc = :drawCode AND uc = :userCode ORDER BY pn DESC", nativeQuery = true)
    List<PagingTempEntity> listing(String lotteryCode, String drawCode, String userCode);

    @Query(value = "SELECT * FROM paging_temp WHERE lottery_code = :lotteryCode AND dc = :drawCode AND uc = :userCode AND pn = :pageNumber ORDER BY id DESC", nativeQuery = true)
    List<PagingTempEntity> findByLotteryCodeAndDrawCodeAndUserCodeAndPageNumber(String lotteryCode, String drawCode, String userCode, Integer pageNumber);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM paging_temp WHERE lottery_code = :lottery and uc = :userCode and dc = :drawCode and pn = :pageNumber", nativeQuery = true)
    void deletePageByUserCodeAndDrawCodeAndLotteryAndPageNumber(String userCode, String drawCode, String lottery, Integer pageNumber);

}