package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.DrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.draw.TempDrawingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.ticket.TempTicketNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.ticket.TicketNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.TicketTotalDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.TicketListRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.PagingRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket.*;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.user.UserReferralUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TicketReportUtility extends BaseServiceIP {

    private final UserRP userRP;
    private final DrawingNQ drawingNQ;
    private final TempDrawingNQ tempDrawingNQ;
    private final TicketNQ ticketNQ;
    private final TempTicketNQ tempTicketNQ;
    private final GeneralUtility generalUtility;
    private final UserReferralUtility userReferralUtility;

    public StructureRS ticketReport(TicketListRQ ticketListRQ, UserToken userToken) {

        TicketListRS ticketListRS = new TicketListRS();
        SummaryAmountRS summaryAmountRS = new SummaryAmountRS();
        List<TicketRS> tickets = new ArrayList<>();
        List<DrawingDTO> drawingList = new ArrayList<>();
        Page<OrderDTO> orderDTOList = Page.empty();
        List<OrderItemsDTO> orderItemsDTOList = new ArrayList<>();
        String userCode = userToken.getUserCode();
        List<String> memberCodes =  new ArrayList<>();
        TicketTotalDTO ticketTotalDTO = new TicketTotalDTO();

        boolean isLevel = true;
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
        }
        if (UserConstant.SYSTEM.equalsIgnoreCase(userToken.getUserType())) {
            isLevel = false;
        } else {
            memberCodes = userReferralUtility.userReferralByLevel(userCode, UserConstant.MEMBER.toLowerCase());
        }

        boolean isCanSeeUserOnline = true;
        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
            isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                isCanSeeUserOnline = true;
        }

        String lotteryType = ticketListRQ.getFilterByLotteryType().toUpperCase();

        if (generalUtility.isTempTable(ticketListRQ.getFilterByDate())) {

            switch (lotteryType) {

                case LotteryConstant.VN1:
                    drawingList = tempDrawingNQ.vnOneTempDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = tempTicketNQ.vnOneTempOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = tempTicketNQ.vnOneTempTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = tempTicketNQ.vnOneTempOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.MT:
                case LotteryConstant.VN2:
                    System.out.println(ticketListRQ);
                    drawingList = tempDrawingNQ.vnTwoTempDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = tempTicketNQ.vnTwoTempOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = tempTicketNQ.vnTwoTempTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = tempTicketNQ.vnTwoTempOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.LEAP:
                    drawingList = tempDrawingNQ.leapTempDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = tempTicketNQ.leapTempOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = tempTicketNQ.leapTempTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = tempTicketNQ.leapTempOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.KH:
                    drawingList = tempDrawingNQ.khTempDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = tempTicketNQ.khTempOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = tempTicketNQ.khTempTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = tempTicketNQ.khTempOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.TN:
                    drawingList = tempDrawingNQ.tnTempDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = tempTicketNQ.tnTempOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = tempTicketNQ.tnTempTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = tempTicketNQ.tnTempOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.SC:
                    drawingList = tempDrawingNQ.scTempDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = tempTicketNQ.scTempOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    orderItemsDTOList = tempTicketNQ.scTempOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.TH:
                    drawingList = tempDrawingNQ.thTempDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = tempTicketNQ.thTempOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    orderItemsDTOList = tempTicketNQ.thTempOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
            }

        } else {

            switch (lotteryType) {

                case LotteryConstant.VN1:
                    drawingList = drawingNQ.vnOneDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = ticketNQ.vnOneOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = ticketNQ.vnOneTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = ticketNQ.vnOneOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.MT:
                case LotteryConstant.VN2:
                    drawingList = drawingNQ.vnTwoDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = ticketNQ.vnTwoOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = ticketNQ.vnTwoTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = ticketNQ.vnTwoOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.LEAP:
                    drawingList = drawingNQ.leapDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = ticketNQ.leapOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = ticketNQ.leapTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = ticketNQ.leapOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.KH:
                    drawingList = drawingNQ.khDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = ticketNQ.khOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = ticketNQ.khTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = ticketNQ.khOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.TN:
                    drawingList = drawingNQ.tnDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = ticketNQ.tnOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    ticketTotalDTO = ticketNQ.tnTotal(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline
                    );

                    orderItemsDTOList = ticketNQ.tnOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.SC:
                    drawingList = drawingNQ.scDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = ticketNQ.scOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    orderItemsDTOList = ticketNQ.scOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
                case LotteryConstant.TH:
                    drawingList = drawingNQ.thDrawingByDate(ticketListRQ.getFilterByDate(), ticketListRQ.getFilterByDate());
                    orderDTOList = ticketNQ.thOrderPaginate(
                            isLevel,
                            memberCodes,
                            ticketListRQ.getDrawCode(),
                            ticketListRQ.getKeyword(),
                            ticketListRQ.getFilterByDate(),
                            ticketListRQ.getIsWin(),
                            ticketListRQ.getIsCancel(),
                            isCanSeeUserOnline,
                            PageRequest.of(ticketListRQ.getPage(), ticketListRQ.getSize())
                    );

                    orderItemsDTOList = ticketNQ.thOrderItemInOrderIds(orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList()), ticketListRQ.getIsWin());

                    break;
            }
        }
        List<String> codes = orderDTOList.stream().map(OrderDTO::getUserCode).collect(Collectors.toList());
        List<UserEntity> userEntities = userRP.getUserByCodeIn(codes);

        return responseBody(HttpStatus.OK,
                MessageConstant.SUCCESSFULLY,
                ticketListRS(ticketListRS, drawingList,  tickets, summaryAmountRS,  orderDTOList, orderItemsDTOList, userEntities, ticketTotalDTO),
                new PagingRS(orderDTOList.getNumber(),
                        orderDTOList.getSize(),
                        orderDTOList.getTotalElements()));
    }


    private TicketListRS ticketListRS(TicketListRS ticketListRS, List<DrawingDTO> drawingList, List<TicketRS> tickets, SummaryAmountRS summaryAmountRS, Page<OrderDTO> orderDTOList, List<OrderItemsDTO> orderItemsDTOList, List<UserEntity> userEntities, TicketTotalDTO ticketTotalDTO) {

        ticketListRS.setDrawShifts(generalUtility.getListDraw(drawingList));
        Map<String, List<OrderItemsDTO>> orderItemTicketMap = orderItemsDTOList.stream().collect(Collectors.groupingBy(OrderItemsDTO::getTicketNumber, Collectors.mapping(item->item, Collectors.toList())));
        Map<String, UserEntity> userEntityMap = userEntities.stream().collect(Collectors.toMap(UserEntity::getCode, Function.identity()));
        if (orderDTOList != null) {
            List<OrderDTO> orderDTOList2 = orderDTOList.stream().filter(generalUtility.distinctByKeys(OrderDTO::getId)).collect(Collectors.toList());
            for (OrderDTO order : orderDTOList2) {
                if (orderItemTicketMap.containsKey(order.getTicketNumber())) {
                    TicketRS ticketRS = new TicketRS();
                    AmountRS amountTicketKhr = new AmountRS();
                    AmountRS amountTicketUsd = new AmountRS();
                    List<GroupOrderItemByPostRS> postGroup = new ArrayList<>();

                    /*
                     * Update postGroup list
                     */
                    groupBySectionNumber(postGroup, orderItemTicketMap.get(order.getTicketNumber()), amountTicketKhr, amountTicketUsd);

                    ticketRS.setTotalKhr(amountTicketKhr);
                    ticketRS.setTotalUsd(amountTicketUsd);
                    ticketRS.setTicketNumber(order.getTicketNumber());
                    ticketRS.setDrawAt(order.getDrawAt());
                    ticketRS.setDrawCode(order.getDrawCode());
                    ticketRS.setCreatedAt(order.getCreatedAt());
                    ticketRS.setUserCode(order.getUserCode());
                    ticketRS.setPostGroup(postGroup);
                    if (userEntityMap.containsKey(order.getUserCode())) {
                        ticketRS.setUsername(userEntityMap.get(order.getUserCode()).getUsername());
                    }
                    tickets.add(ticketRS);

                }
            }
        }

        updateSummeryAmount(summaryAmountRS, ticketTotalDTO);
        ticketListRS.setTickets(tickets);
        ticketListRS.setSummaryAmount(summaryAmountRS);

        return ticketListRS;

    }

    /**
     * update list group by section number of order item in a single ticket
     * @param groupBySectionList List<GroupOrderItemByPostRS>
     * @param orderItems List<OrderItemsDTO>
     * @param amountTicketKhr AmountRS
     * @param amountTicketUsd AmountRS
     */
    public void groupBySectionNumber(List<GroupOrderItemByPostRS> groupBySectionList, List<OrderItemsDTO> orderItems, AmountRS amountTicketKhr, AmountRS amountTicketUsd) {
        Map<Integer, List<OrderItemsDTO>> orderItemMapBySection = orderItems.stream().collect(Collectors.groupingBy(OrderItemsDTO::getSectionNumber, Collectors.mapping(item -> item, Collectors.toList())));
        for (Integer sectionNumber : orderItemMapBySection.keySet()) {
            List<OrderItemsDTO> orderItemsBySection = orderItemMapBySection.get(sectionNumber);
            orderItemsBySection.sort(Comparator.comparing(OrderItemsDTO::getId));
            long singlePostCount = orderItemsBySection.stream().map(OrderItemsDTO::getPosts).distinct().count();

            if (singlePostCount == 1) {
                addGroupOrderItemsRS(groupBySectionList, orderItemsBySection, sectionNumber, amountTicketKhr, amountTicketUsd);
            } else {
                String lastPost = "";
                List<List<OrderItemsDTO>> orderItemsDTOListList = new ArrayList<>();
                List<OrderItemsDTO> subOrderItemsList = new ArrayList<>();
                for (OrderItemsDTO itemsDTO : orderItemsBySection) {
                    if (!lastPost.equals(itemsDTO.getPosts()) && !lastPost.isBlank()) {
                        orderItemsDTOListList.add(subOrderItemsList);
                        subOrderItemsList = new ArrayList<>();
                        subOrderItemsList.add(itemsDTO);
                    }
                    lastPost = itemsDTO.getPosts();
                    subOrderItemsList.add(itemsDTO);
                }
                if (!subOrderItemsList.isEmpty()) {
                    orderItemsDTOListList.add(subOrderItemsList);
                }

                orderItemsDTOListList.forEach(list -> addGroupOrderItemsRS(groupBySectionList, list, list.get(0).getId().intValue(), amountTicketKhr, amountTicketUsd));

            }

        }
    }


    /**
     * update list group by section number of order item in a single ticket
     * @param groupBySectionList List<GroupOrderItemByPostRS>
     * @param orderItems List<OrderItemsDTO>
     * @param amountTicketKhr AmountRS
     * @param amountTicketUsd AmountRS
     * @param total2Digit SummaryAmountRS
     * @param total3Digit SummaryAmountRS
     * @param total4Digit SummaryAmountRS
     */
    public void groupBySectionNumber(List<GroupOrderItemByPostRS> groupBySectionList, List<OrderItemsDTO> orderItems, AmountRS amountTicketKhr, AmountRS amountTicketUsd, SummaryAmountRS total1Digit, SummaryAmountRS total2Digit, SummaryAmountRS total3Digit, SummaryAmountRS total4Digit) {
        Map<Integer, List<OrderItemsDTO>> orderItemMapBySection = orderItems.stream().collect(Collectors.groupingBy(OrderItemsDTO::getSectionNumber, Collectors.mapping(item -> item, Collectors.toList())));
        for (Integer sectionNumber : orderItemMapBySection.keySet()) {
            List<OrderItemsDTO> orderItemsBySection = orderItemMapBySection.get(sectionNumber);
            orderItemsBySection.sort(Comparator.comparing(OrderItemsDTO::getId));
            long singlePostCount = orderItemsBySection.stream().map(OrderItemsDTO::getPosts).distinct().count();

            if (singlePostCount == 1) {
                addGroupOrderItemsRS(groupBySectionList, orderItemsBySection, sectionNumber, amountTicketKhr, amountTicketUsd);
                orderItemsBySection.forEach(itemsDTO -> setTotalDigit(total1Digit, total2Digit, total3Digit, total4Digit, itemsDTO, itemsDTO.getWinQty().intValue()));
            } else {
                String lastPost = "";
                List<List<OrderItemsDTO>> orderItemsDTOListList = new ArrayList<>();
                List<OrderItemsDTO> subOrderItemsList = new ArrayList<>();
                for (OrderItemsDTO itemsDTO : orderItemsBySection) {
                    if (!lastPost.equals(itemsDTO.getPosts()) && !lastPost.isBlank()) {
                        orderItemsDTOListList.add(subOrderItemsList);
                        subOrderItemsList = new ArrayList<>();
                        subOrderItemsList.add(itemsDTO);
                    }
                    lastPost = itemsDTO.getPosts();
                    subOrderItemsList.add(itemsDTO);
                    setTotalDigit(total1Digit, total2Digit, total3Digit, total4Digit, itemsDTO, itemsDTO.getWinQty().intValue());
                }
                if (!subOrderItemsList.isEmpty()) {
                    orderItemsDTOListList.add(subOrderItemsList);
                }

                orderItemsDTOListList.forEach(list -> addGroupOrderItemsRS(groupBySectionList, list, list.get(0).getId().intValue(), amountTicketKhr, amountTicketUsd));

            }

        }
    }

    /**
     * Add group order item
     * @param groupBySectionList List<GroupOrderItemByPostRS>
     * @param orderItemsDTOList List<OrderItemsDTO>
     * @param sectionNumber Integer
     * @param amountTicketKhr AmountRS
     * @param amountTicketUsd AmountRS
     */
    public void addGroupOrderItemsRS(List<GroupOrderItemByPostRS> groupBySectionList, List<OrderItemsDTO> orderItemsDTOList, Integer sectionNumber, AmountRS amountTicketKhr, AmountRS amountTicketUsd) {
        GroupOrderItemByPostRS sectionGroup = new GroupOrderItemByPostRS();

        /*
         * Add group order item by post to list
         */
        sectionGroup.setOrdering(sectionNumber);
        sectionGroup.setPosts(orderItemsDTOList.get(0).getPosts());
        updateGroupOrderItemByPost(sectionGroup, orderItemsDTOList);
        groupBySectionList.add(sectionGroup);

        amountTicketKhr.addFromAmountRS(sectionGroup.getSummaryAmount().getTotalKhr());
        amountTicketUsd.addFromAmountRS(sectionGroup.getSummaryAmount().getTotalUsd());
    }


    /**
     * Update group order item to be added
     * @param sectionGroup GroupOrderItemByPostRS
     * @param orderItems List<OrderItemsDTO>
     */
    public void updateGroupOrderItemByPost(GroupOrderItemByPostRS sectionGroup, List<OrderItemsDTO> orderItems) {
        SummaryAmountRS summeryGroupAmountRS = new SummaryAmountRS();
        List<OrderItemsRS> orderItemsRSList = orderItems.stream().map(OrderItemsRS::new).collect(Collectors.toList());
        AmountRS amountKhr = new AmountRS();
        AmountRS amountUsd = new AmountRS();
        for (OrderItemsDTO item : orderItems) {
            if (LotteryConstant.CURRENCY_KHR.equals(item.getCurrencyCode())) {
                updateAmountRSByOrderItemsRS(amountKhr, item);
            }
            if (LotteryConstant.CURRENCY_USD.equals(item.getCurrencyCode())) {
                updateAmountRSByOrderItemsRS(amountUsd, item);
            }
        }
        /*
         * Update summery amount of group order item by post
         */
        updateSummeryAmount(summeryGroupAmountRS, amountKhr, amountUsd);
        sectionGroup.setItems(orderItemsRSList);
        sectionGroup.setSummaryAmount(summeryGroupAmountRS);
    }

    /**
     * update amountRS from order items dto
     * @param amountRS AmountRS
     * @param itemsDTO OrderItemsDTO
     */
    public void updateAmountRSByOrderItemsRS(AmountRS amountRS, OrderItemsDTO itemsDTO) {
        amountRS.setBetAmount(amountRS.getBetAmount().add(itemsDTO.getTotalAmount()));
        amountRS.setWaterAmount(amountRS.getWaterAmount().add(generalUtility.commissionAmount(itemsDTO.getTotalAmount(), itemsDTO.getCommission())));
        amountRS.setWinAmount(amountRS.getWinAmount().add(itemsDTO.getWinAmount().multiply(itemsDTO.getRebateRate())));
        amountRS.setWinLoseAmount(amountRS.getWaterAmount().subtract(amountRS.getWinAmount()));
    }

    /**
     * update summery amount from amount
     * @param summaryAmountRS SummeryAmountRS
     * @param amountKhr AmountRS
     * @param amountUsd AmountRS
     */
    public void updateSummeryAmount(SummaryAmountRS summaryAmountRS, AmountRS amountKhr, AmountRS amountUsd) {
        AmountRS summeryKhr = summaryAmountRS.getTotalKhr();
        AmountRS summeryUsd = summaryAmountRS.getTotalUsd();

        summeryKhr.addFromAmountRS(amountKhr);
        summeryUsd.addFromAmountRS(amountUsd);

        summaryAmountRS.setTotalKhr(summeryKhr);
        summaryAmountRS.setTotalUsd(summeryUsd);
    }

    public void updateSummeryAmount(SummaryAmountRS summaryAmountRS, TicketTotalDTO ticketTotalDTO) {
        AmountRS summeryKhr = new AmountRS(ticketTotalDTO, LotteryConstant.CURRENCY_KHR);
        AmountRS summeryUsd = new AmountRS(ticketTotalDTO, LotteryConstant.CURRENCY_USD);
        summaryAmountRS.setTotalKhr(summeryKhr);
        summaryAmountRS.setTotalUsd(summeryUsd);
    }

    private void setTotalDigit(SummaryAmountRS total1D, SummaryAmountRS total2D, SummaryAmountRS total3D, SummaryAmountRS total4D, OrderItemsDTO itemsDTO, int winQty) {
        if (LotteryConstant.REBATE1D.equals(itemsDTO.getRebateCode())) {
            setDigitCurrency(total1D, itemsDTO, winQty);
        }
        if (LotteryConstant.REBATE2D.equals(itemsDTO.getRebateCode())) {
            setDigitCurrency(total2D, itemsDTO, winQty);
        }
        if (LotteryConstant.REBATE3D.equals(itemsDTO.getRebateCode())) {
            setDigitCurrency(total3D, itemsDTO, winQty);
        }
        if (LotteryConstant.REBATE4D.equals(itemsDTO.getRebateCode())) {
            setDigitCurrency(total4D, itemsDTO, winQty);
        }
    }

    private void setDigitCurrency(SummaryAmountRS totalDigit, OrderItemsDTO itemsDTO, int winQty) {
        AmountRS amountRS = new AmountRS();
        if (LotteryConstant.CURRENCY_KHR.equals(itemsDTO.getCurrencyCode())) {
            BeanUtils.copyProperties(totalDigit.getTotalKhr(), amountRS);
            amountRS.setBetAmount(amountRS.getBetAmount().add(itemsDTO.getTotalAmount()));
            if (winQty > 0) {
                amountRS.setWinAmount(amountRS.getWinAmount().add(itemsDTO.getBetAmount().multiply(BigDecimal.valueOf(winQty))));
            }
            totalDigit.setTotalKhr(amountRS);
        }
        if (LotteryConstant.CURRENCY_USD.equals(itemsDTO.getCurrencyCode())) {
            BeanUtils.copyProperties(totalDigit.getTotalUsd(), amountRS);
            amountRS.setBetAmount(amountRS.getBetAmount().add(itemsDTO.getTotalAmount()));
            if (winQty > 0) {
                amountRS.setWinAmount(amountRS.getWinAmount().add(itemsDTO.getBetAmount().multiply(BigDecimal.valueOf(winQty))));
            }
            totalDigit.setTotalUsd(amountRS);
        }
    }

}
