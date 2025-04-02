package com.hacknovation.systemservice.v1_0_0.io.entity.common;

import com.hacknovation.systemservice.enums.DrawingStatus;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/*
 * author: kangto
 * createdAt: 13/01/2022
 * time: 15:26
 */
@Getter
@Setter
@MappedSuperclass
public class BaseDrawingEntity extends BaseEntity {

    @Nullable
    @Column(name = "title")
    private String title;

    @Nullable
    @Column(name = "code")
    private String code;

    @Nullable
    @Column(name = "shift_code")
    private String shiftCode;

    @Nullable
    @Column(name = "is_recent")
    private Boolean isRecent = Boolean.TRUE;

    @Nullable
    @Column(name = "is_analyzed_lo")
    private Boolean isAnalyzedLo = Boolean.FALSE;

    @Nullable
    @Column(name = "is_analyzed_post")
    private Boolean isAnalyzedPost = Boolean.FALSE;

    @Nullable
    @Column(name = "is_released_lo")
    private Boolean isReleasedLo = Boolean.FALSE;

    @Nullable
    @Column(name = "is_released_post")
    private Boolean isReleasedPost = Boolean.FALSE;

    @Nullable
    @Column(name = "stoped_lo_at")
    private Date stopedLoAt;

    @Nullable
    @Column(name = "resulted_lo_at")
    private Date resultedLoAt;

    @Nullable
    @Column(name = "stoped_post_at")
    private Date stopedPostAt;

    @Nullable
    @Column(name = "resulted_post_at")
    private Date resultedPostAt;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DrawingStatus status = DrawingStatus.WAITING;

    @Nullable
    @Column(name = "updated_by")
    private String updatedBy;

    @Nullable
    @Column(name = "postpone_number")
    private String postponeNumber;

    @Nullable
    @Column(name = "is_analyzed_post_a")
    private Boolean isAnalyzedPostA;

    @Nullable
    @Column(name = "is_released_post_a")
    private Boolean isReleasedPostA;

    @Nullable
    @Column(name = "stoped_a_at")
    private Date stopedAAt;

    @Nullable
    @Column(name = "resulted_a_at")
    private Date resultedAAt;

    @Nullable
    @Column(name = "is_night")
    private Boolean isNight;

    @Nullable
    @Column(name = "post_a_x")
    private Integer postAx;

    @Nullable
    @Column(name = "lo_x")
    private Integer loX;
}

