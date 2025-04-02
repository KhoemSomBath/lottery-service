package com.hacknovation.systemservice.v1_0_0.io.repo.kh;

import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface KHTempDrawingItemsRP extends JpaRepository<KHTempDrawingItemsEntity, Long> {

    long countAllByDrawingIdAndTwoDigitsIsNull(Integer drawingId);

    @Query(value = "select * from khr_temp_drawing_items where drawing_id = :id order by id", nativeQuery = true)
    List<KHTempDrawingItemsEntity> getAllByDrawId(Long id);

}
