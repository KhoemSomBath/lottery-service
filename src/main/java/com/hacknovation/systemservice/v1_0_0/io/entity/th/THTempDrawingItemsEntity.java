package com.hacknovation.systemservice.v1_0_0.io.entity.th;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import com.sun.istack.Nullable;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "th_temp_drawing_items")
public class THTempDrawingItemsEntity {

    public THTempDrawingItemsEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Integer drawingId) {
        this.drawingId = drawingId;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostGroup() {
        return postGroup;
    }

    public void setPostGroup(String postGroup) {
        this.postGroup = postGroup;
    }

    public String getTwoDigits() {
        return twoDigits;
    }

    public void setTwoDigits(String twoDigits) {
        this.twoDigits = twoDigits;
    }

    public String getThreeDigits() {
        return threeDigits;
    }

    public void setThreeDigits(String threeDigits) {
        this.threeDigits = threeDigits;
    }

    public String getFourDigits() {
        return fourDigits;
    }

    public void setFourDigits(String fourDigits) {
        this.fourDigits = fourDigits;
    }

    public String getFiveDigits() {
        return fiveDigits;
    }

    public void setFiveDigits(String fiveDigits) {
        this.fiveDigits = fiveDigits;
    }

    public String getSixDigits() {
        return sixDigits;
    }

    public void setSixDigits(String sixDigits) {
        this.sixDigits = sixDigits;
    }

    public THTempDrawingItemsEntity(DrawingItemsDTO drawingItemsDTO) {
        BeanUtils.copyProperties(drawingItemsDTO, this);
        this.setId(drawingItemsDTO.getId().longValue());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint unsigned")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    @Column(name = "drawing_id")
    private Integer drawingId;

    @Nullable
    @Column(name = "post_code")
    private String postCode;

    @Nullable
    @Column(name = "post_group")
    private String postGroup;

    @Nullable
    @Column(name = "two_digits")
    private String twoDigits;

    @Nullable
    @Column(name = "three_digits")
    private String threeDigits;

    @Nullable
    @Column(name = "four_digits")
    private String fourDigits;

    @Nullable
    @Column(name = "five_digits")
    private String fiveDigits;

    @Nullable
    @Column(name = "six_digits")
    private String sixDigits;

}
