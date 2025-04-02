package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.userlottery;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import io.github.gasparbarancelli.NativeQueryParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NativeQueryFolder("userlottery")
public interface UserHasLotteryNQ extends NativeQuery {

    List<UserHasLotteryTO> userHasLottery(@NativeQueryParam(value = "lotteryType") String lotteryType,
                                          @NativeQueryParam(value = "userCode") String userCode);

    List<UserHasLotteryTO> userHasLotteryList(@NativeQueryParam(value = "lotteryType") String lotteryType,
                                              @NativeQueryParam(value = "userCode") String userCode);

    List<UserHasLotteryTO> userHasLotteryListByLottery(@NativeQueryParam(value = "lotteryType") List<String> lotteryType,
                                              @NativeQueryParam(value = "userCode") String userCode);

}
