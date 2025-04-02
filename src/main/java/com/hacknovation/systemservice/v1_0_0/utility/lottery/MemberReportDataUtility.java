package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.member.MemberReportNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.member.MemberReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.member.TempMemberReportNQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * author: kangto
 * createdAt: 15/02/2022
 * time: 23:42
 */
@Component
@RequiredArgsConstructor
public class MemberReportDataUtility {
    private final MemberReportNQ memberReportNQ;
    private final TempMemberReportNQ tempMemberReportNQ;
    private final GeneralUtility generalUtility;

    public List<MemberReportTO> getDataLeap(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.leapSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        } else {
            return memberReportNQ.leapSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        }
    }

    public List<MemberReportTO> getDataKh(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.khSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        } else {
            return memberReportNQ.khSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        }
    }

    public List<MemberReportTO> getDataVn1(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.vn1SaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        } else {
            return memberReportNQ.vn1SaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        }
    }

    public List<MemberReportTO> getDataVn2(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.vn2SaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        } else {
            return memberReportNQ.vn2SaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        }
    }

    public List<MemberReportTO> getDataTN(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.tnSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        } else {
            return memberReportNQ.tnSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        }
    }

    public List<MemberReportTO> getDataSC(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.scSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        } else {
            return memberReportNQ.scSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        }
    }

    public List<MemberReportTO> getDataTH(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.thSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        } else {
            return memberReportNQ.thSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate(),
                    reportRQ.getFilterByEndDate()
            );
        }
    }

    public List<OrderDTO> getOrderDataVn1(List<Integer> orderIds, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.vn1OrderByIdIn(orderIds);
        } else {
            return memberReportNQ.vn1OrderByIdIn(orderIds);
        }
    }

    public List<OrderDTO> getOrderDataVn2(List<Integer> orderIds, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.vn2OrderByIdIn(orderIds);
        } else {
            return memberReportNQ.vn2OrderByIdIn(orderIds);
        }
    }

    public List<OrderDTO> getOrderDataLeap(List<Integer> orderIds, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.leapOrderByIdIn(orderIds);
        } else {
            return memberReportNQ.leapOrderByIdIn(orderIds);
        }
    }

    public List<OrderDTO> getOrderDataKh(List<Integer> orderIds, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.khOrderByIdIn(orderIds);
        } else {
            return memberReportNQ.khOrderByIdIn(orderIds);
        }
    }

    public List<OrderDTO> getOrderDataTN(List<Integer> orderIds, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.tnOrderByIdIn(orderIds);
        } else {
            return memberReportNQ.tnOrderByIdIn(orderIds);
        }
    }

    public List<OrderDTO> getOrderDataSC(List<Integer> orderIds, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.scOrderByIdIn(orderIds);
        } else {
            return memberReportNQ.scOrderByIdIn(orderIds);
        }
    }

    public List<OrderDTO> getOrderDataTH(List<Integer> orderIds, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempMemberReportNQ.thOrderByIdIn(orderIds);
        } else {
            return memberReportNQ.thOrderByIdIn(orderIds);
        }
    }


}
