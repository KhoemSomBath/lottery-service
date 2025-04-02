package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseProbabilityEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/*
 * author: kangto
 * createdAt: 18/08/2022
 * time: 23:16
 */
@Entity
@Table(name = "probabilities")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProbabilityEntity extends BaseProbabilityEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProbabilityEntity that = (ProbabilityEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1039955793;
    }
}
