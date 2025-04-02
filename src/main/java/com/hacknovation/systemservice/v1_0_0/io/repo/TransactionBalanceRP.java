package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.TransactionBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionBalanceRP extends JpaRepository<TransactionBalanceEntity, Long> {

    @Query(value = "SELECT * FROM transaction_balance tb WHERE tb.user_code = ?1", nativeQuery = true)
    TransactionBalanceEntity userBalanceByCode(String userCode);

}
