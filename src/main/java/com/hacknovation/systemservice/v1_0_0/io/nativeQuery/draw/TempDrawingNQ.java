package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sombath
 * create at 25/4/22 10:35 AM
 */

@Component
@NativeQueryFolder("drawing/temp")
public interface TempDrawingNQ extends NativeQuery {

    DrawingDTO vnOneTempDrawing(String drawCode);
    DrawingDTO vnTwoTempDrawing(String drawCode);
    DrawingDTO leapTempDrawing(String drawCode);
    DrawingDTO scTempDrawing(String drawCode);
    DrawingDTO thTempDrawing(String drawCode);
    DrawingDTO khTempDrawing(String drawCode);
    DrawingDTO tnTempDrawing(String drawCode);

    List<DrawingDTO> vnOneTempDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> vnTwoTempDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> leapTempDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> khTempDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> tnTempDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> scTempDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> thTempDrawingByDate(String filterByStartDate, String filterByEndDate);

}
