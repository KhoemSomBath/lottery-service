package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.HazelcastConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*
 * author: kangto
 * createdAt: 25/10/2022
 * time: 13:32
 */
@Component
@RequiredArgsConstructor
public class PublishResultUtility {

    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final TNTempDrawingRP tnTempDrawingRP;
    private final LeapTempDrawingRP leapTempDrawingRP;
    private final HazelcastInstance hazelcastInstance;
    private final GeneralUtility generalUtility;

    public void resetWinBalanceUserOnline(String lotteryType, DrawingDTO drawingDTO) {
        String drawCode = drawingDTO.getDrawCode();
        if (drawingDTO.getIsSetWin()) {
            switch (lotteryType) {
                case LotteryConstant.LEAP:
                    leapTempDrawingRP.updateBalanceMemberOnlineByDrawCode(drawCode);
                    break;
                case LotteryConstant.VN1:
                    vnOneTempDrawingRP.updateBalanceMemberOnlineByDrawCode(drawCode);
                    break;
                case LotteryConstant.VN2:
                    vnTwoTempDrawingRP.updateBalanceMemberOnlineByDrawCode(drawCode);
                    break;
                case LotteryConstant.TN:
                    tnTempDrawingRP.updateBalanceMemberOnlineByDrawCode(drawCode);
                    break;
            }
        }
    }

    public void removeDrawDTOCache(String lottery, DrawingDTO drawingDTO) {
        try {
            String key = lottery.concat("_").concat(generalUtility.formatDateYYYYMMDD(drawingDTO.getResultedPostAt()));
            hazelcastInstance.getMap("draws").delete(key);

            hazelcastInstance.getMap(HazelcastConstant.DRAW_ITEM_COLLECTION).delete(lottery.concat("_").concat(drawingDTO.getId().toString()));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
