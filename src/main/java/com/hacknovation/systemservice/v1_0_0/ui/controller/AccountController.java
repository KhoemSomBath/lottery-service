package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.account.AccountSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.CredentialRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.LanguageRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.LogoutRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.ResetRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/v1.0.0/account")
@RequiredArgsConstructor
public class AccountController extends BaseController {

    private final AccountSV accountSV;

    @GetMapping("/profile")
    public ResponseEntity<StructureRS> profile() {
        return response(accountSV.profile());
    }

    @PostMapping("/logout")
    public ResponseEntity<StructureRS> logout(@Validated @RequestBody LogoutRQ loginRQ)
    {
        return response(accountSV.logout(loginRQ));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<StructureRS> resetPassword(@Valid @RequestBody ResetRQ resetRQ) {
        return response(accountSV.resetPassword(resetRQ));
    }

    @PostMapping("/locked-screen")
    public ResponseEntity<StructureRS> lockedScreen()
    {
        accountSV.lockedScreen();
        return response();
    }

    @PostMapping("/unlocked-screen")
    public ResponseEntity<StructureRS> unlockedScreen(@Valid @RequestBody CredentialRQ credentialRQ)
    {
        return response(accountSV.unlockedScreen(credentialRQ));
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<StructureRS> deactivate(@Valid @RequestBody CredentialRQ credentialRQ) {
        accountSV.deactivate(credentialRQ);
        return response();
    }

    @PostMapping("/language")
    public ResponseEntity<StructureRS> language(@Valid @RequestBody LanguageRQ languageRQ)
    {
        accountSV.language(languageRQ);
        return response();
    }

}
