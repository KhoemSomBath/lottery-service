package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.PagingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagingRP extends JpaRepository<PagingEntity, Long> {
}