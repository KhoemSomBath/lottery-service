package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/*
 * author: kangto
 * createdAt: 17/03/2022
 * time: 12:08
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "lockbet_template")
public class LockbetTemplateEntity extends BaseEntity {

    @Column(name = "lt")
    private String lotteryType;

    @Column(name = "sc")
    private String shiftCode;

    @Column(name = "dow")
    private String dayOfWeek;

    @Column(name = "uc")
    private String userCode;

    @Column(name = "stoped_lo_at")
    private String stopLoAt;

    @Nullable
    @Column(name = "stoped_a_at")
    private String stopAAt;

    @Column(name = "stoped_post_at")
    private String stopPostAt;

    @Column(name = "stop_delete")
    private String stopDeleteAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LockbetTemplateEntity that = (LockbetTemplateEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 115007043;
    }
}
