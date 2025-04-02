package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
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
@Table(name="transaction_balance")
public class TransactionBalanceEntity extends BaseEntity {

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "balance_khr")
    private BigDecimal balanceKhr = BigDecimal.ZERO;

    @Column(name = "balance_usd")
    private BigDecimal balanceUsd = BigDecimal.ZERO;

    private Boolean status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransactionBalanceEntity that = (TransactionBalanceEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 373132059;
    }
}
