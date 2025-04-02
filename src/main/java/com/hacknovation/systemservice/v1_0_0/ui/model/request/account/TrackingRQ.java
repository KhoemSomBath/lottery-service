package com.hacknovation.systemservice.v1_0_0.ui.model.request.account;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TrackingRQ {

    @NotNull(message = "Please provide a latitude")
    private String latitude;

    @NotNull(message = "Please provide a longitude")
    private String longitude;

    @NotNull(message = "Please provide a longitude")
    private String address;

}
