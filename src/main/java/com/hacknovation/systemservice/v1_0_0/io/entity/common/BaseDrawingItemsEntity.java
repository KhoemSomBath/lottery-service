package com.hacknovation.systemservice.v1_0_0.io.entity.common;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/*
 * author: kangto
 * createdAt: 13/01/2022
 * time: 15:48
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class BaseDrawingItemsEntity extends BaseEntity {

    @Column(name = "drawing_id")
    private Integer drawingId;

    @Nullable
    @Column(name = "post_code")
    private String postCode;

    @Nullable
    @Column(name = "post_group")
    private String postGroup;

    @Nullable
    @Column(name = "two_digits")
    private String twoDigits;

    @Nullable
    @Column(name = "three_digits")
    private String threeDigits;

    @Nullable
    @Column(name = "four_digits")
    private String fourDigits;

    @Nullable
    @Column(name = "five_digits")
    private String fiveDigits;

    @Nullable
    @Column(name = "six_digits")
    private String sixDigits;

    public void setRenderingStar() {
        twoDigits = "**";
        threeDigits = "***";
        fourDigits = "****";
        fiveDigits = "*****";
        sixDigits = "******";
    }

    public void setRenderingNumber() {
        twoDigits = "??";
        threeDigits = "???";
        fourDigits = "????";
        fiveDigits = "?????";
        sixDigits = "??????";
    }

}
