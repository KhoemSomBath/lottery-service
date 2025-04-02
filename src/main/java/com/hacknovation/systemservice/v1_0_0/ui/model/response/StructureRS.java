package com.hacknovation.systemservice.v1_0_0.ui.model.response;

import com.hacknovation.systemservice.constant.MessageConstant;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class StructureRS {
    private int status = HttpStatus.OK.value();
    private String message = MessageConstant.SUCCESSFULLY;
    private String messageKey = MessageConstant.SUCCESSFULLY_KEY;
    private Object data;
    private PagingRS paging;

    public StructureRS(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public StructureRS(HttpStatus status, String message, Object data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public StructureRS(HttpStatus status, String message, String messageKey, Object data) {
        this.status = status.value();
        this.message = message;
        this.messageKey = messageKey;
        this.data = data;
    }

    public StructureRS(HttpStatus status, String message, String messageKey, Object data, PagingRS paging) {
        this.status = status.value();
        this.message = message;
        this.messageKey = messageKey;
        this.data = data;
        this.paging = paging;
    }

    public StructureRS() {
    }
}
