package com.hacknovation.systemservice.v3_0_0.servive.betting;

import com.hacknovation.systemservice.constant.BettingConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.PagingTempEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.PagingTempRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.userhaslottery.UserHasLotteryRS;
import com.hacknovation.systemservice.v3_0_0.servive.betting.request.PagingRQ;
import com.hacknovation.systemservice.v3_0_0.servive.betting.request.RemovePageRQ;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hacknovation.systemservice.constant.BettingConstant.getListPosts;

/**
 * @author Sombath
 * create at 23/6/23 2:39 AM
 */

public class BettingHelper {

    @Service
    public static class BANHJI {
        private static VNOneTempDrawingRP vnOneTempDrawingRP;
        private static VNOneTempOrderRP vnOneTempOrderRP;
        private static VNOneTempOrderItemsRP vnOneTempOrderItemsRP;

        public BANHJI(VNOneTempDrawingRP vnOneTempDrawingRP, VNOneTempOrderRP vnOneTempOrderRP, VNOneTempOrderItemsRP vnOneTempOrderItemsRP) {
            BANHJI.vnOneTempDrawingRP = vnOneTempDrawingRP;
            BANHJI.vnOneTempOrderRP = vnOneTempOrderRP;
            BANHJI.vnOneTempOrderItemsRP = vnOneTempOrderItemsRP;
        }

        public static Integer getTotalPostLo(Boolean isNight, String rebateCode, String postStr, boolean isLo) {
            if (isLo)
                postStr = postStr.concat("Lo");
            List<String> posts = new ArrayList<>(getListPosts(postStr, isNight));
            int lo = posts.size();
            if (isNight) {
                if (isLo && rebateCode.equals(BettingConstant.TWOD)) {
                    return BettingConstant.LO_2D_NIGHT;
                }
                if (isLo && rebateCode.equals(BettingConstant.THREED)) {
                    return BettingConstant.LO_3D_NIGHT;
                }
            } else {
                if (isLo && rebateCode.equals(BettingConstant.TWOD)) {
                    String[] post = postStr
                            .replace("Lo", "")
                            .replaceAll("\\s+", "")
                            .split("");
                    if (post.length > 0) {
                        lo += post.length;
                    }
                }
            }

            if (posts.contains("A4") && BettingConstant.THREED.equals(rebateCode))
                lo -= 1;

            return lo;
        }

        public static void setRebateAndCommission(BaseOrderItemsEntity item, UserHasLotteryRS uhr) {
            String[] numDetail = item.getNumberDetail().replaceAll("\\s+", "").split(",");
            if (numDetail.length > 0)
                item.setRebateCode(String.valueOf(numDetail[0].length()).concat("D"));
            if (uhr != null) {
                switch (item.getRebateCode()) {
                    case LotteryConstant.REBATE1D:
                        item.setCommission(uhr.getMember().getOneD().getCommission());
                        item.setRebateRate(uhr.getMember().getOneD().getRebateRate());
                        item.setAgentCommission(uhr.getAgent().getOneD().getCommission());
                        item.setAgentRebateRate(uhr.getAgent().getOneD().getRebateRate());
                        item.setMasterCommission(uhr.getMaster().getOneD().getCommission());
                        item.setMasterRebateRate(uhr.getMember().getOneD().getRebateRate());
                        item.setSeniorCommission(uhr.getSenior().getOneD().getCommission());
                        item.setSeniorRebateRate(uhr.getSenior().getOneD().getRebateRate());
                        item.setSuperSeniorCommission(uhr.getSenior().getOneD().getCommission());
                        item.setSuperSeniorRebateRate(uhr.getSenior().getOneD().getRebateRate());
                        break;
                    case LotteryConstant.REBATE2D:
                        item.setCommission(uhr.getMember().getTwoD().getCommission());
                        item.setRebateRate(uhr.getMember().getTwoD().getRebateRate());
                        item.setAgentCommission(uhr.getAgent().getTwoD().getCommission());
                        item.setAgentRebateRate(uhr.getAgent().getTwoD().getRebateRate());
                        item.setMasterCommission(uhr.getMaster().getTwoD().getCommission());
                        item.setMasterRebateRate(uhr.getMember().getTwoD().getRebateRate());
                        item.setSeniorCommission(uhr.getSenior().getTwoD().getCommission());
                        item.setSeniorRebateRate(uhr.getSenior().getTwoD().getRebateRate());
                        item.setSuperSeniorCommission(uhr.getSenior().getTwoD().getCommission());
                        item.setSuperSeniorRebateRate(uhr.getSenior().getTwoD().getRebateRate());
                        break;
                    case LotteryConstant.REBATE3D:
                        item.setCommission(uhr.getMember().getThreeD().getCommission());
                        item.setRebateRate(uhr.getMember().getThreeD().getRebateRate());
                        item.setAgentCommission(uhr.getAgent().getThreeD().getCommission());
                        item.setAgentRebateRate(uhr.getAgent().getThreeD().getRebateRate());
                        item.setMasterCommission(uhr.getMaster().getThreeD().getCommission());
                        item.setMasterRebateRate(uhr.getMember().getThreeD().getRebateRate());
                        item.setSeniorCommission(uhr.getSenior().getThreeD().getCommission());
                        item.setSeniorRebateRate(uhr.getSenior().getThreeD().getRebateRate());
                        item.setSuperSeniorCommission(uhr.getSenior().getThreeD().getCommission());
                        item.setSuperSeniorRebateRate(uhr.getSenior().getThreeD().getRebateRate());
                        break;
                    case LotteryConstant.REBATE4D:
                        item.setCommission(uhr.getMember().getFourD().getCommission());
                        item.setRebateRate(uhr.getMember().getFourD().getRebateRate());
                        item.setAgentCommission(uhr.getAgent().getFourD().getCommission());
                        item.setAgentRebateRate(uhr.getAgent().getFourD().getRebateRate());
                        item.setMasterCommission(uhr.getMaster().getFourD().getCommission());
                        item.setMasterRebateRate(uhr.getMember().getFourD().getRebateRate());
                        item.setSeniorCommission(uhr.getSenior().getFourD().getCommission());
                        item.setSeniorRebateRate(uhr.getSenior().getFourD().getRebateRate());
                        item.setSuperSeniorCommission(uhr.getSenior().getFourD().getCommission());
                        item.setSuperSeniorRebateRate(uhr.getSenior().getFourD().getRebateRate());
                        break;
                }
            }

        }

