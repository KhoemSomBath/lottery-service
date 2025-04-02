package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCDrawingItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoDrawingItemsRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full.FullDrawItemRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full.FullResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full.FullResultRowRQ;
import com.hacknovation.systemservice.v1_0_0.utility.number.MapResultItemUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 27/10/2022
 * time: 15:33
 */
@Component
@RequiredArgsConstructor
public class ResultFullUtility {

    private final LeapDrawingItemsRP leapDrawingItemsRP;
    private final TNDrawingItemsRP tnDrawingItemsRP;
    private final VNOneDrawingItemsRP vnOneDrawingItemsRP;
    private final VNTwoDrawingItemsRP vnTwoDrawingItemsRP;
    private final MapResultItemUtility mapResultItemUtility;
    private final GeneralUtility generalUtility;
    private final SCDrawingItemRP sCDrawingItemRP;

    public void setTNOriginalOrderItem(FullResultRQ resultRQ, DrawingDTO drawingDTO) {
        List<TNDrawingItemsEntity> drawingItemsEntities = tnDrawingItemsRP.getAllByDrawId(drawingDTO.getId().longValue());
        List<String> posts = new ArrayList<>(PostConstant.GET_TN_POST_BY_SHIFT_CODE(drawingDTO.getShiftCode()));
        posts.removeAll(PostConstant.TN_REMOVAL_POST);

        Boolean isNight = drawingDTO.getIsNight();

        for (String post : posts) {
            List<TNDrawingItemsEntity> tnDrawingItemsEntities = drawingItemsEntities.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());
            List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            for (FullResultRowRQ postItemRQ : postItems)
                for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {

                    TNDrawingItemsEntity drawingItemsEntity = tnDrawingItemsEntities.stream()
                            .filter(it -> it.getPostCode().equals(post) && it.getColumnNumber().equals(columnItemRQ.getColumnNumber()))
                            .findAny().orElseThrow(BadRequestException::new);


                    if (columnItemRQ.getNumber().length() >= 4)
                        drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));


