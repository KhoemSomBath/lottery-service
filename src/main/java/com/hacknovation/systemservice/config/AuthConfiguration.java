package com.hacknovation.systemservice.config;

import com.hacknovation.systemservice.exception.httpstatus.UnauthorizedException;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.LoginRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.account.TokenRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserAuth;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthConfiguration {

    private final HttpServletRequest request;
    private final Environment environment;
    private final UserAuth userAuth;
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Create basic auth
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param loginRQ
     * @param credential
     * @return
     */
    public TokenRS getCredential(LoginRQ loginRQ, String credential)
    {
        String baseUrl = "http://localhost:" + environment.getProperty("server.port");
        RestTemplate restAPi = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Authorization", credential);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", loginRQ.getUsername());
        body.add("password", loginRQ.getPassword());
        body.add("grant_type", "password");
        HttpEntity<MultiValueMap<String, String>> payload = new HttpEntity<>(body, headers);
        return restAPi.exchange(baseUrl + "/oauth/token", HttpMethod.POST, payload, TokenRS.class).getBody();
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Create basic auth
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @Param httpHeaders
     * @Return String
     */
    public String oauth2Credential(HttpHeaders httpHeaders)
    {
        return httpHeaders.getFirst("Authorization");
    }


    public String basicToken() {
        String credentials = environment.getProperty("spring.security.OAUTH2.CLIENT_ID") + ":" + environment.getProperty("spring.security.OAUTH2.CLIENT_SECRET");
        return  "Basic " + new String(Base64.encodeBase64(credentials.getBytes()));
    }

}
