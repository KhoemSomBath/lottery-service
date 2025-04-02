package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import io.github.gasparbarancelli.NativeQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@NativeQueryFolder("user")
public interface UserNQ extends NativeQuery {

    Page<UserTO> users(@NativeQueryParam(value = "guardName") String guardName,
                       @NativeQueryParam(value = "userLevel") String userLevel,
                       @NativeQueryParam(value = "keyword") String keyword,
                       Pageable pageable);

    Page<UserTO> userLevel(@NativeQueryParam(value = "userCodes") List<String> userCodes,
                           @NativeQueryParam(value = "userType") String userType,
                           @NativeQueryParam(value = "accessByUser") String accessByUser,
                           @NativeQueryParam(value = "guardName,") String guardName,
                           @NativeQueryParam(value = "userLevel") String userLevel,
                           @NativeQueryParam(value = "status") String status,
                           @NativeQueryParam(value = "keyword") String keyword,
                           Pageable pageable);

    Page<UserTO> userLevelSubAccount(@NativeQueryParam(value = "id") Long id,
                                     @NativeQueryParam(value = "guardName") String guardName,
                                     @NativeQueryParam(value = "keyword") String keyword,
                                     Pageable pageable);

    @Transactional
    List<UserReferralTO> userByReferral(@NativeQueryParam(value = "userLevel") String userLevel,
                                        @NativeQueryParam(value = "userCode") String userCode,
                                        @NativeQueryParam(value = "keyword") String keyword);

    UserTO userByCode(@NativeQueryParam(value = "userCode") String userCode);


    Page<UserLevelReportTO> userReferralCodes(@NativeQueryParam(value = "userCode") String userCode,
                                              @NativeQueryParam(value = "userType") String userType,
                                              @NativeQueryParam(value = "filterUserLevel") String filterUserLevel,
                                              @NativeQueryParam(value = "filterUserCode") String filterUserCode,
                                              @NativeQueryParam(value = "filterByUsername") String filterByUsername,
                                              Pageable pageable);

    List<UserLevelReportTO> userReferralCodesList(@NativeQueryParam(value = "userCode") String userCode,
                                              @NativeQueryParam(value = "userType") String userType,
                                              @NativeQueryParam(value = "filterUserLevel") String filterUserLevel,
                                              @NativeQueryParam(value = "filterUserCode") String filterUserCode,
                                              @NativeQueryParam(value = "filterByUsername") String filterByUsername);

    @Transactional
    List<UserLevelReportTO> userLevelFilter(@NativeQueryParam(value = "userCode") String userCode,
                                            @NativeQueryParam(value = "userType") String userType,
                                            @NativeQueryParam(value = "filterByUsername") String filterByUsername,
                                            @NativeQueryParam(value = "filterUserLevel") String filterUserLevel,
                                            @NativeQueryParam(value = "filterUserCode") String filterUserCode, boolean userOnline);

    List<UserLevelReportTO> userReferralWithCommission(@NativeQueryParam(value = "userCode") String userCode,
                                                       @NativeQueryParam(value = "userType") String userType,
                                                       @NativeQueryParam(value = "filterUserLevel") String filterUserLevel,
                                                       @NativeQueryParam(value = "filterUserCode") String filterUserCode,
                                                       @NativeQueryParam(value = "filterByUsername") String filterByUsername,
                                                       @NativeQueryParam(value = "rebateRateTwoD") String rebateRateTwoD,
                                                       @NativeQueryParam(value = "rebateRateThreeD") String rebateRateThreeD,
                                                       boolean userOnline
    );

    @Transactional
    List<UserTO> listMemberByCodeIn(List<String> userCodes);

}
