package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.TransferTempNumberItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransferTempNumberItemsRP extends JpaRepository<TransferTempNumberItemsEntity, Long> {

    @Query(value = "SELECT * FROM transfer_temp_number_items ttpi WHERE  ttpi.tn_id = :tnId ORDER BY number DESC", nativeQuery = true)
    List<TransferTempNumberItemsEntity> items(Integer tnId);

}
