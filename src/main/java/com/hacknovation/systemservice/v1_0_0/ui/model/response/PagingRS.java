package com.hacknovation.systemservice.v1_0_0.ui.model.response;

import lombok.Data;

@Data
public class PagingRS {
    private Integer page;
    private Integer size;
    private Long totals;


    public PagingRS() {
    }

    public PagingRS(int number, int size, long totalElements) {
        this.page = number;
        this.size = size;
        this.totals = totalElements;
    }
}
