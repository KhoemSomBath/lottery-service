package com.hacknovation.systemservice.v1_0_0.service.page;

import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.page.PagingNQ;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.PagingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.page.PagingListRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PagingIP extends BaseServiceIP implements PagingSV {

    private final PagingNQ pagingNQ;
    private final GeneralUtility generalUtility;
    private final HttpServletRequest request;

    @Override
    public StructureRS listing() {
        PagingListRQ pagingListRQ = new PagingListRQ(request);
        List<PagingDTO> pagingDTOList;
        if (generalUtility.isTempTable(pagingListRQ.getFilterByDate())) {
            pagingDTOList = pagingNQ.getTempPaging(pagingListRQ.getLotteryType(), pagingListRQ.getDrawCode(), pagingListRQ.getFilterByUserCode());
        } else {
            pagingDTOList = pagingNQ.getPaging(pagingListRQ.getLotteryType(), pagingListRQ.getDrawCode(), pagingListRQ.getFilterByUserCode());
        }

        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, pagingDTOList);
    }

}
