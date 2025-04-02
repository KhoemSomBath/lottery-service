package com.hacknovation.systemservice.v1_0_0.service.baseservice;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.http.HttpStatus;

public interface BaseServiceSV {

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * response body follow base structure response
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @return StructureRS
     */
    StructureRS response(Object data);

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * response body follow base structure response
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @return StructureRS
     */
    StructureRS responseBodyWithSuccessMessage();

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * response body follow base structure response
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param data Object
     * @return StructureRS
     */
    StructureRS responseBodyWithSuccessMessage(Object data);

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * response body follow base structure response
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param status HttpStatus
     * @return StructureRS
     */
    StructureRS responseBody(HttpStatus status);

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * response body follow base structure response
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param status  HttpStatus
     * @param message String
     * @return StructureRS
     */
    StructureRS responseBody(HttpStatus status, String message);

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * response body follow base structure response
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param status  HttpStatus
     * @param message String
     * @param data    Object
     * @return StructureRS
     */
    StructureRS responseBody(HttpStatus status, String message, Object data);
}
