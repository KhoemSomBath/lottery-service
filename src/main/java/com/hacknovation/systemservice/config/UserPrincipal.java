package com.hacknovation.systemservice.config;

import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;

@Data
public class UserPrincipal implements UserDetails, Principal {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String username;

    private String phoneCode;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static UserPrincipal build(UserEntity user) {

        return new UserPrincipal(
                user.getId().intValue(),
                user.getUsername(),
                user.getPassword()
        );

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
