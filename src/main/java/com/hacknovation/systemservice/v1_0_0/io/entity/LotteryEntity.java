package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;


@ToString
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "lotteries")
public class LotteryEntity extends BaseEntity {

    @Nullable
    @Column(name = "code")
    private String code;

    @Nullable
    @Column(name = "name")
    private String name;

    @Nullable
    @Column(name = "sort_order")
    private String sortOrder;

    @Nullable
    @Column(name = "status")
    private Boolean status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LotteryEntity that = (LotteryEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1725646289;
    }
}
