package com.hacknovation.systemservice.v1_0_0.io.repo.sc;

import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Sombath
 * create at 25/3/23 4:43 AM
 */
public interface SCTempDrawingItemRP extends JpaRepository<SCTempDrawingItemsEntity, Long> {

    @Query("select s from SCTempDrawingItemsEntity s where s.drawingId = :drawingId")
    List<SCTempDrawingItemsEntity> getAllByDrawingId(@Param("drawingId") Integer drawingId);

    long countByDrawingIdAndTwoDigitsNull(Integer drawingId);


}
