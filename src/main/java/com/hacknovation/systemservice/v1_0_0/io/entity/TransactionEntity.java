package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.enums.FinanceStatusEnum;
import com.hacknovation.systemservice.enums.FinanceTypeEnum;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="transactions")
public class TransactionEntity extends BaseEntity {

    @Column(name = "user_code")
    private String userCode;

    @Nullable
    @Column(name = "proceed_by")
    private String proceedBy;

    @Nullable
    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Nullable
    private String remark;

    @Nullable
    @Column(name = "currency_code")
    private String currencyCode;

    @Nullable
    @Enumerated(EnumType.STRING)
    private FinanceTypeEnum type;

    @Nullable
    @Enumerated(EnumType.STRING)
    private FinanceStatusEnum status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransactionEntity that = (TransactionEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 258111821;
    }
}
