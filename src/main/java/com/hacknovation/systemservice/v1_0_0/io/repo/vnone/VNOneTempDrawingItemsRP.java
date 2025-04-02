package com.hacknovation.systemservice.v1_0_0.io.repo.vnone;

import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VNOneTempDrawingItemsRP extends JpaRepository<VNOneTempDrawingItemsEntity, Long> {

    long countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber);
    long countAllByDrawingIdAndColumnNumberAndPostCodeNotLikeAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber, String postCode);

    @Query(value = "select * from vnone_temp_drawing_items where drawing_id = :id order by id", nativeQuery = true)
    List<VNOneTempDrawingItemsEntity> getAllByDrawId(Long id);

    @Query(value = "select * from vnone_temp_drawing_items where drawing_id = :id AND column_number = :columnNumber AND post_code IN :posts order by id", nativeQuery = true)
    List<VNOneTempDrawingItemsEntity> getAllByDrawIdAndColumnAndPostIn(Long id, Integer columnNumber, List<String> posts);

    @Query(value = "select * from vnone_temp_drawing_items where drawing_id = :id AND column_number = :columnNumber AND post_code = :postCode order by id LIMIT 1", nativeQuery = true)
    VNOneTempDrawingItemsEntity getOneByDrawIdColumnAndPost(Long id, Integer columnNumber, String postCode);

}
