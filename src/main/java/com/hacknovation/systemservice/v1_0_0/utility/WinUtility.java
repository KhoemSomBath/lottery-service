package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.drawItem.DrawingItemNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.drawItem.DrawingItemTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.originalOrder.OriginalTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.transform.DrawItemTF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * author : phokkinnky
 * date : 8/24/21
 */
@Component
@RequiredArgsConstructor
public class WinUtility {

    private final DrawingItemNQ drawingItemNQ;

    public List<DrawingItemTO> getResultSetLeap(String drawAt) {
        return drawingItemNQ.leapDrawingItem(drawAt);
    }

    public List<DrawingItemTO> getResultSetKhr(String drawAt) {
        return drawingItemNQ.khrDrawingItem(drawAt);
    }

    public List<DrawingItemTO> getResultSetVNOne(String drawAt) {
        return drawingItemNQ.vnoneDrawingItem(drawAt);
    }

    public List<DrawingItemTO> getResultSetVNTwo(String drawAt) {
        return drawingItemNQ.vntwoDrawingItem(drawAt);
    }

    public List<DrawingItemTO> getResultSetTN(String drawAt) {
        return drawingItemNQ.tnDrawingItem(drawAt);
    }

    private List<String> getPostLoDay(List<String> posts) {
        List<String> loList = new ArrayList<>(posts);
        for (int i = 1; i <= 15; i++) {

            loList.add("lo" + i);
        }
        return loList;
    }

    private List<String> getPostLoNight(List<String> posts) {
        List<String> loList = new ArrayList<>(posts);
        for (int i = 1; i <= 19; i++) {

            loList.add("lo" + i);
        }
        return loList;
    }

    public int countWinOriginalOrder(List<DrawingItemTO> drawingItemTOList, OriginalTO orderingTO, boolean isNight) {
        int winQty = 0;
        Map<String, DrawingItemTO> mapDrawItemByPost = drawingItemTOList.stream().collect(Collectors.toMap(DrawingItemTO::getPostCode, Function.identity()));
        List<String> posts = new ArrayList<>(getListPosts(orderingTO.getPosts(), isNight));
        String[] numberQuantities = orderingTO.getNumberDetail().replaceAll("\\s+","").split(",");
        for (String number : numberQuantities) {
            for (String post : posts) {
                if (post.equalsIgnoreCase("A1")) post = "A";
                if (mapDrawItemByPost.get(post.toUpperCase()) != null) {
                    DrawingItemTO drawingItemTO = mapDrawItemByPost.get(post.toUpperCase());
                    if (orderingTO.getPosts().contains("Lo")) {
                        if (!post.contains("lo") && number.length() == 2) {
                            if (StringUtils.right(drawingItemTO.getThreeDigits(), 2).equalsIgnoreCase(number)) {
                                winQty = winQty + 1;
                            }
                            if (drawingItemTO.getTwoDigits().equalsIgnoreCase(number)) {
                                winQty = winQty + 1;
                            }
                        } else {
                            if (drawingItemTO.getTwoDigits().equalsIgnoreCase(number) || drawingItemTO.getThreeDigits().equalsIgnoreCase(number)) {
                                winQty = winQty + 1;
                            }
                        }
                    } else {
                        if (drawingItemTO.getTwoDigits().equalsIgnoreCase(number) || drawingItemTO.getThreeDigits().equalsIgnoreCase(number)) {
                            winQty = winQty + 1;
                        }
                    }
                    if (number.length() == 1 && drawingItemTO.getThreeDigits().length() == 3 ) {
                        if (StringUtils.left(drawingItemTO.getThreeDigits(), 1).equalsIgnoreCase(number)) {
                            winQty = winQty + 1;
                        }
                    }
                }

            }
        }

        return winQty;
    }

    public int countWinLeapOriginalOrder(List<DrawingItemTO> drawingItemTOList, OriginalTO orderingTO) {
        int winQty = 0;
        Map<String, DrawingItemTO> mapDrawItemByPost = drawingItemTOList.stream().collect(Collectors.toMap(DrawingItemTO::getPostCode, Function.identity()));
        List<String> posts = Arrays.asList(orderingTO.getPosts()
                .replace("L", "").replace("o", "")
                .replaceAll("\\s+", "")
                .split(""));
        if (orderingTO.getPosts().contains("Lo")) {
            posts = new ArrayList<>(getPostLoNight(posts));
            posts.addAll(List.of("A", "B", "C", "D", "lo20"));
        }
        String[] numberQuantities = orderingTO.getNumberDetail().replaceAll("\\s+","").split(",");
        for (String number : numberQuantities) {
            for (String post : posts) {
                if (mapDrawItemByPost.get(post.toUpperCase()) != null) {
                    DrawingItemTO drawingItemTO = mapDrawItemByPost.get(post.toUpperCase());
                    if (drawingItemTO.getTwoDigits().equalsIgnoreCase(number) || drawingItemTO.getThreeDigits().equalsIgnoreCase(number) || drawingItemTO.getFourDigits().equalsIgnoreCase(number)) {
                        winQty = winQty + 1;
                    }
                    if (number.length() == 1 && drawingItemTO.getThreeDigits().length() == 3 ) {
                        if (StringUtils.left(drawingItemTO.getThreeDigits(), 1).equalsIgnoreCase(number)) {
                            winQty = winQty + 1;
                        }
                    }
                    if (orderingTO.getPosts().contains("Lo") && !post.contains("lo")) {
                        /**
                         * If mapDrawItemPost don't have LO16 mean it VN
                         */
                        if (mapDrawItemByPost.get("LO16") == null) {
                            if (number.length() == 2 && StringUtils.right(drawingItemTO.getThreeDigits(), 2).equalsIgnoreCase(number)) {
                                    winQty = winQty + 1;
                            }
                        }
                    }
                }
            }
        }

        return winQty;
    }

