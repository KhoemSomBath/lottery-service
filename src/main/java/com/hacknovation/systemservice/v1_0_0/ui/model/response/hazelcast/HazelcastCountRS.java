package com.hacknovation.systemservice.v1_0_0.ui.model.response.hazelcast;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 29/12/2022
 * time: 14:59
 */
@Data
public class HazelcastCountRS {

    private Integer onlineMembers=0;
    private Integer subscribers=0;

}
