package com.hacknovation.systemservice.v1_0_0.service.resultBanhji;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.resultBanhji.ResultBanhjiItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.resultBanhji.ResultBanhjiRS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 21/11/2022
 * time: 11:04
 */
@Service
@RequiredArgsConstructor
public class ResultBanhjiIP implements ResultBanhjiSV {

    private final LeapTempDrawingRP leapTempDrawingRP;
    private final LeapTempDrawingItemsRP leapTempDrawingItemsRP;
    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final VNTwoTempDrawingItemsRP vnTwoTempDrawingItemsRP;

    @Override
    public ResultBanhjiRS getResultBanhjiByLotteryAndDate(String lotteryType, String drawAt) {
        ResultBanhjiRS resultBanhjiRS = new ResultBanhjiRS();
        resultBanhjiRS.setLotteryType(lotteryType);
        switch (lotteryType) {
            case LotteryConstant.MT:
            case LotteryConstant.VN2:
                VNTwoTempDrawingEntity twoTempDrawing = vnTwoTempDrawingRP.getDrawingByDrawAt(drawAt);
                if (twoTempDrawing != null && !twoTempDrawing.getIsRecent()) {
                    resultBanhjiRS.setStatus(1);
                    resultBanhjiRS.setIsNight(twoTempDrawing.getIsNight());
                    List<VNTwoTempDrawingItemsEntity> columnOne = vnTwoTempDrawingItemsRP.getAllByDrawIdAndColumnNumber(twoTempDrawing.getId(), 1);
                    Map<String, VNTwoTempDrawingItemsEntity> mapByPostCode = columnOne.stream().collect(Collectors.toMap(VNTwoTempDrawingItemsEntity::getPostCode, Function.identity()));
                    List<String> posts = twoTempDrawing.getIsNight() ? PostConstant.VN_NIGHT : PostConstant.VN_DAY;
                    for (String post : posts) {
                        ResultBanhjiItemRS item = new ResultBanhjiItemRS();
                        item.setPostCode(post);
                        if (mapByPostCode.containsKey(post)) {
                            VNTwoTempDrawingItemsEntity drawingItem = mapByPostCode.get(post);
                            item.setPostGroup(drawingItem.getPostGroup());
                            item.setTwoDigits(drawingItem.getTwoDigits());
                            item.setThreeDigits(drawingItem.getThreeDigits());
                            item.setNumber(drawingItem.getFiveDigits());
                            if (PostConstant.LO_1_TO_4.contains(post) && !twoTempDrawing.getIsNight()) {
                                item.setNumber(drawingItem.getFourDigits());
                            }
                            if (PostConstant.LO_10_TO_19.contains(post) && twoTempDrawing.getIsNight()) {
                                item.setNumber(drawingItem.getFourDigits());
                            }
                        }
                        resultBanhjiRS.getResults().add(item);
                    }
                }
                break;

            case LotteryConstant.LEAP:
                LeapTempDrawingEntity leapTempDrawing = leapTempDrawingRP.getDrawingByDrawAt(drawAt);
                if (leapTempDrawing != null && !leapTempDrawing.getIsRecent()) {
                    resultBanhjiRS.setStatus(1);
                    List<LeapTempDrawingItemsEntity> drawingItemsEntities = leapTempDrawingItemsRP.getAllByDrawId(leapTempDrawing.getId());
                    for (LeapTempDrawingItemsEntity drawingItems : drawingItemsEntities) {
                        ResultBanhjiItemRS itemRS = new ResultBanhjiItemRS();
                        BeanUtils.copyProperties(drawingItems, itemRS);
                        itemRS.setNumber(drawingItems.getFiveDigits());
                        if (LotteryConstant.POST.equals(drawingItems.getPostGroup()))
                            itemRS.setNumber(drawingItems.getSixDigits());
                        resultBanhjiRS.getResults().add(itemRS);
                    }
                }
                break;
        }

        return resultBanhjiRS;
    }
}