    public void transformDrawItem(List<DrawItemTF> drawItemTFList, List<DrawingItemTO> drawingItemTOList) {
        for (DrawingItemTO drawingItemTO : drawingItemTOList) {
            DrawItemTF drawItemTF = new DrawItemTF();
            BeanUtils.copyProperties(drawingItemTO, drawItemTF);
            drawItemTFList.add(drawItemTF);
        }
    }

    public void calculateWinMap(List<DrawingItemTO> drawingItemTOList, List<OriginalTO> originalTOList, Map<String, BigDecimal> mapDrawPageRebate) {
        Map<String, List<DrawingItemTO>> mapDrawCodeDrawItem = drawingItemTOList.stream().collect(Collectors.groupingBy(DrawingItemTO::getDrawCode, Collectors.toList()));
        for (OriginalTO originalTO : originalTOList) {
            String key = getKeyMap(originalTO);
            List<DrawingItemTO> drawingItemTOS = new ArrayList<>();
            if (mapDrawCodeDrawItem.containsKey(originalTO.getDrawCode())) {
                drawingItemTOS = mapDrawCodeDrawItem.get(originalTO.getDrawCode());
            }
            int winQty;
            if (drawingItemTOS.get(0).getIsNight()) {
                winQty = countWinOriginalOrder(drawingItemTOS, originalTO, true);
            } else {
                winQty = countWinLeapOriginalOrder(drawingItemTOS, originalTO);
            }
            if (mapDrawPageRebate.containsKey(key)) {
                BigDecimal oldValue = mapDrawPageRebate.get(key);
                mapDrawPageRebate.put(key, oldValue.add(originalTO.getBetAmount().multiply(BigDecimal.valueOf(winQty))));
            } else {
                mapDrawPageRebate.put(key, originalTO.getBetAmount().multiply(BigDecimal.valueOf(winQty)));
            }
        }
    }

    public void calculateWin(List<DrawingItemTO> drawingItemTOList, List<OriginalTO> originalTOList, Map<String, BigDecimal> mapDrawPageRebate) {
        Map<String, List<DrawingItemTO>> mapDrawCodeDrawItem = drawingItemTOList.stream().collect(Collectors.groupingBy(DrawingItemTO::getDrawCode, Collectors.toList()));
        for (OriginalTO originalTO : originalTOList) {
            String key = originalTO.getUserCode().concat(originalTO.getRebateCode()).concat(originalTO.getCurrencyCode());
            List<DrawingItemTO> drawingItemTOS = new ArrayList<>();
            if (mapDrawCodeDrawItem.containsKey(originalTO.getDrawCode())) {
                drawingItemTOS = mapDrawCodeDrawItem.get(originalTO.getDrawCode());
            }
            int winQty;
            if (drawingItemTOS.get(0).getIsNight()) {
                winQty = countWinOriginalOrder(drawingItemTOS, originalTO, true);
            } else {
                winQty = countWinLeapOriginalOrder(drawingItemTOS, originalTO);
            }
            if (mapDrawPageRebate.containsKey(key)) {
                BigDecimal oldValue = mapDrawPageRebate.get(key);
                mapDrawPageRebate.put(key, oldValue.add(originalTO.getBetAmount().multiply(BigDecimal.valueOf(winQty))));
            } else {
                mapDrawPageRebate.put(key, originalTO.getBetAmount().multiply(BigDecimal.valueOf(winQty)));
            }
        }
    }

    public String getKeyMap(OriginalTO item) {
        try {
            return item.getUserCode().concat("_").concat(item.getDrawCode()).concat("_").concat(String.valueOf(item.getPageNumber())).concat("_").concat(item.getRebateCode()).concat("_").concat(item.getCurrencyCode());
        } catch (Exception exception) {
//            exception.printStackTrace();
            return "";
        }
    }

    public List<String> getListPosts(String postStr, boolean isNight) {
        List<String> posts  = new ArrayList<>();
        if (postStr.contains("A1")) {
            posts.add("A1");
        }
        if (postStr.contains("A2")) {
            posts.add("A2");
        }
        if (postStr.contains("A3")) {
            posts.add("A3");
        }
        if (postStr.contains("A4")) {
            posts.add("A4");
        }
        String[] postsArr = postStr
                .replace("L", "")
                .replace("o", "")
                .replaceAll("\\s+", "")
                .replace("A1", "")
                .replace("A2", "")
                .replace("A3", "")
                .replace("A4", "")
                .split("");
        if (postsArr.length > 0) {
            posts = new ArrayList<>(posts);
            posts.addAll(List.of(postsArr));
        }
        if (postStr.contains("Lo")) {
            posts = new ArrayList<>(getPostLoDay(posts));
            posts.addAll(List.of("A", "B", "C", "D"));
        }
        if (isNight) {
            if (postStr.contains("Lo")) {
                posts.addAll(List.of("lo16", "lo17", "lo18", "lo19"));
            }
            if (posts.contains("A")) {
                posts = new ArrayList<>(posts);
                posts.addAll(List.of("A2", "A3", "A4"));
            }
        }
        posts.removeAll(List.of(""));

        return posts;
    }
}
