package com.hacknovation.systemservice.v1_0_0.io.entity.sc;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "sc_temp_drawing_items")
public class SCTempDrawingItemsEntity extends BaseDrawingItemsEntity {
    public SCTempDrawingItemsEntity(DrawingItemsDTO drawingItemsDTO) {
        BeanUtils.copyProperties(drawingItemsDTO, this);
        this.setId(drawingItemsDTO.getId().longValue());
    }
}
