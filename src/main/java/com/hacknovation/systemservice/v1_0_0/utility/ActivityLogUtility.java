package com.hacknovation.systemservice.v1_0_0.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.ActivityLogEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.ActivityLogRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.DrawItemRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.activityLog.AuthRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.activityLog.ResetPasswordRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 28/12/2021
 * time: 13:41
 */
@Component
@RequiredArgsConstructor
public class ActivityLogUtility {

    private final ActivityLogRP activityLogRP;
    private final HttpServletRequest request;
    private final com.fasterxml.jackson.databind.ObjectMapper mapper;
    private final JwtToken jwtToken;

    /**
     * common log activity
     *
     * @param lotteryType String
     * @param moduleName  String
     * @param userType    String
     * @param actionType  String
     * @param userCode    String
     * @param object      Object
     */
    public void addActivityLog(String lotteryType, String moduleName, String userType, String actionType, String userCode, Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ActivityLogEntity activityLogEntity = new ActivityLogEntity();

            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }

            activityLogEntity.setLotteryType(lotteryType);
            activityLogEntity.setModuleName(moduleName);
            activityLogEntity.setUserType(userType);
            activityLogEntity.setActionType(actionType);
            activityLogEntity.setUserCode(userCode);
            activityLogEntity.setIpAddress(ipAddress);
            activityLogEntity.setUserAgent(request.getHeader("user-agent"));
            activityLogEntity.setDescription(objectMapper.writeValueAsString(object));

            activityLogRP.save(activityLogEntity);
        } catch (Exception ex) {
            System.out.println("Warning => ActivityLogUtility.addActivityLog");
            System.out.println(ex.getMessage());
        }
    }

    public void addActivityLog(String lotteryType, String moduleName, String userType, String actionType, String userCode, String object) {
        try {
            ActivityLogEntity activityLogEntity = new ActivityLogEntity();

            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }

            activityLogEntity.setLotteryType(lotteryType);
            activityLogEntity.setModuleName(moduleName);
            activityLogEntity.setUserType(userType);
            activityLogEntity.setActionType(actionType);
            activityLogEntity.setUserCode(userCode);
            activityLogEntity.setIpAddress(ipAddress);
            activityLogEntity.setUserAgent(request.getHeader("user-agent"));
            activityLogEntity.setDescription(object);

            activityLogRP.save(activityLogEntity);
        } catch (Exception ex) {
            System.out.println("Warning => ActivityLogUtility.addActivityLog");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @param lotteryType         String
     * @param drawingDTO          DrawingDTO
     * @param userToken           UserToken
     * @param drawItemRQ          DrawItemRQ
     * @param activityLogEntities List<ActivityLogEntity>
     */
    public void logSetResult(String lotteryType, DrawingDTO drawingDTO, UserToken userToken, DrawItemRQ drawItemRQ, List<ActivityLogEntity> activityLogEntities) {
        try {
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }

            ActivityLogEntity activityLogEntity = new ActivityLogEntity();
            activityLogEntity.setDrawAt(drawingDTO.getResultedPostAt());
            activityLogEntity.setLotteryType(lotteryType);
            activityLogEntity.setActionType(ActivityLogConstant.SET_RESULT);
            activityLogEntity.setModuleName(ActivityLogConstant.RESULT);
            activityLogEntity.setDescription(mapper.writeValueAsString(drawItemRQ));
            activityLogEntity.setDrawCode(drawingDTO.getDrawCode());
            activityLogEntity.setPostCode(drawItemRQ.getPostCode());
            activityLogEntity.setUserCode(userToken.getUserCode());
            activityLogEntity.setUserAgent(userToken.getAgentCode());
            activityLogEntity.setUserType(userToken.getUserType());
            activityLogEntity.setIpAddress(ipAddress);

            activityLogEntities.add(activityLogEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * log login
     *
     * @param userEntity UserEntity
     */
    public void logLoginActivity(UserEntity userEntity) {
        AuthRS item = new AuthRS();
        item.setDeviceType(request.getHeader("X-DeviceType"));
        item.setIpAddress(request.getRemoteAddr());
        item.setLoginAt(userEntity.getUpdatedAt());
        addActivityLog("", ActivityLogConstant.MODULE_AUTH, userEntity.getRoleCode(), ActivityLogConstant.ACTION_LOGIN, userEntity.getCode(), item);
    }

    /**
     * log reset password
     *
     * @param userEntity UserEntity
     */
    public void resetPasswordActivity(UserEntity userEntity) {
        ResetPasswordRS item = new ResetPasswordRS();
        item.setResetAt(userEntity.getUpdatedAt());
        item.setDeviceType(request.getHeader("X-DeviceType"));
        item.setIpAddress(request.getRemoteAddr());
        addActivityLog("", ActivityLogConstant.MODULE_AUTH, userEntity.getRoleCode(), ActivityLogConstant.ACTION_RESET_PASSWORD, userEntity.getCode(), item);
    }

    public List<ActivityLogEntity> getActivityLog(String lottery, String module, String username) {
        return activityLogRP.getLog(lottery, module, username, jwtToken.getUserToken().getUserCode());
    }

    public List<String> getModuleOption() {
        List<ActivityLogEntity> activityLogEntities = activityLogRP.getLogByModule();
        return activityLogEntities.stream().map(ActivityLogEntity::getModuleName).collect(Collectors.toList());
    }
}
