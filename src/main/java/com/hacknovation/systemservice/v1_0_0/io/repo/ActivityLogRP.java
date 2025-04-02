package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.ActivityLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * author: kangto
 * createdAt: 28/12/2021
 * time: 12:09
 */
@Repository
public interface ActivityLogRP extends JpaRepository<ActivityLogEntity, Long> {

    @Query(nativeQuery = true,
            value = "select l.* " +
                    "from activity_logs l " +
                    "         left join users u on l.uc = u.code " +
                    "         left join users loggedUser on loggedUser.code = :loggedInUserCode " +
                    "where if(:lotteryType is not null, l.lottery_type = :lotteryType, true) " +
                    "  and if(:username is not null, u.username like CONCAT('%', :username, '%'), true) " +
                    "  and if(:module is not null, l.module_name = :module, true) " +
                    "  and if(loggedUser.user_type <> 'system', " +
                    "         case " +
                    "             when loggedUser.role_code = 'super-senior' then " +
                    "                 u.super_senior_code = loggedUser.code " +
                    "             when loggedUser.role_code = 'senior' then " +
                    "                 u.senior_code = loggedUser.code " +
                    "             when loggedUser.role_code = 'master' then " +
                    "                 u.master_code = loggedUser.code " +
                    "             when loggedUser.role_code = 'agent' then " +
                    "                 u.agent_code = loggedUser.code " +
                    "             else " +
                    "                 u.code = loggedUser.code " +
                    "             end " +
                    "    , true) " +
                    "group by l.id order by l.id desc ")
    List<ActivityLogEntity> getLog(String lotteryType, String module, String username, String loggedInUserCode);

    @Query(nativeQuery = true,
    value = "select * from activity_logs where module_name is not null group by module_name")
    List<ActivityLogEntity> getLogByModule();
}
