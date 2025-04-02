package com.hacknovation.systemservice.v1_0_0.ui.model.response.probability;

import com.hacknovation.systemservice.v1_0_0.io.entity.ProbabilityHasDrawingEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 23:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProbabilityItemDrawRS extends ProbabilityItemRS {
    private String shiftCode;
    private String shiftAt;

    public ProbabilityItemDrawRS (ProbabilityHasDrawingEntity item) {
        BeanUtils.copyProperties(item, this);
        this.setProKey(item.getProbabilityKey());
    }
}
