package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.ResetPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResetPasswordRP extends JpaRepository<ResetPasswordEntity, Long> {

    @Query(value = "SELECT * FROM reset_passwords WHERE username = ?1 AND device_id = ?2 AND verified = false", nativeQuery = true)
    ResetPasswordEntity checkUsernameByDevice(String username, String deviceId);

    @Query(value = "SELECT * FROM reset_passwords WHERE username = ?1 AND device_id = ?2 AND verified = false", nativeQuery = true)
    List<ResetPasswordEntity> getUsernameByDevice(String username, String deviceId);

}
