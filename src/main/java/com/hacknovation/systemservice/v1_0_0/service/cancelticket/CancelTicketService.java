package com.hacknovation.systemservice.v1_0_0.service.cancelticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.HazelcastConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.ticket.TempTicketNQ;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.cache.TicketBettingHZ;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.user.UserCancelTicketDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.SqlUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hazelcast.collection.IList;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sombath
 * create at 1/3/23 3:07 PM
 */

@Service
@RequiredArgsConstructor
public class CancelTicketService extends BaseServiceIP {

    private final TempTicketNQ tempTicketNQ;
    private final HazelcastInstance hazelcastInstance;
    private final SqlUtility sqlUtility;
    private final ActivityLogUtility activityLogUtility;
    private final JwtToken jwtToken;
    private final ObjectMapper objectMapper;

    private final Map<String, String> tempDrawTable =Map.of(

            "LEAP", "leap_temp_drawing",
            "TN", "tn_temp_drawing",
            "KH", "khr_temp_drawing",
            "VN1", "vnone_temp_drawing",
            "VN2", "vntwo_temp_drawing",
            "SC", "sc_temp_drawing",
            "TH", "th_temp_drawing"

    );

    private final Map<String, String> tempOrderTable =Map.of(

            "LEAP", "leap_temp_orders",
            "TN", "tn_temp_orders",
            "KH", "khr_temp_orders",
            "VN1", "vnone_temp_orders",
            "SC", "sc_temp_orders",
            "VN2", "vntwo_temp_orders",
            "TH", "th_temp_orders"

    );

    public StructureRS cancelTicket(Long id, String lotteryType, String drawCode, Integer status){

        UserToken userToken = jwtToken.getUserToken();

        String getStopLoQuery = "select stoped_lo_at from %s where code = %s";
        Date stopLoAt = sqlUtility.executeQueryForObject(String.format(getStopLoQuery, tempDrawTable.get(lotteryType), drawCode), Date.class);

        String getStatusQuery = String.format("select status from %s where id = %s", tempOrderTable.get(lotteryType), id);
        Integer orderStatus = sqlUtility.executeQueryForObject(getStatusQuery, Integer.class);

        if(orderStatus == null || orderStatus == 0)
            return responseBodyWithBadRequest("Ticket canceled", "ticket_canceled");

        if(stopLoAt == null || stopLoAt.toInstant().isBefore(Instant.now()))
            return responseBodyWithBadRequest("Could not cancel ticket, result released", "could_not_cancel_ticket_result_released");

        String query = String.format("update %s set status = %s where id = %s", tempOrderTable.get(lotteryType), status, id);

        sqlUtility.executeQuery(query);

        activityLogUtility.addActivityLog(lotteryType, ActivityLogConstant.CANCEL_TICKET, userToken.getUserType(), ActivityLogConstant.ACTION_UPDATE, userToken.getUserCode(), query);

        removeAnalyzeCacheOnCancelTicket(lotteryType, id, status);

        return responseBodyWithSuccessMessage();
    }

    private void removeAnalyzeCacheOnCancelTicket(String lotteryType, Long id, Integer status) {
        String query = "SELECT u.`code` memberCode, u.username, u.super_senior_code superSeniorCode FROM %s vn INNER JOIN users u ON vn.uc = u.`code` WHERE vn.id = %s";

        UserCancelTicketDTO user = sqlUtility.executeQueryForObject(String.format(query, tempOrderTable.get(lotteryType), id), UserCancelTicketDTO.class);

        List<OrderItemsDTO> orderItemsDTOList = this.getOrderItemByOrderId(lotteryType, id);

        String key = "%s:%s";
        if (status == 0) {
            orderItemsDTOList = orderItemsDTOList.stream().peek(it-> {
                it.setBetAmount(it.getBetAmount().multiply(BigDecimal.valueOf(-1)));
                it.setTotalAmount(it.getTotalAmount().multiply(BigDecimal.valueOf(-1)));
            }).collect(Collectors.toList());
        }

        if (orderItemsDTOList.size() > 0) {
            OrderItemsDTO first = orderItemsDTOList.get(0);
            key = String.format(key, first.getDrawCode(), StringUtils.left(user.getUsername(), 4));

            TicketBettingHZ bettingHZ = new TicketBettingHZ();
            bettingHZ.setLotteryType(lotteryType);
            bettingHZ.setDrawAt(first.getDrawAt());
            bettingHZ.setKey(key);
            bettingHZ.setOrderId(id);
            bettingHZ.setUsername(user.getUsername());
            bettingHZ.setMemberCode(user.getMemberCode());
            bettingHZ.setSuperSeniorCode(user.getSuperSeniorCode());
            bettingHZ.setItems(orderItemsDTOList);

            IList<String> iList = hazelcastInstance.getList(HazelcastConstant.CANCEL_TICKET_REMOVE);
            try {
                iList.add(objectMapper.writeValueAsString(bettingHZ));
            } catch (JsonProcessingException e) {
                System.out.println("CancelTicketService.removeAnalyzeCacheOnCancelTicket JsonProcessingException");
                e.printStackTrace();
            }
        }
    }

    private List<OrderItemsDTO> getOrderItemByOrderId(String lottery, Long id) {
        BigInteger orderId = BigInteger.valueOf(id);
        switch (lottery) {
            case LotteryConstant.LEAP:
                return tempTicketNQ.leapTempOrderItemInOrderIds(List.of(orderId), "false");
            case LotteryConstant.VN1:
                return tempTicketNQ.vnOneTempOrderItemInOrderIds(List.of(orderId), "false");
            case LotteryConstant.VN2:
            case LotteryConstant.MT:
                return tempTicketNQ.vnTwoTempOrderItemInOrderIds(List.of(orderId), "false");
            case LotteryConstant.TN:
                return tempTicketNQ.tnTempOrderItemInOrderIds(List.of(orderId), "false");
            case LotteryConstant.KH:
                return tempTicketNQ.khTempOrderItemInOrderIds(List.of(orderId), "false");
        }

        return new ArrayList<>();
    }

}
