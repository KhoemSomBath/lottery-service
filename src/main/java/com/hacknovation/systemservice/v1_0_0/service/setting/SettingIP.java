package com.hacknovation.systemservice.v1_0_0.service.setting;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.ConfigurationEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.ConfigurationRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration.SettingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration.UploadAPKRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.configuration.ConfigurationRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.ConfigurationUtility;
import com.hacknovation.systemservice.v1_0_0.utility.UploadFileUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SettingIP extends BaseServiceIP implements SettingSV {

    private final ConfigurationRP configurationRP;
    private final ConfigurationUtility configurationUtility;
    private final ActivityLogUtility activityLogUtility;
    private final UploadFileUtility uploadFileUtility;
    private final JwtToken jwtToken;

    @Override
    public StructureRS lists(String type) {
        List<ConfigurationEntity> configurationEntities = configurationUtility.getConfigurationByType(type);
        List<ConfigurationRS> configurationRSList = configurationUtility.getConfigurationRSList(configurationEntities);

        return responseBodyWithSuccessMessage(configurationRSList);
    }

    @Override
    public StructureRS update(SettingRQ settingRQ) {
        ConfigurationEntity configurationEntity = configurationRP.getByCode(settingRQ.getCode());
        if (configurationEntity == null)
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.CONFIG_NOT_FOUND, null);
        if (settingRQ.getValue() != null) {
            configurationEntity.setValue(settingRQ.getValue());
        }
        if (settingRQ.getName() != null) {
            configurationEntity.setName(settingRQ.getName());
        }
        if (settingRQ.getNote() != null) {
            configurationEntity.setNote(settingRQ.getNote());
        }

        configurationRP.save(configurationEntity);

        var userToken = jwtToken.getUserToken();
        activityLogUtility.addActivityLog("", ActivityLogConstant.MODULE_SETTING, userToken.getUserType(), ActivityLogConstant.ACTION_UPDATE, userToken.getUserCode(), configurationEntity);

        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS uploadApk(UploadAPKRQ uploadAPKRQ, MultipartFile apkFile, MultipartFile apkHMFile) {
        if (uploadAPKRQ.getVersion() == null) {
            return responseBodyWithBadRequest(MessageConstant.VERSION_REQUIRED, MessageConstant.VERSION_REQUIRED_KEY);
        }

        String pathAPK = uploadFileUtility.uploadFile(apkFile, LotteryConstant.APK_FOLDER_PATH);

        if (pathAPK == null) {
            return responseBodyWithBadRequest(MessageConstant.FAILED_UPLOAD, MessageConstant.FAILED_UPLOAD_KEY);
        }

        String pathHMAPK = uploadFileUtility.uploadFile(apkHMFile, LotteryConstant.APK_FOLDER_PATH);

        if (pathHMAPK == null) {
            return responseBodyWithBadRequest(MessageConstant.FAILED_UPLOAD, MessageConstant.FAILED_UPLOAD_KEY);
        }

        Map<String, String> mapApk = Map.of(
                "LMS168", pathAPK,
                "HM", pathHMAPK
        );

        configurationUtility.autoUpdateConfigAndroid(uploadAPKRQ, mapApk);

        return response(mapApk);
    }

}
