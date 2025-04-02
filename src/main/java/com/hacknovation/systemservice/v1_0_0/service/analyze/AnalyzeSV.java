package com.hacknovation.systemservice.v1_0_0.service.analyze;

import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyzelog.AnalyzeLogRS;
import org.springframework.stereotype.Service;

@Service
public interface AnalyzeSV {
    StructureRS analyseSelling();
    StructureRS analyzeLog(String lottery, String drawCode);
    AnalyzeLogRS leapAnalyzeLog(String drawCode);
    AnalyzeLogRS vnTwoAnalyzeLog(VNTwoTempDrawingEntity drawingEntity);
    AnalyzeLogRS tnAnalyzeLog(TNTempDrawingEntity drawingEntity);
    AnalyzeLogRS scAnalyzeLog(String drawCode);
}
