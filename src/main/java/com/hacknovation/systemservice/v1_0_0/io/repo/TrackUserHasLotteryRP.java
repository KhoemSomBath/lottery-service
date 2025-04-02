package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.TrackUserHasLotteryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 * author: kangto
 * createdAt: 29/03/2022
 * time: 11:45
 */
@Repository
public interface TrackUserHasLotteryRP extends JpaRepository<TrackUserHasLotteryEntity, Long> {


    @Query(value = "SELECT * FROM track_user_has_lottery WHERE lt = :lotteryType AND uc = :userCode ORDER BY id DESC LIMIT 1", nativeQuery = true)
    TrackUserHasLotteryEntity getLastUserHasLottery(String lotteryType, String userCode);

    @Query(value = "DELETE FROM track_user_has_lottery WHERE lt = :lotteryType AND issued_at > :issuedAt AND uc IN :userCodes", nativeQuery = true)
    @Transactional
    @Modifying
    void deleteDuplicateByDate(String lotteryType, Date issuedAt, List<String> userCodes);

    List<TrackUserHasLotteryEntity> getAllByIdIn(List<Long> trackIds);

}
