package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.originalOrder;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import io.github.gasparbarancelli.NativeQueryParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * author : phokkinnky
 * date : 8/24/21
 */
@Component
@NativeQueryFolder("original")
public interface OriginalTempNQ extends NativeQuery {

    List<OriginalTO> leapOriginalOrderItem(@NativeQueryParam(value = "userCode") String userCode,
                                           @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                           @NativeQueryParam(value = "filterDate") String drawAt);

    List<OriginalTO> agentLeapOriginalOrderItem(@NativeQueryParam(value = "userCode") String agentCode,
                                           @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                           @NativeQueryParam(value = "filterDate") String drawAt);

    List<OriginalTO> khrOriginalOrderItem(@NativeQueryParam(value = "userCode") String userCode,
                                           @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                           @NativeQueryParam(value = "filterDate") String drawAt);


    List<OriginalTO> agentKhrOriginalOrderItem(@NativeQueryParam(value = "userCode") String agentCode,
                                                @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                                @NativeQueryParam(value = "filterDate") String drawAt);

    List<OriginalTO> vnoneOriginalOrderItem(@NativeQueryParam(value = "userCode") String userCode,
                                          @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                          @NativeQueryParam(value = "filterDate") String drawAt);

    List<OriginalTO> agentVNOneOriginalOrderItem(@NativeQueryParam(value = "userCode") String agentCode,
                                               @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                               @NativeQueryParam(value = "filterDate") String drawAt);

    List<OriginalTO> vntwoOriginalOrderItem(@NativeQueryParam(value = "userCode") String userCode,
                                          @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                          @NativeQueryParam(value = "filterDate") String drawAt);

    List<OriginalTO> agentVNTwoOriginalOrderItem(@NativeQueryParam(value = "userCode") String agentCode,
                                               @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                               @NativeQueryParam(value = "filterDate") String drawAt);

    List<OriginalTO> tnOriginalOrderItem(@NativeQueryParam(value = "userCode") String userCode,
                                         @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                         @NativeQueryParam(value = "filterDate") String drawAt);

    List<OriginalTO> agentTNOriginalOrderItem(@NativeQueryParam(value = "userCode") String agentCode,
                                                 @NativeQueryParam(value = "filterByCurrency") String currencyCode,
                                                 @NativeQueryParam(value = "filterDate") String drawAt);

}
