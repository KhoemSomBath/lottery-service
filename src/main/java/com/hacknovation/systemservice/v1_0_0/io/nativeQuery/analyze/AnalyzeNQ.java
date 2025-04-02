package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.analyze;


import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@NativeQueryFolder("analyze")
public interface AnalyzeNQ extends NativeQuery {
    List<AnalyzeTO> vn1AnalyzeLo(List<String> userCodes, String drawCode, BigDecimal amount2D, BigDecimal amount3D);
    List<AnalyzeTO> vn1AnalyzePost(List<String> userCodes, String drawCode, BigDecimal amount2D, BigDecimal amount3D);
    List<AnalyzeTO> vn2AnalyzeLo(List<String> userCodes, String drawCode, BigDecimal amount2D, BigDecimal amount3D);
    List<AnalyzeTO> vn2AnalyzePost(List<String> userCodes, String drawCode, BigDecimal amount2D, BigDecimal amount3D);
    List<AnalyzeTO> leapAnalyzeLo(List<String> userCodes, String drawCode, BigDecimal amount2D, BigDecimal amount3D, BigDecimal amount4D);
    List<AnalyzeTO> leapAnalyzePost(List<String> userCodes, String drawCode, BigDecimal amount2D, BigDecimal amount3D, BigDecimal amount4D);
}