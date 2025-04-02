package com.hacknovation.systemservice.v1_0_0.io.repo.vntwo;

import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VNTwoTempDrawingItemsRP extends JpaRepository<VNTwoTempDrawingItemsEntity, Long> {

    long countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber);
    long countAllByDrawingIdAndColumnNumberAndPostCodeNotLikeAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber, String postCode);

    @Query(value = "select * from vntwo_temp_drawing_items where drawing_id = :id order by id", nativeQuery = true)
    List<VNTwoTempDrawingItemsEntity> getAllByDrawId(Long id);

    @Query(value = "select * from vntwo_temp_drawing_items where drawing_id = :id AND column_number = :columnNumber order by id", nativeQuery = true)
    List<VNTwoTempDrawingItemsEntity> getAllByDrawIdAndColumnNumber(Long id, Integer columnNumber);
}
