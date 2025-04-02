package com.hacknovation.systemservice.security;

import com.hacknovation.systemservice.config.UserPrincipal;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.PermissionEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.PermissionRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class CustomJWTEnhancer implements TokenEnhancer  {

    private final UserRP userRP;
    private final PermissionRP permissionRP;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Map<String, Object> additionalInfo = new HashMap<>();

        var userPrincipal = (UserPrincipal) authentication.getPrincipal();

        UserEntity userEntity = userRP.getUserByUsername(userPrincipal.getUsername());
        UserEntity parentEntity = new UserEntity();
        parentEntity.setRoleCode(userEntity.getRoleCode());
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userEntity.getRoleCode()))
            parentEntity = userRP.getUserByParentId(userEntity.getParentId());
        List<PermissionEntity> permissionEntities = permissionRP.permissions(parentEntity.getRoleCode());
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userEntity.getRoleCode()))
            removePermissionFromSubAccount(permissionEntities);

        List<String> permissions = permissionEntities.stream().map(PermissionEntity::getName).collect(Collectors.toList());

        additionalInfo.put("id", userEntity.getId());
        additionalInfo.put("username", userEntity.getUsername());
        additionalInfo.put("nickname", userEntity.getNickname());
        additionalInfo.put("userCode",userEntity.getCode());
        additionalInfo.put("userRole",userEntity.getRoleCode());
        additionalInfo.put("currency", userEntity.getCurrencyCode());
        additionalInfo.put("userType", userEntity.getUserType());
        additionalInfo.put("seniorCode", userEntity.getSeniorCode());
        additionalInfo.put("masterCode", userEntity.getMasterCode());
        additionalInfo.put("agentCode", userEntity.getAgentCode());
        additionalInfo.put("parentId", userEntity.getParentId());
        if (parentEntity.getCode() != null) {
            additionalInfo.put("parentUsername", parentEntity.getUsername());
            additionalInfo.put("parentCode", parentEntity.getCode());
            additionalInfo.put("parentRole", parentEntity.getRoleCode());
            additionalInfo.put("parentNickname", parentEntity.getNickname());
        }
        additionalInfo.put("permissions", permissions);
        additionalInfo.put("issued", new Date());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }


    /**
     * remove permissions from sub account role
     * @param permissionEntities List<PermissionEntity>
     */
    private void removePermissionFromSubAccount(List<PermissionEntity> permissionEntities) {
        List<String> removePermissions = List.of(
                "list-initial-balance",
                "list-transfer-number",
                "edit-senior",
                "edit-master",
                "edit-agent",
                "edit-member",
                "create-sub-account",
                "create-senior",
                "create-master",
                "create-agent",
                "create-member"
        );
        permissionEntities.removeIf(item -> removePermissions.contains(item.getName()));
    }

}
