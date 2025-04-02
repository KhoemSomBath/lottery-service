package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.result;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * author: kangto
 * createdAt: 21/01/2022
 * time: 10:29
 */
@NativeQueryFolder("result")
@Component
public interface ResultNQ extends NativeQuery {

    List<DrawingItemsDTO> getVNOneResult(Integer drawingId);

    List<DrawingItemsDTO> getVNTwoResult(Integer drawingId);

    List<DrawingItemsDTO> getTNResult(Integer drawingId);

    List<DrawingItemsDTO> getLeapResult(Integer drawingId);

    List<DrawingItemsDTO> getKhResult(Integer drawingId);

    List<DrawingItemsDTO> getSCResult(Integer drawingId);

    List<DrawingItemsDTO> getTHResult(Integer drawingId);

}
