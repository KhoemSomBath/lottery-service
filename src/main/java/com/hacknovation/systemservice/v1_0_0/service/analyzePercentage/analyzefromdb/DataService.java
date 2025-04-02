package com.hacknovation.systemservice.v1_0_0.service.analyzePercentage.analyzefromdb;

import com.hacknovation.systemservice.constant.BettingConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempOrderItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempOrderItemsRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.analyzenumber.AnalyzeItemsDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.RebateRQ;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Sombath
 * create at 7/3/23 10:58 PM
 */

@Service
@RequiredArgsConstructor
class DataService {

    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final VNOneTempOrderItemsRP vnOneTempOrderItemsRP;

    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final VNTwoTempOrderItemsRP vnTwoTempOrderItemsRP;

    private final LeapTempDrawingRP leapTempDrawingRP;
    private final LeapTempOrderItemsRP leapTempOrderItemsRP;

    private final SCTempDrawingRP scTempDrawingRP;
    private final SCTempOrderItemRP scTempOrderItemRP;

    private final TNTempDrawingRP tnTempDrawingRP;
    private final TNTempOrderItemsRP tnTempOrderItemsRP;

    private final KHTempDrawingRP khTempDrawingRP;
    private final KHTempOrderItemsRP khTempOrderItemsRP;
    private final BigDecimal USD_TO_KHR_RATE = BigDecimal.valueOf(4000);

