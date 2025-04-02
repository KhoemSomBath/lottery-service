package com.hacknovation.systemservice.v1_0_0.service.page;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

@Service
public interface PagingSV {
    StructureRS listing();
}
