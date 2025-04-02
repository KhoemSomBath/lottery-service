package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/*
 * author: kangto
 * createdAt: 08/02/2023
 * time: 12:12
 */
@Entity
@Table(name = "configuration")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ConfigurationEntity extends BaseEntity {
    private String code;
    private String name;
    private String value;
    private String type;
    private String inputType;
    private String note;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConfigurationEntity that = (ConfigurationEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 768546340;
    }
}
