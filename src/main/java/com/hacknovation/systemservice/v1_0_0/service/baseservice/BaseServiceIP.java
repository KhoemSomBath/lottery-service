package com.hacknovation.systemservice.v1_0_0.service.baseservice;

import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.PagingRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.http.HttpStatus;

public class BaseServiceIP implements BaseServiceSV {

    @Override
    public StructureRS response(Object data) {
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, data);
    }

    @Override
    public StructureRS responseBodyWithSuccessMessage() {
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, null);
    }

    @Override
    public StructureRS responseBodyWithSuccessMessage(Object data) {
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, data);
    }

    public StructureRS responseBodyWithBadRequest(String message, String messageTr)
    {
        return responseBody(HttpStatus.BAD_REQUEST, message, messageTr, null, new PagingRS());
    }

    public StructureRS responseBodySomethingWentWrong()
    {
        return responseBodyWithBadRequest(MessageConstant.SOMETHING_WENT_WRONG, MessageConstant.SOMETHING_WENT_WRONG_TR);
    }

    @Override
    public StructureRS responseBody(HttpStatus status) {
        return responseBody(status, null, null);
    }

    @Override
    public StructureRS responseBody(HttpStatus status, String message) {
        return responseBody(status, message, null);
    }

    public StructureRS responseBody(HttpStatus status, String message, Object data)
    {
        return responseBody(status, message, data, new PagingRS());
    }

    public StructureRS responseBody(HttpStatus status, String message, Object data, PagingRS paging)
    {
        StructureRS structureRS = new StructureRS();
        structureRS.setStatus(status.value());
        structureRS.setMessage(message);
        structureRS.setData(data);
        structureRS.setPaging(paging);

        return structureRS;
    }

    public StructureRS responseBody(HttpStatus status, String message, String messageTr, Object data, PagingRS paging)
    {
        StructureRS structureRS = new StructureRS();
        structureRS.setStatus(status.value());
        structureRS.setMessage(message);
        structureRS.setMessageKey(messageTr);
        structureRS.setData(data);
        structureRS.setPaging(paging);

        return structureRS;
    }
}
