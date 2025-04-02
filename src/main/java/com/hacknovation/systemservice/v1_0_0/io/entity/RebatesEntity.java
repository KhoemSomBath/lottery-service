package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "rebates")
public class RebatesEntity extends BaseEntity {

    @Nullable
    @Column(name = "code")
    private String code;

    @Nullable
    @Column(name = "name")
    private String name;

    @Nullable
    @Column(name = "rebate_rate")
    private BigDecimal rebateRate;

    @Nullable
    @Column(name = "water_rate")
    private BigDecimal waterRate;

    @Nullable
    @Column(name = "updated_by")
    private String updatedBy;

    @Nullable
    @Column(name = "type")
    private String type;

    @Nullable
    @Column(name = "sort_order")
    private Integer sortOrder;

    @Nullable
    @Column(name = "status")
    private Boolean status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RebatesEntity that = (RebatesEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1867540770;
    }
}
