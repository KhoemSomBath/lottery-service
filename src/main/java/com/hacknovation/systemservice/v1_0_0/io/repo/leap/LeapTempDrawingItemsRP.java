package com.hacknovation.systemservice.v1_0_0.io.repo.leap;

import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LeapTempDrawingItemsRP extends JpaRepository<LeapTempDrawingItemsEntity, Long> {

    long countAllByDrawingIdAndTwoDigitsIsNull(Integer drawingId);

    @Query(value = "select * from leap_temp_drawing_items where drawing_id = :id order by id", nativeQuery = true)
    List<LeapTempDrawingItemsEntity> getAllByDrawId(Long id);

}
