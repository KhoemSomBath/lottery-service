package com.hacknovation.systemservice.v1_0_0.io.entity.th;

import com.hacknovation.systemservice.enums.DrawingStatus;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.sun.istack.Nullable;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "th_temp_drawing")
public class THTempDrawingEntity {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Boolean getRecent() {
        return isRecent;
    }

    public void setRecent(Boolean recent) {
        isRecent = recent;
    }

    public Boolean getAnalyzedLo() {
        return isAnalyzedLo;
    }

    public void setAnalyzedLo(Boolean analyzedLo) {
        isAnalyzedLo = analyzedLo;
    }

    public Boolean getAnalyzedPost() {
        return isAnalyzedPost;
    }

    public void setAnalyzedPost(Boolean analyzedPost) {
        isAnalyzedPost = analyzedPost;
    }

    public Boolean getReleasedLo() {
        return isReleasedLo;
    }

    public void setReleasedLo(Boolean releasedLo) {
        isReleasedLo = releasedLo;
    }

    public Boolean getReleasedPost() {
        return isReleasedPost;
    }

    public void setReleasedPost(Boolean releasedPost) {
        isReleasedPost = releasedPost;
    }

    public Date getStopedLoAt() {
        return stopedLoAt;
    }

    public void setStopedLoAt(Date stopedLoAt) {
        this.stopedLoAt = stopedLoAt;
    }

    public Date getResultedLoAt() {
        return resultedLoAt;
    }

    public void setResultedLoAt(Date resultedLoAt) {
        this.resultedLoAt = resultedLoAt;
    }

    public Date getStopedPostAt() {
        return stopedPostAt;
    }

    public void setStopedPostAt(Date stopedPostAt) {
        this.stopedPostAt = stopedPostAt;
    }

    public Date getResultedPostAt() {
        return resultedPostAt;
    }

    public void setResultedPostAt(Date resultedPostAt) {
        this.resultedPostAt = resultedPostAt;
    }

    public DrawingStatus getStatus() {
        return status;
    }

    public void setStatus(DrawingStatus status) {
        this.status = status;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getPostponeNumber() {
        return postponeNumber;
    }

    public void setPostponeNumber(String postponeNumber) {
        this.postponeNumber = postponeNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint unsigned")
    private Long id;

    @Nullable
    @Column(name = "title")
    private String title;

    @Nullable
    @Column(name = "code")
    private String code;

    @Nullable
    @Column(name = "shift_code")
    private String shiftCode;

    @Nullable
    @Column(name = "is_recent")
    private Boolean isRecent = Boolean.TRUE;

    @Nullable
    @Column(name = "is_analyzed_lo")
    private Boolean isAnalyzedLo = Boolean.FALSE;

    @Nullable
    @Column(name = "is_analyzed_post")
    private Boolean isAnalyzedPost = Boolean.FALSE;

    @Nullable
    @Column(name = "is_released_lo")
    private Boolean isReleasedLo = Boolean.FALSE;

    @Nullable
    @Column(name = "is_released_post")
    private Boolean isReleasedPost = Boolean.FALSE;

    @Nullable
    @Column(name = "stoped_lo_at")
    private Date stopedLoAt;

    @Nullable
    @Column(name = "resulted_lo_at")
    private Date resultedLoAt;

    @Nullable
    @Column(name = "stoped_post_at")
    private Date stopedPostAt;

    @Nullable
    @Column(name = "resulted_post_at")
    private Date resultedPostAt;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DrawingStatus status = DrawingStatus.WAITING;

    @Nullable
    @Column(name = "updated_by")
    private String updatedBy;

    @Nullable
    @Column(name = "postpone_number")
    private String postponeNumber;

    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    public void updateStopAt(DrawingDTO drawingDTO) {
        this.setStopedLoAt(drawingDTO.getStoppedLoAt());
        this.setStopedPostAt(drawingDTO.getStoppedPostAt());
        this.setPostponeNumber(drawingDTO.getPostponeNumber());
    }
}
