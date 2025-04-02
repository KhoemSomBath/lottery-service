package com.hacknovation.systemservice.v1_0_0.service.report;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

@Service
public interface ReportSV {
    StructureRS dailyReport();
    StructureRS memberReport();
    StructureRS ticketReport();
    StructureRS clearingReport();
}