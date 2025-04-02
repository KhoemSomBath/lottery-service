package com.hacknovation.systemservice.security;

import com.hacknovation.systemservice.config.UserPrincipal;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {

    private final UserRP userRP;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {

        UserEntity user = userRP.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid Username or password");
        }

        return UserPrincipal.build(user);

    }

}
