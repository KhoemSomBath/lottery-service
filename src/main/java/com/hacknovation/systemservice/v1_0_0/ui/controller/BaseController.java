package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected ResponseEntity<StructureRS> response(StructureRS structureRS)
    {
        return ResponseEntity
                .status(structureRS.getStatus())
                .body(structureRS);
    }

    protected ResponseEntity<StructureRS> response()
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new StructureRS(HttpStatus.OK, MessageConstant.SUCCESSFULLY, null));
    }

    protected ResponseEntity<StructureRS> response(Object data)
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new StructureRS(HttpStatus.OK, MessageConstant.SUCCESSFULLY, data));
    }

}
