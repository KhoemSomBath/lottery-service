package com.hacknovation.systemservice.v1_0_0.service.hazelcast;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 29/12/2022
 * time: 14:46
 */
@Service
public interface HazelcastSV {

    StructureRS countOnline();

    void logoutAllUser();

}
