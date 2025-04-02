package com.hacknovation.systemservice.v1_0_0.io.entity.th;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseWinOrderItems;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.WinOrderItemsDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/*
 * author: kangto
 * createdAt: 01/02/2022
 * time: 15:09
 */
@Entity
@Table(name = "th_temp_win_order_items")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class THTempWinOrderItemsEntity extends BaseWinOrderItems {

    public THTempWinOrderItemsEntity(WinOrderItemsDTO winOrderItemsDTO) {
        BeanUtils.copyProperties(winOrderItemsDTO, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        THTempWinOrderItemsEntity that = (THTempWinOrderItemsEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 1940641196;
    }
}
