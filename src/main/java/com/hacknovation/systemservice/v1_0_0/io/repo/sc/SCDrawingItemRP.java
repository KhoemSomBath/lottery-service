package com.hacknovation.systemservice.v1_0_0.io.repo.sc;

import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Sombath
 * create at 25/3/23 4:43 AM
 */
public interface SCDrawingItemRP extends JpaRepository<SCDrawingItemsEntity, Long> {

    @Query("select s from SCDrawingItemsEntity s where s.drawingId = :drawingId")
    List<SCDrawingItemsEntity> getAllByDrawingId(@Param("drawingId") Integer drawingId);

    long countByDrawingIdAndTwoDigitsNull(Integer drawingId);


}
