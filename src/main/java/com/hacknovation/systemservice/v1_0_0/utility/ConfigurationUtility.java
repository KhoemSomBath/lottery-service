package com.hacknovation.systemservice.v1_0_0.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.ConfigurationEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.ConfigurationRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration.UploadAPKRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.configuration.ConfigurationItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.configuration.ConfigurationRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConfigurationUtility {
    private final ConfigurationRP configurationRP;
    private final ActivityLogUtility activityLogUtility;
    private final ObjectMapper objectMapper;
    private final JwtToken jwtToken;

    public List<ConfigurationEntity> getConfigurationByType(String type) {
        return configurationRP.findAllByType(type);
    }

    /**
     * map data to configurationRS list
     * @param configurationEntities List<ConfigurationEntity>
     * @return List<ConfigurationRS>
     */
    public List<ConfigurationRS> getConfigurationRSList(List<ConfigurationEntity> configurationEntities) {
        List<ConfigurationRS> configurationRSList = new ArrayList<>();
        Map<String, List<ConfigurationEntity>> mapConfigurationByType = configurationEntities.stream().collect(Collectors.groupingBy(ConfigurationEntity::getType));
        mapConfigurationByType.forEach((type, items) -> {
            ConfigurationRS configurationRS = new ConfigurationRS();
            configurationRS.setType(type);
            items.forEach(it-> {
                ConfigurationItemRS item = new ConfigurationItemRS();
                BeanUtils.copyProperties(it, item);
                configurationRS.getItems().add(item);
            });
            configurationRSList.add(configurationRS);
        });

        return configurationRSList;
    }

    public void autoUpdateConfigAndroid(UploadAPKRQ uploadAPKRQ, Map<String, String> mapApkLink) {

        String appVersionKey = "android_app_version";
        String appLinkKey = "android_app_link";
        List<ConfigurationEntity> configurationEntities = configurationRP.findAllByCodeIn(List.of(appVersionKey, appLinkKey,"is_force_update"));
        Map<String, ConfigurationEntity> configurationEntityMapByCode = configurationEntities.stream().collect(Collectors.toMap(ConfigurationEntity::getCode, Function.identity()));

        ConfigurationEntity appVersion = configurationEntityMapByCode.get(appVersionKey);
        appVersion.setValue(uploadAPKRQ.getVersion());

        ConfigurationEntity appLink = configurationEntityMapByCode.get(appLinkKey);
        appLink.setValue(stringJson(mapApkLink));

        ConfigurationEntity forceUpdate = configurationEntityMapByCode.get("is_force_update");
        forceUpdate.setValue(uploadAPKRQ.getIsForce().toString());

        configurationRP.saveAll(List.of(appVersion, appLink, forceUpdate));

        UserToken userToken = jwtToken.getUserToken();
        activityLogUtility.addActivityLog("", ActivityLogConstant.MODULE_SETTING, userToken.getUserType(), ActivityLogConstant.ACTION_UPDATE, userToken.getUserCode(), List.of(appVersion, appLink));
    }

    public String getSystemVersion() {
        ConfigurationEntity configurationEntity = configurationRP.getByCode("system_version");
        if (configurationEntity != null) {
            return configurationEntity.getValue();
        }
        return null;
    }

    private String stringJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException exception) {
            System.out.println("SettingIP.stringJson JsonProcessingException");
            exception.printStackTrace();
            return null;
        }
    }

}
