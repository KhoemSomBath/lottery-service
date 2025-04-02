package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.result;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.result.ResultTO;
import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sombath
 * create at 26/4/22 11:26 AM
 */
@NativeQueryFolder("result/temp")
@Component
public interface ResultTempNQ extends NativeQuery {

    List<DrawingItemsDTO> getVNOneTempResult(Integer drawingId);

    List<DrawingItemsDTO> getVNTwoTempResult(Integer drawingId);

    List<DrawingItemsDTO> getTNResult(Integer drawingId);

    List<DrawingItemsDTO> getLeapTempResult(Integer drawingId);

    List<DrawingItemsDTO> getSCTempResult(Integer drawingId);

    List<DrawingItemsDTO> getTHTempResult(Integer drawingId);

    List<DrawingItemsDTO> getKhTempResult(Integer drawingId);

    // result probability

    List<ResultTO> getResultProbability(Long drawId, boolean isNight);
}
