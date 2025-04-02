package com.hacknovation.systemservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
@RequiredArgsConstructor
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServer extends ResourceServerConfigurerAdapter {

    private final OncePerRequest oncePerRequest;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(
                        "/api/v1.0.0/auth/**",
                        "/auth/oauth/token",
                        "/api/v1.0.0/public/**",
                        "/api/result/**",
                        "/swagger-ui/**",
                        "/v3/**"
                ).permitAll()
                .antMatchers("/**").authenticated();
        http.addFilterAfter(oncePerRequest, WebAsyncManagerIntegrationFilter.class);
    }

}