package com.hacknovation.systemservice.v1_0_0.io.entity.tn;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseDrawingEntity;
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
@Table(name = "tn_temp_drawing")
public class TNTempDrawingEntity extends BaseDrawingEntity {

    @Nullable
    @Column(name = "is_analyzed_post_a")
    private Boolean isAnalyzedPostA;

    @Nullable
    @Column(name = "is_released_post_a")
    private Boolean isReleasedPostA;

    @Nullable
    @Column(name = "stoped_a_at")
    private Date stopedAAt;

    @Nullable
    @Column(name = "resulted_a_at")
    private Date resultedAAt;

    @Nullable
    @Column(name = "is_night")
    private Boolean isNight;

    @Nullable
    @Column(name = "post_a_x")
    private Integer postAx;

    @Nullable
    @Column(name = "lo_x")
    private Integer loX;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TNTempDrawingEntity that = (TNTempDrawingEntity) o;

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
        if (this.getIsNight())
            this.setStopedAAt(drawingDTO.getStoppedAAt());
    }

    public Boolean isDay(){
        return !this.isNight;
    }
}
