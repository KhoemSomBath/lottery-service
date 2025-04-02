package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.UserDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sombath
 * create at 20/10/21 3:50 PM
 */
public interface UserDeviceRP extends JpaRepository<UserDeviceEntity, Long> {

    UserDeviceEntity findByDeviceTokenAndUserCode(String token, String userCode);

    UserDeviceEntity findByUserCode(String userCode);

    @Transactional
    void deleteAllByUserCodeAndDeviceToken(String userCode, String token);
}
