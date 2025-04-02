package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.TransferTempNumbersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransferTempNumbersRP extends JpaRepository<TransferTempNumbersEntity, Long> {

    @Query(value = "SELECT * FROM transfer_temp_numbers ttp WHERE  ttp.id = :id ORDER BY id DESC LIMIT 1", nativeQuery = true)
    TransferTempNumbersEntity itemById(Integer id);

    @Query(value = "SELECT * FROM transfer_temp_numbers ttp WHERE  ttp.lt = :lotteryType AND ttp.dc = :drawCode ORDER BY id DESC", nativeQuery = true)
    List<TransferTempNumbersEntity> listing(String lotteryType, String drawCode);

}