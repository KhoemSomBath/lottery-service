package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.page;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.PagingDTO;
import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * author: kangto
 * createdAt: 19/01/2022
 * time: 17:04
 */
@NativeQueryFolder("page")
@Component
public interface PagingNQ extends NativeQuery {

    List<PagingDTO> getPaging(String lotteryCode, String drawCode, String userCode);

    List<PagingDTO> getTempPaging(String lotteryCode, String drawCode, String userCode);

}
