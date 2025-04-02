package com.hacknovation.systemservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Sombath
 * create at 27/11/21 2:11 PM
 */

@Component
@RequiredArgsConstructor
public class OncePerRequest extends OncePerRequestFilter {

    private final JwtToken jwtToken;
    private final UserRP userRP;
    List<String> loginRoute = List.of("/api/v1.0.0/auth/login", "/oauth/token", "/oauth/token", "/api/v1.0.0/auth/check-health");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getHeader("Authorization") != null) {

            if (!loginRoute.contains(request.getServletPath())) {
                try {
                    String authorization = request.getHeader("Authorization");

                    String username = jwtToken.getClaim("username", String.class, authorization);

                    UserEntity userEntity = userRP.getUserByUsername(username);

//                    if (userEntity.getIsLockedBetting() || "DEACTIVATE".equals(userEntity.getStatus().toString()))
//                        accountLocked(response);

                    Date issued = jwtToken.getClaim("issued", Date.class, authorization);

                    if (issued != null && userEntity.getLastLoginWeb() != null) {
                        long time = userEntity.getLastLoginWeb().getTime() - issued.getTime();
                        if (time > 1000) {
                            unauthorized(response);
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    unauthorized(response);
                }
            }
        }
        filterChain.doFilter(request, response);
        response.setHeader("Access-Control-Allow-Origin", "*");
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("Access-Control-Allow-Origin", "*");
        new ObjectMapper().writeValue(response.getOutputStream(), new StructureRS(HttpStatus.UNAUTHORIZED, MessageConstant.UNAUTHORIZED, null));
    }

    private void accountLocked(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("Access-Control-Allow-Origin", "*");
        new ObjectMapper().writeValue(response.getOutputStream(), new StructureRS(HttpStatus.BAD_REQUEST, MessageConstant.LOCK_ACCOUNT, MessageConstant.LOCK_ACCOUNT_KEY, null));
    }
}
