package com.hacknovation.systemservice.v1_0_0.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.enums.UserStatusEnum;
import com.hacknovation.systemservice.enums.UserTypeEnum;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.InitialBalanceEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserReferralTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.InitialBalanceRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserHasLotteryRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.service.cache.UserCacheSV;
import com.hacknovation.systemservice.v1_0_0.service.config.ConfigSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.user.UserDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.account.UserRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.user.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.PagingRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.account.UserRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.CheckUserRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.LimitBetItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.LotteryTyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.user.UserReferralRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.ConfigurationUtility;
import com.hacknovation.systemservice.v1_0_0.utility.UniqueCode;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.UserHasLotteryJsonUtility;
import com.hacknovation.systemservice.v1_0_0.utility.user.UserReferralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.user.UserStatusUtility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserIP extends BaseServiceIP implements UserSV {

    private final UserNQ userNQ;
    private final JwtToken jwtToken;
    private final HttpServletRequest request;
    private final UserRP userRP;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserReferralUtility referralUtility;
    private final UserStatusUtility userStatusUtility;
    private final UserHasLotteryRP userHasLotteryRP;
    private final ConfigSV configSV;
    private final InitialBalanceRP initialBalanceRP;
    private final ActivityLogUtility activityLogUtility;
    private final UserCacheSV userCacheSV;
    private final UserHasLotteryJsonUtility userHasLotteryJsonUtility;
    private final ObjectMapper objectMapper;
    private final ConfigurationUtility configurationUtility;


    @Override
    public StructureRS userListing() {
        UserListingRQ userListingRQ = new UserListingRQ(request);

        var userToken = jwtToken.getUserToken();
        String loggedRole = userToken.getUserRole();
        String loggedUserCode = userToken.getUserCode();

        boolean isCanSeeUserOnline = true;
        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
            isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                isCanSeeUserOnline = true;
        }

        boolean isSubAccount = UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userListingRQ.getFilterByLevel());
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            loggedRole = userToken.getParentRole();
            loggedUserCode = userToken.getParentCode();
        }

        Page<UserDTO> userList = userRP.getUserReferral(
                userListingRQ.getFilterByLevel(),
                "LEVEL",
                userListingRQ.getKeyword(),
                loggedUserCode,
                userToken.getUserType(),
                loggedRole,
                userListingRQ.getUserCode(),
                isSubAccount,
                isCanSeeUserOnline,
                userListingRQ.getStatus(),
                PageRequest.of(userListingRQ.getPage(), userListingRQ.getSize())
        );

        return responseBody(HttpStatus.OK,
                MessageConstant.SUCCESSFULLY,
                userList.getContent(),
                new PagingRS(userList.getNumber(),
                        userList.getSize(),
                        userList.getTotalElements()));
    }

    @Override
    public StructureRS userReferrals() {

        UserReferralRQ userReferralRQ = new UserReferralRQ(request);
        System.out.println("===================================================");
        System.out.println("UserIP.userReferrals => userReferralRQ = " + userReferralRQ);
        System.out.println("===================================================");
        try {

            UserTO userTO = userNQ.userByCode(userReferralRQ.getReferralUserCode());
            List<UserReferralTO> userReferralTOS = userNQ.userByReferral(
                    "all",
                    userTO.getUserCode(),
                    userReferralRQ.getKeyword());

            List<UserReferralTO> seniorReferrals = userReferralTOS.stream()
                    .filter(senior -> senior.getRoleCode().equalsIgnoreCase("senior"))
                    .collect(Collectors.toList());

            List<UserReferralTO> masterReferrals = userReferralTOS.stream()
                    .filter(master -> master.getRoleCode().equalsIgnoreCase("master"))
                    .collect(Collectors.toList());

            List<UserReferralTO> agentReferrals = userReferralTOS.stream()
                    .filter(agent -> agent.getRoleCode().equalsIgnoreCase("agent"))
                    .collect(Collectors.toList());

            List<UserReferralTO> memberReferrals = userReferralTOS.stream()
                    .filter(member -> member.getRoleCode().equalsIgnoreCase("member"))
                    .collect(Collectors.toList());

            if (userTO.getRoleCode().equalsIgnoreCase("super-senior")) {
                UserReferralRS userReferralRS = referralUtility.superSeniorReferral(userTO, seniorReferrals, masterReferrals, agentReferrals, memberReferrals);
                return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, userReferralRS, new PagingRS());
            }

            if (userTO.getRoleCode().equalsIgnoreCase("senior")) {
                UserReferralRS userReferralRS = referralUtility.seniorReferral(userTO, masterReferrals, agentReferrals, memberReferrals);
                return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, userReferralRS, new PagingRS());
            }

            if (userTO.getRoleCode().equalsIgnoreCase("master")) {
                UserReferralRS userReferralRS = referralUtility.masterReferral(userTO, agentReferrals, memberReferrals);
                return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, userReferralRS, new PagingRS());
            }

            if (userTO.getRoleCode().equalsIgnoreCase("agent")) {
                UserReferralRS userReferralRS = referralUtility.agentReferral(userTO, memberReferrals);
                return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, userReferralRS, new PagingRS());
            }


            return responseBodyWithSuccessMessage();

        } catch (Exception e) {
            e.printStackTrace();
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.BAD_REQUEST);
        }

    }

    @Override
    public StructureRS employeeListing() {
        UserListingRQ userListingRQ = new UserListingRQ(request);
        String guardName = "system";
        String userLevel = "all";
        Page<UserTO> userTO = userNQ.users(
                guardName,
                userLevel,
                userListingRQ.getKeyword(),
                PageRequest.of(userListingRQ.getPage(), userListingRQ.getSize()));

        return userBodyListing(userTO);
    }

    public StructureRS userBodyListing(Page<UserTO> userTO) {
        List<UserRS> userRS = new ArrayList<>();
        userTO.forEach(item -> {
            UserRS userRS1 = new UserRS();
            BeanUtils.copyProperties(item, userRS1);
            userRS1.setStatus(UserStatusEnum.valueOf(item.getStatus()));
            userRS.add(userRS1);
        });

        return responseBody(HttpStatus.OK,
                MessageConstant.SUCCESSFULLY,
                userRS,
                new PagingRS(userTO.getNumber(),
                        userTO.getSize(),
                        userTO.getTotalElements()));
    }

    public StructureRS userProfile(String userCode) {

        try {
            return responseBodyWithSuccessMessage(prepareUser(userCode));
        } catch (Exception e) {
            e.printStackTrace();
            return responseBodyWithBadRequest(MessageConstant.BAD_REQUEST, MessageConstant.BAD_REQUEST_KEY);
        }

    }

    @Override
    public StructureRS lotteryType(String userCode) {
        UserEntity oldUserEntity = userRP.findByCode(userCode);
        List<LotteryTyRS> listLotteryType = new ArrayList<>();
        if (oldUserEntity != null) {
            String lotteryStr;
            if (oldUserEntity.getRoleCode().equalsIgnoreCase(UserConstant.SUPER_SENIOR)) {
                lotteryStr = oldUserEntity.getLotteryType();
            } else {
                UserEntity userEntity = userRP.findByCode(oldUserEntity.getSuperSeniorCode());
                lotteryStr = userEntity.getLotteryType();
            }
            listLotteryType = getListLotteryType(lotteryStr);
        }
        return responseBodyWithSuccessMessage(listLotteryType);
    }

    public UserRS prepareUser(String userCode) {
        UserTO userTO = userNQ.userByCode(userCode);
        if (userTO.getLotteryType() != null && !userTO.getLotteryType().equalsIgnoreCase("")) {
            List<String> items = getListLotteryStr(userTO.getLotteryType());
            userTO.setLotteryType(String.join(",", items));
        }
        UserRS userRS = new UserRS();
        userRS.setSystemVersion(configurationUtility.getSystemVersion());
        BeanUtils.copyProperties(userTO, userRS);
        userRS.setStatus(UserStatusEnum.valueOf(userTO.getStatus()));
        return userRS;

    }

    public StructureRS createUserLevel(CreateUserRQ createUserRQ) {
//        if (!referralUtility.isCommissionValid(createUserRQ)) {
//            return responseBodyWithBadRequest(MessageConstant.COMMISSION_INVALID, MessageConstant.COMMISSION_INVALID_KEY);
//        }
        UserEntity user = userRP.findByCode(createUserRQ.getLevelUnderUserCode());
        createUserRQ.setPlatformType(user.getPlatformType());
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(user.getRoleCode())) {
            UserEntity parent = userRP.getUserByParentId(user.getParentId());
            createUserRQ.setLevelUnderUserCode(parent.getCode());
            createUserRQ.setPlatformType(parent.getPlatformType());
        }

        try {
            UserTO userTO = userNQ.userByCode(createUserRQ.getLevelUnderUserCode());
            if (!isAbleCreateUser(createUserRQ))
                return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.USER_NOT_CREATE + "," + MessageConstant.PLEASE_SET_PARENT_COMMISSION);
            UserEntity userEntity = checkUserLevels(createUserRQ, userTO);
            if (userEntity != null && createUserRQ.getCommissions() != null && !createUserRQ.getCommissions().isEmpty())
                configSV.createUserHasLotteryFromCommissionList(userEntity, createUserRQ.getCommissions(), jwtToken.getUserToken());
            return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, userEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.BAD_REQUEST, e);
        }

    }

    private UserEntity checkUserLevels(CreateUserRQ createUserRQ, UserTO userTO) {
        var userToken = jwtToken.getUserToken();
        String userCode = userToken.getUserCode();
        createUserRQ.setCreatedBy(userCode);
        if (createUserRQ.getLevelUnderUserCode() != null && !createUserRQ.getLevelUnderUserCode().isEmpty()) {
            userCode = createUserRQ.getLevelUnderUserCode();
        }

        if (userTO == null)
            userTO = userNQ.userByCode(userCode);

        // Create senior
        if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userTO.getRoleCode())) {
            createUserRQ.setUsername(userTO.getUsername() + createUserRQ.getUsername().substring(createUserRQ.getUsername().length() - 2));
            return createSenior(userTO, createUserRQ);
        }

        // Create master
        if (UserConstant.SENIOR.equalsIgnoreCase(userTO.getRoleCode())) {
            createUserRQ.setUsername(userTO.getUsername() + createUserRQ.getUsername().substring(createUserRQ.getUsername().length() - 2));
            return createMaster(userTO, createUserRQ);
        }

        // Create agent
        if (UserConstant.MASTER.equalsIgnoreCase(userTO.getRoleCode())) {
            createUserRQ.setUsername(userTO.getUsername() + createUserRQ.getUsername().substring(createUserRQ.getUsername().length() - 2));
            return createAgent(userTO, createUserRQ);
        }

        // Create member
        if (UserConstant.AGENT.equalsIgnoreCase(userTO.getRoleCode())) {
            createUserRQ.setUsername(userTO.getUsername() + createUserRQ.getUsername().substring(createUserRQ.getUsername().length() - 3));
            return createMember(userTO, createUserRQ);
        }

        // Can not create member
        throw new BadRequestException(MessageConstant.BAD_REQUEST, null);

    }

    public UserEntity createMember(UserTO userTO, CreateUserRQ createUserRQ) {
        UserRQ userRQ = new UserRQ();
        userRQ.setIsOnline(userTO.getIsOnline());
        BeanUtils.copyProperties(createUserRQ, userRQ);
        userRQ.setUserType(createUserRQ.getUserType().toString());
        userRQ.setPassword(passwordEncoder.encode(createUserRQ.getPassword()));
        userRQ.setCode(userUniqueCode());
        userRQ.setRoleCode("member");
        userRQ.setSuperSeniorCode(userTO.getSuperSeniorCode());
        userRQ.setSeniorCode(userTO.getSeniorCode());
        userRQ.setMasterCode(userTO.getMasterCode());
        userRQ.setAgentCode(userTO.getUserCode());

        // Create profile
        UserProfileRQ userProfileRQ = new UserProfileRQ();
        BeanUtils.copyProperties(createUserRQ, userProfileRQ);
        userProfileRQ.setCurrencyCode(referralUtility.getCurrency(userTO.getSuperSeniorCode()));
        return createProfile(userProfileRQ, userRQ);
    }

    public UserEntity createAgent(UserTO userTO, CreateUserRQ createUserRQ) {
        UserRQ userRQ = new UserRQ();
        userRQ.setIsOnline(userTO.getIsOnline());
        BeanUtils.copyProperties(createUserRQ, userRQ);
        userRQ.setUserType(createUserRQ.getUserType().toString());
        userRQ.setPassword(passwordEncoder.encode(createUserRQ.getPassword()));
        userRQ.setCode(userUniqueCode());
        userRQ.setRoleCode("agent");
        userRQ.setSuperSeniorCode(userTO.getSuperSeniorCode());
        userRQ.setSeniorCode(userTO.getSeniorCode());
        userRQ.setMasterCode(userTO.getUserCode());

        // Create profile
        UserProfileRQ userProfileRQ = new UserProfileRQ();
        BeanUtils.copyProperties(createUserRQ, userProfileRQ);
        userProfileRQ.setCurrencyCode(referralUtility.getCurrency(userTO.getSuperSeniorCode()));
        return createProfile(userProfileRQ, userRQ);
    }

    public UserEntity createMaster(UserTO userTO, CreateUserRQ createUserRQ) {
        UserRQ userRQ = new UserRQ();
        userRQ.setIsOnline(userTO.getIsOnline());
        BeanUtils.copyProperties(createUserRQ, userRQ);
        userRQ.setUserType(createUserRQ.getUserType().toString());
        userRQ.setPassword(passwordEncoder.encode(createUserRQ.getPassword()));
        userRQ.setCode(userUniqueCode());
        userRQ.setRoleCode("master");
        userRQ.setSuperSeniorCode(userTO.getSuperSeniorCode());
        userRQ.setSeniorCode(userTO.getUserCode());

        // Create profile
        UserProfileRQ userProfileRQ = new UserProfileRQ();
        BeanUtils.copyProperties(createUserRQ, userProfileRQ);
        userProfileRQ.setCurrencyCode(referralUtility.getCurrency(userTO.getSuperSeniorCode()));
        return createProfile(userProfileRQ, userRQ);
    }

    public UserEntity createSenior(UserTO userTO, CreateUserRQ createUserRQ) {
        UserRQ userRQ = new UserRQ();
        BeanUtils.copyProperties(createUserRQ, userRQ);
        userRQ.setIsOnline(userTO.getIsOnline());
        userRQ.setUserType(createUserRQ.getUserType().toString());
        userRQ.setPassword(passwordEncoder.encode(createUserRQ.getPassword()));
        userRQ.setCode(userUniqueCode());
        userRQ.setRoleCode("senior");
        userRQ.setSuperSeniorCode(userTO.getUserCode());

        // Create profile
        UserProfileRQ userProfileRQ = new UserProfileRQ();
        BeanUtils.copyProperties(createUserRQ, userProfileRQ);
        userProfileRQ.setCurrencyCode(referralUtility.getCurrency(userTO.getUserCode()));
        return createProfile(userProfileRQ, userRQ);
    }

    @Override
    public StructureRS createSuperSenior(CreateEmployeeRQ employeeRQ) {

        if(userRP.existsByUsernameIgnoreCase(employeeRQ.getUsername()))
            return responseBodyWithBadRequest(MessageConstant.USERNAME_ALREADY_EXIST, MessageConstant.USERNAME_ALREADY_EXIST_KEY);

        if (employeeRQ.getLotteryType() == null || employeeRQ.getLotteryType().size() < 1)
            return responseBodyWithBadRequest(MessageConstant.LOTTERY_TYPE_REQUIRED, MessageConstant.LOTTERY_TYPE_REQUIRED_KEY);

        try {
            employeeRQ.setRoleCode("super-senior");
            createEmployee(employeeRQ);
            return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
        } catch (DataIntegrityViolationException exception) {
            return responseBodyWithBadRequest(MessageConstant.USERNAME_ALREADY_EXIST, MessageConstant.USERNAME_ALREADY_EXIST_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return responseBodyWithBadRequest(MessageConstant.BAD_REQUEST, e.getMessage());
        }

    }

    @Override
    public StructureRS createSubAccount(CreateEmployeeRQ employeeRQ) {
        try {
            UserToken token = jwtToken.getUserToken();
            int parentId = token.getId().intValue();
            employeeRQ.setRoleCode("sub-account");
            employeeRQ.setParentId(parentId);
            UserEntity parent = userRP.getUserByParentId(parentId);
            employeeRQ.setPlatformType(parent.getPlatformType());
            createEmployee(employeeRQ);
            return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
        } catch (DataIntegrityViolationException exception) {
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.USERNAME_ALREADY_EXIST);
        } catch (Exception e) {
            e.printStackTrace();
            return responseBody(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public StructureRS createEmployee(CreateEmployeeRQ employeeRQ) {
        UserRQ userRQ = new UserRQ();
        employeeRQ.setCreatedBy(jwtToken.getUserToken().getUserCode());
        BeanUtils.copyProperties(employeeRQ, userRQ);
        userRQ.setUserType(employeeRQ.getUserType().toString());
        userRQ.setPassword(passwordEncoder.encode(employeeRQ.getPassword()));
        userRQ.setCode(userUniqueCode());

        // Create user profile
        UserProfileRQ userProfileRQ = new UserProfileRQ();
        BeanUtils.copyProperties(employeeRQ, userProfileRQ);

        UserEntity userEntity = createProfile(userProfileRQ, userRQ);
        if (userEntity.getId() != null && UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode()))
            configSV.createUserHasLotteryFromCommissionList(userEntity, employeeRQ.getCommissions(), jwtToken.getUserToken());

        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, userEntity);
    }

    public UserEntity createProfile(UserProfileRQ userProfileRQ, UserRQ userRQ) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userRQ, userEntity);
        userEntity.setNickname(userProfileRQ.getNickname());
        userEntity.setUserType(UserTypeEnum.valueOf(userRQ.getUserType()));
        userEntity.setLanguageCode("km");

        if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userRQ.getRoleCode())) {
            userEntity.setPlatformType(userRQ.getPlatformType());
            userEntity.setIsOnline(userRQ.getIsOnline());
        }

        if (userRQ.getLotteryType() != null && !userRQ.getLotteryType().isEmpty()) {
            userEntity.setLotteryType(String.join(",", getListLotteryStr(String.join(",", userRQ.getLotteryType()))));
        }

        if (userProfileRQ.getCurrencyCode() != null) {
            userEntity.setLanguageCode(userProfileRQ.getCurrencyCode().equalsIgnoreCase("KHR") ? "km" : "en");
        }
        userEntity.setCreatedBy(jwtToken.getUserToken().getUserCode());
        userEntity.setUpdatedBy(userEntity.getCreatedBy());
        userEntity.setIsLockedBetting(false);
        userEntity = userRP.save(userEntity);

        activityLogUtility.addActivityLog("", ActivityLogConstant.MODULE_USER, userRQ.getUserType(), ActivityLogConstant.ACTION_ADD, jwtToken.getUserToken().getUserCode(), userEntity);

        if (!userEntity.getUserType().equals(UserTypeEnum.SYSTEM))
            setUpInitialBalance(userEntity);
        return userEntity;
    }

    public String userUniqueCode() {
        return UniqueCode.getUniqueCode(userRP.getUser() != null ? userRP.getUser().getCode() : null);
    }

    @Override
    public StructureRS cloneUserLevel(CloneUserRQ cloneUserRQ) {

        CreateUserRQ createUserRQ = new CreateUserRQ();
        createUserRQ.setUsername(cloneUserRQ.getUsername());
        createUserRQ.setNickname(cloneUserRQ.getNickname());
        createUserRQ.setLevelUnderUserCode(cloneUserRQ.getUnderUserCode());
        createUserRQ.setPassword(cloneUserRQ.getPassword());
        createUserRQ.setConfirmPassword(cloneUserRQ.getConfirmPassword());
        UserEntity created = checkUserLevels(createUserRQ, null);

        List<UserHasLotteryEntity> userHasLotteryEntities = userHasLotteryRP.userHasLotteries(cloneUserRQ.getCloneFromUserCode());

        List<UserHasLotteryEntity> hasLotteryEntities = new ArrayList<>();
        for (UserHasLotteryEntity item : userHasLotteryEntities) {
            UserHasLotteryEntity userHasLotteryEntity = new UserHasLotteryEntity();
            userHasLotteryEntity.setUserCode(created.getCode());
            userHasLotteryEntity.setLotteryCode(item.getLotteryCode());
            userHasLotteryEntity.setRebateCode(item.getRebateCode());
            userHasLotteryEntity.setRebateRate(item.getRebateRate());
            userHasLotteryEntity.setWaterRate(item.getWaterRate());
            userHasLotteryEntity.setShare(item.getShare());
            userHasLotteryEntity.setCommission(item.getCommission());
            userHasLotteryEntity.setMaxBetFirst(item.getMaxBetFirst());
            userHasLotteryEntity.setMaxBetFirst(item.getMaxBetSecond());
            userHasLotteryEntity.setLimitDigit(item.getLimitDigit());
            userHasLotteryEntity.setUpdatedBy(jwtToken.getUserToken().getUserCode());
            userHasLotteryEntity.setStatus(true);
            hasLotteryEntities.add(userHasLotteryEntity);
        }

        userHasLotteryRP.saveAll(hasLotteryEntities);
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);

    }

    @Override
    public StructureRS update(String code, UpdateUserRQ request) {

        UserEntity userEntity = userRP.findByCode(code);

        if (userEntity == null)
            return responseBody(HttpStatus.NO_CONTENT, "User not found");

        userStatusUtility.changeStatus(userEntity, request);
        userCacheSV.removeUserCacheAll();
        modelMapper.map(request, userEntity);
        if (!UserConstant.MEMBER.equalsIgnoreCase(userEntity.getRoleCode()) && request.getLotteryType() != null) {
            String lotteryStr = request.getLotteryType().replace(LotteryConstant.MT, LotteryConstant.VN2);

            List<String> lotteryType = getListLotteryStr(lotteryStr);

            userEntity.setLotteryType(String.join(",", lotteryType));
            userEntity.setPlatformType(request.getPlatformType());

            validateChildLotteryType(userEntity);

            if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userEntity.getRoleCode()))
                validateUserHasLottery(userEntity.getCode(), lotteryType);
        }
        userEntity.setUpdatedBy(jwtToken.getUserToken().getUserCode());

        userRP.save(userEntity);

        return responseBody(HttpStatus.OK, "User has been updated");
    }

    @Override
    public StructureRS changePassword(String code, ChangeUserPasswordRQ request) {
        Optional<UserEntity> optUserEntity = userRP.getUserByCode(code);

        if (optUserEntity.isEmpty())
            return responseBody(HttpStatus.NO_CONTENT, "User not found");

        UserEntity user = optUserEntity.get();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUpdatedBy(jwtToken.getUserToken().getUserCode());
        user.setLastLogin(new Date());
        user.setLastLoginApp(new Date());
        user.setLastLoginWeb(new Date());
        userRP.save(user);
        userCacheSV.removeUserCacheByUsername(user.getUsername());
        activityLogUtility.resetPasswordActivity(user);

        return responseBody(HttpStatus.OK, user.getRoleCode() + "'s password has been changed");
    }

    @Override
    public StructureRS checkUsername(String username) {

        UserEntity userEntity = userRP.getUserByUsername(username);
        CheckUserRS checkUserRS = new CheckUserRS();
        if (userEntity != null)
            checkUserRS.setIsExist(true);

        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, checkUserRS);
    }

    @Override
    public StructureRS listLimitBet(String userCode) {

        UserEntity userEntity = userRP.findByCode(userCode);
        if (userEntity == null)
            return responseBodyWithBadRequest(MessageConstant.USER_COULD_NOT_BE_FOUND, MessageConstant.USER_COULD_NOT_BE_FOUND_KEY);

        List<LimitBetItemRS> defaultLimit = getDefaultLimitBetRS(userEntity);

        if (userEntity.getLimitBet() != null) {
            List<LimitBetItemRS> userLimit = getLimitBetRS(userEntity.getLimitBet());
            for (LimitBetItemRS itemRS : userLimit) {
                defaultLimit.stream()
                        .filter(it->it.getLotteryType().equals(itemRS.getLotteryType()))
                        .findFirst()
                        .ifPresent(it-> it.setLimitAmount(itemRS.getLimitAmount()));
            }
        }

        return responseBodyWithSuccessMessage(defaultLimit);
    }

    @Override
    public StructureRS updateLimitBet(UpdateLimitBetRQ updateLimitBetRQ) {
        UserToken userToken = jwtToken.getUserToken();
        UserEntity userEntity = userRP.findByCode(updateLimitBetRQ.getUserCode());
        if (userEntity == null)
            return responseBodyWithBadRequest(MessageConstant.USER_COULD_NOT_BE_FOUND, MessageConstant.USER_COULD_NOT_BE_FOUND_KEY);
        List<LimitBetItemRS> itemRSList;
        if (userEntity.getLimitBet() == null) {
            itemRSList = new ArrayList<>(getDefaultLimitBetRS(userEntity));
        } else {
            itemRSList = new ArrayList<>(getLimitBetRS(userEntity.getLimitBet()));
        }
        Optional<LimitBetItemRS> optLimitBet = itemRSList.stream().filter(it-> it.getLotteryType().equalsIgnoreCase(updateLimitBetRQ.getLotteryType())).findFirst();
        if (optLimitBet.isPresent()) {
            optLimitBet.get().setLimitAmount(updateLimitBetRQ.getLimitAmount());
        } else {
            LimitBetItemRS itemRS = new LimitBetItemRS(updateLimitBetRQ.getLotteryType());
            itemRS.setLimitAmount(updateLimitBetRQ.getLimitAmount());
            itemRSList.add(itemRS);
        }

        userEntity.setLimitBet(getLimitBetString(itemRSList));
        userRP.save(userEntity);
        activityLogUtility.addActivityLog(LotteryConstant.ALL,
                ActivityLogConstant.MODULE_USER_LIMIT,
                userToken.getUserType(),
                ActivityLogConstant.ACTION_UPDATE,
                userToken.getUserCode(),
                updateLimitBetRQ);
        userCacheSV.removeUserCacheByUsername(userEntity.getUsername());

        return responseBodyWithSuccessMessage();
    }

    /**
     * Check user is super senior has lotteries
     *
     * @param createUserRQ
     * @return true allow create
     */
    private boolean isAbleCreateUser(CreateUserRQ createUserRQ) {
        if (!UserConstant.SUPER_SENIOR.equalsIgnoreCase(createUserRQ.getRoleCode())) {
            List<UserHasLotteryEntity> userHasLotteryEntities = userHasLotteryRP.userHasLotteries(createUserRQ.getLevelUnderUserCode());
            return userHasLotteryEntities.size() > 0;
        }
        return true;
    }


    private List<LotteryTyRS> getListLotteryType(String lotteryStr) {
        List<LotteryTyRS> lotteryType = new ArrayList<>();
        for (String s : getListLotteryStr(lotteryStr)) {
            LotteryTyRS lotteryTyRS = new LotteryTyRS(s);
            lotteryType.add(lotteryTyRS);
        }

        return lotteryType.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getListLotteryStr(String lotteryStr) {
        if (StringUtils.isEmpty(lotteryStr))
            return new ArrayList<>();
        return Arrays.stream(lotteryStr.split(",")).distinct().collect(Collectors.toList());
    }

    private void setUpInitialBalance(UserEntity userEntity) {
        List<InitialBalanceEntity> initialBalanceEntities = new ArrayList<>();
        try {
            UserToken userToken = jwtToken.getUserToken();
            List<String> lotteries = List.of(LotteryConstant.VN2, LotteryConstant.VN1, LotteryConstant.LEAP, LotteryConstant.TN, LotteryConstant.KH);
            for (String lottery : lotteries) {
                InitialBalanceEntity initialBalanceEntity = new InitialBalanceEntity();

                initialBalanceEntity.setBalanceKhr(BigDecimal.ZERO);
                initialBalanceEntity.setBalanceUsd(BigDecimal.ZERO);
                initialBalanceEntity.setLotteryType(lottery);
                initialBalanceEntity.setUserCode(userEntity.getCode());
                initialBalanceEntity.setIssuedAt(new Date());
                initialBalanceEntity.setCreatedBy(userToken.getUserCode());
                initialBalanceEntity.setUpdatedBy(userToken.getUserCode());

                initialBalanceEntities.add(initialBalanceEntity);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            initialBalanceRP.saveAll(initialBalanceEntities);
        }
    }

    private void validateUserHasLottery(String userCode, List<String> lotteryTypes) {
        List<UserHasLotteryEntity> userHasLotteryEntities = new ArrayList<>();
        List<UserEntity> userEntities = userRP.findAllByCodeOrSuperSeniorCode(userCode, userCode);

        Map<String, List<String>> rebateCodeByLottery = Map.of(
                "VN1", List.of("1D", "2D", "3D", "4D"),
                "TN", List.of("1D", "2D", "3D", "4D"),
                "VN2", List.of("1D", "2D", "3D", "4D"),
                "LEAP", List.of("2D", "3D", "4D"),
                "SC", List.of("2D", "3D", "4D", "5D"),
                "KH", List.of("2D", "3D"),
                "TH", List.of("2D", "3D")
        );

        Map<String, BigDecimal> rebateRateByCode = Map.of(
                "1D", LotteryConstant.REBATE_RATE_1D,
                "2D", LotteryConstant.REBATE_RATE_2D,
                "3D", LotteryConstant.REBATE_RATE_3D,
                "4D", LotteryConstant.REBATE_RATE_4D,
                "5D", LotteryConstant.REBATE_RATE_4D
        );

        Map<String, BigDecimal> commissionByCode = Map.of(
                "1D", LotteryConstant.COMMISSION_RATE_1D,
                "2D", LotteryConstant.COMMISSION_RATE_2D,
                "3D", LotteryConstant.COMMISSION_RATE_3D,
                "4D", LotteryConstant.COMMISSION_RATE_4D,
                "5D", LotteryConstant.COMMISSION_RATE_4D
        );

        Map<String, BigDecimal> limitDigit = Map.of(
                "1D", LotteryConstant.LIMIT_DIGIT_1D,
                "2D", LotteryConstant.LIMIT_DIGIT_2D,
                "3D", LotteryConstant.LIMIT_DIGIT_3D,
                "4D", LotteryConstant.LIMIT_DIGIT_4D,
                "5D", LotteryConstant.LIMIT_DIGIT_4D
        );

        Map<String, BigDecimal> maxBetFirst = Map.of(
                "1D", LotteryConstant.MAX_BET_FIRST_1D,
                "2D", LotteryConstant.MAX_BET_FIRST_2D,
                "3D", LotteryConstant.MAX_BET_FIRST_3D,
                "4D", LotteryConstant.MAX_BET_FIRST_4D,
                "5D", LotteryConstant.MAX_BET_FIRST_4D
        );

        Map<String, BigDecimal> maxBetSec = Map.of(
                "1D", LotteryConstant.MAX_BET_SECOND_1D,
                "2D", LotteryConstant.MAX_BET_SECOND_2D,
                "3D", LotteryConstant.MAX_BET_SECOND_3D,
                "4D", LotteryConstant.MAX_BET_SECOND_4D,
                "5D", LotteryConstant.MAX_BET_SECOND_4D
        );

        Map<String, String> drawByLottery = Map.of(
                "LEAP", "30,30,30,30,30,30,30",
                "SC", "30,30,30",
                "VN1", "30,30",
                "TH", "30",
                "KH", "30,30,30,30,30,30",
                "VN2", "30,30,30,30",
                "TN", "30,30,30,30,30"
        );
        List<String> newLotteries = new ArrayList<>();
        for (String lotteryType : lotteryTypes) {
            if (!userHasLotteryRP.existsByUserCodeAndLotteryCode(userCode, lotteryType)) {
                newLotteries.add(lotteryType);
                for (UserEntity userEntity : userEntities) {
                    for (String rebate : rebateCodeByLottery.get(lotteryType.toUpperCase())) {
                        UserHasLotteryEntity userHasLotteryEntity = new UserHasLotteryEntity();
                        userHasLotteryEntity.setUserCode(userEntity.getCode());
                        userHasLotteryEntity.setLotteryCode(lotteryType);
                        userHasLotteryEntity.setRebateCode(rebate);
                        userHasLotteryEntity.setShare(BigDecimal.valueOf(100));
                        userHasLotteryEntity.setCommission(commissionByCode.get(rebate));
                        userHasLotteryEntity.setRebateRate(rebateRateByCode.get(rebate));
                        userHasLotteryEntity.setMaxBetFirst(maxBetFirst.get(rebate));
                        userHasLotteryEntity.setMaxBetSecond(maxBetSec.get(rebate));
                        userHasLotteryEntity.setMaxBetSecondMin(drawByLottery.get(lotteryType));
                        userHasLotteryEntity.setLimitDigit(limitDigit.get(rebate));
                        userHasLotteryEntities.add(userHasLotteryEntity);
                    }
                }
            }
        }
        if (!userHasLotteryEntities.isEmpty())
            userHasLotteryRP.saveAll(userHasLotteryEntities);

        for (String newLottery : newLotteries) {
            userHasLotteryJsonUtility.trackUserHasLottery(newLottery, userCode, jwtToken.getUserToken());
        }
    }

    private void validateChildLotteryType(UserEntity userEntity){
        List<UserEntity> userEntities = userRP.getAllByParentCode(userEntity.getCode());

        List<String> parentLotteryType = getListLotteryStr(userEntity.getLotteryType());

        for(UserEntity user: userEntities){

            List<String> lotteryType = new ArrayList<>(getListLotteryStr(user.getLotteryType()));

            lotteryType.removeIf(lottery -> !parentLotteryType.contains(lottery));

            user.setLotteryType(String.join(",", lotteryType));

        }

        userRP.saveAll(userEntities);
    }

    private List<LimitBetItemRS> getDefaultLimitBetRS(UserEntity userEntity) {
        if (userEntity.getLotteryType() != null) {
            List<String> lotteries = List.of(userEntity.getLotteryType().split(","));
            List<LimitBetItemRS> itemRSList = new ArrayList<>();
            lotteries.forEach(lottery -> itemRSList.add(new LimitBetItemRS(lottery)));
            return itemRSList;
        }
        return new ArrayList<>();
    }

    private List<LimitBetItemRS> getLimitBetRS(String limitBet) {
        try {
            return objectMapper.readValue(limitBet, new TypeReference<List<LimitBetItemRS>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private String getLimitBetString(List<LimitBetItemRS> itemRSList) {
        try {
            return objectMapper.writeValueAsString(itemRSList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
