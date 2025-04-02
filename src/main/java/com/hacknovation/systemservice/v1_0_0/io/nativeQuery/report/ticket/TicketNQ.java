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
@NativeQueryFolder("report/tickets")
public interface TicketNQ extends NativeQuery {

    List<OrderDTO> vnOneOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> vnOneOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> vnOneOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO vnOneTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);


    List<OrderDTO> vnTwoOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> vnTwoOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> vnTwoOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO vnTwoTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);

    List<OrderDTO> leapOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> leapOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> leapOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO leapTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);

    List<OrderDTO> tnOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> tnOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> tnOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO tnTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);

    List<OrderDTO> khOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> khOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> khOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    TicketTotalDTO khTotal(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline);

    List<OrderDTO> scOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    List<OrderDTO> thOrder(String userCode, String drawCode, String pageNumber, String filterByDate, String isWin, String isCancel);
    Page<OrderDTO> scOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    Page<OrderDTO> thOrderPaginate(boolean isLevel, List<String> memberCodes, String drawCode, String keyword, String filterByDate, String isWin, String isCancel, boolean userOnline, Pageable pageable);
    List<OrderItemsDTO> scOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);
    List<OrderItemsDTO> thOrderItemInOrderIds(List<BigInteger> orderIds, String isWin);

}