        public static BaseDrawingEntity getDraw(String lottery, String code) {
            switch (lottery.toUpperCase()) {
                case LotteryConstant.VN1:
                    return vnOneTempDrawingRP.findByCode(code);
            }

            return null;
        }

        public static BaseOrderEntity saveOrder(String lottery, BaseOrderEntity baseOrderEntity) {
            switch (lottery.toUpperCase()) {
                case LotteryConstant.VN1:
                    VNOneTempOrdersEntity ordersEntity = new VNOneTempOrdersEntity();
                    BeanUtils.copyProperties(baseOrderEntity, ordersEntity);
                    return vnOneTempOrderRP.save(ordersEntity);
            }

            return null;
        }

        public static void saveOrderItem(String lottery, List<BaseOrderItemsEntity> baseOrderEntities) {
            switch (lottery.toUpperCase()) {
                case LotteryConstant.VN1:
                    List<VNOneTempOrderItemsEntity> items = baseOrderEntities.stream().map(it -> {
                        VNOneTempOrderItemsEntity item = new VNOneTempOrderItemsEntity();
                        BeanUtils.copyProperties(it, item);
                        return item;
                    }).collect(Collectors.toList());
                    vnOneTempOrderItemsRP.saveAll(items);
                    break;
            }
        }
    }


    @Service
    public static class PAGING {

        private static PagingTempRP pagingTempRP;

        public PAGING(PagingTempRP pagingTempRP) {
            PAGING.pagingTempRP = pagingTempRP;
        }

        public static List<PagingTempEntity> listing(PagingRQ pagingRQ) {
            return pagingTempRP.listing(pagingRQ.getLotteryCode(), pagingRQ.getDrawCode(), pagingRQ.getUserCode());
        }

        public static void add(PagingRQ pagingRQ) {
            PagingTempEntity pagingEntityLatest = pagingTempRP.findOnePageDesc(pagingRQ.getLotteryCode(), pagingRQ.getDrawCode(), pagingRQ.getUserCode());
            if (pagingEntityLatest == null) {
                pagingEntityLatest = new PagingTempEntity();
                BeanUtils.copyProperties(pagingRQ, pagingEntityLatest);
                pagingEntityLatest.setPageNumber(0);
            }

            PagingTempEntity pagingEntityTobeSave = new PagingTempEntity();
            BeanUtils.copyProperties(pagingEntityLatest, pagingEntityTobeSave);
            pagingEntityTobeSave.setId(null);
            pagingEntityTobeSave.setPageNumber(pagingEntityLatest.getPageNumber() + 1);
            if (pagingRQ.getPageNumber() != null) {
                List<PagingTempEntity> pagingEntities = pagingTempRP.findByLotteryCodeAndDrawCodeAndUserCodeAndPageNumber(pagingRQ.getLotteryCode(), pagingRQ.getDrawCode(), pagingRQ.getUserCode(), pagingRQ.getPageNumber());
                if (pagingEntities.size() == 0) {
                    pagingEntityTobeSave.setPageNumber(pagingRQ.getPageNumber());
                }
            }
            pagingTempRP.save(pagingEntityTobeSave);
        }

        public static void delete(Long id) {
            pagingTempRP.deleteById(id);
        }

        public static void delete(RemovePageRQ removePageRQ) {
            pagingTempRP.deletePageByUserCodeAndDrawCodeAndLotteryAndPageNumber(removePageRQ.getUserCode(), removePageRQ.getDrawCode(), removePageRQ.getLotteryType(), removePageRQ.getPageNumber());
        }


    }
}
