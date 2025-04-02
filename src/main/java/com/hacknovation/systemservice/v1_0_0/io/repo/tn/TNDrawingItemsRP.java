package com.hacknovation.systemservice.v1_0_0.io.repo.tn;

import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TNDrawingItemsRP extends JpaRepository<TNDrawingItemsEntity, Long> {

    long countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber);
    long countAllByDrawingIdAndColumnNumberAndPostCodeNotInAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber, List<String> postCode);

    @Query(value = "select * from tn_drawing_items where drawing_id = :id order by id", nativeQuery = true)
    List<TNDrawingItemsEntity> getAllByDrawId(Long id);
}
