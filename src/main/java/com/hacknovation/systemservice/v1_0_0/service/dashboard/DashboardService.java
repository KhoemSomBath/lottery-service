package com.hacknovation.systemservice.v1_0_0.service.dashboard;

import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.dashboard.UserSummaryRS;
import com.hacknovation.systemservice.v1_0_0.utility.SqlUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Sombath
 * create at 5/10/22 10:58 AM
 */

@Service
@RequiredArgsConstructor
public class DashboardService extends BaseServiceIP {

    private final SqlUtility sqlUtility;
    private final JwtToken jwtToken;

    @Value("classpath:query/dashboard/count-user-level.sql")
    Resource userSummariesSql;

    public StructureRS getUserSummaries(){

        UserToken userToken = jwtToken.getUserToken();
        String userRole = userToken.getUserRole();
        String userCode = userToken.getUserCode();

        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM))
            userRole = userToken.getUserType();

        if (userRole.equalsIgnoreCase(UserConstant.SUB_ACCOUNT)) {
            if(userToken.getParentId() == null)
                throw new BadRequestException(MessageConstant.COULD_NOT_FIND_PARENT_USER);
            userCode = userToken.getParentCode();
            userRole = userToken.getParentRole();
        }

        Map<String, Object> param = Map.of(
                "userCode", userCode,
                "roleCode", userRole
        );

        List<UserSummaryRS> userSummaryRS = sqlUtility.executeQueryForList(userSummariesSql, param, UserSummaryRS.class);
        return response(userSummaryRS);
    }

}
