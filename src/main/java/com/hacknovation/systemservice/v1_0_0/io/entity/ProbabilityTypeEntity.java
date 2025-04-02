package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 21:39
 */
@Entity
@Table(name = "probability_types")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProbabilityTypeEntity extends BaseEntity {

    @Column(name = "lottery_type")
    private String lotteryType;

    @Column(name = "is_percentage_all_drawing")
    private Boolean isPercentageAllDrawing;

    @Column(name = "updated_by")
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProbabilityTypeEntity that = (ProbabilityTypeEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 572162607;
    }
}
