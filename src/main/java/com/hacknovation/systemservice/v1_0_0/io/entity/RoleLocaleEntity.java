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
@Table(name = "role_locales")
public class RoleLocaleEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Nullable
    @Column(name = "language_code")
    private String languageCode;

    @Nullable
    @Column(name = "role_code")
    private String roleCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoleLocaleEntity that = (RoleLocaleEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1827734026;
    }
}
