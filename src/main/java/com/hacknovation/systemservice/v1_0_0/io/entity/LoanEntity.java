package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name="loans")
public class LoanEntity extends BaseEntity {

    private String lotteryType;
    @Column(name = "uc")
    private String userCode;
    private Date nextPayment = new Date();
    private Date issuedAt = new Date();

    private BigDecimal borrowKhr = BigDecimal.ZERO;
    private BigDecimal paybackKhr = BigDecimal.ZERO;
    private BigDecimal borrowUsd = BigDecimal.ZERO;
    private BigDecimal paybackUsd = BigDecimal.ZERO;
    private BigDecimal installmentPaymentKhr = BigDecimal.ZERO;
    private BigDecimal installmentPaymentUsd = BigDecimal.ZERO;

    private final Boolean isPaid = false;

    public void setNextPayment(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.nextPayment = formatter.parse(date + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setIssuedAt(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.issuedAt = formatter.parse(date + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LoanEntity that = (LoanEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 582873103;
    }
}