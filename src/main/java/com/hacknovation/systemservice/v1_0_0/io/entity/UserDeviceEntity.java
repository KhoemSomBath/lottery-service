package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "user_devices")
public class UserDeviceEntity extends BaseEntity {

    @Column(name = "user_code")
    private String userCode;
    private String deviceId;
    private String deviceName;
    private String deviceModel;
    private String deviceToken;
    private String createdBy;
    private String updatedBy;
    private Boolean status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserDeviceEntity that = (UserDeviceEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1533669428;
    }
}
