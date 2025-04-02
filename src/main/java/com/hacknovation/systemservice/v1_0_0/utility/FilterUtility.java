package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import org.springframework.stereotype.Component;

@Component
public class FilterUtility {

    public void underRoleFilter(ReportRQ reportRQ, String userRole, String userType) {

        if (reportRQ.getFilterByLevel().equalsIgnoreCase(UserConstant.ALL)) {

            if (UserConstant.SYSTEM.equalsIgnoreCase(userType)) {
                reportRQ.setFilterByLevel(UserConstant.SENIOR);
            } else {
                if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(userRole)) {
                    reportRQ.setFilterByLevel(UserConstant.SENIOR);
                }

                if (UserConstant.SENIOR.equalsIgnoreCase(userRole)) {
                    reportRQ.setFilterByLevel(UserConstant.MASTER);
                }

                if (UserConstant.MASTER.equalsIgnoreCase(userRole)) {
                    reportRQ.setFilterByLevel(UserConstant.AGENT);
                }

                if (UserConstant.AGENT.equalsIgnoreCase(userRole)) {
                    reportRQ.setFilterByLevel(UserConstant.MEMBER);
                }
            }

        } else {

            String originalLevel = reportRQ.getFilterByLevel().toUpperCase();

            if (UserConstant.SUPER_SENIOR.equals(originalLevel)) {
                reportRQ.setFilterByLevel(UserConstant.SENIOR);
            }

            if (UserConstant.SENIOR.equals(originalLevel)) {
                reportRQ.setFilterByLevel(UserConstant.MASTER);
            }

            if (UserConstant.MASTER.equals(originalLevel)) {
                reportRQ.setFilterByLevel(UserConstant.AGENT);
            }

            if (UserConstant.AGENT.equals(originalLevel)) {
                reportRQ.setFilterByLevel(UserConstant.MEMBER);
            }

            if (UserConstant.MEMBER.equals(originalLevel)) {
                reportRQ.setFilterByLevel(UserConstant.MEMBER);
            }

            if (UserConstant.COMPANY.equalsIgnoreCase(reportRQ.getFilterByUserCode())) {
                reportRQ.setFilterByLevel(UserConstant.SUPER_SENIOR);
            }

        }
    }

}
