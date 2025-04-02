package com.hacknovation.systemservice.v1_0_0.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempWinOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempWinOrderItemRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.WinOrderItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.winItem.WinItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.winItem.WinItemsRS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 01/02/2022
 * time: 14:29
 */
@Component
@RequiredArgsConstructor
public class WinItemTHUtility {


    private static final int NO_DRAW_ITEM = -1;
    private static final int NO_DATA_FOUND = -2;
    private static final int WIN_ITEM_FOUND = 1;
    private static final int NO_ONE_WIN_THIS_DRAW = 2;
    private static final int NO_ORDER_ITEM = 3;

    private final SqlUtility sqlUtility;
    private final THTempDrawingRP thTempDrawingRP;
    private final THTempWinOrderItemRP thTempWinOrderItemRP;

    @Value("classpath:query/drawItem/*.sql")
    Resource[] drawItemResources;

    @Value("classpath:query/orderItem/*.sql")
    Resource[] orderItemResources;


    public int setWinLoseByDraw(Long drawId, String drawCode, boolean isTemp) {
        try {
            String prefix = isTemp ? "th_temp": "th";
            String query = sqlUtility.getQuery(SqlUtility.getResource(drawItemResources, "th-drawing-item.sql"), Map.of("PREFIX", prefix));
            List<DrawingItemsDTO> drawingItemsDTOList = sqlUtility.executeQueryForList(query, Map.of("drawId", drawId), DrawingItemsDTO.class);

            if (drawingItemsDTOList.size() == 0) {
                return NO_DRAW_ITEM;
            }

            String queryOrderItem = sqlUtility.getQuery(SqlUtility.getResource(orderItemResources, "order-item-by-drawCode.sql"), Map.of("PREFIX", prefix));
            List<OrderItemsDTO> orderItemsDTOList = sqlUtility.executeQueryForList(queryOrderItem, Map.of("drawCode", drawCode), OrderItemsDTO.class);

            if (orderItemsDTOList.size() == 0) {
                return NO_ORDER_ITEM;
            }

            thTempDrawingRP.updateSetWinTrue(drawCode);

            Map<String, List<String>> mapPostWinNumbers = this.getWinNumbersMapByPost(drawingItemsDTOList);

            Map<String, BigDecimal> mapWinUserCode = new HashMap<>();
            List<WinOrderItemsDTO> winOrderItemsDTOList = new ArrayList<>();
            orderItemsDTOList.forEach(item -> this.findWinOrderItem(item, mapPostWinNumbers, winOrderItemsDTOList, mapWinUserCode));
            if (!winOrderItemsDTOList.isEmpty()) {
                System.out.println("WinItemTHUtility.setWinOrderItems has win size = " + mapWinUserCode.size());
                System.out.println(mapWinUserCode);
                List<THTempWinOrderItemsEntity> winOrderItemsEntities = winOrderItemsDTOList.stream().map(THTempWinOrderItemsEntity::new).collect(Collectors.toList());
                thTempWinOrderItemRP.saveAll(winOrderItemsEntities);
                thTempWinOrderItemRP.updateBalanceMemberOnlineByDrawCode(drawCode);

                copyDrawDrawItemAndWinItem(drawCode);

                copyOrderData(drawCode);

                return WIN_ITEM_FOUND;
            } else {
                System.out.println("No one win in this draw code" + drawId);
                return NO_ONE_WIN_THIS_DRAW;
            }
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return NO_DATA_FOUND;
        }
    }


    /**
     * get win item number
     * @param drawingItemsTOList List<DrawingItemsTO>
     * @return Map<String, List<String>>
     */
    public Map<String, List<String>> getWinNumbersMapByPost(List<DrawingItemsDTO> drawingItemsTOList) {
        Map<String, List<String>> mapPostWinNumbers = new HashMap<>();
        for (DrawingItemsDTO item : drawingItemsTOList) {
            List<String> numbers = new ArrayList<>();
            if (PostConstant.POST_B.equals(item.getPostCode()) &&  item.getTwoDigits() != null)
                numbers.add(item.getTwoDigits());
            if (PostConstant.POST_A.equals(item.getPostCode()) &&  item.getThreeDigits() != null)
                numbers.add(item.getThreeDigits());

            mapPostWinNumbers.put(item.getPostCode(), numbers);
        }

        return mapPostWinNumbers;
    }

