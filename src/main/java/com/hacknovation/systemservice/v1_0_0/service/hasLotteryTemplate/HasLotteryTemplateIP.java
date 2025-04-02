package com.hacknovation.systemservice.v1_0_0.service.hasLotteryTemplate;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.HasLotteryTemplateEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.HasLotteryTemplateRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.service.cache.UserCacheSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.hasLotteryTemplate.HasLotteryTemplateDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.hasLotteryTemplate.HasLotteryTemplateListRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.hasLotteryTemplate.HasLotteryTemplateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
 * author: kangto
 * createdAt: 30/11/2022
 * time: 12:55
 */
@Service
@RequiredArgsConstructor
public class HasLotteryTemplateIP extends BaseServiceIP implements HasLotteryTemplateSV {

    private final HasLotteryTemplateRP hasLotteryTemplateRP;
    private final ActivityLogUtility activityLogUtility;
    private final JwtToken jwtToken;
    private final HttpServletRequest httpServletRequest;
    private final UserCacheSV userCacheSV;

    @Override
    public StructureRS listHasLotteryTemplate() {
        HasLotteryTemplateListRQ listRQ = new HasLotteryTemplateListRQ(httpServletRequest);

        List<HasLotteryTemplateDTO> itemRSList = hasLotteryTemplateRP.getItemList(
                listRQ.getFilterByDayOfWeek(),
                listRQ.getFilterByLotteryType(),
                listRQ.getFilterByUserCode()
        );

        return responseBodyWithSuccessMessage(itemRSList);
    }

    @Override
    public StructureRS createOrUpdateTemplate(HasLotteryTemplateRQ templateRQ) {

        UserToken userToken = jwtToken.getUserToken();

        HasLotteryTemplateEntity itemEntity = hasLotteryTemplateRP.getOneTemplate(
                templateRQ.getDayOfWeek(),
                templateRQ.getLotteryType(),
                templateRQ.getUserCode(),
                templateRQ.getPostCode(),
                templateRQ.getRebateCode()
                );
        String actionType = ActivityLogConstant.ACTION_UPDATE;
        if (itemEntity == null) {
            actionType = ActivityLogConstant.ACTION_ADD;
            itemEntity = new HasLotteryTemplateEntity();
            itemEntity.setDayOfWeek(templateRQ.getDayOfWeek());
            itemEntity.setLotteryCode(templateRQ.getLotteryType());
            itemEntity.setUserCode(templateRQ.getUserCode());
            itemEntity.setPostCode(templateRQ.getPostCode());
            itemEntity.setRebateCode(templateRQ.getRebateCode());
        }

        itemEntity.setStatus(templateRQ.getStatus());
        itemEntity.setLimitDigit(templateRQ.getLimitDigit());
        itemEntity.setUpdatedBy(userToken.getUsername());

        hasLotteryTemplateRP.save(itemEntity);

        activityLogUtility.addActivityLog(templateRQ.getLotteryType(),
                ActivityLogConstant.MODULE_HAS_LOTTERY_TEMPLATE,
                userToken.getUserType(),
                actionType,
                templateRQ.getUserCode(),
                templateRQ);

        String key = itemEntity.getDayOfWeek()
                .concat("_")
                .concat(itemEntity.getLotteryCode())
                .concat("_")
                .concat(itemEntity.getUserCode());
        userCacheSV.removeHasLotteryTemplate(key.toUpperCase());

        return responseBodyWithSuccessMessage();
    }

}
