package com.hacknovation.systemservice.v1_0_0.service.betting;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

@Service
public interface BettingSV {
    StructureRS listing();
    StructureRS updateIsSeen(String lottery, Long orderId);
    StructureRS updateIsMark(String lottery, Long orderId);

    StructureRS monitorTicket(String lottery, String drawCode, Integer size, Integer isMark, Integer page);
}
