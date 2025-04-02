package com.hacknovation.systemservice.v1_0_0.io.entity.leap;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * author: kangto
 * createdAt: 28/04/2022
 * time: 11:58
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "leap_drawing_items")
public class LeapDrawingItemsEntity extends BaseDrawingItemsEntity {
    public LeapDrawingItemsEntity(DrawingItemsDTO drawingItemsDTO) {
        BeanUtils.copyProperties(drawingItemsDTO, this);
        this.setId(drawingItemsDTO.getId().longValue());
    }
}
