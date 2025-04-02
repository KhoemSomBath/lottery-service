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

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_has_postpone_number")
public class UserHasPostponeNumberEntity extends BaseEntity {

    @Column(name = "lottery_type")
    private String lotteryType;

    @Column(name = "dc")
    private String drawCode;

    @Column(name = "uc")
    private String userCode;

    @Column(name = "number")
    private String number;

    @Column(name = "limit_amount")
    private BigDecimal limitAmount;

    @Nullable
    @Column(name = "iam")
    private Boolean isAllMember = false;

    @Nullable
    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Nullable
    @Column(name = "sc")
    private String shiftCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserHasPostponeNumberEntity that = (UserHasPostponeNumberEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 939242724;
    }
}
