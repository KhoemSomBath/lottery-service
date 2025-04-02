package com.hacknovation.systemservice.v1_0_0.io.repo.th;

import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kangto
 * create at 5/5/23 2:43 PM
 */
public interface THTempDrawingRP extends JpaRepository<THTempDrawingEntity, Long> {
    THTempDrawingEntity findByCode(String code);


    @Query(value = "SELECT * FROM th_temp_drawing where date(resulted_post_at) = :date", nativeQuery = true)
    Page<THTempDrawingEntity> getDrawByDate(String date, Pageable pageable);

    Boolean existsByCode(String code);

    @Query(value = "SELECT * FROM th_temp_drawing  ORDER BY id DESC LIMIT 1", nativeQuery = true)
    THTempDrawingEntity lastDraw();

    @Query(value = "SELECT * FROM th_temp_drawing WHERE `is_recent` = 1 ORDER BY id ASC LIMIT 1", nativeQuery = true)
    THTempDrawingEntity recentDrawing();

    @Query(value = "SELECT * FROM th_drawing WHERE `code` = :drawCode LIMIT 1", nativeQuery = true)
    THTempDrawingEntity getOriginalDraw(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE th_temp_drawing SET is_recent = :isRecent, is_set_win = 0 WHERE code = :drawCode", nativeQuery = true)
    void updateIsRecent(String drawCode, boolean isRecent);

    @Transactional
    @Modifying
    @Query(value = "UPDATE th_temp_drawing SET is_set_win = 1 WHERE code = :drawCode", nativeQuery = true)
    void updateSetWinTrue(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE th_temp_orders SET is_cal_reward = 0 WHERE date(draw_at) = date(:drawAt)", nativeQuery = true)
    void resetIsCalTempOrder(String drawAt);

    @Transactional
    @Modifying
    @Query(value = "UPDATE th_temp_drawing SET `status` = 'AWARDED' WHERE `code` = :drawCode ", nativeQuery = true)
    void updateDrawAwarded(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM th_drawing WHERE code = :drawCode", nativeQuery = true)
    void resetDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM th_drawing_items WHERE drawing_id = (SELECT id FROM th_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void resetDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO th_drawing SELECT * FROM th_temp_drawing WHERE code = :drawCode", nativeQuery = true)
    void saveDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO th_drawing_items SELECT * FROM th_temp_drawing_items WHERE drawing_id = (SELECT id FROM th_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void saveDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM th_temp_win_order_items WHERE dc = :drawCode", nativeQuery = true)
    void resetWinOrderItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `th_win_order_items` SELECT * FROM th_temp_win_order_items WHERE dc = :drawCode", nativeQuery = true)
    void saveWinOrderItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM th_win_order_items WHERE dc = :drawCode", nativeQuery = true)
    void resetOriginalWinOrderItems(String drawCode);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM th_orders WHERE dc = :drawCode", nativeQuery = true)
    void resetOrder(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM th_order_items WHERE dc = :drawCode", nativeQuery = true)
    void resetOrderItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `th_orders` SELECT * FROM `th_temp_orders` WHERE dc = :drawCode", nativeQuery = true)
    void saveOrder(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `th_order_items` SELECT * FROM `th_temp_order_items` WHERE dc = :drawCode", nativeQuery = true)
    void saveOrderItems(String drawCode);

}
