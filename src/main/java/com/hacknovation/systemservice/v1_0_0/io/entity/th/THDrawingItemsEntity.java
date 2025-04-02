package com.hacknovation.systemservice.v1_0_0.io.entity.th;

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
 * createdAt: 09/01/2022
 * time: 11:58
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "th_temp_drawing_items")
public class THDrawingItemsEntity extends BaseDrawingItemsEntity {
    public THDrawingItemsEntity(DrawingItemsDTO drawingItemsDTO) {
        BeanUtils.copyProperties(drawingItemsDTO, this);
        this.setId(drawingItemsDTO.getId().longValue());
    }
}
