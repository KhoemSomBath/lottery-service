package com.hacknovation.systemservice.v1_0_0.io.repo.th;

import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface THTempDrawingItemsRP extends JpaRepository<THTempDrawingItemsEntity, Long> {

    List<THTempDrawingItemsEntity> findByDrawingId(Integer drawingId);

    @Transactional
    @Modifying
    void deleteAllByDrawingId(Integer id);

    Long countByDrawingIdAndTwoDigitsNull(Integer drawId);

}