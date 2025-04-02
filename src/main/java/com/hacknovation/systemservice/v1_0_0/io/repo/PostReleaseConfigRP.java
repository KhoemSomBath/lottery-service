package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.PostReleaseConfigEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.PostReleaseConfigTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * author: kangto
 * createdAt: 30/08/2022
 * time: 11:09
 */
@Repository
public interface PostReleaseConfigRP extends JpaRepository<PostReleaseConfigEntity, Long> {

    @Query(value = "SELECT p.lt lotteryType, " +
            "p.pc postCode, " +
            "p.is_can_release isCanRelease, " +
            "p.updated_by updatedBy " +
            "FROM post_release_config p " +
            "WHERE CASE WHEN :lotteryType <> 'ALL' THEN lt = :lotteryType ELSE TRUE END", nativeQuery = true)
    List<PostReleaseConfigTO> findAllByLotteryType(String lotteryType);


    @Query(value = "SELECT * FROM post_release_config WHERE lt = :lotteryType AND pc = :postCode ORDER BY id LIMIT 1", nativeQuery = true)
    PostReleaseConfigEntity findByLotteryTypeAndPostCode(String lotteryType, String postCode);

    @Query(value = "SELECT * FROM post_release_config WHERE lt = :lotteryType AND pc in :postCode", nativeQuery = true)
    List<PostReleaseConfigEntity> findByLotteryTypeAndPostCodeIn(String lotteryType, List<String> postCode);

}
