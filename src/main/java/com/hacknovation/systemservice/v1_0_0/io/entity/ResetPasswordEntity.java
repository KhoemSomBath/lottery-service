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
@Table(name="reset_passwords")
public class ResetPasswordEntity extends BaseEntity {

    @Column(length=50)
    private String username;

    @Nullable
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "verified")
    private Boolean verified = false;

    @Column(name = "reseted_by")
    private Integer resetedBy;

    @Column(name = "device_id")
    private String deviceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResetPasswordEntity that = (ResetPasswordEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 631576360;
    }
}
