package com.hacknovation.systemservice.v1_0_0.service.report;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ClearingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.TicketListRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.ClearingUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.DailyFormat1ReportUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.DailyFormat2ReportUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.TicketReportUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportIP extends BaseServiceIP implements ReportSV {

    private final JwtToken jwtToken;
    private final HttpServletRequest request;
    private final TicketReportUtility ticketReportUtility;
    private final UserNQ userNQ;
    private final DailyFormat1ReportUtility dailyFormat1ReportUtility;
    private final DailyFormat2ReportUtility dailyFormat2ReportUtility;
    private final ClearingUtility clearingUtility;
    private final UserRP userRP;

    /**
     *
     * Daily Report
     *
     * @return StructureRS
     */
    @Override
    public StructureRS dailyReport() {
        ReportRQ reportRQ = new ReportRQ(request);

        UserToken userToken = jwtToken.getUserToken();

        String userType = userToken.getUserType().toLowerCase();

        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userToken.setUserCode(userToken.getParentCode());
        }

        boolean isCanSeeUserOnline = true;
        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
            isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                isCanSeeUserOnline = true;
        }

        /*
         * Get member codes
         */
        List<UserLevelReportTO> filterMemberTOS = userNQ.userLevelFilter(userToken.getUserCode(), userType, reportRQ.getFilterByUsername(), UserConstant.MEMBER.toLowerCase(), reportRQ.getFilterByUserCode(), isCanSeeUserOnline);

        /*
         * Get user by level
         */
        List<UserLevelReportTO> userLevelReportTOS = userNQ.userReferralWithCommission(userToken.getUserCode(), userType, reportRQ.getFilterByLevel(), reportRQ.getFilterByUserCode(), reportRQ.getFilterByUsername(), reportRQ.getFilterByTwoDRebateRate(), reportRQ.getFilterByThreeDRebateRate(), isCanSeeUserOnline);

        if(userLevelReportTOS.isEmpty())
            return responseBodyWithBadRequest("User level " + reportRQ.getFilterByLevel() + " with that commission are not found", "User level with that commission are not found");

        if (!reportRQ.getFilterByReportType().equalsIgnoreCase(LotteryConstant.ONE)) {
            return responseBodyWithSuccessMessage(dailyFormat2ReportUtility.memberReportResponse(reportRQ, userLevelReportTOS, filterMemberTOS));
        } else {
            return responseBodyWithSuccessMessage(dailyFormat1ReportUtility.memberReportResponse(reportRQ, userLevelReportTOS, filterMemberTOS));
        }

    }

    /**
     *
     * Member Report
     *
     * @return StructureRS
     */
    @Override
    public StructureRS memberReport() {
        UserToken userToken = jwtToken.getUserToken();
        ReportRQ reportRQ = new ReportRQ(request);
        String userCode = userToken.getUserCode();
        String userType = userToken.getUserType().toLowerCase();
        String filterByUserCode = reportRQ.getFilterByUserCode();

        if (filterByUserCode.equalsIgnoreCase(LotteryConstant.ALL))
            throw new BadRequestException(MessageConstant.BAD_REQUEST, null);

        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
        }

        UserEntity userEntity = userRP.findByCode(reportRQ.getFilterByUserCode());
        if (!UserConstant.AGENT.equalsIgnoreCase(userEntity.getRoleCode()))
            throw new BadRequestException(MessageConstant.BAD_REQUEST, null);

        boolean isCanSeeUserOnline = true;
        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
            isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                isCanSeeUserOnline = true;
        }

        /**
         * Get member codes
         */
        List<UserLevelReportTO> filterMemberTOS = userNQ.userLevelFilter(userCode, userType, LotteryConstant.ALL, UserConstant.MEMBER.toLowerCase(), reportRQ.getFilterByUserCode(), isCanSeeUserOnline);

//        return responseBodyWithSuccessMessage(dailyFormat2ReportUtility.memberReportResponse(reportRQ, userEntity, filterMemberTOS));
        return null;
    }

    /**
     *
     * Ticket Report
     *
     * @return StructureRS
     */
    @Override
    public StructureRS ticketReport() {
        TicketListRQ ticketListRQ = new TicketListRQ(request);
        UserToken userToken = jwtToken.getUserToken();
        return ticketReportUtility.ticketReport(ticketListRQ, userToken);
    }

    @Override
    public StructureRS clearingReport() {
        ClearingRQ clearingRQ = new ClearingRQ(request);
        switch (clearingRQ.getFilterByLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:
                return responseBodyWithSuccessMessage(clearingUtility.vn1ClearingReport(clearingRQ));
//            case LotteryConstant.VN2:
//                return responseBodyWithSuccessMessage();
//            case LotteryConstant.LEAP:
//                return responseBodyWithSuccessMessage();
//            case LotteryConstant.TN:
//                return responseBodyWithSuccessMessage();
//            case LotteryConstant.KH:
//                return responseBodyWithSuccessMessage();
            default:
                return responseBodyWithSuccessMessage();
        }
    }

}
