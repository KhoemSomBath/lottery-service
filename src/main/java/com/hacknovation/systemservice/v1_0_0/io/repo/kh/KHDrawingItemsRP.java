package com.hacknovation.systemservice.v1_0_0.io.repo.kh;

import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface KHDrawingItemsRP extends JpaRepository<KHDrawingItemsEntity, Long> {

    long countAllByDrawingIdAndTwoDigitsIsNull(Integer drawingId);

    @Query(value = "select * from khr_drawing_items where drawing_id = :id order by id", nativeQuery = true)
    List<KHDrawingItemsEntity> getAllByDrawId(Long id);

}
