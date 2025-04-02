package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

/**
 * author : phokkinnky
 * date : 8/12/21
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "activity_logs")
public class ActivityLogEntity extends BaseEntity {

    @Column(name = "lottery_type")
    private String lotteryType;

    @Column(name = "dc")
    private String drawCode;

    @Column(name = "draw_at")
    private Date drawAt;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "uc")
    private String userCode;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "description")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ActivityLogEntity that = (ActivityLogEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 951079609;
    }
}
