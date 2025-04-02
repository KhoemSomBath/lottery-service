package com.hacknovation.systemservice.v1_0_0.utility.auth;

import com.hacknovation.systemservice.exception.httpstatus.UnauthorizedException;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuth {

    @Autowired
    private UserRP userRP;

    public UserEntity userPrincipal()
    {

        Authentication userAuth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRP.getUserByUsername(userAuth.getName());

        if (user == null) {
            throw new UnauthorizedException(MessageConstant.UNAUTHORIZED, null);
        }

        if (UserConstant.DEACTIVATE.equalsIgnoreCase(user.getStatus().toString())) {
            throw new UnauthorizedException(MessageConstant.DEACTIVATE, null);
        }

        if (UserConstant.SUSPEND.equalsIgnoreCase(user.getStatus().toString())) {
            throw new UnauthorizedException(MessageConstant.SUSPEND, null);
        }

        return user;

    }

}
