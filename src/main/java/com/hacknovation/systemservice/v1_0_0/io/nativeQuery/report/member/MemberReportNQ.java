package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.member;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderDTO;
import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import io.github.gasparbarancelli.NativeQueryParam;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@NativeQueryFolder("report/members/origin")
public interface MemberReportNQ extends NativeQuery {


    List<MemberReportTO> leapSaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                        String filterByStartDate,
                                        String filterByEndDate);

    List<MemberReportTO> khSaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                        String filterByStartDate,
                                        String filterByEndDate);

    List<MemberReportTO> vn1SaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                       String filterByStartDate,
                                       String filterByEndDate);

    List<MemberReportTO> vn2SaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                       String filterByStartDate,
                                       String filterByEndDate);

    List<MemberReportTO> tnSaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                       String filterByStartDate,
                                       String filterByEndDate);

    List<MemberReportTO> scSaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                        String filterByStartDate,
                                        String filterByEndDate);

    List<MemberReportTO> thSaleReport(@NativeQueryParam(value = "memberCodes") List<String> memberCodes,
                                        String filterByStartDate,
                                        String filterByEndDate);


    List<OrderDTO> vn1OrderByIdIn(@NativeQueryParam(value = "orderIds") List<Integer> orderIds);
    List<OrderDTO> vn2OrderByIdIn(@NativeQueryParam(value = "orderIds") List<Integer> orderIds);
    List<OrderDTO> leapOrderByIdIn(@NativeQueryParam(value = "orderIds") List<Integer> orderIds);
    List<OrderDTO> khOrderByIdIn(@NativeQueryParam(value = "orderIds") List<Integer> orderIds);
    List<OrderDTO> tnOrderByIdIn(@NativeQueryParam(value = "orderIds") List<Integer> orderIds);
    List<OrderDTO> scOrderByIdIn(@NativeQueryParam(value = "orderIds") List<Integer> orderIds);
    List<OrderDTO> thOrderByIdIn(@NativeQueryParam(value = "orderIds") List<Integer> orderIds);
}
