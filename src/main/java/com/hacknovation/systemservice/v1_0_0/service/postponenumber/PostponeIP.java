package com.hacknovation.systemservice.v1_0_0.service.postponenumber;

import com.hacknovation.systemservice.constant.HazelcastConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.PostponeNumberEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasPostponeNumberEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.postpone.PostponeNumberNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.postpone.PostponeNumberTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.PostponeNumberRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserHasPostponeNumberRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.ListUserHasPostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.PostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.UpdateUserHasPostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.postponenumber.PostponeItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.postponenumber.PostponeListRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.postponenumber.PostponeRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.UserHasPostponeUtility;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 21/01/2022
 * time: 18:41
 */
@Service
@RequiredArgsConstructor
public class PostponeIP extends BaseServiceIP implements PostponeSV {

    private final UserRP userRP;
    private final UserHasPostponeNumberRP userHasPostponeNumberRP;
    private final PostponeNumberRP postponeNumberRP;
    private final PostponeNumberNQ postponeNumberNQ;
    private final TempDrawingNQ tempDrawingNQ;
    private final HttpServletRequest request;
    private final UserHasPostponeUtility userHasPostponeUtility;
    private final GeneralUtility generalUtility;
    private final HazelcastInstance hazelcastInstance;

    @Override
    public StructureRS listing() {
        List<PostponeRS> postponeRSList = new ArrayList<>();
        List<PostponeNumberTO> postponeNumberTOS = postponeNumberNQ.postponeNumberList();
        postponeNumberTOS.forEach(item -> {
            PostponeRS postponeRS = new PostponeRS();
            BeanUtils.copyProperties(item, postponeRS);
            if (LotteryConstant.VN2.equals(item.getLotteryType()))
                postponeRS.setLotteryType(LotteryConstant.MT);
            postponeRSList.add(postponeRS);
        });
        return responseBodyWithSuccessMessage(postponeRSList);
    }

    @Override
    public StructureRS updatePostpone(PostponeRQ postponeRQ) {
        if (LotteryConstant.MT.equals(postponeRQ.getLotteryType()))
            postponeRQ.setLotteryType(LotteryConstant.VN2);

        PostponeNumberEntity postponeNumberEntity = postponeNumberRP.postponeNumber(postponeRQ.getLotteryType());
        if (postponeNumberEntity == null)
            postponeNumberEntity = new PostponeNumberEntity();
        postponeNumberEntity.setLotteryType(postponeRQ.getLotteryType());
        postponeNumberEntity.setNumberDetail(postponeRQ.getNumberDetail());
        postponeNumberEntity.setStatus(postponeRQ.getStatus());
        postponeNumberRP.save(postponeNumberEntity);
        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS userHasPostponeList() {
        ListUserHasPostponeRQ listUserHasPostponeRQ = new ListUserHasPostponeRQ(request);
        UserEntity userEntity = userRP.findByCode(listUserHasPostponeRQ.getFilterByUserCode());
        if (userEntity == null)
            return responseBodyWithBadRequest(MessageConstant.USER_COULD_NOT_BE_FOUND, MessageConstant.USER_COULD_NOT_BE_FOUND_KEY);

        DrawingDTO drawingDTO = null;

        switch (listUserHasPostponeRQ.getFilterByLotteryType()) {
            case LotteryConstant.VN1:
                drawingDTO = tempDrawingNQ.vnOneTempDrawing(listUserHasPostponeRQ.getFilterByDrawCode());
                break;
            case LotteryConstant.VN2:
                drawingDTO = tempDrawingNQ.vnTwoTempDrawing(listUserHasPostponeRQ.getFilterByDrawCode());
                break;
            case LotteryConstant.TN:
                drawingDTO = tempDrawingNQ.tnTempDrawing(listUserHasPostponeRQ.getFilterByDrawCode());
                break;
            case LotteryConstant.LEAP:
                drawingDTO = tempDrawingNQ.leapTempDrawing(listUserHasPostponeRQ.getFilterByDrawCode());
                break;
            case LotteryConstant.SC:
                drawingDTO = tempDrawingNQ.scTempDrawing(listUserHasPostponeRQ.getFilterByDrawCode());
                break;
            case LotteryConstant.TH:
                drawingDTO = tempDrawingNQ.thTempDrawing(listUserHasPostponeRQ.getFilterByDrawCode());
                break;
            case LotteryConstant.KH:
                drawingDTO = tempDrawingNQ.khTempDrawing(listUserHasPostponeRQ.getFilterByDrawCode());
                break;
        }

        if (drawingDTO == null)
            return responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);

        List<UserHasPostponeNumberEntity> userHasPostponeNumberEntities = userHasPostponeNumberRP.findAllByLotteryTypeAndDrawCodeInAndUserCodeOrderByNumberAsc(listUserHasPostponeRQ.getFilterByLotteryType(), List.of(drawingDTO.getDrawCode()), userEntity.getCode());
        PostponeListRS postponeListRS = new PostponeListRS();
        postponeListRS.setLotteryType(listUserHasPostponeRQ.getFilterByLotteryType());
        postponeListRS.setDrawCode(drawingDTO.getDrawCode());
        postponeListRS.setDrawAt(drawingDTO.getResultedPostAt());
        postponeListRS.setItems(getPostponeItemRSList(userHasPostponeNumberEntities.stream().filter(it-> (!it.getIsAllMember() && !it.getIsDefault())).collect(Collectors.toList())));
        postponeListRS.setAllMembers(getPostponeItemRSList(userHasPostponeNumberEntities.stream().filter(it-> (it.getIsAllMember() && !it.getIsDefault())).collect(Collectors.toList())));
        postponeListRS.setDefaultItems(getPostponeItemRSList(userHasPostponeNumberEntities.stream().filter(UserHasPostponeNumberEntity::getIsDefault).collect(Collectors.toList())));

        return responseBodyWithSuccessMessage(postponeListRS);
    }

