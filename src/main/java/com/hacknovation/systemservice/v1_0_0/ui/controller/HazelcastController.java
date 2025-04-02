package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.hazelcast.HazelcastSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * author: kangto
 * createdAt: 29/12/2022
 * time: 14:43
 */
@RestController
@RequestMapping(value = "/api/v1.0.0")
@RequiredArgsConstructor
public class HazelcastController extends BaseController {

    private final HazelcastSV hazelcastSV;

    @GetMapping("/hazelcast/count")
    public ResponseEntity<StructureRS> getCountOnline() {
        return response(hazelcastSV.countOnline());
    }

    @PostMapping("/hazelcast/logout")
    public ResponseEntity<StructureRS> logoutAllUser() {
        hazelcastSV.logoutAllUser();
        return response();
    }


}
