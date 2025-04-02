package com.hacknovation.systemservice.v1_0_0.ui.controller.swagger;

import com.hacknovation.systemservice.v1_0_0.service.account.AccountSV;
import com.hacknovation.systemservice.v1_0_0.ui.controller.BaseController;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.LoginRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1.0.0/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class AuthController extends BaseController {

    private final AccountSV accountSV;

    @PostMapping("/login")
    public ResponseEntity<StructureRS> login(@RequestHeader HttpHeaders httpHeaders, @Validated @RequestBody LoginRQ loginRQ) {
        return response(accountSV.login(httpHeaders, loginRQ));
    }

    @GetMapping("/check-health")
    public String checkHealth() {
        return "Okay";
    }

}
