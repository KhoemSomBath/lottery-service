package com.hacknovation.systemservice.v1_0_0.utility.auth;

import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtToken {

    private final HttpServletRequest request;
    private final GenericConversionService genericConversionService;

    private  <T> T getClaim(Map<String, ?> tokenData, String claimKey, Class<T> type) {
        try {
            return genericConversionService.convert(tokenData.get(claimKey), type);
        } catch (Exception ex) {
            return null;
        }
    }

    private Map<String, ?> parseToken() {
        try {
            JsonParser parser = JsonParserFactory.getJsonParser();
            String authorization = request.getHeader("Authorization");
            return parser.parseMap(JwtHelper.decode(authorization.split(" ")[1]).getClaims());
        } catch (Exception ex) {
            return null;
        }
    }

    private Map<String, ?> parseToken(String token) {
        try {
            JsonParser parser = JsonParserFactory.getJsonParser();
            String authorization = request.getHeader("Authorization");
            return parser.parseMap(JwtHelper.decode(authorization.split(" ")[1]).getClaims());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public <T> T getClaim(String claimKey, Class<T> type, String authorization) {
        try {
            JsonParser parser = JsonParserFactory.getJsonParser();
            Map<String, ?> tokenData = parser.parseMap(JwtHelper.decode(authorization.split(" ")[1]).getClaims());
            return genericConversionService.convert(tokenData.get(claimKey), type);
        } catch (Exception ex) {
            return null;
        }
    }

    public UserToken getUserToken() {

        // avoiding parse token many time
        Map<String, ?> tokenData = this.parseToken();

        UserToken userToken = new UserToken();
        userToken.setId(this.getClaim(tokenData, "id", Long.class));
        userToken.setUsername(this.getClaim(tokenData,"username", String.class));
        userToken.setUserCode(this.getClaim(tokenData,"userCode", String.class));
        userToken.setUserRole(this.getClaim(tokenData,"userRole", String.class));
        userToken.setUserType(this.getClaim(tokenData,"userType", String.class));
        userToken.setParentId(this.getClaim(tokenData,"parentId", Integer.class));
        userToken.setParentCode(this.getClaim(tokenData,"parentCode", String.class));
        userToken.setParentRole(this.getClaim(tokenData,"parentRole", String.class));
        userToken.setSuperSeniorCode(this.getClaim(tokenData,"superSeniorCode", String.class));
        userToken.setSeniorCode(this.getClaim(tokenData,"seniorCode", String.class));
        userToken.setMasterCode(this.getClaim(tokenData,"masterCode", String.class));
        userToken.setAgentCode(this.getClaim(tokenData,"agentCode", String.class));
        userToken.setNickName(this.getClaim(tokenData,"nickname", String.class));
        String _permissions = this.getClaim(tokenData,"permissions", String.class);
        if (_permissions != null) {
            userToken.setPermissions(Stream.of(_permissions.split(",")) .collect(Collectors.toList()));
        }

        return userToken;
    }

}