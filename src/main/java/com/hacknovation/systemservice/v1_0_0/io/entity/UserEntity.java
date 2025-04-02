package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.enums.UserStatusEnum;
import com.hacknovation.systemservice.enums.UserTypeEnum;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="users", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}, name="USER_UNIQUE_USERNAME"))
public class UserEntity extends BaseEntity {

    @Column(name = "code", unique=true)
    private String code;

    @Column(name = "username", unique=true)
    private String username;

    @Column
    private String password;

    @Nullable
    @Column(name = "nickname")
    private String nickname;

    @Nullable
    @Column(name = "role_code")
    private String roleCode;

    @Nullable
    @Column(name = "super_senior_code")
    private String superSeniorCode;

    @Nullable
    @Column(name = "senior_code")
    private String seniorCode;

    @Nullable
    @Column(name = "master_code")
    private String masterCode;

    @Nullable
    @Column(name = "agent_code")
    private String agentCode;

    @Nullable
    @Column(name = "language_code")
    private String languageCode;

    @Nullable
    @Column(name = "currency_code")
    private String currencyCode;

    @Nullable
    @Column(name = "parent_id")
    private Integer parentId;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status = UserStatusEnum.ACTIVATE;

    @Nullable
    @Enumerated(EnumType.STRING)
    private UserTypeEnum userType;

    @Nullable
    @Column(name = "lottery_type")
    private String lotteryType;

    @Nullable
    private String platformType;

    @Nullable
    @Column(name = "limit_bet")
    private String limitBet;

    @Nullable
    @Column(name = "is_locked")
    private Boolean isLocked;

    private Boolean isOnline;

    @Nullable
    @Column(name = "is_locked_betting")
    private Boolean isLockedBetting;

    @Nullable
    @Column(name = "is_locked_screen")
    private Boolean isLockedScreen = Boolean.TRUE;

    @Nullable
    @Column(name = "last_login")
    private Date lastLogin;

    @Nullable
    @Column(name = "last_login_web")
    private Date lastLoginWeb;

    @Nullable
    @Column(name = "last_login_app")
    private Date lastLoginApp;

    @Nullable
    @Column(name = "created_by")
    private String createdBy;

    @Nullable
    @Column(name = "updated_by")
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1838525018;
    }
}






