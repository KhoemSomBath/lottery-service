package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

/*
 * author: kangto
 * createdAt: 29/03/2022
 * time: 11:32
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "track_user_has_lottery")
public class TrackUserHasLotteryEntity extends BaseEntity {

    @Column(name = "lt")
    private String lotteryType;

    @Column(name = "uc")
    private String userCode;

    @Column(name = "has_lottery_json")
    private String hasLotteryJson;

    @Column(name = "issued_at")
    private Date issuedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TrackUserHasLotteryEntity that = (TrackUserHasLotteryEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1688682847;
    }
}