    @Override
    public StructureRS updateUserHasPostpone(UpdateUserHasPostponeRQ postponeRQ) {

        DrawingDTO drawingDTO = new DrawingDTO();
        switch (postponeRQ.getLotteryType()) {
            case LotteryConstant.VN1:
                drawingDTO = tempDrawingNQ.vnOneTempDrawing(postponeRQ.getDrawCode());
                break;
            case LotteryConstant.VN2:
                drawingDTO = tempDrawingNQ.vnTwoTempDrawing(postponeRQ.getDrawCode());
                break;
            case LotteryConstant.TN:
                drawingDTO = tempDrawingNQ.tnTempDrawing(postponeRQ.getDrawCode());
                break;
            case LotteryConstant.LEAP:
                drawingDTO = tempDrawingNQ.leapTempDrawing(postponeRQ.getDrawCode());
                break;
            case LotteryConstant.SC:
                drawingDTO = tempDrawingNQ.scTempDrawing(postponeRQ.getDrawCode());
                break;
            case LotteryConstant.KH:
                drawingDTO = tempDrawingNQ.khTempDrawing(postponeRQ.getDrawCode());
                break;
            case LotteryConstant.TH:
                drawingDTO = tempDrawingNQ.thTempDrawing(postponeRQ.getDrawCode());
                break;
        }

        if (drawingDTO == null) {
            return  responseBodyWithBadRequest(MessageConstant.DRAW_CODE_NOT_FOUND, MessageConstant.DRAW_CODE_NOT_FOUND_KEY);
        }

        if (!generalUtility.formatDateYYYYMMDD(new Date()).equals(generalUtility.formatDateYYYYMMDD(drawingDTO.getResultedPostAt()))) {
            return  responseBodyWithBadRequest(MessageConstant.UNABLE_TO_EDIT_RIGHT_NOW, MessageConstant.UNABLE_TO_EDIT_RIGHT_NOW_KEY);
        }
        if (postponeRQ.getIsDefault()) {
            postponeRQ.setIsAllMember(false);
        }

        List<String> numbers = userHasPostponeUtility.getListNumbers(postponeRQ.getNumberDetail());

        if (!numbers.isEmpty()) {
            userHasPostponeNumberRP.deleteAllByLotteryTypeAndDrawCodeAndUserCodeAndNumberInAndIsAllMemberAndIsDefault(
                    postponeRQ.getLotteryType(),
                    postponeRQ.getDrawCode(),
                    postponeRQ.getUserCode(),
                    numbers,
                    postponeRQ.getIsAllMember(),
                    postponeRQ.getIsDefault());
            List<UserHasPostponeNumberEntity> userHasPostponeNumberEntities = new ArrayList<>();
            for (String num : numbers) {
                UserHasPostponeNumberEntity item = new UserHasPostponeNumberEntity();
                BeanUtils.copyProperties(postponeRQ, item);
                item.setNumber(num);
                item.setShiftCode(drawingDTO.getShiftCode());
                userHasPostponeNumberEntities.add(item);
            }
            userHasPostponeNumberRP.saveAll(userHasPostponeNumberEntities);

            String key = String.format("%s_%s_%s", postponeRQ.getLotteryType(), postponeRQ.getDrawCode(), postponeRQ.getUserCode()).toUpperCase();
            removeCacheUserHasPostponeNumber(key);
        }

        return responseBodyWithSuccessMessage();

    }

    @Override
    public StructureRS removeUserHasPostpone(Integer itemId) {
        Optional<UserHasPostponeNumberEntity> optUserHasPostpone = userHasPostponeNumberRP.findById(itemId.longValue());
        if (optUserHasPostpone.isPresent()) {
            UserHasPostponeNumberEntity item = optUserHasPostpone.get();
            String key = String.format("%s_%s_%s", item.getLotteryType(), item.getDrawCode(), item.getUserCode()).toUpperCase();
            removeCacheUserHasPostponeNumber(key);
            userHasPostponeNumberRP.deleteById(itemId.longValue());
            return responseBodyWithSuccessMessage();
        }

        return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);
    }


    private List<PostponeItemRS> getPostponeItemRSList(List<UserHasPostponeNumberEntity> userHasPostponeNumberEntities) {
        List<PostponeItemRS> postponeItemRSList = new ArrayList<>();
        userHasPostponeNumberEntities.forEach(item -> {
            PostponeItemRS postponeItemRS = new PostponeItemRS();
            BeanUtils.copyProperties(item, postponeItemRS);
            postponeItemRS.setItemId(item.getId().intValue());
            postponeItemRSList.add(postponeItemRS);
        });

        return postponeItemRSList;
    }

    private void removeCacheUserHasPostponeNumber(String key) {
        hazelcastInstance.getMap(HazelcastConstant.USER_HAS_POSTPONE_NUMBER_COLLECTION).delete(key);
    }
}
