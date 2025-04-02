package com.hacknovation.systemservice.v1_0_0.ui.model.response.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sombath
 * create at 24/11/21 4:10 PM
 */

@Data
public class CommissionLottery {
    private List<Commission> leap = new ArrayList<>();
    private List<Commission> vn2 = new ArrayList<>();
    private List<Commission> vn1 = new ArrayList<>();
    private List<Commission> kh = new ArrayList<>();
    private List<Commission> tn = new ArrayList<>();

    public List<Commission> toList(){
        List<Commission> commissions = new ArrayList<>();

        if(leap != null)
            commissions.addAll(this.leap);

        if(vn1 != null)
            commissions.addAll(this.vn1);

        if(vn2 != null)
            commissions.addAll(this.vn2);

        if(tn != null)
            commissions.addAll(this.tn);

        if(kh != null)
            commissions.addAll(this.kh);

        return commissions;
    }
}