    public List<AnalyzeItemsDTO> getData(AnalyzeRQ analyzeRQ, List<String> memberCodes, DrawingDTO drawingDTO) {

        switch (analyzeRQ.getLotteryType()) {

            case LotteryConstant.VN1:

                VNOneTempDrawingEntity vnOneTempDrawing = vnOneTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                BeanUtils.copyProperties(vnOneTempDrawing, drawingDTO);

                List<VNOneTempOrderItemsEntity> vnOneTempOrderItemsEntities;

                if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType()) || LotteryConstant.POST.equals(analyzeRQ.getPostGroup()))
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            vnOneTempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        case BettingConstant.NO_RANGE:
                            vnOneTempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        default:
                            vnOneTempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeIn(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                    }
                else
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            vnOneTempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        case BettingConstant.NO_RANGE:
                            vnOneTempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        default:
                            vnOneTempOrderItemsEntities = vnOneTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                    }

                return vnOneTempOrderItemsEntities.stream().map(AnalyzeItemsDTO::new).collect(Collectors.toList());

            case LotteryConstant.VN2:

                VNTwoTempDrawingEntity vnTwoTempDrawingEntity = vnTwoTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                BeanUtils.copyProperties(vnTwoTempDrawingEntity, drawingDTO);

                List<VNTwoTempOrderItemsEntity> vnTwoTempOrderItemsEntities;

                if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType()) || LotteryConstant.POST.equals(analyzeRQ.getPostGroup()))
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        case BettingConstant.NO_RANGE:
                            vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        default:
                            vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeIn(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                    }
                else
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        case BettingConstant.NO_RANGE:
                            vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        default:
                            vnTwoTempOrderItemsEntities = vnTwoTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                    }

                return vnTwoTempOrderItemsEntities.stream().map(AnalyzeItemsDTO::new).collect(Collectors.toList());

            case LotteryConstant.LEAP:

                LeapTempDrawingEntity leapTempDrawingEntity = leapTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                BeanUtils.copyProperties(leapTempDrawingEntity, drawingDTO);

                List<LeapTempOrderItemsEntity> leapTempOrderItemsEntities;

                if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType()) || LotteryConstant.POST.equals(analyzeRQ.getPostGroup()))
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        case BettingConstant.NO_RANGE:
                            leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        default:
                            leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeIn(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                    }
                else
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        case BettingConstant.NO_RANGE:
                            leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        default:
                            leapTempOrderItemsEntities = leapTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                    }

                return leapTempOrderItemsEntities.stream().map(AnalyzeItemsDTO::new).collect(Collectors.toList());

            case LotteryConstant.SC:

                SCTempDrawingEntity scTempDrawingEntity = scTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                BeanUtils.copyProperties(scTempDrawingEntity, drawingDTO);

                List<SCTempOrderItemsEntity> scTempOrderItemsEntities;

                switch (analyzeRQ.getAnalyzeType()) {
                    case BettingConstant.RANGE:
                        scTempOrderItemsEntities = scTempOrderItemRP.findAllByDrawCodeAndMemberCodeInAndRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    case BettingConstant.NO_RANGE:
                        scTempOrderItemsEntities = scTempOrderItemRP.findAllByDrawCodeAndMemberCodeInAndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                    default:
                        scTempOrderItemsEntities = scTempOrderItemRP.findAllByDrawCodeAndMemberCodeIn(analyzeRQ.getDrawCode(), memberCodes);
                        break;
                }

                return scTempOrderItemsEntities.stream().map(AnalyzeItemsDTO::new).collect(Collectors.toList());

            case LotteryConstant.KH:

                KHTempDrawingEntity khTempDrawingEntity = khTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                BeanUtils.copyProperties(khTempDrawingEntity, drawingDTO);

                List<KHTempOrderItemsEntity> khTempOrderItemsEntities;

                if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType()) || LotteryConstant.POST.equals(analyzeRQ.getPostGroup()))
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        case BettingConstant.NO_RANGE:
                            khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        default:
                            khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeIn(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                    }
                else
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        case BettingConstant.NO_RANGE:
                            khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        default:
                            khTempOrderItemsEntities = khTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                    }

                return khTempOrderItemsEntities.stream().map(AnalyzeItemsDTO::new).collect(Collectors.toList());

            default:

                TNTempDrawingEntity tnTempDrawingEntity = tnTempDrawingRP.findByCode(analyzeRQ.getDrawCode());
                BeanUtils.copyProperties(tnTempDrawingEntity, drawingDTO);

                List<TNTempOrderItemsEntity> tnTempOrderItemsEntities;
                if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType()) || LotteryConstant.POST.equals(analyzeRQ.getPostGroup()))
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        case BettingConstant.NO_RANGE:
                            tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndNoRange(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                        default:
                            tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeIn(analyzeRQ.getDrawCode(), memberCodes);
                            break;
                    }
                else
                    switch (analyzeRQ.getAnalyzeType()) {
                        case BettingConstant.RANGE:
                            tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        case BettingConstant.NO_RANGE:
                            tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                        default:
                            tnTempOrderItemsEntities = tnTempOrderItemsRP.findAllByDrawCodeAndMemberCodeInAndIsLo(analyzeRQ.getDrawCode(), memberCodes, true);
                            break;
                    }

                return tnTempOrderItemsEntities.stream().map(AnalyzeItemsDTO::new).collect(Collectors.toList());
        }

    }

    public Map<String, List<AnalyzeItemsDTO>> groupOrderItemByPost(List<AnalyzeItemsDTO> orderItems, List<RebateRQ> rebates) {

        Map<String, List<AnalyzeItemsDTO>> orderItemByPost = new HashMap<>();

        for (RebateRQ rebate : rebates) {

            String post = rebate.getPostCode();

            List<AnalyzeItemsDTO> _items = new ArrayList<>();

            for (AnalyzeItemsDTO item : orderItems) {

                if (LotteryConstant.CURRENCY_USD.equalsIgnoreCase(item.getCurrencyCode())) {
                    item.setBetAmount(item.getBetAmount().multiply(USD_TO_KHR_RATE));
                    item.setTotalAmount(item.getTotalAmount().multiply(USD_TO_KHR_RATE));
                    item.setCurrencyCode(LotteryConstant.CURRENCY_KHR);
                }

                if (orderItemByPost.containsKey(post))
                    _items = orderItemByPost.get(post);

                String[] posts = item.getPostAnalyze().split(":");

                for (String _post : posts) {

                    switch (rebate.getPostCode()) {

                        case BettingConstant.POST_Ax4:
                            if (item.getPostAnalyze().contains("A:") && _post.equals(BettingConstant.POST_A)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case BettingConstant.POST_A1:
                        case BettingConstant.POST_A2:
                        case BettingConstant.POST_A3:
                        case BettingConstant.POST_A4:
                            if (item.getPosts().contains(post) && !_post.equals(BettingConstant.POST_A)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoF":
                            if (item.getIsLo() && _post.equals(PostConstant.POST_F)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoI":
                            if (item.getIsLo() && _post.equals(PostConstant.POST_I)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoN":
                            if (item.getIsLo() && _post.equals(PostConstant.POST_N)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoK":
                            if (item.getIsLo() && _post.equals(PostConstant.POST_K)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoFI":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_F) && item.getPosts().contains(PostConstant.POST_I)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoFN":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_F) && item.getPosts().contains(PostConstant.POST_N)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoFK":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_F) && item.getPosts().contains(PostConstant.POST_K)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoIN":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_I) && item.getPosts().contains(PostConstant.POST_N)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoIK":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_I) && item.getPosts().contains(PostConstant.POST_K)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoNK":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_N) && item.getPosts().contains(PostConstant.POST_K)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoFIN":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_F) && item.getPosts().contains(PostConstant.POST_I) && item.getPosts().contains(PostConstant.POST_N)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoFIK":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_F) && item.getPosts().contains(PostConstant.POST_I) && item.getPosts().contains(PostConstant.POST_K)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoINK":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_K) && item.getPosts().contains(PostConstant.POST_I) && item.getPosts().contains(PostConstant.POST_N)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case "LoFINK":
                            if (item.getIsLo() && item.getPosts().contains(PostConstant.POST_F) && item.getPosts().contains(PostConstant.POST_I) && item.getPosts().contains(PostConstant.POST_N) && item.getPosts().contains(PostConstant.POST_K)) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case BettingConstant.POST_A:
                        case BettingConstant.POST_B:
                        case BettingConstant.POST_C:
                        case BettingConstant.POST_D:
                        case BettingConstant.POST_E:
                        case BettingConstant.POST_F:
                        case BettingConstant.POST_I:
                        case BettingConstant.POST_N:
                        case BettingConstant.POST_K:
                        case BettingConstant.POST_P:
                        case BettingConstant.POST_O:
                        case BettingConstant.POST_Z:
                            if ((item.getPosts().contains(post) && _post.equals(post))) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                        case PostConstant.LO_GROUP:
                            if (item.getIsLo()) {
                                List<AnalyzeItemsDTO> _sub_items = _items;
                                OptionalInt indexOpt = IntStream.range(0, _sub_items.size())
                                        .filter(i -> item.getId().equals(_sub_items.get(i).getId()))
                                        .findFirst();
                                if (indexOpt.isEmpty())
                                    _items.add(item);
                            }
                            break;

                    }

                }

                orderItemByPost.put(post, _items);

            }
        }

        return orderItemByPost;
    }

    public Map<String, Map<String, BigDecimal>> totalBetPerPostPerRebate(Map<String, List<AnalyzeItemsDTO>> orderItems, String role) {

        Map<String, Map<String, BigDecimal>> totalBetPerPostPerRebate = new HashMap<>();

        for (String post : orderItems.keySet()) {

            Map<String, BigDecimal> totalPerRebate = new HashMap<>();

            for (AnalyzeItemsDTO item : orderItems.get(post)) {

                if(totalPerRebate.containsKey(item.getRebateCode()))
                    totalPerRebate.put(item.getRebateCode(), totalPerRebate.get(item.getRebateCode()).add(item.getTotalAmount().multiply(this.getCommissionByRole(item, role).divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY))));
                else
                    totalPerRebate.put(item.getRebateCode(), item.getTotalAmount().multiply(this.getCommissionByRole(item, role).divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY)));

            }

            totalBetPerPostPerRebate.put(post, totalPerRebate);
        }


        return totalBetPerPostPerRebate;
    }

    private BigDecimal getCommissionByRole(AnalyzeItemsDTO it, String roleCode) {
        switch (roleCode.toUpperCase()) {
            case UserConstant.SUPER_SENIOR:
                return it.getSeniorCommission();
            case UserConstant.SENIOR:
                return it.getMasterCommission();
        }
        return it.getSuperSeniorCommission();
    }
}
