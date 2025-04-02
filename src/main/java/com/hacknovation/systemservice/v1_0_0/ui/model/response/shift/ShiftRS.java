package com.hacknovation.systemservice.v1_0_0.ui.model.response.shift;

import com.hacknovation.systemservice.v1_0_0.io.entity.ShiftsEntity;
import lombok.Data;

/**
 * @author Sombath
 * create at 17/3/22 4:03 PM
 */

@Data
public class ShiftRS {

    private String shiftCode;
    private String drawAt;


    public ShiftRS() {}

    public ShiftRS(ShiftsEntity shiftsEntity) {
        this.shiftCode = shiftsEntity.getCode();
        this.drawAt = shiftsEntity.getResultedPostAt();
    }
}