                    switch (columnItemRQ.getRebateCode()) {
                        case LotteryConstant.REBATE2D:
                            drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE3D:
                            drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE4D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE5D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                            drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                            break;
                        case LotteryConstant.REBATE6D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                            drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                            break;
                    }

                }

        }

        mapResultItemUtility.mappingTNOriginalResult(drawingItemsEntities, isNight);
        tnDrawingItemsRP.saveAll(drawingItemsEntities);
    }

    public void setVN1OriginalOrderItem(FullResultRQ resultRQ, DrawingDTO drawingDTO) {
        List<VNOneDrawingItemsEntity> vnOneDrawingItemsEntities = vnOneDrawingItemsRP.getAllByDrawId(drawingDTO.getId().longValue());

        Boolean isNight = drawingDTO.getIsNight();

        List<String> posts = new ArrayList<>(PostConstant.getVNPostByIsNight(isNight));
        posts.removeAll(PostConstant.VN_REMOVAL_POST);

        for (String post : posts) {
            List<VNOneDrawingItemsEntity> _vnOneDrawingItemsEntities = vnOneDrawingItemsEntities.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());
            List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            for (FullResultRowRQ postItemRQ : postItems)
                for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {

                    VNOneDrawingItemsEntity drawingItemsEntity = _vnOneDrawingItemsEntities.stream()
                            .filter(it -> it.getPostCode().equals(post) && it.getColumnNumber().equals(columnItemRQ.getColumnNumber()))
                            .findAny().orElseThrow(BadRequestException::new);


                    if (columnItemRQ.getNumber().length() >= 4)
                        drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));

                    switch (columnItemRQ.getRebateCode()) {
                        case LotteryConstant.REBATE2D:
                            drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE3D:
                            drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE4D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE5D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                            drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                            break;
                        case LotteryConstant.REBATE6D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                            drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                            break;
                    }

                }

        }

        mapResultItemUtility.mappingVN1OriginalResult(vnOneDrawingItemsEntities, drawingDTO.getResultedPostAt(), isNight);
        vnOneDrawingItemsRP.saveAll(vnOneDrawingItemsEntities);
    }

    public void setVN2OriginalOrderItem(FullResultRQ resultRQ, DrawingDTO drawingDTO) {
        List<VNTwoDrawingItemsEntity> vnTwoDrawingItemsEntities = vnTwoDrawingItemsRP.getAllByDrawId(drawingDTO.getId().longValue());

        Boolean isNight = drawingDTO.getIsNight();

        List<String> posts = new ArrayList<>(PostConstant.getVNPostByIsNight(isNight));

        posts.removeAll(PostConstant.VN_REMOVAL_POST);

        for (String post : posts) {
            List<VNTwoDrawingItemsEntity> _vnTwoDrawingItemsEntities = vnTwoDrawingItemsEntities.stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());
            List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            for (FullResultRowRQ postItemRQ : postItems)
                for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {

                    VNTwoDrawingItemsEntity drawingItemsEntity = _vnTwoDrawingItemsEntities.stream()
                            .filter(it -> it.getPostCode().equals(post) && it.getColumnNumber().equals(columnItemRQ.getColumnNumber()))
                            .findAny().orElseThrow(BadRequestException::new);


                    if (columnItemRQ.getNumber().length() >= 4)
                        drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));


                    switch (columnItemRQ.getRebateCode()) {
                        case LotteryConstant.REBATE2D:
                            drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE3D:
                            drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE4D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE5D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                            drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                            break;
                        case LotteryConstant.REBATE6D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                            drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                            break;
                    }

                }

        }

        mapResultItemUtility.mappingVN2OriginalResult(vnTwoDrawingItemsEntities, isNight);
        vnTwoDrawingItemsRP.saveAll(vnTwoDrawingItemsEntities);
    }

    public void setLeapOriginalOrderItem(FullResultRQ resultRQ, DrawingDTO drawingDTO) {
        List<LeapDrawingItemsEntity> leapDrawingItemsEntities = leapDrawingItemsRP.getAllByDrawId(drawingDTO.getId().longValue());
        Map<String, LeapDrawingItemsEntity> mapDrawingItemByPost = generalUtility.getMapByKeyPostCode(leapDrawingItemsEntities);

        for (String post : mapDrawingItemByPost.keySet()) {

            LeapDrawingItemsEntity drawingItemsEntity = mapDrawingItemByPost.get(post);

            List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            for (FullResultRowRQ postItemRQ : postItems)
                for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {
                    switch (columnItemRQ.getRebateCode()) {
                        case LotteryConstant.REBATE2D:
                            drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE3D:
                            drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE4D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE5D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));
                            drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                            drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                            break;
                        case LotteryConstant.REBATE6D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));
                            drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                            drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                            break;
                    }

                }

        }
        leapDrawingItemsRP.saveAll(leapDrawingItemsEntities);
    }

    public void setSCOriginalOrderItem(FullResultRQ resultRQ, DrawingDTO drawingDTO) {
        List<SCDrawingItemsEntity> scDrawingItemsEntities = sCDrawingItemRP.getAllByDrawingId(drawingDTO.getId().intValue());
        Map<String, SCDrawingItemsEntity> mapDrawingItemByPost = generalUtility.getMapByKeyPostCode(scDrawingItemsEntities);

        for (String post : mapDrawingItemByPost.keySet()) {

            SCDrawingItemsEntity drawingItemsEntity = mapDrawingItemByPost.get(post);

            List<FullResultRowRQ> postItems = resultRQ.getResults().stream().filter(it -> it.getPostCode().equals(post)).collect(Collectors.toList());

            for (FullResultRowRQ postItemRQ : postItems)
                for (FullDrawItemRQ columnItemRQ : postItemRQ.getItems()) {
                    switch (columnItemRQ.getRebateCode()) {
                        case LotteryConstant.REBATE2D:
                            drawingItemsEntity.setTwoDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE3D:
                            drawingItemsEntity.setThreeDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE4D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(columnItemRQ.getNumber());
                            break;
                        case LotteryConstant.REBATE5D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));
                            drawingItemsEntity.setFiveDigits(columnItemRQ.getNumber());
                            drawingItemsEntity.setSixDigits("*".concat(columnItemRQ.getNumber()));
                            break;
                        case LotteryConstant.REBATE6D:
                            drawingItemsEntity.setTwoDigits(StringUtils.right(columnItemRQ.getNumber(), 2));
                            drawingItemsEntity.setThreeDigits(StringUtils.right(columnItemRQ.getNumber(), 3));
                            drawingItemsEntity.setFourDigits(StringUtils.right(columnItemRQ.getNumber(), 4));
                            drawingItemsEntity.setFiveDigits(StringUtils.right(columnItemRQ.getNumber(), 5));
                            drawingItemsEntity.setSixDigits(columnItemRQ.getNumber());
                            break;
                    }

                }

        }
        sCDrawingItemRP.saveAll(scDrawingItemsEntities);
    }
}
