package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/*
 * author: kangto
 * createdAt: 10/01/2022
 * time: 11:36
 */
@Component
@NativeQueryFolder("drawing")
public interface DrawingNQ extends NativeQuery {

    @Modifying
    @Transactional
    void vnOneUpdateIsRecent(String drawCode, Boolean isRecent);

    @Modifying
    @Transactional
    void vnOneResetWinOrderItems(String drawCode);

    @Modifying
    @Transactional
    void vnOneResetIsCalTempOrder(Date drawAt);

    @Modifying
    @Transactional
    void vnTwoUpdateIsRecent(String drawCode, Boolean isRecent);

    @Modifying
    @Transactional
    void vnTwoResetWinOrderItems(String drawCode);

    @Modifying
    @Transactional
    void vnTwoResetIsCalTempOrder(Date drawAt);

    @Modifying
    @Transactional
    void tnUpdateIsRecent(String drawCode, Boolean isRecent);

    @Modifying
    @Transactional
    void tnResetWinOrderItems(String drawCode);

    @Modifying
    @Transactional
    void tnResetIsCalTempOrder(Date drawAt);

    @Modifying
    @Transactional
    void leapUpdateIsRecent(String drawCode, Boolean isRecent);

    @Modifying
    @Transactional
    void scUpdateIsRecent(String drawCode, Boolean isRecent);

    @Modifying
    @Transactional
    void khUpdateIsRecent(String drawCode, Boolean isRecent);

    @Modifying
    @Transactional
    void leapResetWinOrderItems(String drawCode);

    @Modifying
    @Transactional
    void khResetWinOrderItems(String drawCode);

    @Modifying
    @Transactional
    void leapResetIsCalTempOrder(Date drawAt);

    @Modifying
    @Transactional
    void khResetIsCalTempOrder(Date drawAt);

    @Modifying
    @Transactional
    void resetSummaryDaily(String lotteryType, Date drawAt);

    DrawingDTO vnOneDrawing(String drawCode);
    DrawingDTO vnTwoDrawing(String drawCode);
    DrawingDTO leapDrawing(String drawCode);
    DrawingDTO tnDrawing(String drawCode);
    DrawingDTO khDrawing(String drawCode);
    DrawingDTO scDrawing(String drawCode);
    DrawingDTO thDrawing(String drawCode);

    List<DrawingDTO> vnOneDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingItemsDTO> vnOneDrawingItems(List<BigInteger> drawingId);

    List<DrawingDTO> vnTwoDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> leapDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> scDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> khDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> tnDrawingByDate(String filterByStartDate, String filterByEndDate);
    List<DrawingDTO> thDrawingByDate(String filterByStartDate, String filterByEndDate);

}
