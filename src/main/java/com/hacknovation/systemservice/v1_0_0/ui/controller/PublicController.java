package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.schedule.InitialBalanceSchedule;
import com.hacknovation.systemservice.schedule.RemoveDataSchedule;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1.0.0/public")
@RequiredArgsConstructor
public class PublicController extends BaseController {

    private final InitialBalanceSchedule initialBalanceSchedule;
    private final RemoveDataSchedule removeDataSchedule;
    private final JwtToken jwtToken;

    @GetMapping("/initial-balance")
    public ResponseEntity<StructureRS> initialBalance() {
        initialBalanceSchedule.reCalculateInitialBalance();
        return response();
    }

    @GetMapping("/remove-data")
    public ResponseEntity<StructureRS> removeData() {
        removeDataSchedule.deleteData();
        return response();
    }

    @GetMapping("/extract-token")
    public Map<String, String> extractToken() {
        UserToken userToken = jwtToken.getUserToken();

        String id = userToken.getId().toString();
        String code = userToken.getUserCode();
        String role = userToken.getUserRole();

        Map<String, String> hasuraRespond = new HashMap<>(Map.of(
                "X-Hasura-User-Id", id,
                "X-Hasura-User-Code", code,
                "X-Hasura-Is-Owner", "true",
                "X-Hasura-Role", role
        ));

        if(userToken.getAgentCode() != null)
            hasuraRespond.put("X-Hasura-Agent-Code", userToken.getAgentCode());

        if (role.equalsIgnoreCase(UserConstant.AGENT))
            hasuraRespond.put("X-Hasura-Agent-Code", code);


        return hasuraRespond;

    }


}
