package com.hacknovation.systemservice.v1_0_0.io.repo.th;

import com.hacknovation.systemservice.v1_0_0.io.entity.th.THDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface THDrawingItemsRP extends JpaRepository<THDrawingItemsEntity, Long> {
    long countByDrawingIdAndTwoDigitsNull(int drawId);
}