    /**
     * Find win order item by temp order item
     * @param orderItemsTO         OrderItemsTO
     * @param mapPostWinNumbers     Map<String, List<String>>
     * @param winOrderItemsDTOList List<WinOrderItemsDTO>
     * @param mapWinUserCode        Map<String, BigDecimal>
     */
    public void findWinOrderItem(OrderItemsDTO orderItemsTO, Map<String, List<String>> mapPostWinNumbers, List<WinOrderItemsDTO> winOrderItemsDTOList, Map<String, BigDecimal> mapWinUserCode) {
        List<String> posts = Arrays.stream(orderItemsTO.getPostAnalyze().split(":")).collect(Collectors.toList());
        int winQty = 0;
        WinItemsRS winItemsRS = new WinItemsRS();
        List<WinItemRS> winItemRSList = new ArrayList<>();
        String[] numberDetails = orderItemsTO.getNumberDetail().replaceAll("\\s+", "").split(",");
        for (String post : posts) {
            List<String> winNumbers = new ArrayList<>();
            if (mapPostWinNumbers.containsKey(post.toUpperCase())) {
                winNumbers = new ArrayList<>(mapPostWinNumbers.get(post.toUpperCase()));
            }
            for (String number : numberDetails) {
                if (winNumbers.contains(number)) {
                    winQty = winQty + 1;
                    addWinItem(orderItemsTO, winItemRSList, post, number);
                }
            }
        }
        addWinItemDTOList(orderItemsTO, winOrderItemsDTOList, mapWinUserCode, winQty, winItemsRS, winItemRSList);
    }

    private void addWinItemDTOList(OrderItemsDTO orderItemsTO, List<WinOrderItemsDTO> winOrderItemsDTOList, Map<String, BigDecimal> mapWinUserCode, int winQty, WinItemsRS winItemsRS, List<WinItemRS> winItemRSList) {
        if (winQty > 0) {
            try {
                winItemsRS.setWinItems(winItemRSList);
                ObjectMapper objectMapper = new ObjectMapper();
                WinOrderItemsDTO winOrderItemsDTO = new WinOrderItemsDTO();
                winOrderItemsDTO.setDrawCode(orderItemsTO.getDrawCode());
                winOrderItemsDTO.setOrderId(orderItemsTO.getOrderId());
                winOrderItemsDTO.setOrderItemId(orderItemsTO.getId().intValue());
                winOrderItemsDTO.setWinQty(winQty);
                winOrderItemsDTO.setWinDetail(objectMapper.writeValueAsString(winItemsRS));
                winOrderItemsDTO.setIsSettlement(true);
                winOrderItemsDTOList.add(winOrderItemsDTO);

                /*
                 * Set map win user code
                 */
                BigDecimal winReward = orderItemsTO.getBetAmount().multiply(BigDecimal.valueOf(winQty)).multiply(orderItemsTO.getRebateRate());
                if (mapWinUserCode.containsKey(orderItemsTO.getMemberCode())) {
                    BigDecimal oldWin = mapWinUserCode.get(orderItemsTO.getMemberCode());
                    mapWinUserCode.put(orderItemsTO.getMemberCode(), oldWin.add(winReward));
                } else {
                    mapWinUserCode.put(orderItemsTO.getMemberCode(), winReward);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * add win order item to a list
     * @param orderItemsTO OrderItemsTO
     * @param winItemRSList List<WinItemRS>
     * @param post String
     * @param number String
     */
    private void addWinItem(OrderItemsDTO orderItemsTO, List<WinItemRS> winItemRSList, String post, String number) {
        WinItemRS winItemRS = new WinItemRS();
        winItemRS.setPostCode(post.toUpperCase());
        winItemRS.setNumber(number);
        winItemRS.setBetAmount(orderItemsTO.getBetAmount());
        winItemRS.setRebateCode(orderItemsTO.getRebateCode());
        winItemRSList.add(winItemRS);
    }


    private void copyDrawDrawItemAndWinItem(String drawCode) {

        /*
         * reset first before save
         */
        thTempDrawingRP.resetDrawing(drawCode);
        thTempDrawingRP.resetDrawingItems(drawCode);
        thTempDrawingRP.resetOriginalWinOrderItems(drawCode);

        /*
         * save draw, draw item and win item
         */
        thTempDrawingRP.saveDrawing(drawCode);
        thTempDrawingRP.saveDrawingItems(drawCode);
        thTempDrawingRP.saveWinOrderItems(drawCode);
    }

    private void copyOrderData(String drawCode) {

        // reset orders
        thTempDrawingRP.resetOrder(drawCode);
        thTempDrawingRP.resetOrderItems(drawCode);

        // save orders
        thTempDrawingRP.saveOrder(drawCode);
        thTempDrawingRP.saveOrderItems(drawCode);
    }
}
