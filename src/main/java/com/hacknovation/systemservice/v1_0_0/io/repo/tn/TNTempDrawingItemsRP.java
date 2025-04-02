package com.hacknovation.systemservice.v1_0_0.io.repo.tn;

import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TNTempDrawingItemsRP extends JpaRepository<TNTempDrawingItemsEntity, Long> {

    long countAllByDrawingIdAndColumnNumberAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber);
    long countAllByDrawingIdAndColumnNumberAndPostCodeNotInAndTwoDigitsIsNull(Integer drawingId, Integer columnNumber, List<String> postCode);

    @Query(value = "select * from tn_temp_drawing_items where drawing_id = :id order by id", nativeQuery = true)
    List<TNTempDrawingItemsEntity> getAllByDrawId(Long id);

    @Query(value = "select * from tn_temp_drawing_items where drawing_id = :id AND column_number = :columnNumber AND post_code IN :posts order by id", nativeQuery = true)
    List<TNTempDrawingItemsEntity> getAllByDrawIdAndColumnAndPostIn(Long id, Integer columnNumber, List<String> posts);


    @Query(value = "select * from tn_temp_drawing_items where drawing_id = :id AND column_number = :columnNumber AND post_code = :postCode order by id LIMIT 1", nativeQuery = true)
    TNTempDrawingItemsEntity getOneByDrawIdColumnAndPost(Long id, Integer columnNumber, String postCode);
}
