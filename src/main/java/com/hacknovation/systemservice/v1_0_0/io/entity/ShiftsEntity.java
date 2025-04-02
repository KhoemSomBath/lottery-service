package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = "shifts")
public class ShiftsEntity extends BaseEntity {

    @Nullable
    @Column(name = "code")
    private String code;

    @Nullable
    @Column(name = "stoped_lo_at")
    private String stopedLoAt;

    @Nullable
    @Column(name = "resulted_lo_at")
    private String resultedLoAt;

    @Nullable
    @Column(name = "stoped_a_at")
    private String stopedAAt;

    @Nullable
    @Column(name = "resulted_a_at")
    private String resultedAAt;

    @Nullable
    @Column(name = "stoped_post_at")
    private String stopedPostAt;

    @Nullable
    @Column(name = "resulted_post_at")
    private String resultedPostAt;

    @Column(name = "type")
    private String type;

    @Nullable
    @Column(name = "status")
    private Boolean status;

    @Nullable
    @Column(name = "is_night")
    private Boolean isNight;

    @Nullable
    @Column(name = "post_a_x")
    private Integer postAx;

    @Nullable
    @Column(name = "lo_x")
    private Integer loX;

    public Boolean getIsNight() {
        if (this.isNight == null)
            return Boolean.FALSE;
        return isNight;
    }

    public Integer getPostAx() {
        if (this.postAx == null)
            return 1;
        return postAx;
    }

    public Integer getLoX() {
        if (this.loX == null)
            return 20;
        return loX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShiftsEntity that = (ShiftsEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 53701563;
    }
}
