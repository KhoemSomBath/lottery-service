package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.RebatesEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.RebateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RebatesRP extends JpaRepository<RebatesEntity, Long> {

    @Query(value = "SELECT * FROM rebates r WHERE CASE WHEN :lotteryType <> 'ALL' THEN r.type = :lotteryType AND r.status = 1 ELSE r.status = 1 END ORDER BY r.sort_order ASC ", nativeQuery = true)
    List<RebatesEntity> listing(String lotteryType);

    @Query(value = "select u.code,  " +
            "       truncate(2duhl.rebate_rate, 0) rebateRate2D,  " +
            "       truncate(3duhl.rebate_rate, 0) rebateRate3D,  " +
            "       truncate(4duhl.rebate_rate, 0) rebateRate4D  " +
            "from users u  " +
            "         inner join user_has_lotteries 2duhl on u.code = 2duhl.uc and 2duhl.rc = '2d'  " +
            "         inner join user_has_lotteries 3duhl on u.code = 3duhl.uc and 3duhl.rc = '3d'  " +
            "         inner join user_has_lotteries 4duhl on u.code = 4duhl.uc and 4duhl.rc = '4d'  " +
            "where u.role_code = :filterByLevel  " +
            "  and if(:userType <> 'system', case  " +
            "                                    when :filterByLevel = 'super-senior' then u.code = :userCode  " +
            "                                    when :filterByLevel = 'senior' then u.super_senior_code = :userCode  " +
            "                                    when :filterByLevel = 'master' then case  " +
            "                                                                            when :roleCode = 'super-senior'  " +
            "                                                                                then u.super_senior_code = :userCode  " +
            "                                                                            when :roleCode = 'senior'  " +
            "                                                                                then u.senior_code = :userCode  " +
            "                                                                            else u.code = :userCode  " +
            "                                        end  " +
            "                                    when :filterByLevel = 'agent' then  " +
            "                                        case  " +
            "                                            when :roleCode = 'super-senior'  " +
            "                                                then u.super_senior_code = :userCode  " +
            "                                            when :roleCode = 'senior'  " +
            "                                                then u.senior_code = :userCode  " +
            "                                            when :roleCode = 'master'  " +
            "                                                then u.master_code = :userCode  " +
            "                                            else u.code = :userCode  " +
            "                                            end  " +
            "                                    when :filterByLevel = 'member' then case  " +
            "                                                                            when :roleCode = 'super-senior'  " +
            "                                                                                then u.super_senior_code = :userCode  " +
            "                                                                            when :roleCode = 'senior'  " +
            "                                                                                then u.senior_code = :userCode  " +
            "                                                                            when :roleCode = 'master'  " +
            "                                                                                then u.master_code = :userCode  " +
            "                                                                            when :roleCode = 'agent'  " +
            "                                                                                then u.agent_code = :userCode  " +
            "                                                                            else u.code = :userCode  " +
            "                                        end  " +
            "    end, true)  " +
            "  and 3duhl.lottery_code = :lotteryType  " +
            "  and 2duhl.lottery_code = :lotteryType  " +
            "  and IF(:lotteryType = 'LEAP', 4duhl.lottery_code = :lotteryType and 4duhl.rebate_rate > 0, TRUE)"+
            "  and 2duhl.rebate_rate > 0  " +
            "  and 3duhl.rebate_rate > 0 " +
            " group by 2duhl.rebate_rate, 3duhl.rebate_rate, 4duhl.rebate_rate;", nativeQuery = true)
    List<RebateDTO> getRebate(String lotteryType, String filterByLevel, String userType, String userCode, String roleCode);

}
