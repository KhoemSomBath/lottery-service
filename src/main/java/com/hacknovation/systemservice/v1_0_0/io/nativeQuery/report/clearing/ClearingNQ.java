package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.clearing;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;

import java.util.List;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 14:22
 */
@NativeQueryFolder("report/clearing/origin")
public interface ClearingNQ extends NativeQuery {

    List<ClearingTO> vn1Clearing(List<String> userCodes, String filterByDate);

}
