package com.hacknovation.systemservice.v1_0_0.service.account;


import com.hacknovation.systemservice.config.AuthConfiguration;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.enums.UserStatusEnum;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.TransactionBalanceEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.TransactionBalanceRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserDeviceRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.service.cache.UserCacheSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.account.TokenRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.account.UserDetailRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.account.UserRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.ConfigurationUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountIP extends BaseServiceIP implements AccountSV {

    private final UserRP userRP;
    private final TransactionBalanceRP transactionBalanceRP;
    private final AuthConfiguration authConfiguration;
    private final UserAuth userAuth;
    private final HttpServletRequest request;
    private final UserDeviceRP userDeviceRP;
    private final JwtToken jwtToken;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogUtility activityLogUtility;
    private final UserCacheSV userCacheSV;
    private final ConfigurationUtility configurationUtility;

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * User login
     * -----------------------------------------------------------------------------------------------------------------
     *
     *
     * @param httpHeaders HttpHeaders
     * @param loginRQ     LoginRQ
     * @return UserDetailRS
     */
    public StructureRS login(HttpHeaders httpHeaders, LoginRQ loginRQ) {

        if (!authConfiguration.basicToken().equals(authConfiguration.oauth2Credential(httpHeaders)))
            return responseBodyWithBadRequest(MessageConstant.INVALID_CREDENTIAL, MessageConstant.INVALID_CREDENTIAL);

        UserEntity user = userRP.getUserByUsername(loginRQ.getUsername());
        verifyStatus(user, loginRQ);

        /*
         * restrict platform type
         */
        String platformTypeHeader = request.getHeader("PlatformType");
        System.out.println("AccountIP.login");
        System.out.println("Platform type = " + platformTypeHeader);
        if (platformTypeHeader == null)
            platformTypeHeader = "VN157";
        if (!platformTypeHeader.contains("local") && !UserConstant.SYSTEM.equalsIgnoreCase(user.getUserType().toString()) && !isCorrectPlatformLogin(platformTypeHeader, user.getPlatformType()))
            throw new BadRequestException(MessageConstant.INCORRECT_PASSWORD, null);
        try {
            TokenRS tokenRS = authConfiguration.getCredential(loginRQ, authConfiguration.oauth2Credential(httpHeaders));
            // Reset is locked
            Date now = new Date();
            user.setLastLoginApp(now);
            user.setLastLoginWeb(now);
            user.setLastLogin(now);

            user = userRP.save(user);
            UserDetailRS userDetailRS = userToken(user);
            userDetailRS.setToken(tokenRS.getAccess_token());
            /*
             * Add activity log login
             */
            activityLogUtility.logLoginActivity(user);
            return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, userDetailRS);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(MessageConstant.INCORRECT_PASSWORD, null);
        }

    }

    @Override
    public StructureRS logout(LogoutRQ loginRQ) {
        userDeviceRP.deleteAllByUserCodeAndDeviceToken(jwtToken.getUserToken().getUserCode(), loginRQ.getDeviceToken());
        return response(null);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Verification user status
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param user    UserEntity
     * @param loginRQ LoginRQ
     */
    public void verifyStatus(UserEntity user, LoginRQ loginRQ) {

        if (user == null) {
            throw new BadRequestException(MessageConstant.INCORRECT_PASSWORD, null);
        }

        if (UserConstant.DEACTIVATE.equalsIgnoreCase(user.getStatus().toString())) {
            throw new BadRequestException(MessageConstant.DEACTIVATE, null);
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(loginRQ.getPassword(), user.getPassword())) {
            throw new BadRequestException(MessageConstant.INCORRECT_PASSWORD, null);
        }

        if (user.getRoleCode().equalsIgnoreCase(UserConstant.MEMBER)) {
            throw new BadRequestException(MessageConstant.INCORRECT_PASSWORD, null);
        }

    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Reset password
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Override
    public StructureRS resetPassword(ResetRQ resetRQ) {

        if (!resetRQ.getPassword().equals(resetRQ.getConfirmPassword())) {
            throw new BadRequestException(MessageConstant.NOT_MATCH_PASSWORD, null);
        }

        UserEntity userEntity = userRP.findByCode(jwtToken.getUserToken().getUserCode());

        if (userEntity == null)
            throw new BadRequestException(MessageConstant.ACCOUNT_NOT_EXISTED, null);

        if (!passwordEncoder.matches(resetRQ.getCurrentPassword(), userEntity.getPassword()))
            throw new BadRequestException(MessageConstant.INCORRECT_CURRENT_PASSWORD);


        userEntity.setPassword(passwordEncoder.encode(resetRQ.getPassword()));
        userEntity = userRP.save(userEntity);
        userCacheSV.removeUserCacheByUsername(userEntity.getUsername());

        /*
         * Add activity log on reset password
         */
        activityLogUtility.resetPasswordActivity(userEntity);

        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Retrieve user
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param userEntity
     * @return
     */
    public UserRS user(UserEntity userEntity) {
        UserEntity user = userRP.getUserByUsername(userEntity.getUsername());
        UserRS userRS = new UserRS();
        BeanUtils.copyProperties(user, userRS);
        return userRS;
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Retrieve user token
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param userEntity
     * @return
     */
    public UserDetailRS userToken(UserEntity userEntity) {
        UserEntity user = userRP.getUserByUsername(userEntity.getUsername());

        UserRS userRS = new UserRS();
        BeanUtils.copyProperties(user, userRS);
        userRS.setUserCode(user.getCode());

        userRS.setSystemVersion(configurationUtility.getSystemVersion());

        TransactionBalanceEntity balanceEntity = transactionBalanceRP.userBalanceByCode(user.getCode());
        if (balanceEntity != null) {
            userRS.setBalanceKhr(balanceEntity.getBalanceKhr());
            userRS.setBalanceUsd(balanceEntity.getBalanceUsd());
        }

        if(UserConstant.SUB_ACCOUNT.equalsIgnoreCase(user.getRoleCode())){
            UserEntity parent = userRP.getUserByParentId(user.getParentId());
            userRS.setParentId(parent.getId());
            userRS.setParentUsername(parent.getUsername());
            userRS.setIsSubAccount(Boolean.TRUE);
        }

        UserDetailRS userDetailRS = new UserDetailRS();
        userDetailRS.setUser(userRS);
        return userDetailRS;
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * User locked screen
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @return null
     */
    public void lockedScreen() {
        UserEntity userEntity = userRP.getUserByUsername(jwtToken.getUserToken().getUsername());
        userEntity.setIsLockedScreen(true);
        userRP.save(userEntity);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * User locked screen
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @return UserDetailRS
     */
    public StructureRS unlockedScreen(CredentialRQ credentialRQ) {
        LoginRQ loginRQ = new LoginRQ();
        loginRQ.setUsername(jwtToken.getUserToken().getUsername());
        loginRQ.setPassword(credentialRQ.getPassword());

        UserEntity user = userRP.getUserByUsername(jwtToken.getUserToken().getUsername());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(credentialRQ.getPassword(), user.getPassword()))
            throw new BadRequestException(MessageConstant.INCORRECT_PASSWORD);

        TokenRS tokenRS = authConfiguration.getCredential(loginRQ, authConfiguration.basicToken());

        // Reset is locked
        user.setIsLockedScreen(false);
        userRP.save(user);

        UserDetailRS userDetailRS = new UserDetailRS();
        userDetailRS.setUser(user(user));
        userDetailRS.setToken(tokenRS.getAccess_token());
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, userDetailRS);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * User deactivate account
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param credentialRQ
     * @return
     */
    public void deactivate(CredentialRQ credentialRQ) {
        UserEntity user = userAuth.userPrincipal();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(credentialRQ.getPassword(), user.getPassword()))
            throw new BadRequestException(MessageConstant.INCORRECT_PASSWORD);

        user.setStatus(UserStatusEnum.valueOf(UserConstant.DEACTIVATE));
        userRP.save(user);
        userCacheSV.removeUserCacheByUsername(user.getUsername());
    }

    @Override
    public StructureRS profile() {
        var token = jwtToken.getUserToken();
        return responseBodyWithSuccessMessage(userToken(userRP.getUserByUsername(token.getUsername())));
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Change languages
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param languageRQ
     * @return
     */
    public void language(LanguageRQ languageRQ) {
        UserEntity userEntity = userRP.getUserByUsername(jwtToken.getUserToken().getUsername());
        userEntity.setLanguageCode(languageRQ.getLanguageCode());
        userRP.save(userEntity);
        userCacheSV.removeUserCacheByUsername(userEntity.getUsername());

    }


    private boolean isCorrectPlatformLogin(String platformHeader, String platformUser) {
        if (LotteryConstant.PLATFORM_ALLOWS.contains(platformHeader.toUpperCase())) return true;
        return platformHeader.equalsIgnoreCase(platformUser);
    }

}
