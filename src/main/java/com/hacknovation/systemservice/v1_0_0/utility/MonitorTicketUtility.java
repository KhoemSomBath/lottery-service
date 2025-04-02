package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempOrderItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempOrdersRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempOrderRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.PagingRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.betting.BettingListRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.betting.ColumnRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Sombath
 * create at 18/3/23 5:10 AM
 */

@RequiredArgsConstructor
@Service
public class MonitorTicketUtility extends BaseServiceIP {

    private final JwtToken jwtToken;
    private final UserRP userRP;
    private final VNOneTempOrderRP vNOneTempOrderRP;
    private final VNTwoTempOrderRP vNTwoTempOrderRP;
    private final LeapTempOrderRP leapTempOrderRP;
    private final KHTempOrderRP kHTempOrderRP;
    private final TNTempOrderRP tNTempOrderRP;
    private final SCTempOrderRP scTempOrderRP;

    private final VNOneTempOrderItemsRP vnOneTempOrderItemsRP;
    private final VNTwoTempOrderItemsRP vNTwoTempOrderItemsRP;
    private final LeapTempOrderItemsRP leapTempOrderItemsRP;
    private final KHTempOrderItemsRP kHTempOrderItemsRP;
    private final TNTempOrderItemsRP tNTempOrderItemsRP;
    private final SCTempOrderItemRP scTempOrderItemRP;

    private final BettingUtility bettingUtility;
    private final THTempOrdersRP tHTempOrdersRP;
    private final THTempOrderItemsRP tHTempOrderItemsRP;

    public StructureRS getTicket(String lottery, String drawCode, Integer size, Integer isMark, Integer page) {

        BettingListRS bettingListRS = new BettingListRS();

        UserToken userToken = jwtToken.getUserToken();
        String userCode = userToken.getUserCode();
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
        }

        List<String> memberCode = new ArrayList<>();
        Map<String, String> mapUserCodeUsername = new HashMap<>();

        if (!UserConstant.SYSTEM.equalsIgnoreCase(userToken.getUserType())) {
            for (UserEntity userEntity : userRP.getAllByParentCode(userCode)) {
                String code = userEntity.getCode();
                memberCode.add(code);
                mapUserCodeUsername.put(code, userEntity.getUsername());
            }
        } else {
            for (UserEntity userEntity : userRP.getAllMember()) {
                String code = userEntity.getCode();
                memberCode.add(code);
                mapUserCodeUsername.put(code, userEntity.getUsername());
            }
        }


        List<OrderItemsDTO> orderItems = new ArrayList<>();
        List<OrderDTO> orders = new ArrayList<>();

        long total = 0L;
        int number = 0;

        List<Integer> ids;
        Pageable pageable;
        if (page != null)
            pageable = PageRequest.of(page > 0 ? page - 1 : 0, size);
        else
            pageable = Pageable.ofSize(size);

        switch (lottery) {
            case LotteryConstant.VN1:
                Page<VNOneTempOrdersEntity> vnOneTempOrdersEntityPage = vNOneTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);

