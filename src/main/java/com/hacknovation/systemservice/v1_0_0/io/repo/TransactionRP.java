package com.hacknovation.systemservice.v1_0_0.io.repo;


import com.hacknovation.systemservice.v1_0_0.io.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRP extends JpaRepository<TransactionEntity, Long> {

}
