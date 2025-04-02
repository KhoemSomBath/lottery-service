package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasPostponeNumberEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserHasPostponeNumberRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.postponenumber.UserHasPostponeRS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 14/02/2022
 * time: 10:53
 */
@RequiredArgsConstructor
@Component
public class UserHasPostponeUtility {

    private final UserHasPostponeNumberRP userHasPostponeNumberRP;

    /**
     * update upper postpone in list userHasPostpone
     * @param userHasPostponeRSList List<UserHasPostponeRS>
     * @param userEntity UserEntity
     * @param drawingDTOList List<DrawingDTO>
     * @param lotteryType String
     */
    public void updateUpperPostponeNumber(List<UserHasPostponeRS> userHasPostponeRSList, UserEntity userEntity, List<DrawingDTO> drawingDTOList, String lotteryType) {
        List<String> drawCodes = drawingDTOList.stream().map(DrawingDTO::getDrawCode).collect(Collectors.toList());
        List<String> upperCodes = getUpperCodes(userEntity);
        Map<String, UserHasPostponeRS> mapUserHasPostponeByDrawCode = userHasPostponeRSList.stream().collect(Collectors.toMap(UserHasPostponeRS::getDrawCode, Function.identity()));
        Map<String, DrawingDTO> mapDrawingDTOByDrawCode = drawingDTOList.stream().collect(Collectors.toMap(DrawingDTO::getDrawCode, Function.identity()));

        List<UserHasPostponeNumberEntity> userHasPostponeNumberEntities = userHasPostponeNumberRP.findAllByLotteryTypeAndDrawCodeInAndUserCodeIn(lotteryType, drawCodes, upperCodes);
        Map<String, List<UserHasPostponeNumberEntity>> groupByUserHasPostponeByDraw = new HashMap<>();
        if (!userHasPostponeNumberEntities.isEmpty()) {
            groupByUserHasPostponeByDraw = userHasPostponeNumberEntities.stream().collect(Collectors.groupingBy(UserHasPostponeNumberEntity::getDrawCode, Collectors.toList()));
        }

        for (String drawCode : mapDrawingDTOByDrawCode.keySet()) {
            List<String> upperPostponeNumbers = new ArrayList<>();

            /*
             * get postpone from draw (company)
             */
            getNumberDetail(upperPostponeNumbers, mapDrawingDTOByDrawCode.get(drawCode).getPostponeNumber());


            /*
             * get postpone from upper level
             */
            if (groupByUserHasPostponeByDraw.containsKey(drawCode)) {
                for (UserHasPostponeNumberEntity item : groupByUserHasPostponeByDraw.get(drawCode)) {
                    upperPostponeNumbers.add(item.getNumber());
                }
            }

            List<String> fullPostponeNumbers = new ArrayList<>(upperPostponeNumbers);

            /*
             * get postpone from current level
             */
            if (mapUserHasPostponeByDrawCode.containsKey(drawCode)) {

                UserHasPostponeRS currentPostpone = mapUserHasPostponeByDrawCode.get(drawCode);
                currentPostpone.setNumberDetail(getListNumberDetail(currentPostpone.getNumberDetail()));
                currentPostpone.setDrawAt(mapDrawingDTOByDrawCode.get(drawCode).getResultedPostAt());

                if (!upperPostponeNumbers.isEmpty()) {
                    upperPostponeNumbers = upperPostponeNumbers.stream().distinct().sorted().collect(Collectors.toList());
                    upperPostponeNumbers.removeAll(List.of(""));
                    Collections.sort(upperPostponeNumbers);
                    currentPostpone.setUpperPostpone(String.join(",", upperPostponeNumbers));
                }

                /*
                 * Set full postpone to UserHasPostponeRS
                 */
                getNumberDetail(fullPostponeNumbers, currentPostpone.getNumberDetail());
                fullPostponeNumbers.removeAll(List.of(""));
                Collections.sort(fullPostponeNumbers);
                currentPostpone.setFullPostpone(String.join(",", fullPostponeNumbers));

            }
        }
    }

    /**
     * add postpone to list from number detail
     * @param postponeNumbers List<String>
     * @param numberDetail String
     */
    private void getNumberDetail(List<String> postponeNumbers, String numberDetail) {
        if (numberDetail != null) {
            postponeNumbers.addAll(List.of(numberDetail.split(",")));
        }
    }

    /**
     * Get upper code from super-senior to agent
     * @param userEntity UserEntity
     * @return List<String>
     */
    private List<String> getUpperCodes(UserEntity userEntity) {
        List<String> upperCodes = new ArrayList<>();
        if (userEntity.getSuperSeniorCode() != null)
            upperCodes.add(userEntity.getSuperSeniorCode());
        if (userEntity.getSeniorCode() != null)
            upperCodes.add(userEntity.getSeniorCode());
        if (userEntity.getMasterCode() != null)
            upperCodes.add(userEntity.getMasterCode());
        if (userEntity.getAgentCode() != null)
            upperCodes.add(userEntity.getAgentCode());

        return upperCodes;
    }

    public String getListNumberDetail(String numberDetail) {
        String postpone = "";
        if (numberDetail.length() > 0) {
            List<String> numbers = new ArrayList<>(List.of(numberDetail.split(",")));
            numbers.removeAll(List.of(""));
            Collections.sort(numbers);
            postpone = String.join(",", numbers);
        }

        return postpone;
    }

    public List<String> getListNumbers(String numberDetail) {
        if (numberDetail.length() > 0) {
            List<String> numbers = new ArrayList<>(List.of(numberDetail.split(",")));
            numbers.removeAll(List.of(""));
            Collections.sort(numbers);
            return numbers;
        }
        return new ArrayList<>();
    }

}
