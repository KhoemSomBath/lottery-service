package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.ticket.TempTicketNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.ticket.TicketNQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.betting.BettingListRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.betting.BettingListRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.betting.ColumnRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket.AmountRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket.GroupOrderItemByPostRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket.SummaryAmountRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket.TicketRS;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.TicketReportUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 24/01/2022
 * time: 12:03
 */
@Component
@RequiredArgsConstructor
public class BettingUtility {

    private final TicketNQ ticketNQ;
    private final TempTicketNQ tempTicketNQ;
    private final GeneralUtility generalUtility;
    private final TicketReportUtility ticketReportUtility;

    /**
     * get betting list
     * @param bettingListRS BettingListRS
     * @param bettingListRQ BettingListRQ
     */
    public void bettingList(BettingListRS bettingListRS, BettingListRQ bettingListRQ) {
        boolean isTemp = generalUtility.isTempTable(bettingListRQ.getFilterByDate());
        List<OrderDTO> orderDTOList = getOrderDTOData(bettingListRQ, isTemp);

        if (orderDTOList.size() > 0) {
            Map<BigInteger, OrderDTO> mapOrderById = orderDTOList.stream().filter(generalUtility.distinctByKeys(OrderDTO::getId)).collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
            List<BigInteger> orderIds = orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList());
            List<OrderItemsDTO> orderItemsDTOList = getOrderItemsDTO(bettingListRQ.getLotteryType(), orderIds, bettingListRQ.getIsWin(), isTemp);

            SummaryAmountRS summaryAmount = new SummaryAmountRS();
            SummaryAmountRS total1DPage = new SummaryAmountRS();
            SummaryAmountRS total2DPage = new SummaryAmountRS();
            SummaryAmountRS total3DPage = new SummaryAmountRS();
            SummaryAmountRS total4DPage = new SummaryAmountRS();

            Map<Integer, List<OrderItemsDTO>> columnGroups = orderItemsDTOList.stream().collect(Collectors.groupingBy(OrderItemsDTO::getColumnNumber, Collectors.mapping(item -> item, Collectors.toList())));
            List<ColumnRS> columnRSList = new ArrayList<>();
            for (Integer columnNumber : columnGroups.keySet()) {

                ColumnRS columnRS = getColumn(columnNumber, mapOrderById, columnGroups.get(columnNumber));
                columnRS.setPageNumber(bettingListRQ.getPageNumber());
                columnRSList.add(columnRS);

                total1DPage.addFromSummaryAmountRS(columnRS.getTotal1Digit());
                total2DPage.addFromSummaryAmountRS(columnRS.getTotal2Digit());
                total3DPage.addFromSummaryAmountRS(columnRS.getTotal3Digit());
                total4DPage.addFromSummaryAmountRS(columnRS.getTotal4Digit());

            }
            summaryAmount.addFromSummaryAmountRS(total1DPage);
            summaryAmount.addFromSummaryAmountRS(total2DPage);
            summaryAmount.addFromSummaryAmountRS(total3DPage);
            summaryAmount.addFromSummaryAmountRS(total4DPage);
            bettingListRS.setTotal1Digit(total1DPage);
            bettingListRS.setTotal2Digit(total2DPage);
            bettingListRS.setTotal3Digit(total3DPage);
            bettingListRS.setTotal4Digit(total4DPage);
            bettingListRS.setSummaryAmount(summaryAmount);
            bettingListRS.setLanes(columnRSList);
        }
    }
    private List<OrderDTO> getOrderDTOData(BettingListRQ bettingListRQ, boolean isTemp) {
        List<OrderDTO> orderDTOList = new ArrayList<>();

        switch (bettingListRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:
                if (isTemp) {
                    orderDTOList = tempTicketNQ.vnOneTempOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                } else {
                    orderDTOList = ticketNQ.vnOneOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                }
                break;
            case LotteryConstant.VN2:
                if (isTemp) {
                    orderDTOList = tempTicketNQ.vnTwoTempOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                } else {
                    orderDTOList = ticketNQ.vnTwoOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                }
                break;
            case LotteryConstant.LEAP:
                if (isTemp) {
                    orderDTOList = tempTicketNQ.leapTempOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                } else {
                    orderDTOList = ticketNQ.leapOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                }
                break;
            case LotteryConstant.KH:
                if (isTemp) {
                    orderDTOList = tempTicketNQ.khTempOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                } else {
                    orderDTOList = ticketNQ.khOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                }
                break;
            case LotteryConstant.TN:
                if (isTemp) {
                    orderDTOList = tempTicketNQ.tnTempOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                } else {
                    orderDTOList = ticketNQ.tnOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                }
                break;
            case LotteryConstant.SC:
                if (isTemp) {
                    orderDTOList = tempTicketNQ.scTempOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                } else {
                    orderDTOList = ticketNQ.scOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                }
                break;
            case LotteryConstant.TH:
                if (isTemp) {
                    orderDTOList = tempTicketNQ.thTempOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                } else {
                    orderDTOList = ticketNQ.thOrder(bettingListRQ.getFilterByUserCode(), bettingListRQ.getDrawCode(), bettingListRQ.getPageNumber(), bettingListRQ.getFilterByDate(), bettingListRQ.getIsWin(), bettingListRQ.getIsCancel());
                }
                break;
        }

        return orderDTOList;
    }

    /**
     * get order items by orderId and isTemp
     * @param orderIds List<BigInteger>
     * @param isWin String
     * @param isTemp boolean
     * @return List<OrderItemsDTO>
     */
    public List<OrderItemsDTO> getOrderItemsDTO(String lotteryType, List<BigInteger> orderIds, String isWin, boolean isTemp) {
        List<OrderItemsDTO> orderItemsDTOList = new ArrayList<>();
        switch (lotteryType.toUpperCase()) {
            case LotteryConstant.VN1:
                orderItemsDTOList = isTemp ? tempTicketNQ.vnOneTempOrderItemInOrderIds(orderIds, isWin) : ticketNQ.vnOneOrderItemInOrderIds(orderIds, isWin);
                break;
            case LotteryConstant.VN2:
                orderItemsDTOList = isTemp ? tempTicketNQ.vnTwoTempOrderItemInOrderIds(orderIds, isWin) : ticketNQ.vnTwoOrderItemInOrderIds(orderIds, isWin);
                break;
            case LotteryConstant.LEAP:
                orderItemsDTOList = isTemp ? tempTicketNQ.leapTempOrderItemInOrderIds(orderIds, isWin) : ticketNQ.leapOrderItemInOrderIds(orderIds, isWin);
                break;
            case LotteryConstant.KH:
                orderItemsDTOList = isTemp ? tempTicketNQ.khTempOrderItemInOrderIds(orderIds, isWin) : ticketNQ.khOrderItemInOrderIds(orderIds, isWin);
                break;
            case LotteryConstant.TN:
                orderItemsDTOList = isTemp ? tempTicketNQ.tnTempOrderItemInOrderIds(orderIds, isWin) : ticketNQ.tnOrderItemInOrderIds(orderIds, isWin);
                break;
            case LotteryConstant.SC:
                orderItemsDTOList = isTemp ? tempTicketNQ.scTempOrderItemInOrderIds(orderIds, isWin) : ticketNQ.scOrderItemInOrderIds(orderIds, isWin);
                break;
            case LotteryConstant.TH:
                orderItemsDTOList = isTemp ? tempTicketNQ.thTempOrderItemInOrderIds(orderIds, isWin) : ticketNQ.thOrderItemInOrderIds(orderIds, isWin);
                break;
        }

        return orderItemsDTOList;
    }



    /**
     * get a column
     * @param columnNumber Integer
     * @param mapOrderById Map<BigInteger, OrderDTO>
     * @param orderItemOfColumn List<OrderItemsDTO>
     * @return ColumnRS
     */
    public ColumnRS getColumn(Integer columnNumber, Map<BigInteger, OrderDTO> mapOrderById, List<OrderItemsDTO> orderItemOfColumn) {
        ColumnRS columnRS = new ColumnRS();
        SummaryAmountRS total1DDigitInColumn = new SummaryAmountRS();
        SummaryAmountRS total2DDigitInColumn = new SummaryAmountRS();
        SummaryAmountRS total3DDigitInColumn = new SummaryAmountRS();
        SummaryAmountRS total4DDigitInColumn = new SummaryAmountRS();
        SummaryAmountRS summaryAmountInColumn = new SummaryAmountRS();
        List<TicketRS> cards = new ArrayList<>();

        Map<Integer, List<OrderItemsDTO>> mapOrderItemByOrderId = orderItemOfColumn.stream().collect(Collectors.groupingBy(OrderItemsDTO::getOrderId, Collectors.mapping(item -> item, Collectors.toList())));
        for (Integer orderId : mapOrderItemByOrderId.keySet()) {
            TicketRS ticketRS = new TicketRS();
            AmountRS amountTicketKhr = new AmountRS();
            AmountRS amountTicketUsd = new AmountRS();
            SummaryAmountRS total1Digit = new SummaryAmountRS();
            SummaryAmountRS total2Digit = new SummaryAmountRS();
            SummaryAmountRS total3Digit = new SummaryAmountRS();
            SummaryAmountRS total4Digit = new SummaryAmountRS();
            List<GroupOrderItemByPostRS> groupOrderByPost = new ArrayList<>();
            ticketReportUtility.groupBySectionNumber(groupOrderByPost, mapOrderItemByOrderId.get(orderId), amountTicketKhr, amountTicketUsd, total1Digit, total2Digit, total3Digit, total4Digit);
            OrderDTO orderDTO = mapOrderById.get(BigInteger.valueOf(orderId));
            BeanUtils.copyProperties(orderDTO, ticketRS);
            ticketRS.setStatus(orderDTO.getStatus().intValue());
            ticketRS.setTotalKhr(amountTicketKhr);
            ticketRS.setTotalUsd(amountTicketUsd);
            ticketRS.setPostGroup(groupOrderByPost);
            ticketRS.setTotal1Digit(total1Digit);
            ticketRS.setTotal2Digit(total2Digit);
            ticketRS.setTotal3Digit(total3Digit);
            ticketRS.setTotal4Digit(total4Digit);
            total1DDigitInColumn.addFromSummaryAmountRS(total1Digit);
            total2DDigitInColumn.addFromSummaryAmountRS(total2Digit);
            total3DDigitInColumn.addFromSummaryAmountRS(total3Digit);
            total4DDigitInColumn.addFromSummaryAmountRS(total4Digit);
            cards.add(ticketRS);
        }

        summaryAmountInColumn.addFromSummaryAmountRS(total1DDigitInColumn);
        summaryAmountInColumn.addFromSummaryAmountRS(total2DDigitInColumn);
        summaryAmountInColumn.addFromSummaryAmountRS(total3DDigitInColumn);
        summaryAmountInColumn.addFromSummaryAmountRS(total4DDigitInColumn);
        columnRS.setId(columnNumber.toString());
        columnRS.setTitle("ជួរទី " + columnRS.getId());
        columnRS.setTotal1Digit(total1DDigitInColumn);
        columnRS.setTotal2Digit(total2DDigitInColumn);
        columnRS.setTotal3Digit(total3DDigitInColumn);
        columnRS.setTotal4Digit(total4DDigitInColumn);
        columnRS.setSummaryAmount(summaryAmountInColumn);
        columnRS.setCards(cards);

        return columnRS;
    }

    public ColumnRS getColumn(Integer columnNumber, Map<BigInteger, OrderDTO> mapOrderById, List<OrderItemsDTO> orderItemOfColumn, Map<String, String> mapUserCodeUsername) {
        ColumnRS columnRS = new ColumnRS();
        SummaryAmountRS total1DDigitInColumn = new SummaryAmountRS();
        SummaryAmountRS total2DDigitInColumn = new SummaryAmountRS();
        SummaryAmountRS total3DDigitInColumn = new SummaryAmountRS();
        SummaryAmountRS total4DDigitInColumn = new SummaryAmountRS();
        SummaryAmountRS summaryAmountInColumn = new SummaryAmountRS();
        List<TicketRS> cards = new ArrayList<>();

        Map<Integer, List<OrderItemsDTO>> mapOrderItemByOrderId = orderItemOfColumn.stream().collect(Collectors.groupingBy(OrderItemsDTO::getOrderId, Collectors.mapping(item -> item, Collectors.toList())));
        for (Integer orderId : mapOrderItemByOrderId.keySet()) {
            TicketRS ticketRS = new TicketRS();
            AmountRS amountTicketKhr = new AmountRS();
            AmountRS amountTicketUsd = new AmountRS();
            SummaryAmountRS total1Digit = new SummaryAmountRS();
            SummaryAmountRS total2Digit = new SummaryAmountRS();
            SummaryAmountRS total3Digit = new SummaryAmountRS();
            SummaryAmountRS total4Digit = new SummaryAmountRS();
            List<GroupOrderItemByPostRS> groupOrderByPost = new ArrayList<>();
            ticketReportUtility.groupBySectionNumber(groupOrderByPost, mapOrderItemByOrderId.get(orderId), amountTicketKhr, amountTicketUsd, total1Digit, total2Digit, total3Digit, total4Digit);
            OrderDTO orderDTO = mapOrderById.get(BigInteger.valueOf(orderId));
            BeanUtils.copyProperties(orderDTO, ticketRS);
            ticketRS.setStatus(orderDTO.getStatus().intValue());
            ticketRS.setTotalKhr(amountTicketKhr);
            ticketRS.setTotalUsd(amountTicketUsd);
            ticketRS.setPostGroup(groupOrderByPost);
            ticketRS.setTotal1Digit(total1Digit);
            ticketRS.setTotal2Digit(total2Digit);
            ticketRS.setTotal3Digit(total3Digit);
            ticketRS.setTotal4Digit(total4Digit);
            total1DDigitInColumn.addFromSummaryAmountRS(total1Digit);
            total2DDigitInColumn.addFromSummaryAmountRS(total2Digit);
            total3DDigitInColumn.addFromSummaryAmountRS(total3Digit);
            total4DDigitInColumn.addFromSummaryAmountRS(total4Digit);
            ticketRS.setUsername(mapUserCodeUsername.get(orderDTO.getUserCode()));
            cards.add(ticketRS);
        }

        summaryAmountInColumn.addFromSummaryAmountRS(total1DDigitInColumn);
        summaryAmountInColumn.addFromSummaryAmountRS(total2DDigitInColumn);
        summaryAmountInColumn.addFromSummaryAmountRS(total3DDigitInColumn);
        summaryAmountInColumn.addFromSummaryAmountRS(total4DDigitInColumn);
        columnRS.setId(columnNumber.toString());
        columnRS.setTitle("ជួរទី " + columnRS.getId());
        columnRS.setTotal1Digit(total1DDigitInColumn);
        columnRS.setTotal2Digit(total2DDigitInColumn);
        columnRS.setTotal3Digit(total3DDigitInColumn);
        columnRS.setTotal4Digit(total4DDigitInColumn);
        columnRS.setSummaryAmount(summaryAmountInColumn);
        columnRS.setCards(cards);

        return columnRS;
    }


}
