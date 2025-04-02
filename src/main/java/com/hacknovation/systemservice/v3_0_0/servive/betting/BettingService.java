package com.hacknovation.systemservice.v3_0_0.servive.betting;

import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.TrackUserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.TrackUserHasLotteryRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.userhaslottery.UserHasLotteryRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.UserHasLotteryJsonUtility;
import com.hacknovation.systemservice.v3_0_0.servive.betting.request.BaseBanhJiBettingRQ;
import com.hacknovation.systemservice.v3_0_0.servive.betting.request.BettingRQ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sombath
 * create at 23/6/23 1:27 AM
 */

@Service
@RequiredArgsConstructor
public class BettingService extends BaseServiceIP {

    private final JwtToken jwtToken;
    private final UserRP userRP;
    private final TrackUserHasLotteryRP trackUserHasLotteryRP;
    private final UserHasLotteryJsonUtility userHasLotteryJsonUtility;

    public StructureRS betting(BaseBanhJiBettingRQ request) {
        this.placeBet(request);
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
    }

    private void placeBet(BaseBanhJiBettingRQ request) {

        BaseDrawingEntity drawingEntity = BettingHelper.BANHJI.getDraw(request.getLotteryType(), request.getDrawCode());
        if (drawingEntity == null)
            throw new BadRequestException(MessageConstant.DRAW_CODE_NOT_FOUND);

        UserEntity userEntity = userRP.findByCode(request.getUserCode());
        if (userEntity == null)
            throw new BadRequestException(MessageConstant.USER_COULD_NOT_BE_FOUND);
        if (!userEntity.getRoleCode().equalsIgnoreCase(UserConstant.MEMBER))
            throw new BadRequestException(String.format("user %s is not a member", userEntity.getUsername()));

        TrackUserHasLotteryEntity trackUserHasLotteryEntity = trackUserHasLotteryRP.getLastUserHasLottery(request.getLotteryType(), userEntity.getCode());
        UserHasLotteryRS userHasLotteryRS = userHasLotteryJsonUtility.getUserHasLotteryRSFromTrackEntity(trackUserHasLotteryEntity);

        BaseOrderEntity orderEntity = new BaseOrderEntity();
        orderEntity.setUserCode(userEntity.getCode());
        orderEntity.setDrawCode(drawingEntity.getCode());
        orderEntity.setDrawAt(drawingEntity.getResultedPostAt());
        orderEntity.setCreatedBy(jwtToken.getUserToken().getUserCode());
        orderEntity.setPageNumber(request.getPageNumber());
        orderEntity.setPlatformType("PAPER");
        orderEntity.setColumnNumber(request.getColumnNumber());
        orderEntity.setUserHasLotteryId(trackUserHasLotteryEntity.getId().intValue());

        orderEntity = BettingHelper.BANHJI.saveOrder(request.getLotteryType(), orderEntity);

        List<BaseOrderItemsEntity> tempOrderItemEntities = new ArrayList<>();
        if (orderEntity != null && orderEntity.getId() != null)
            for (BettingRQ item : request.getBetting()) {
                BaseOrderItemsEntity tempOrderItemEntity = new BaseOrderItemsEntity();
                tempOrderItemEntity.setOrderId(orderEntity.getId().intValue());
                tempOrderItemEntity.setTicketNumber(orderEntity.getTicketNumber());

                tempOrderItemEntity.setPageNumber(request.getPageNumber());
                tempOrderItemEntity.setColumnNumber(request.getColumnNumber());
                tempOrderItemEntity.setSectionNumber(item.getSectionNumber());
                tempOrderItemEntity.setBetType(item.getBetType());
                tempOrderItemEntity.setBetTitle(item.getBetTitle());
                tempOrderItemEntity.setNumberDetail(item.getNumberDetail());

                BettingHelper.BANHJI.setRebateAndCommission(tempOrderItemEntity, userHasLotteryRS);

                // Set amount
                int posts = BettingHelper.BANHJI.getTotalPostLo(drawingEntity.getIsNight(), tempOrderItemEntity.getRebateCode(), item.getPosts(), item.getIsLo());
                int numberQuantities = item.getNumberDetail().replaceAll("\\s+", "").split(",").length;
                tempOrderItemEntity.setNumberQuantity(numberQuantities);
                tempOrderItemEntity.setTotalAmount(item.getBetAmount().multiply(BigDecimal.valueOf(posts).multiply(BigDecimal.valueOf(numberQuantities))).setScale(2, RoundingMode.CEILING));
                tempOrderItemEntities.add(tempOrderItemEntity);
            }

        BettingHelper.BANHJI.saveOrderItem(request.getLotteryType(), tempOrderItemEntities);
    }

}
