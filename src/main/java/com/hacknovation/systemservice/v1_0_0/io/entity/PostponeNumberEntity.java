package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "postpone_number")
public class PostponeNumberEntity extends BaseEntity {

    @Column(name = "lottery_type")
    private String lotteryType;

    @Column(name = "number_detail")
    private String numberDetail;

    @Nullable
    @Column(name = "status")
    private Boolean status = Boolean.TRUE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostponeNumberEntity that = (PostponeNumberEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1499330100;
    }
}
