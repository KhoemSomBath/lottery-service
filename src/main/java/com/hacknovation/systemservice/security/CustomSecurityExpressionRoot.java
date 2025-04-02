package com.hacknovation.systemservice.security;

import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customSecurityExpressionRoot")
@Slf4j
@RequiredArgsConstructor
public class CustomSecurityExpressionRoot {

    private final JwtToken jwtToken;

    public boolean can(String permission) {
        if (UserConstant.SUPER_ADMIN.equalsIgnoreCase(jwtToken.getUserToken().getUserRole()))
            return true;
        List<String> permissions = jwtToken.getUserToken().getPermissions();
        return permissions.contains(permission);
    }

    public boolean canRole(String roleCode) {
        if (UserConstant.SUPER_ADMIN.equalsIgnoreCase(jwtToken.getUserToken().getUserRole()))
            return true;
        return roleCode.equals(jwtToken.getUserToken().getUserRole());
    }

    public boolean canContains(String permission) {
        if (UserConstant.SUPER_ADMIN.equalsIgnoreCase(jwtToken.getUserToken().getUserRole()))
            return true;
        List<String> permissions = jwtToken.getUserToken().getPermissions();
        for (String per : permissions) {
            if (per.contains(permission))
                return true;
        }

        return false;
    }

}
