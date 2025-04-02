package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.daily;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import io.github.gasparbarancelli.NativeQueryParam;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@NativeQueryFolder("report/daily/origin")
public interface DailyReportNQ extends NativeQuery {


    List<DailyReportTO> leapSaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                       @NativeQueryParam(value = "filterDate") String filterDate);

    List<DailyReportTO> vn1SaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                      @NativeQueryParam(value = "filterDate") String filterDate);

    List<DailyReportTO> vn2SaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                      @NativeQueryParam(value = "filterDate") String filterDate);

}
