package com.hacknovation.systemservice.v1_0_0.service.setting;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration.SettingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration.UploadAPKRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface SettingSV {
    StructureRS lists(String type);
    StructureRS update(SettingRQ settingRQ);
    StructureRS uploadApk(UploadAPKRQ uploadAPKRQ, MultipartFile apkFile,MultipartFile apkHMFile);
}
