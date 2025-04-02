package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 16:37
 */
@Data
public class NoteRS {
    private NoteItemRS owed = new NoteItemRS();
    private NoteItemRS returned = new NoteItemRS();
    private NoteItemRS total = new NoteItemRS();
}
