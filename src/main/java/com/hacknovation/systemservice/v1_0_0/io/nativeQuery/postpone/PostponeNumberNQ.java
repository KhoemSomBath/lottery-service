package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.postpone;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * author: kangto
 * createdAt: 26/01/2022
 * time: 15:07
 */
@Component
@NativeQueryFolder("postpone")
public interface PostponeNumberNQ extends NativeQuery {

    List<PostponeNumberTO> postponeNumberList();
}
