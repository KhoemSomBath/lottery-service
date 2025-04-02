package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * author: kangto
 * createdAt: 09/06/2022
 * time: 13:54
 */
@Repository
public interface ConfigurationRP extends JpaRepository<ConfigurationEntity, Long> {

    @Query(value = "SELECT * FROM configuration WHERE CASE WHEN :type <> 'all' THEN type = :type ELSE TRUE END ORDER BY id", nativeQuery = true)
    List<ConfigurationEntity> findAllByType(String type);

    @Query(value = "SELECT * FROM configuration WHERE code = lower(:code) ORDER BY id DESC LIMIT 1", nativeQuery = true)
    ConfigurationEntity getByCode(String code);

    @Query(value = "SELECT * FROM configuration WHERE code IN (:codes)", nativeQuery = true)
    List<ConfigurationEntity> findAllByCodeIn(List<String> codes);

}
