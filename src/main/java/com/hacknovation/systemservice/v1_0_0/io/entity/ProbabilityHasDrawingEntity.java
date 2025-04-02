package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseProbabilityEntity;
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
 * createdAt: 18/08/2022
 * time: 23:19
 */
@Entity
@Table(name = "probability_has_drawing")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProbabilityHasDrawingEntity extends BaseProbabilityEntity {

    @Column(name = "sc")
    private String shiftCode;

    @Column(name = "shift_at")
    private String shiftAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProbabilityHasDrawingEntity that = (ProbabilityHasDrawingEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 2021332267;
    }
}
