package com.hacknovation.systemservice.v1_0_0.ui.model.dto;

import com.hacknovation.systemservice.constant.BettingConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.DrawItemRQ;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigInteger;
import java.util.List;

@Data
public class DrawingItemsDTO {
    private BigInteger id;
    private Integer drawingId;
    private String postCode;
    private String postGroup;
    private String twoDigits;
    private String threeDigits;
    private String fourDigits;
    private String fiveDigits;
    private String sixDigits;

    private Integer columnNumber;
    private String provinceCode;
    private String rewardCode;
    private String rewardGroup;

    public DrawingItemsDTO() {
    }

    public DrawingItemsDTO(VNOneTempDrawingItemsEntity item) {
        BeanUtils.copyProperties(item, this);
    }

    public DrawingItemsDTO(TNTempDrawingItemsEntity item) {
        BeanUtils.copyProperties(item, this);
    }

    public void setDigits(String lotteryType, DrawItemRQ drawItemRQ, Boolean isNight, DrawingDTO drawingDTO) {

        switch (lotteryType) {
            case LotteryConstant.VN1:
            case LotteryConstant.VN2:
                this.setFiveDigits(drawItemRQ.getNumber());
                if (BettingConstant.POST.equals(drawItemRQ.getPostGroup())) {
                    this.setTwoDigits(drawItemRQ.getTwoDigits());
                    this.setThreeDigits(drawItemRQ.getThreeDigits());

                    this.setFiveDigits(drawItemRQ.getTwoDigits().concat(drawItemRQ.getThreeDigits()));
                    if (this.getPostCode().equals("A4"))
                        this.setThreeDigits(null);

                    if (drawItemRQ.getPostCode().equals(PostConstant.POST_K)) {
                        this.setTwoDigits(StringUtils.right(drawItemRQ.getNumber(), 2));
                        this.setThreeDigits(StringUtils.right(drawItemRQ.getNumber(), 3));
                        this.setFourDigits(StringUtils.right(drawItemRQ.getNumber(), 4));
                        String fiveD = "*".concat(drawItemRQ.getNumber());
                        if (fiveD.length() == 6)
                            fiveD = drawItemRQ.getNumber();
                        this.setFiveDigits(StringUtils.right(drawItemRQ.getNumber(), 5));
                    }
                } else {
                    List<String> lo4Digits = !isNight ? BettingConstant.POST_LO_1_TO_4 : BettingConstant.POST_LO_10_TO_19;
                    this.setFiveDigits(drawItemRQ.getNumber());
                    if (lo4Digits.contains(this.getPostCode())) {
                        this.setFiveDigits("*".concat(drawItemRQ.getNumber()));
                    }
                    this.setTwoDigits(StringUtils.right(this.getFiveDigits(), 2));
                    this.setThreeDigits(StringUtils.right(this.getFiveDigits(), 3));
                }

                this.setFourDigits(StringUtils.right(this.getFiveDigits(), 4));
                this.setSixDigits("*".concat(this.getFiveDigits()));
                break;

            case LotteryConstant.LEAP:
            case LotteryConstant.SC:
                if (drawItemRQ.getNumber().length() == 4) {
                    drawItemRQ.setNumber("**".concat(drawItemRQ.getNumber()));
                }
                if (drawItemRQ.getNumber().length() >= 5) {
                    this.setTwoDigits(StringUtils.right(drawItemRQ.getNumber(), 2));
                    this.setThreeDigits(StringUtils.right(drawItemRQ.getNumber(), 3));
                    this.setFourDigits(StringUtils.right(drawItemRQ.getNumber(), 4));
                    this.setFiveDigits(StringUtils.right(drawItemRQ.getNumber(), 5));
                    if (drawItemRQ.getNumber().length() > 5) {
                        this.setSixDigits(drawItemRQ.getNumber());
                    } else {
                        this.setSixDigits("*".concat(drawItemRQ.getNumber()));
                    }
                }
                break;


            case LotteryConstant.TH:
                if (List.of(PostConstant.POST_A, PostConstant.POST_C, PostConstant.POST_D).contains(drawItemRQ.getPostCode())) {
                    this.setTwoDigits(StringUtils.right(drawItemRQ.getNumber(), 2));
                    this.setThreeDigits(StringUtils.right(drawItemRQ.getNumber(), 3));
                    this.setFourDigits(StringUtils.right(drawItemRQ.getNumber(), 4));
                    this.setFiveDigits(StringUtils.right(drawItemRQ.getNumber(), 5));
                    if (drawItemRQ.getNumber().length() > 5) {
                        this.setSixDigits(drawItemRQ.getNumber());
                    } else {
                        this.setSixDigits("*".concat(drawItemRQ.getNumber()));
                    }
                }

                if(drawItemRQ.getPostCode().equals(PostConstant.POST_B)){
                    this.setTwoDigits(drawItemRQ.getNumber());
                }
                break;

            case LotteryConstant.KH:
                this.setTwoDigits(drawItemRQ.getTwoDigits());
                this.setThreeDigits(drawItemRQ.getThreeDigits());
                break;


            case LotteryConstant.TN:
                switch (drawingDTO.getShiftCode()) {
                    case PostConstant.TN_SHIFT_1_CODE:
                    case PostConstant.TN_SHIFT_2_CODE:
                    case PostConstant.TN_SHIFT_4_CODE:
                        if (drawItemRQ.getPostGroup().equals(BettingConstant.POST)) {

                            this.setTwoDigits(drawItemRQ.getTwoDigits());
                            this.setThreeDigits(drawItemRQ.getThreeDigits());
                            this.setFiveDigits(drawItemRQ.getTwoDigits().concat(drawItemRQ.getThreeDigits()));
                            this.setFourDigits(StringUtils.right(this.fiveDigits, 4));
                            this.setSixDigits("*" + drawItemRQ.getTwoDigits().concat(drawItemRQ.getThreeDigits()));

                        } else {
                            this.setTwoDigits(StringUtils.right(drawItemRQ.getNumber(), 2));
                            this.setThreeDigits(StringUtils.left(drawItemRQ.getNumber(), 3));
                            this.setFourDigits(StringUtils.right(drawItemRQ.getNumber(), 4));
                            this.setFiveDigits(StringUtils.right(drawItemRQ.getNumber(), 5));
                        }
                        break;
                    case PostConstant.TN_SHIFT_3_CODE:
                    case PostConstant.TN_SHIFT_5_CODE:
                        if (drawItemRQ.getPostGroup().equals(BettingConstant.POST)) {
                            this.setTwoDigits(drawItemRQ.getTwoDigits());
                            this.setThreeDigits(drawItemRQ.getThreeDigits());
                            this.setFiveDigits(drawItemRQ.getTwoDigits().concat(drawItemRQ.getThreeDigits()));

                            if (this.getPostCode().equals("A4"))
                                this.setThreeDigits(null);

                            if (this.postCode.equals("B"))
                                this.setFourDigits(StringUtils.right(drawItemRQ.getNumber(), 4));

                        } else {
                            this.setTwoDigits(StringUtils.right(drawItemRQ.getNumber(), 2));
                            this.setThreeDigits(StringUtils.left(drawItemRQ.getNumber(), 3));

                            this.setFourDigits(StringUtils.right(drawItemRQ.getNumber(), 4));
                            this.setFiveDigits(drawItemRQ.getNumber());

                        }
                        break;
                }
                break;
        }

    }
}