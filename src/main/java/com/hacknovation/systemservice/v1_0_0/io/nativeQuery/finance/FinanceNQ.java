package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.finance;

import io.github.gasparbarancelli.NativeQuery;
import io.github.gasparbarancelli.NativeQueryFolder;
import io.github.gasparbarancelli.NativeQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@NativeQueryFolder("finance")
public interface FinanceNQ  extends NativeQuery {
    Page<FinanceListTO> financeListing(@NativeQueryParam(value = "userCode") String userCode,
                                       @NativeQueryParam(value = "transactionType") String transactionType,
                                       @NativeQueryParam(value = "keyword") String keyword,
                                       Pageable pageable);
}
