package com.hacknovation.systemservice.v1_0_0.service.account;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.account.UserDetailRS;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface AccountSV {

    StructureRS login(HttpHeaders httpHeaders, LoginRQ loginRQ);
    StructureRS logout(LogoutRQ loginRQ);
    StructureRS resetPassword(ResetRQ resetRQ);
    void lockedScreen();
    StructureRS unlockedScreen(CredentialRQ credentialRQ);
    void language(LanguageRQ languageRQ);
    void deactivate(CredentialRQ credentialRQ);
    StructureRS profile();

}
