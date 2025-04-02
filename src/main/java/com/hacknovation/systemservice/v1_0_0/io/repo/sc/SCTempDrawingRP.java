package com.hacknovation.systemservice.v1_0_0.io.repo.sc;

import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sombath
 * create at 25/3/23 4:43 AM
 */
public interface SCTempDrawingRP extends JpaRepository<SCTempDrawingEntity, Long> {
    SCTempDrawingEntity findByCode(String code);

    Boolean existsByCode(String code);

    @Query(value = "SELECT * FROM sc_temp_drawing  ORDER BY id DESC LIMIT 1", nativeQuery = true)
    SCTempDrawingEntity lastDraw();

    @Query(value = "SELECT * FROM sc_temp_drawing WHERE `is_recent` = 1 ORDER BY id ASC LIMIT 1", nativeQuery = true)
    SCTempDrawingEntity recentDrawing();

    @Query(value = "SELECT * FROM sc_drawing WHERE `code` = :drawCode LIMIT 1", nativeQuery = true)
    SCTempDrawingEntity getOriginalDraw(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE sc_temp_drawing SET is_recent = :isRecent, is_set_win = 0 WHERE code = :drawCode", nativeQuery = true)
    void updateIsRecent(String drawCode, boolean isRecent);

    @Transactional
    @Modifying
    @Query(value = "UPDATE sc_temp_orders SET is_cal_reward = 0 WHERE date(draw_at) = date(:drawAt)", nativeQuery = true)
    void resetIsCalTempOrder(String drawAt);

    @Transactional
    @Modifying
    @Query(value = "UPDATE sc_temp_drawing SET `status` = 'AWARDED' WHERE `code` = :drawCode ", nativeQuery = true)
    void updateDrawAwarded(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM sc_drawing WHERE code = :drawCode", nativeQuery = true)
    void resetDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM sc_drawing_items WHERE drawing_id = (SELECT id FROM sc_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void resetDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO sc_drawing SELECT * FROM sc_temp_drawing WHERE code = :drawCode", nativeQuery = true)
    void saveDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO sc_drawing_items SELECT * FROM sc_temp_drawing_items WHERE drawing_id = (SELECT id FROM sc_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void saveDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM sc_temp_win_order_items WHERE dc = :drawCode", nativeQuery = true)
    void resetWinOrderItems(String drawCode);
}
