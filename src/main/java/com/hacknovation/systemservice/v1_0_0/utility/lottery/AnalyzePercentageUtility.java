package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacknovation.systemservice.constant.HazelcastConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.analyzenumber.AnalyzingItemHZ;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.RebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.MainAnalyseRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:13
 */
@Component
@RequiredArgsConstructor
public class AnalyzePercentageUtility {

    private final LeapTempDrawingRP leapTempDrawingRP;
    private final KHTempDrawingRP khTempDrawingRP;
    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final VNOneTempOrderItemsRP vnOneTempOrderItemsRP;

    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final VNTwoTempOrderItemsRP vnTwoTempOrderItemsRP;

    private final LeapTempOrderItemsRP leapTempOrderItemsRP;
    private final KHTempOrderItemsRP khTempOrderItemsRP;

    private final TNTempDrawingRP tnTempDrawingRP;
    private final TNTempOrderItemsRP tnTempOrderItemsRP;

    private final GeneralUtility generalUtility;
    private final HazelcastInstance hazelcastInstance;

    private final UserNQ userNQ;

    private final JwtToken jwtToken;

    private String analyzeType;

    /**
     * getOrderItemLoData
     *
     * @param analyzeRQ   AnalyzeRQ
     * @param memberCodes List<String>
     * @return List<OrderItemsDTO>
     */
    private List<OrderItemsDTO> getOrderItemsDTOLoData(AnalyzeRQ analyzeRQ, List<String> memberCodes, String analyzeType) {
        List<OrderItemsDTO> orderItemsDTOList = new ArrayList<>();
        switch (analyzeRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:
                List<VNOneTempOrderItemsEntity> tempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    case "NO_RANGE":
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    default:
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                }
                orderItemsDTOList = tempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.VN2:
                List<VNTwoTempOrderItemsEntity> vnTwoTempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    case "NO_RANGE":
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    default:
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                }
                orderItemsDTOList = vnTwoTempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.TN:
                List<TNTempOrderItemsEntity> tnTempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    case "NO_RANGE":
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    default:
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                }
                orderItemsDTOList = tnTempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.LEAP:
                List<LeapTempOrderItemsEntity> leapTempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    case "NO_RANGE":
                        leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    default:
                        leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                }
                orderItemsDTOList = leapTempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.KH:
                List<KHTempOrderItemsEntity> khTempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    case "NO_RANGE":
                        khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                    default:
                        khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                        break;
                }
                orderItemsDTOList = khTempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
        }

        return orderItemsDTOList;
    }

    @SneakyThrows
    private List<AnalyzingItemHZ> getAnalyzeTOData(AnalyzeRQ analyzeRQ, List<String> usernameList, String drawCode, String analyzeType) {
        List<AnalyzingItemHZ> analyzingItemHZS = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        String lotteryType = analyzeRQ.getLotteryType().toUpperCase();
        String collection = HazelcastConstant.GET_ANALYZE_NUMBER(lotteryType);
        IMap<String, Map<String, List<String>>> data = hazelcastInstance.getMap(collection);
        List<String> drawCodeFtSeniorCode = usernameList.stream().map(l -> drawCode.concat(":").concat(l)).collect(Collectors.toList());

        boolean isRange = analyzeType.equals("RANGE");
        List<String> cacheKeys = new ArrayList<>();
        List<String> allKey = new ArrayList<>(data.keySet());
        for (String code : drawCodeFtSeniorCode) {
            for (String s : allKey) {
                if (s.startsWith(code)) {
                    cacheKeys.add(s);
                }
            }
        }

        for (String drawAndSeniorKey : cacheKeys) {
            Map<String, List<String>> itemByDrawAndSenior = data.get(drawAndSeniorKey);
            for (String post : itemByDrawAndSenior.keySet()) {
                List<String> itemByPost = itemByDrawAndSenior.get(post);
                for (String itemString : itemByPost) {
                    AnalyzingItemHZ item = mapper.readValue(itemString, AnalyzingItemHZ.class);
                    if (!"NORMAL".equals(analyzeType)) {
                        if (isRange & !item.getIsRange()) continue;
                        if (!isRange && item.getIsRange()) continue;
                    }
                    if (LotteryConstant.POST.equals(item.getPostGroup()) || PostConstant.LO1.equals(item.getPostCode())) {

                        // find duplicate
                        OptionalInt indexOpt = IntStream.range(0, analyzingItemHZS.size())
                                .filter(it -> analyzingItemHZS.get(it).getPostCode().equals(item.getPostCode()) &&
                                        analyzingItemHZS.get(it).getNumber().equals(item.getNumber())
                                )
                                .findFirst();

                        if (indexOpt.isEmpty())
                            analyzingItemHZS.add(item);
                        else {
                            AnalyzingItemHZ _analyzingItemHZ = analyzingItemHZS.get(indexOpt.getAsInt());
                            _analyzingItemHZ.setBetAmount(_analyzingItemHZ.getBetAmount().add(item.getBetAmount()));
                            _analyzingItemHZ.setCommissionAmount(_analyzingItemHZ.getCommissionAmount().add(item.getCommissionAmount()));
                            _analyzingItemHZ.setPrizeAmount(_analyzingItemHZ.getPrizeAmount().add(item.getPrizeAmount()));

                            _analyzingItemHZ.setSuperCommissionAmount(_analyzingItemHZ.getSuperCommissionAmount().add(item.getSuperCommissionAmount()));
                            _analyzingItemHZ.setSuperPrizeAmount(_analyzingItemHZ.getSuperPrizeAmount().add(item.getSuperPrizeAmount()));

                            _analyzingItemHZ.setMasterCommissionAmount(_analyzingItemHZ.getMasterCommissionAmount().add(item.getMasterCommissionAmount()));
                            _analyzingItemHZ.setMasterPrizeAmount(_analyzingItemHZ.getMasterPrizeAmount().add(item.getMasterPrizeAmount()));

                            analyzingItemHZS.set(indexOpt.getAsInt(), _analyzingItemHZ);
                        }
                    }
                }
            }
        }

        return analyzingItemHZS;
    }

    private List<OrderItemsDTO> getOrderItemsDTOAx4(AnalyzeRQ analyzeRQ, List<String> memberCodes, String analyzeType) {
        List<OrderItemsDTO> orderItemsDTOList = new ArrayList<>();
        switch (analyzeRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:
                List<VNOneTempOrderItemsEntity> tempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByPostAx4AndRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    case "NO_RANGE":
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByPostAx4AndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    default:
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByPostAx4(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                }
                orderItemsDTOList = tempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.VN2:
                List<VNTwoTempOrderItemsEntity> vnTwoTempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByPostAx4AndRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    case "NO_RANGE":
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByPostAx4AndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    default:
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByPostAx4(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                }
                orderItemsDTOList = vnTwoTempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.TN:
                List<TNTempOrderItemsEntity> tnTempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByPostAx4AndRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    case "NO_RANGE":
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByPostAx4AndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    default:
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByPostAx4(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                }
                orderItemsDTOList = tnTempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
        }

        return orderItemsDTOList;
    }

    private List<OrderItemsDTO> getOrderItemsDTOA1_4(AnalyzeRQ analyzeRQ, List<String> memberCodes, String analyzeType) {
        List<OrderItemsDTO> orderItemsDTOList = new ArrayList<>();
        switch (analyzeRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:
                List<VNOneTempOrderItemsEntity> tempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByPostA1A2A3A4AndRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    case "NO_RANGE":
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByPostA1A2A3A4AndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    default:
                        tempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByPostA1A2A3A4(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                }
                orderItemsDTOList = tempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.VN2:
                List<VNTwoTempOrderItemsEntity> vnTwoTempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByPostA1A2A3A4AndRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    case "NO_RANGE":
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByPostA1A2A3A4AndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    default:
                        vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByPostA1A2A3A4(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                }
                orderItemsDTOList = vnTwoTempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
            case LotteryConstant.TN:
                List<TNTempOrderItemsEntity> tnTempOrderItemsEntities;
                switch (analyzeType) {
                    case "RANGE":
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByPostA1A2A3A4AndRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    case "NO_RANGE":
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByPostA1A2A3A4AndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    default:
                        tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByPostA1A2A3A4(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                }
                orderItemsDTOList = tnTempOrderItemsEntities.stream().map(OrderItemsDTO::new).collect(Collectors.toList());
                break;
        }

        return orderItemsDTOList;
    }

    /**
     * update default filter
     *
     * @param mainAnalyseRS MainAnalyseRS
     * @param lotteryType   String
     * @param isNight       boolean
     */
    public void updateDefaultVNFilter(MainAnalyseRS mainAnalyseRS, String lotteryType, boolean isNight) {
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        List<String> posts = new ArrayList<>(PostConstant.VN1_ALL_POST);
        if (!List.of(LotteryConstant.VN1, LotteryConstant.VN2).contains(lotteryType)) {
            posts.remove(PostConstant.POST_K);
        }
        if (isNight) {
            for (String postCode : PostConstant.A_NIGHT) {
                RebateRQ rebateRQ = new RebateRQ();
                rebateRQ.setPostCode(postCode);
                analyzeRQ.getFilterPosts().add(rebateRQ);
            }
        }
        for (String postCode : posts) {
            if (isNight && List.of(PostConstant.POST_A, PostConstant.POST_F, PostConstant.POST_I, PostConstant.POST_N, PostConstant.POST_K).contains(postCode))
                continue;
            RebateRQ rebateRQ = new RebateRQ();
            rebateRQ.setPostCode(postCode);
            analyzeRQ.getFilterPosts().add(rebateRQ);
        }
        if (!isNight) {
            for (String postCode : PostConstant.LO_DAY_ANALYZE) {
                RebateRQ rebateRQ = new RebateRQ();
                rebateRQ.setPostCode(postCode);
                analyzeRQ.getFilterPosts().add(rebateRQ);
            }
        }
    }

    public void updateDefaultTNFilter(MainAnalyseRS mainAnalyseRS, String shiftCode) {
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        List<String> posts = new ArrayList<>(PostConstant.GET_TN_POST_BY_SHIFT_CODE(shiftCode));
        posts.removeIf(element -> element.startsWith("LO"));

        boolean nightShift = PostConstant.TN_SHIFT_3_CODE.equals(shiftCode) || PostConstant.TN_SHIFT_5_CODE.equals(shiftCode);
        if (nightShift) {
            List<String> _post = new ArrayList<>(PostConstant.A_NIGHT);
            _post.addAll(List.of("O", "O2", "O3"));
            for (String postCode : _post) {
                RebateRQ rebateRQ = new RebateRQ();
                rebateRQ.setPostCode(postCode);
                analyzeRQ.getFilterPosts().add(rebateRQ);
            }
        }

        for (String postCode : posts) {
            if (nightShift && List.of(PostConstant.POST_A, PostConstant.POST_F, PostConstant.POST_I, PostConstant.POST_N, PostConstant.POST_K, PostConstant.POST_P).contains(postCode))
                continue;

            RebateRQ rebateRQ = new RebateRQ();
            rebateRQ.setPostCode(postCode);
            analyzeRQ.getFilterPosts().add(rebateRQ);
        }
        if (!nightShift) {
            for (String postCode : PostConstant.LO_DAY_ANALYZE) {
                RebateRQ rebateRQ = new RebateRQ();
                rebateRQ.setPostCode(postCode);
                analyzeRQ.getFilterPosts().add(rebateRQ);
            }
        }
    }

    /**
     * update default filter
     *
     * @param mainAnalyseRS MainAnalyseRS
     */
    public void updateDefaultLeapFilter(MainAnalyseRS mainAnalyseRS) {
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        for (String postCode : PostConstant.POST_LEAP) {
            RebateRQ rebateRQ = new RebateRQ();
            rebateRQ.setPostCode(postCode);
            analyzeRQ.getFilterPosts().add(rebateRQ);
        }
        for (String postCode : PostConstant.LO_LEAP_ANALYZE) {
            RebateRQ rebateRQ = new RebateRQ();
            rebateRQ.setPostCode(postCode);
            analyzeRQ.getFilterPosts().add(rebateRQ);
        }
    }

    /**
     * update default filter
     *
     * @param mainAnalyseRS MainAnalyseRS
     */
    public void updateDefaultSCFilter(MainAnalyseRS mainAnalyseRS) {
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        for (String postCode : PostConstant.POST_SC) {
            RebateRQ rebateRQ = new RebateRQ();
            rebateRQ.setPostCode(postCode);
            analyzeRQ.getFilterPosts().add(rebateRQ);
        }
    }

    /**
     * update default filter
     *
     * @param mainAnalyseRS MainAnalyseRS
     */
    public void updateDefaultKHFilter(MainAnalyseRS mainAnalyseRS) {
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        for (String postCode : PostConstant.POST_KH) {
            RebateRQ rebateRQ = new RebateRQ();
            rebateRQ.setPostCode(postCode);
            analyzeRQ.getFilterPosts().add(rebateRQ);
        }
    }

    public void updateDefaultTransferMoney(MainAnalyseRS mainAnalyseRS) {
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        analyzeRQ.getMainRebate().setDefaultMoney();
        List<RebateRQ> rebateRQList = new ArrayList<>();
        List<String> posts;
        switch (analyzeRQ.getLotteryType()) {
            case LotteryConstant.LEAP:
                posts = PostConstant.POST_TRANSFER_MONEY_LEAP;
                break;
            case LotteryConstant.SC:
                posts = PostConstant.POST_TRANSFER_MONEY_SC;
                break;
            default:
                posts = PostConstant.POST_TRANSFER_MONEY;
                break;
        }
        for (String postCode : posts) {
            RebateRQ rebateRQ = new RebateRQ();
            rebateRQ.setPostCode(postCode);
            rebateRQ.setDefaultMoney();
            rebateRQList.add(rebateRQ);
        }
        analyzeRQ.setFilterPosts(rebateRQList);
    }

    /**
     * analyze percentage of betting
     *
     * @param mainAnalyseRS MainAnalyseRS
     * @param memberCodes   List<String>
     * @param usernameList  List<String>
     * @param analyzeType   String
     * @param drawingDTO    DrawingDTO
     */
    public void analyzePercentage(MainAnalyseRS mainAnalyseRS, List<String> memberCodes, List<String> usernameList, String analyzeType, DrawingDTO drawingDTO) {
        UserToken userToken = jwtToken.getUserToken();
        this.analyzeType = analyzeType;
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        Map<String, RebateRQ> mapFilterByPost = analyzeRQ.getFilterPosts().stream().collect(Collectors.toMap(RebateRQ::getPostCode, Function.identity()));

        boolean isVN = true;

        switch (analyzeRQ.getLotteryType().toUpperCase()) {
            case LotteryConstant.VN1:
                VNOneTempDrawingEntity drawingEntity = vnOneTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                analyzeRQ.setDrawAt(drawingEntity.getResultedPostAt());
                BeanUtils.copyProperties(drawingEntity, drawingDTO);
                break;
            case LotteryConstant.VN2:
                VNTwoTempDrawingEntity vnTwoTempDrawingEntity = vnTwoTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                analyzeRQ.setDrawAt(vnTwoTempDrawingEntity.getResultedPostAt());
                BeanUtils.copyProperties(vnTwoTempDrawingEntity, drawingDTO);
                break;
            case LotteryConstant.TN:
                TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                analyzeRQ.setDrawAt(tnTempDrawingEntity.getResultedPostAt());
                BeanUtils.copyProperties(tnTempDrawingEntity, drawingDTO);
                break;
            case LotteryConstant.KH:
                KHTempDrawingEntity khTempDrawingEntity = khTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                analyzeRQ.setDrawAt(khTempDrawingEntity.getResultedPostAt());
                BeanUtils.copyProperties(khTempDrawingEntity, drawingDTO);
                break;
            default:
                LeapTempDrawingEntity leapTempDrawingEntity = leapTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                analyzeRQ.setDrawAt(leapTempDrawingEntity.getResultedPostAt());
                BeanUtils.copyProperties(leapTempDrawingEntity, drawingDTO);
                isVN = false;
                break;
        }
        if (isVN) {
            if (drawingDTO.getIsNight()) {
                if (LotteryConstant.LO_GROUP.equals(analyzeRQ.getPostGroup())) {
                    analyzeLo(mainAnalyseRS, memberCodes, mapFilterByPost);
                } else {
                    String userCode = generalUtility.getUserCode(userToken);
                    List<UserLevelReportTO> filterMemberTOS = userNQ.userLevelFilter(userCode, userToken.getUserType(), LotteryConstant.ALL, "member", UserConstant.ALL, true);
                    analyzePostNight(mainAnalyseRS,
                            filterMemberTOS.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList()),
                            usernameList,
                            mapFilterByPost,
                            analyzeRQ.getDrawCode());
                }
            } else {
                if (LotteryConstant.LO_GROUP.equals(analyzeRQ.getPostGroup())) {
                    analyzeLo(mainAnalyseRS, memberCodes, mapFilterByPost);
                } else {
                    analyzePostDay(mainAnalyseRS, usernameList, mapFilterByPost, analyzeRQ.getDrawCode());
                }
            }
        } else {
            if (LotteryConstant.LO_GROUP.equals(analyzeRQ.getPostGroup())) {
                analyzeLo(mainAnalyseRS, memberCodes, mapFilterByPost);
            } else {
                analyzePostDay(mainAnalyseRS, usernameList, mapFilterByPost, analyzeRQ.getDrawCode());
            }
        }

    }

    /**
     * sort analyze percentage by rebate code and bet amount
     *
     * @param mainAnalyseRS MainAnalyseRS
     */

    /**
     * get analyze for post with draw night
     *
     * @param mainAnalyseRS   MainAnalyseRS
     * @param memberCodes     List<String>
     * @param usernameList
     * @param mapFilterByPost Map<String, RebateRQ>
     */
    private void analyzePostNight(MainAnalyseRS mainAnalyseRS, List<String> memberCodes, List<String> usernameList, Map<String, RebateRQ> mapFilterByPost, String drawCode) {

        List<AnalyseRS> analyseRSList = new ArrayList<>();
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();

        /*
         * prepare post A, and separate A1,A2,A3,A4
         */
        List<OrderItemsDTO> orderItemsDTOAx4List = getOrderItemsDTOAx4(analyzeRQ, memberCodes, "NORMAL");
        List<OrderItemsDTO> orderItemsDTOAx4ListPrimary = new ArrayList<>(orderItemsDTOAx4List);
        if (!this.analyzeType.equals("NORMAL")) {
            orderItemsDTOAx4ListPrimary = getOrderItemsDTOAx4(analyzeRQ, memberCodes, this.analyzeType);
        }

        Map<String, List<OrderItemsDTO>> orderItemAx4GroupByRebateCode = orderItemsDTOAx4List.stream().collect(Collectors.groupingBy(OrderItemsDTO::getRebateCode));
        Map<String, List<OrderItemsDTO>> orderItemAx4GroupByRebateCodePrimary = orderItemsDTOAx4ListPrimary.stream().collect(Collectors.groupingBy(OrderItemsDTO::getRebateCode));

        String roleCode = generalUtility.getRoleCode(jwtToken.getUserToken());

        AnalyseRS analyseRSAx4 = new AnalyseRS();
        analyseRSAx4.setPostCode("Ax4");
        List<AnalyseItemsRS> analyseItemsRSAx4List = new ArrayList<>();
        for (String rebateCode : orderItemAx4GroupByRebateCodePrimary.keySet()) {
            BigDecimal totalBet = BigDecimal.ZERO;
            Map<String, BigDecimal> betAmountByNumber = new HashMap<>();
            Map<String, BigDecimal> rewardByNumber = new HashMap<>();

            List<OrderItemsDTO> mainOrderItems = orderItemAx4GroupByRebateCode.get(rebateCode);
            for (OrderItemsDTO item : mainOrderItems) {
                BigDecimal betAmount = item.getBetAmount();
                if (LotteryConstant.CURRENCY_USD.equals(item.getCurrencyCode())) {
                    betAmount = item.getBetAmount().multiply(BigDecimal.valueOf(4000));
                }
                // find total bet
                BigDecimal commissionRate = this.getCommissionRateByRole(item, roleCode);
                BigDecimal commissionAmount = generalUtility.commissionAmount(betAmount.multiply(BigDecimal.valueOf(item.getNumberQuantity())), commissionRate);
                totalBet = totalBet.add(commissionAmount);
            }
            List<OrderItemsDTO> orderItemsPrimary = orderItemAx4GroupByRebateCodePrimary.get(rebateCode);

            for (OrderItemsDTO item : orderItemsPrimary) {
                // find total bet
                calculateBetAmountAndRewardAmount(item, betAmountByNumber, rewardByNumber, roleCode);
            }
            BigDecimal finalTotalBet = totalBet;
            betAmountByNumber.forEach((num, betAmount) -> {
                AnalyseItemsRS analyseItemsRS = new AnalyseItemsRS();
                analyseItemsRS.setNumber(num);
                analyseItemsRS.setRebateCode(rebateCode);
                analyseItemsRS.setTotalSale(betAmount);
                analyseItemsRS.setBetAmount(betAmount);
                analyseItemsRS.setTotalReward(rewardByNumber.get(num));
                analyseItemsRS.setPercentage(getPercentage(rewardByNumber.get(num), finalTotalBet));
                addAnalyzeItemToList(analyseItemsRSAx4List, analyseItemsRS, mapFilterByPost.get("Ax4"), mainAnalyseRS);
            });
        }
        analyseRSAx4.setItems(analyseItemsRSAx4List);
        if (analyseRSAx4.getItems().size() > 0) {
            analyseRSList.add(analyseRSAx4);
        }


        //A1,A2,A3,A4
        List<OrderItemsDTO> orderItemsDTOListA1_A4 = getOrderItemsDTOA1_4(analyzeRQ, memberCodes, "NORMAL");

        updatePostAnalyzeRemoveBCD(orderItemsDTOListA1_A4);

        List<OrderItemsDTO> orderItemsDTOListA1_A4Primary = new ArrayList<>(orderItemsDTOListA1_A4);
        if (!this.analyzeType.equals("NORMAL")) {
            orderItemsDTOListA1_A4Primary = getOrderItemsDTOA1_4(analyzeRQ, memberCodes, this.analyzeType);
            updatePostAnalyzeRemoveBCD(orderItemsDTOListA1_A4Primary);
        }

        Map<String, List<OrderItemsDTO>> orderItemA1_A4GroupByRebateCode = orderItemsDTOListA1_A4.stream().collect(Collectors.groupingBy(OrderItemsDTO::getRebateCode));
        Map<String, List<OrderItemsDTO>> orderItemA1_A4GroupByRebateCodePrimary = orderItemsDTOListA1_A4Primary.stream().collect(Collectors.groupingBy(OrderItemsDTO::getRebateCode));

        Map<String, AnalyseRS> mapAnalyseRSByPost = new HashMap<>();
        for (String rebateCode : orderItemA1_A4GroupByRebateCodePrimary.keySet()) {
            List<OrderItemsDTO> mainOrderItems = orderItemA1_A4GroupByRebateCode.get(rebateCode);

            List<OrderItemsDTO> orderItems = orderItemA1_A4GroupByRebateCodePrimary.get(rebateCode);
            Map<String, BigDecimal> mapTotalBetByPost = new HashMap<>();
            Map<String, Map<String, BigDecimal>> mapBetAmountByPostAndByNumber = new HashMap<>();
            Map<String, Map<String, BigDecimal>> mapRewardByPostAndByNumber = new HashMap<>();

            for (OrderItemsDTO item : mainOrderItems) {
                BigDecimal betAmount = item.getBetAmount();
                if (LotteryConstant.CURRENCY_USD.equals(item.getCurrencyCode())) {
                    betAmount = item.getBetAmount().multiply(BigDecimal.valueOf(4000));
                }
                List<String> posts = List.of(item.getPostAnalyze().split(":"));
                for (String post : posts) {
                    // find total bet
                    if (mapTotalBetByPost.containsKey(post)) {
                        BigDecimal amount = mapTotalBetByPost.get(post);
                        amount = amount.add(betAmount.multiply(BigDecimal.valueOf(item.getNumberQuantity())));
                        mapTotalBetByPost.put(post, amount);
                    } else {
                        mapTotalBetByPost.put(post, betAmount.multiply(BigDecimal.valueOf(item.getNumberQuantity())));
                    }
                }
            }

            for (OrderItemsDTO item : orderItems) {
                List<String> posts = List.of(item.getPostAnalyze().split(":"));
                for (String post : posts) {
                    if (mapBetAmountByPostAndByNumber.containsKey(post)) {
                        calculateBetAmountAndRewardAmount(item, mapBetAmountByPostAndByNumber.get(post), mapRewardByPostAndByNumber.get(post), roleCode);
                    } else {
                        Map<String, BigDecimal> betAmountByNumber = new HashMap<>();
                        Map<String, BigDecimal> rewardByNumber = new HashMap<>();
                        calculateBetAmountAndRewardAmount(item, betAmountByNumber, rewardByNumber, roleCode);
                        mapBetAmountByPostAndByNumber.put(post, betAmountByNumber);
                        mapRewardByPostAndByNumber.put(post, rewardByNumber);
                    }
                }
            }

            mapTotalBetByPost.forEach((post, totalBet) -> {
                List<AnalyseItemsRS> analyseItemsRSList = new ArrayList<>();
                AnalyseRS analyseRS;
                if (mapAnalyseRSByPost.containsKey(post)) {
                    analyseRS = mapAnalyseRSByPost.get(post);
                    analyseItemsRSList = new ArrayList<>(analyseRS.getItems());
                } else {
                    analyseRS = new AnalyseRS();
                    analyseRS.setPostCode(post);
                }
                addMapAnalyseRS(mapFilterByPost, mapAnalyseRSByPost, rebateCode, mapBetAmountByPostAndByNumber, mapRewardByPostAndByNumber, post, totalBet, analyseItemsRSList, analyseRS, mainAnalyseRS);
            });
        }
        mapAnalyseRSByPost.forEach((post, analyseRS) -> analyseRSList.add(analyseRS));

        /*
         * calculate post BCD
         */
        List<AnalyzingItemHZ> analyzePosts = getAnalyzeTOData(analyzeRQ, usernameList, drawCode, "NORMAL");
        List<AnalyzingItemHZ> analyzingItemHZListRange = new ArrayList<>();
        List<AnalyzingItemHZ> analyzingItemHZListNoRange = new ArrayList<>();
        if (this.analyzeType.equals("RANGE")) {
            analyzingItemHZListRange = getAnalyzeTOData(analyzeRQ, memberCodes, drawCode, this.analyzeType);
        }
        if (this.analyzeType.equals("NO_RANGE")) {
            analyzingItemHZListNoRange = getAnalyzeTOData(analyzeRQ, memberCodes, drawCode, this.analyzeType);
        }

        analyzePost(mainAnalyseRS,
                analyseRSList,
                analyzePosts,
                analyzingItemHZListRange,
                analyzingItemHZListNoRange,
                mapFilterByPost);

        mainAnalyseRS.setItems(analyseRSList);
    }

    /**
     * @param mapFilterByPost               Map<String, RebateRQ>
     * @param mapAnalyseRSByPost            Map<String, AnalyseRS>
     * @param rebateCode                    String
     * @param mapBetAmountByPostAndByNumber Map<String, Map<String, BigDecimal>>
     * @param mapRewardByPostAndByNumber    Map<String, Map<String, BigDecimal>>
     * @param post                          String
     * @param totalBet                      BigDecimal
     * @param analyseItemsRSList            List<AnalyseItemsRS>
     * @param analyseRS                     AnalyseRS
     * @param mainAnalyseRS                 MainAnalyseRS
     */
    private void addMapAnalyseRS(Map<String, RebateRQ> mapFilterByPost, Map<String, AnalyseRS> mapAnalyseRSByPost, String rebateCode, Map<String, Map<String, BigDecimal>> mapBetAmountByPostAndByNumber, Map<String, Map<String, BigDecimal>> mapRewardByPostAndByNumber, String post, BigDecimal totalBet, List<AnalyseItemsRS> analyseItemsRSList, AnalyseRS analyseRS, MainAnalyseRS mainAnalyseRS) {
        Map<String, BigDecimal> rewardByPost = mapRewardByPostAndByNumber.get(post);
        Map<String, BigDecimal> betAmountByPost = mapBetAmountByPostAndByNumber.get(post);
        for (String num : mapBetAmountByPostAndByNumber.get(post).keySet()) {
            AnalyseItemsRS analyseItemsRS = new AnalyseItemsRS();
            analyseItemsRS.setNumber(num);
            analyseItemsRS.setRebateCode(rebateCode);
            analyseItemsRS.setTotalSale(betAmountByPost.get(num));
            analyseItemsRS.setTotalReward(rewardByPost.get(num));
            analyseItemsRS.setBetAmount(betAmountByPost.get(num));
            analyseItemsRS.setPercentage(getPercentage(rewardByPost.get(num), totalBet));
            addAnalyzeItemToList(analyseItemsRSList, analyseItemsRS, mapFilterByPost.get(post), mainAnalyseRS);
        }

        analyseItemsRSList.sort(Comparator.comparing(AnalyseItemsRS::getRebateCode));
        analyseRS.setItems(analyseItemsRSList);
        mapAnalyseRSByPost.put(post, analyseRS);
    }


    /**
     * get analyze for post with draw day
     *
     * @param mainAnalyseRS   MainAnalyseRS
     * @param usernameList    List<String>
     * @param mapFilterByPost Map<String, RebateRQ>
     * @param drawCode        String
     */
    private void analyzePostDay(MainAnalyseRS mainAnalyseRS, List<String> usernameList, Map<String, RebateRQ> mapFilterByPost, String drawCode) {
        List<AnalyseRS> analyseRSList = new ArrayList<>();
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        List<AnalyzingItemHZ> analyzePosts = getAnalyzeTOData(analyzeRQ, usernameList, drawCode, "NORMAL");

        List<AnalyzingItemHZ> analyzingItemHZListRange = new ArrayList<>();
        List<AnalyzingItemHZ> analyzingItemHZListNoRange = new ArrayList<>();
        if (this.analyzeType.equals("RANGE")) {
            analyzingItemHZListRange = getAnalyzeTOData(analyzeRQ, usernameList, drawCode, this.analyzeType);
        }
        if (this.analyzeType.equals("NO_RANGE")) {
            analyzingItemHZListNoRange = getAnalyzeTOData(analyzeRQ, usernameList, drawCode, this.analyzeType);
        }

        analyzePost(mainAnalyseRS,
                analyseRSList,
                analyzePosts,
                analyzingItemHZListRange,
                analyzingItemHZListNoRange,
                mapFilterByPost);

        mainAnalyseRS.setItems(analyseRSList);
    }

    /**
     * @param mainAnalyseRS   MainAnalyseRS
     * @param analyseRSList   List<AnalyseRS>
     * @param analyzePosts    List<AnalyzeTO>
     * @param mapFilterByPost Map<String, RebateRQ>
     */
    private void analyzePost(MainAnalyseRS mainAnalyseRS,
                             List<AnalyseRS> analyseRSList,
                             List<AnalyzingItemHZ> analyzePosts,
                             List<AnalyzingItemHZ> analyzingItemHZListRange,
                             List<AnalyzingItemHZ> analyzingItemHZListNoRange,
                             Map<String, RebateRQ> mapFilterByPost) {


        if (!analyzePosts.isEmpty()) {
            long totalBet = analyzePosts.stream().map(AnalyzingItemHZ::getBetAmount).mapToLong(BigDecimal::longValue).sum();
            mainAnalyseRS.setTotalSale(BigDecimal.valueOf(totalBet));

            /*
             * prepare post
             */
            updateAnalyzeRSList(analyseRSList,
                    analyzePosts,
                    analyzingItemHZListRange,
                    analyzingItemHZListNoRange,
                    mapFilterByPost,
                    mainAnalyseRS);
        }
    }

    /**
     * @param mainAnalyseRS   MainAnalyseRS
     * @param memberCodes     List<String>
     * @param mapFilterByPost Map<String, RebateRQ>
     */
    private void analyzeLo(MainAnalyseRS mainAnalyseRS, List<String> memberCodes, Map<String, RebateRQ> mapFilterByPost) {
        List<AnalyseRS> analyseRSList = new ArrayList<>();
        AnalyzeRQ analyzeRQ = mainAnalyseRS.getFilter();
        List<OrderItemsDTO> orderItemsDTOListMain = getOrderItemsDTOLoData(analyzeRQ, memberCodes, "NORMAL");
        List<OrderItemsDTO> orderItemsDTOListPrimary = new ArrayList<>(orderItemsDTOListMain);
        if (!this.analyzeType.equals("NORMAL")) {
            orderItemsDTOListPrimary = getOrderItemsDTOLoData(analyzeRQ, memberCodes, this.analyzeType);
        }

        String roleCode = generalUtility.getRoleCode(jwtToken.getUserToken());
        Map<String, List<OrderItemsDTO>> orderItemGroupByPostMain = orderItemsDTOListMain.stream().collect(Collectors.groupingBy(OrderItemsDTO::getPosts));
        Map<String, List<OrderItemsDTO>> orderItemGroupByPostPrimary = orderItemsDTOListPrimary.stream().collect(Collectors.groupingBy(OrderItemsDTO::getPosts));
        orderItemGroupByPostPrimary.forEach((post, list) -> {
            AnalyseRS analyseRS = new AnalyseRS();
            String postLo = "Lo".concat(post.replace("ABCD", ""));
            List<AnalyseItemsRS> analyseItemsRSList = new ArrayList<>();

            List<OrderItemsDTO> mainPostOrderItems = orderItemGroupByPostMain.get(post);

            Map<String, List<OrderItemsDTO>> orderItemGroupByRebateCodeMain = mainPostOrderItems.stream().collect(Collectors.groupingBy(OrderItemsDTO::getRebateCode));
            Map<String, List<OrderItemsDTO>> orderItemGroupByRebateCode = list.stream().collect(Collectors.groupingBy(OrderItemsDTO::getRebateCode));

            orderItemGroupByRebateCode.forEach((rebateCode, orderItems) -> {
                BigDecimal totalBet = getTurnOverFromLoOrderItems(orderItemGroupByRebateCodeMain.get(rebateCode));

                Map<String, BigDecimal> mapBetAmountByNumber = new HashMap<>();
                Map<String, BigDecimal> mapRewardByNumber = new HashMap<>();
                orderItems.forEach(it -> calculateBetAmountAndRewardAmount(it, mapBetAmountByNumber, mapRewardByNumber, roleCode));
                for (String number : mapBetAmountByNumber.keySet()) {
                    AnalyseItemsRS analyseItemsRS = new AnalyseItemsRS();
                    BigDecimal betAmount = mapBetAmountByNumber.get(number);
                    BigDecimal rewardAmount = mapRewardByNumber.get(number);
                    analyseItemsRS.setNumber(number);
                    analyseItemsRS.setTotalSale(betAmount);
                    analyseItemsRS.setTotalReward(rewardAmount);
                    analyseItemsRS.setBetAmount(betAmount);
                    analyseItemsRS.setRebateCode(rebateCode);
                    analyseItemsRS.setPercentage(getPercentage(rewardAmount, totalBet));
                    addAnalyzeItemToList(analyseItemsRSList, analyseItemsRS, mapFilterByPost.get(postLo), mainAnalyseRS);
                }
                analyseRS.setTotalSale(analyseRS.getTotalSale().add(totalBet));
            });
            analyseRS.setPostCode(postLo);
            analyseItemsRSList.sort(Comparator.comparing(AnalyseItemsRS::getRebateCode));
            analyseRS.setItems(analyseItemsRSList);
            analyseRSList.add(analyseRS);
            mainAnalyseRS.setTotalSale(mainAnalyseRS.getTotalSale().add(analyseRS.getTotalSale()));
        });
        mainAnalyseRS.setItems(analyseRSList);
    }

    /**
     * get turnover by calculate commission
     *
     * @param items List<VNOneTempOrderItemsEntity>
     * @return BigDecimal
     */
    private BigDecimal getTurnOverFromLoOrderItems(List<OrderItemsDTO> items) {
        BigDecimal turnover = BigDecimal.ZERO;
        for (OrderItemsDTO it : items) {
            BigDecimal totalAmount = it.getBetAmount().multiply(BigDecimal.valueOf(it.getNumberQuantity()));
            if (LotteryConstant.CURRENCY_USD.equals(it.getCurrencyCode())) {
                totalAmount = totalAmount.multiply(BigDecimal.valueOf(4000));
            }
            turnover = turnover.add(generalUtility.commissionAmount(totalAmount, it.getCommission()));
        }

        return turnover;
    }

    /**
     * calculate total amount of bet and reward
     *
     * @param item                 OrderItemsDTO
     * @param mapBetAmountByNumber Map<String, BigDecimal>
     * @param mapRewardByNumber    Map<String, BigDecimal>
     * @param roleCode
     */
    private void calculateBetAmountAndRewardAmount(OrderItemsDTO item, Map<String, BigDecimal> mapBetAmountByNumber, Map<String, BigDecimal> mapRewardByNumber, String roleCode) {
        List<String> numbers = List.of(item.getNumberDetail().split(","));
        BigDecimal betAmount = item.getBetAmount();
        if (LotteryConstant.CURRENCY_USD.equals(item.getCurrencyCode())) {
            betAmount = betAmount.multiply(BigDecimal.valueOf(4000));
        }
        for (String num : numbers) {
            updateMapNumberValue(mapBetAmountByNumber, num, betAmount, BigDecimal.valueOf(1));
            if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(roleCode)) {
                updateMapNumberValue(mapRewardByNumber, num, betAmount, item.getSeniorRebateRate());
            } else if (UserConstant.SENIOR.equalsIgnoreCase(roleCode)) {
                updateMapNumberValue(mapRewardByNumber, num, betAmount, item.getMasterRebateRate());
            } else {
                updateMapNumberValue(mapRewardByNumber, num, betAmount, item.getSuperSeniorRebateRate());
            }
        }
    }

    /**
     * update map bet amount, and reward amount
     *
     * @param mapValue   Map<String, BigDecimal>
     * @param num        String
     * @param value      BigDecimal
     * @param rebateRate BigDecimal
     */
    private void updateMapNumberValue(Map<String, BigDecimal> mapValue, String num, BigDecimal value, BigDecimal rebateRate) {
        BigDecimal plusValue = value.multiply(rebateRate);
        if (mapValue.containsKey(num)) {
            BigDecimal amount = mapValue.get(num);
            amount = amount.add(plusValue);
            mapValue.put(num, amount);
        } else {
            mapValue.put(num, plusValue);
        }
    }

    /**
     * update analyze rs list
     *
     * @param analyseRSList   List<AnalyseRS>
     * @param analyzePosts    List<AnalyzeDTO>
     * @param mapFilterByPost Map<String, RebateRQ>
     * @param mainAnalyseRS   MainAnalyseRS
     */
    private void updateAnalyzeRSList(List<AnalyseRS> analyseRSList,
                                     List<AnalyzingItemHZ> analyzePosts,
                                     List<AnalyzingItemHZ> analyzingItemHZListRange,
                                     List<AnalyzingItemHZ> analyzingItemHZListNoRange,
                                     Map<String, RebateRQ> mapFilterByPost,
                                     MainAnalyseRS mainAnalyseRS) {
        Map<String, List<AnalyzingItemHZ>> mapAnalyzeGroupByPost = analyzePosts.stream().collect(Collectors.groupingBy(AnalyzingItemHZ::getPostCode));
        Map<String, List<AnalyzingItemHZ>> mapAnalyzeGroupByPostRange = analyzingItemHZListRange.stream().collect(Collectors.groupingBy(AnalyzingItemHZ::getPostCode));
        Map<String, List<AnalyzingItemHZ>> mapAnalyzeGroupByPostNoRange = analyzingItemHZListNoRange.stream().collect(Collectors.groupingBy(AnalyzingItemHZ::getPostCode));

        Map<String, List<AnalyzingItemHZ>> primaryMap = new HashMap<>(mapAnalyzeGroupByPost);
        if (this.analyzeType.equals("RANGE")) {
            primaryMap = new HashMap<>(mapAnalyzeGroupByPostRange);
        }
        if (this.analyzeType.equals("NO_RANGE")) {
            primaryMap = new HashMap<>(mapAnalyzeGroupByPostNoRange);
        }

        String roleCode = generalUtility.getRoleCode(jwtToken.getUserToken());

        primaryMap.forEach((postCode, analyzes) -> {
            List<AnalyzingItemHZ> mainPostAnalyzingList = mapAnalyzeGroupByPost.get(postCode);
            String post = postCode;
            if (mapFilterByPost.containsKey("A1") && post.equals(PostConstant.POST_A))
                post = "A1";
            AnalyseRS analyseRS = new AnalyseRS();
            calculatePercentageOfEachNumber(analyseRS,
                    analyzes,
                    mainPostAnalyzingList,
                    mapFilterByPost.get(post),
                    mainAnalyseRS, roleCode);
            analyseRS.setPostCode(post);
            analyseRSList.add(analyseRS);
        });
    }

    /**
     * calculate percentage of each number
     *
     * @param analyseRS             AnalyseRS
     * @param analyzes              List<AnalyzingItemHZ>
     * @param mainPostAnalyzingList List<AnalyzingItemHZ>
     * @param filterRebateRQ        RebateRQ
     * @param mainAnalyseRS         MainAnalyseRS
     * @param roleCode
     */
    private void calculatePercentageOfEachNumber(AnalyseRS analyseRS, List<AnalyzingItemHZ> analyzes, List<AnalyzingItemHZ> mainPostAnalyzingList, RebateRQ filterRebateRQ, MainAnalyseRS mainAnalyseRS, String roleCode) {
        List<AnalyseItemsRS> analyseItemsRSList = new ArrayList<>();
        long totalBet1D = mainPostAnalyzingList.stream().filter(it -> LotteryConstant.REBATE1D.equals(it.getRebateCode()))
                .map(it -> this.getCommissionByRole(it, roleCode)).mapToLong(BigDecimal::longValue).sum();
        long totalBet2D = mainPostAnalyzingList.stream().filter(it -> LotteryConstant.REBATE2D.equals(it.getRebateCode()))
                .map(AnalyzingItemHZ::getCommissionAmount).mapToLong(BigDecimal::longValue).sum();
        long totalBet3D = mainPostAnalyzingList.stream().filter(it -> LotteryConstant.REBATE3D.equals(it.getRebateCode()))
                .map(it -> this.getCommissionByRole(it, roleCode)).mapToLong(BigDecimal::longValue).sum();
        long totalBet4D = mainPostAnalyzingList.stream().filter(it -> LotteryConstant.REBATE4D.equals(it.getRebateCode()))
                .map(it -> this.getCommissionByRole(it, roleCode)).mapToLong(BigDecimal::longValue).sum();
        analyzes.forEach(item -> {
            AnalyseItemsRS analyseItemsRS = new AnalyseItemsRS();
            BeanUtils.copyProperties(item, analyseItemsRS);
            long totalBet = 0;
            switch (item.getRebateCode()) {
                case LotteryConstant.REBATE1D:
                    totalBet = totalBet1D;
                    break;
                case LotteryConstant.REBATE2D:
                    totalBet = totalBet2D;
                    break;
                case LotteryConstant.REBATE3D:
                    totalBet = totalBet3D;
                    break;
                case LotteryConstant.REBATE4D:
                    totalBet = totalBet4D;
                    break;
            }
            analyseItemsRS.setTotalSale(item.getBetAmount());
            analyseItemsRS.setTotalReward(this.getPrizeAmountByRole(item, roleCode));
            analyseItemsRS.setBetAmount(item.getBetAmount());
            analyseItemsRS.setPercentage(getPercentage(analyseItemsRS.getTotalReward(), BigDecimal.valueOf(totalBet)));
            addAnalyzeItemToList(analyseItemsRSList, analyseItemsRS, filterRebateRQ, mainAnalyseRS);
            analyseRS.setTotalSale(analyseRS.getTotalSale().add(item.getBetAmount()));
        });
        analyseRS.setItems(analyseItemsRSList);
    }

    /**
     * get percentage to filter
     *
     * @param filterRebateRQ RebateRQ
     * @param rebateCode     String
     * @return BigDecimal
     */
    private BigDecimal getFilterValue(RebateRQ filterRebateRQ, String rebateCode) {
        BigDecimal filterValue = filterRebateRQ.getTwoD();
        if (LotteryConstant.REBATE1D.equalsIgnoreCase(rebateCode)) {
            filterValue = filterRebateRQ.getOneD();
        }
        if (LotteryConstant.REBATE3D.equalsIgnoreCase(rebateCode)) {
            filterValue = filterRebateRQ.getThreeD();
        }
        if (LotteryConstant.REBATE4D.equalsIgnoreCase(rebateCode)) {
            filterValue = filterRebateRQ.getFourD();
        }

        return filterValue;
    }

    /**
     * get percentage value
     *
     * @param amount    BigDecimal
     * @param dividedBy BigDecimal
     * @return BigDecimal
     */
    private BigDecimal getPercentage(BigDecimal amount, BigDecimal dividedBy) {
        if (dividedBy.compareTo(BigDecimal.ZERO) > 0) {
            double result = (amount.doubleValue() / dividedBy.doubleValue()) * 100;
            return generalUtility.setScaleAndHALFEVEN(BigDecimal.valueOf(result), 4);
        }
        return BigDecimal.ZERO;
    }

    /**
     * add item to analyze items list that filter accepted
     *
     * @param analyseItemsRSList List<AnalyseItemsRS>
     * @param analyseItemsRS     AnalyseItemsRS
     * @param filterRebateRQ     RebateRQ
     * @param mainAnalyseRS      MainAnalyseRS
     */
    private void addAnalyzeItemToList(List<AnalyseItemsRS> analyseItemsRSList, AnalyseItemsRS analyseItemsRS, RebateRQ filterRebateRQ, MainAnalyseRS mainAnalyseRS) {
        try {
            if (filterRebateRQ != null) {
                BigDecimal filterValue = getFilterValue(filterRebateRQ, analyseItemsRS.getRebateCode());
                AnalyzeRQ filter = mainAnalyseRS.getFilter();
                if (
                        (!LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(filter.getTransferType()) && analyseItemsRS.getPercentage().compareTo(filterValue) >= 0) ||
                                (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(filter.getTransferType()) && analyseItemsRS.getTotalSale().compareTo(filterValue) >= 0)
                ) {
                    RebateRQ rebateTransfer = mainAnalyseRS.getFilter().getRebateTransfer();
                    /*
                     * find transfer percentage that over filter than find amount with that percentage of total bet
                     */
                    BigDecimal transferPercentage = analyseItemsRS.getPercentage().subtract(filterValue);
                    BigDecimal rewardTransfer = transferPercentage.multiply(analyseItemsRS.getTotalReward()).divide(analyseItemsRS.getPercentage(), 2, RoundingMode.HALF_EVEN);

                    BigDecimal rebateRate = rebateTransfer.getTwoD();

                    if (LotteryConstant.REBATE1D.equals(analyseItemsRS.getRebateCode())) {
                        rebateRate = rebateTransfer.getOneD();
                    }
                    if (LotteryConstant.REBATE3D.equals(analyseItemsRS.getRebateCode())) {
                        rebateRate = rebateTransfer.getThreeD();
                    }
                    if (LotteryConstant.REBATE4D.equals(analyseItemsRS.getRebateCode())) {
                        rebateRate = rebateTransfer.getFourD();
                    }

                    BigDecimal transferAmount = rewardTransfer.divide(rebateRate, 2, RoundingMode.HALF_EVEN);
                    analyseItemsRS.setBetAmount(transferAmount.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100)));
                    analyseItemsRSList.add(analyseItemsRS);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * update post analyze by remove BCD
     *
     * @param orderItems List<VNOneTempOrderItemsEntity>
     */
    private void updatePostAnalyzeRemoveBCD(List<OrderItemsDTO> orderItems) {
        for (OrderItemsDTO item : orderItems) {
            int removePost = 0;
            if (item.getPosts().contains(PostConstant.POST_B)) {
                removePost += 1;
                item.setPostAnalyze(item.getPostAnalyze().replace("B", ""));
            }
            if (item.getPosts().contains(PostConstant.POST_C)) {
                removePost += 1;
                item.setPostAnalyze(item.getPostAnalyze().replace("C", ""));
            }
            if (item.getPosts().contains(PostConstant.POST_D)) {
                removePost += 1;
                item.setPostAnalyze(item.getPostAnalyze().replace("D", ""));
            }
            if (removePost > 0) {
                List<String> posts = new ArrayList<>(List.of(item.getPostAnalyze().split(":")));
                posts.removeAll(Collections.singleton(""));
                item.setPostAnalyze(String.join(":", posts));
            }
        }
    }


    private BigDecimal getCommissionByRole(AnalyzingItemHZ it, String roleCode) {
        switch (roleCode.toUpperCase()) {
            case UserConstant.SUPER_SENIOR:
                return it.getCommissionAmount();
            case UserConstant.SENIOR:
                return it.getMasterCommissionAmount();
        }
        return it.getSuperCommissionAmount();
    }

    private BigDecimal getPrizeAmountByRole(AnalyzingItemHZ it, String roleCode) {
        switch (roleCode.toUpperCase()) {
            case UserConstant.SUPER_SENIOR:
                return it.getPrizeAmount();
            case UserConstant.SENIOR:
                return it.getMasterPrizeAmount();
        }
        return it.getSuperPrizeAmount();
    }

    private BigDecimal getCommissionRateByRole(OrderItemsDTO item, String roleCode) {
        if (UserConstant.SUPER_SENIOR.equalsIgnoreCase(roleCode))
            return item.getSeniorCommission();
        else if (UserConstant.SENIOR.equalsIgnoreCase(roleCode))
            return item.getMasterCommission();
        return item.getSuperSeniorCommission();
    }

}
