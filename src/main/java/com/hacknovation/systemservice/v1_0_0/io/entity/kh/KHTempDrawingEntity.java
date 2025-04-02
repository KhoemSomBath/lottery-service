package com.hacknovation.systemservice.v1_0_0.io.entity.kh;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.sun.istack.Nullable;
import lombok.Data;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "khr_temp_drawing")
public class KHTempDrawingEntity extends BaseDrawingEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LeapTempDrawingEntity that = (LeapTempDrawingEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 2108324252;
    }

    public void updateStopAt(DrawingDTO drawingDTO) {
        this.setStopedLoAt(drawingDTO.getStoppedLoAt());
        this.setStopedPostAt(drawingDTO.getStoppedPostAt());
        this.setPostponeNumber(drawingDTO.getPostponeNumber());
    }

    public void updateStopAtKH(DrawingDTO drawingDTO) {
        this.setStopedLoAt(drawingDTO.getStoppedPostAt());
        this.setStopedPostAt(drawingDTO.getStoppedPostAt());
        this.setPostponeNumber(drawingDTO.getPostponeNumber());
    }
}