                if (page == null && vnOneTempOrdersEntityPage.getTotalPages() > 1) {
                    pageable = PageRequest.of(vnOneTempOrdersEntityPage.getTotalPages() - 1, size);
                    vnOneTempOrdersEntityPage = vNOneTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);
                }

                total = vnOneTempOrdersEntityPage.getTotalElements();
                number = vnOneTempOrdersEntityPage.getNumber();

                orders = vnOneTempOrdersEntityPage.stream().map(OrderDTO::new).collect(Collectors.toList());
                ids = orders.stream().map(i -> i.getId().intValue()).collect(Collectors.toList());
                orderItems = vnOneTempOrderItemsRP.findByOrderIdIn(ids).stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.VN2:
                Page<VNTwoTempOrdersEntity> vnTwoTempOrdersEntityPage = vNTwoTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);

                if (page == null && vnTwoTempOrdersEntityPage.getTotalPages() > 1) {
                    pageable = PageRequest.of(vnTwoTempOrdersEntityPage.getTotalPages() - 1, size);
                    vnTwoTempOrdersEntityPage = vNTwoTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);
                }

                total = vnTwoTempOrdersEntityPage.getTotalElements();
                number = vnTwoTempOrdersEntityPage.getNumber();

                orders = vnTwoTempOrdersEntityPage.stream().map(OrderDTO::new).collect(Collectors.toList());
                ids = orders.stream().map(i -> i.getId().intValue()).collect(Collectors.toList());
                orderItems = vNTwoTempOrderItemsRP.findByOrderIdIn(ids).stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.LEAP:
                Page<LeapTempOrdersEntity> leapTempOrdersEntityPage = leapTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);

                if (page == null && leapTempOrdersEntityPage.getTotalPages() > 1) {
                    pageable = PageRequest.of(leapTempOrdersEntityPage.getTotalPages() - 1, size);
                    leapTempOrdersEntityPage = leapTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);
                }

                total = leapTempOrdersEntityPage.getTotalElements();
                number = leapTempOrdersEntityPage.getNumber();

                orders = leapTempOrdersEntityPage.stream().map(OrderDTO::new).collect(Collectors.toList());
                ids = orders.stream().map(i -> i.getId().intValue()).collect(Collectors.toList());
                orderItems = leapTempOrderItemsRP.findByOrderIdIn(ids).stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.KH:
                Page<KHTempOrdersEntity> khTempOrdersEntityPage = kHTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);

                if (page == null && khTempOrdersEntityPage.getTotalPages() > 1) {
                    pageable = PageRequest.of(khTempOrdersEntityPage.getTotalPages() - 1, size);
                    khTempOrdersEntityPage = kHTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);
                }

                total = khTempOrdersEntityPage.getTotalElements();
                number = khTempOrdersEntityPage.getNumber();

                orders = khTempOrdersEntityPage.stream().map(OrderDTO::new).collect(Collectors.toList());
                ids = orders.stream().map(i -> i.getId().intValue()).collect(Collectors.toList());
                orderItems = kHTempOrderItemsRP.findByOrderIdIn(ids).stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.TN:
                Page<TNTempOrdersEntity> tnTempOrdersEntityPage = tNTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);

                if (page == null && tnTempOrdersEntityPage.getTotalPages() > 1) {
                    pageable = PageRequest.of(tnTempOrdersEntityPage.getTotalPages() - 1, size);
                    tnTempOrdersEntityPage = tNTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);
                }

                total = tnTempOrdersEntityPage.getTotalElements();
                number = tnTempOrdersEntityPage.getNumber();

                orders = tnTempOrdersEntityPage.stream().map(OrderDTO::new).collect(Collectors.toList());
                ids = orders.stream().map(i -> i.getId().intValue()).collect(Collectors.toList());
                orderItems = tNTempOrderItemsRP.findByOrderIdIn(ids).stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.SC:
                Page<SCTempOrdersEntity> scTempOrdersEntityPage = scTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);

                if (page == null && scTempOrdersEntityPage.getTotalPages() > 1) {
                    pageable = PageRequest.of(scTempOrdersEntityPage.getTotalPages() - 1, size);
                    scTempOrdersEntityPage = scTempOrderRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);
                }

                total = scTempOrdersEntityPage.getTotalElements();
                number = scTempOrdersEntityPage.getNumber();

                orders = scTempOrdersEntityPage.stream().map(OrderDTO::new).collect(Collectors.toList());
                ids = orders.stream().map(i -> i.getId().intValue()).collect(Collectors.toList());
                orderItems = scTempOrderItemRP.findByOrderIdIn(ids).stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.TH:
                Page<THTempOrdersEntity> thTempOrdersEntityPage = tHTempOrdersRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);

                if (page == null && thTempOrdersEntityPage.getTotalPages() > 1) {
                    pageable = PageRequest.of(thTempOrdersEntityPage.getTotalPages() - 1, size);
                    thTempOrdersEntityPage = tHTempOrdersRP.findByDrawCodeAndUserCodeInOrderByIdDesc(drawCode, memberCode, isMark, pageable);
                }

                total = thTempOrdersEntityPage.getTotalElements();
                number = thTempOrdersEntityPage.getNumber();

                orders = thTempOrdersEntityPage.stream().map(OrderDTO::new).collect(Collectors.toList());
                ids = orders.stream().map(i -> i.getId().intValue()).collect(Collectors.toList());
                orderItems = tHTempOrderItemsRP.findByOrderIdIn(ids).stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
        }

        Map<BigInteger, OrderDTO> mapOrderById = orders.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
        Map<Integer, List<OrderItemsDTO>> groupByOrderId = orderItems.stream().collect(Collectors.groupingBy(OrderItemsDTO::getOrderId));
        List<ColumnRS> columnRSList = new ArrayList<>();
        List<BigInteger> orderIds = new ArrayList<>(mapOrderById.keySet());
        Collections.sort(orderIds);
        for (BigInteger orderId : orderIds) {
            OrderDTO orderDTO = mapOrderById.get(orderId);
            ColumnRS columnRS = bettingUtility.getColumn(orderDTO.getId().intValue(), mapOrderById, groupByOrderId.get(orderId.intValue()), mapUserCodeUsername);
            columnRS.setStatus(orderDTO.getStatus().intValue());
            columnRSList.add(columnRS);
        }

        bettingListRS.setLanes(columnRSList);

        return responseBody(HttpStatus.OK,
                MessageConstant.SUCCESSFULLY,
                bettingListRS,
                new PagingRS(number, size, total));
    }

}


