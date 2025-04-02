package com.hacknovation.systemservice.v1_0_0.ui.model.response.configuration;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 08/02/2023
 * time: 12:17
 */
@Data
public class ConfigurationRS {
    private String type;
    private List<ConfigurationItemRS> items = new ArrayList<>();
}
