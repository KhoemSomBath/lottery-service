package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.ticket;


import com.hacknovation.systemservice.v1_0_0.ui.model.dto.TicketTotalDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
@NativeQueryFolder("report/tickets/temp")
public interface TempTicketNQ extends NativeQuery {

    List<OrderDTO> vnOneTempOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> vnOneTempOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    TicketTotalDTO vnOneTempTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);
    List<OrderItemsDTO> vnOneTempOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);

    List<OrderDTO> vnTwoTempOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> vnTwoTempOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> vnTwoTempOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO vnTwoTempTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);

    List<OrderDTO> leapTempOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> leapTempOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> leapTempOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO leapTempTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);

    List<OrderDTO> tnTempOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> tnTempOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> tnTempOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO tnTempTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);

    List<OrderDTO> khTempOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> khTempOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> khTempOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO khTempTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);

    List<OrderDTO> scTempOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    List<OrderDTO> thTempOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> scTempOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    Page<OrderDTO> thTempOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> scTempOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    List<OrderItemsDTO> thTempOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
}