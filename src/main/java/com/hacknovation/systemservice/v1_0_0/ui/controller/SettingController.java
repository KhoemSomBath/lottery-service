package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.setting.SettingSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration.SettingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration.UploadAPKRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1.0.0/settings")
@RequiredArgsConstructor
public class SettingController extends BaseController {

    private final SettingSV settingSV;

    @PreAuthorize("@customSecurityExpressionRoot.can('list-setting')")
    @GetMapping
    public ResponseEntity<StructureRS> lists(@RequestParam(required = false, value = "all") String type) {
        return response(settingSV.lists(type));
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('update-setting')")
    @PutMapping
    public ResponseEntity<StructureRS> updateSetting(@Valid @RequestBody SettingRQ settingRQ) {
        return response(settingSV.update(settingRQ));
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('update-setting')")
    @PostMapping(value = "/upload-apk", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<StructureRS> uploadApk(@Valid @ModelAttribute UploadAPKRQ uploadAPKRQ,
                                                 @RequestParam(value = "apkFile") MultipartFile apkFile,
                                                 @RequestParam(value = "apkHMFile") MultipartFile apkHMFile) {
        return response(settingSV.uploadApk(uploadAPKRQ, apkFile, apkHMFile));
    }

}
