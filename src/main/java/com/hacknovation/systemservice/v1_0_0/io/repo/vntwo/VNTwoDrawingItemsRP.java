package com.hacknovation.systemservice.v1_0_0.io.repo.vntwo;

import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VNTwoDrawingItemsRP extends JpaRepository<VNTwoDrawingItemsEntity, Long> {

    long countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber);
    long countAllByDrawingIdAndColumnNumberAndPostCodeNotLikeAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber, String postCode);

    @Query(value = "select * from vntwo_drawing_items where drawing_id = :id order by id", nativeQuery = true)
    List<VNTwoDrawingItemsEntity> getAllByDrawId(Long id);
}
