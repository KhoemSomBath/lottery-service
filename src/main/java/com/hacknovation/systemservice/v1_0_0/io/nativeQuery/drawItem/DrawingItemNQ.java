package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.drawItem;

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
@NativeQueryFolder("drawItem")
public interface DrawingItemNQ extends NativeQuery {

    List<DrawingItemTO> leapDrawingItem(@NativeQueryParam(value = "filterDate") String filterDate);

    List<DrawingItemTO> khrDrawingItem(@NativeQueryParam(value = "filterDate") String filterDate);

    List<DrawingItemTO> vnoneDrawingItem(@NativeQueryParam(value = "filterDate") String filterDate);

    List<DrawingItemTO> vntwoDrawingItem(@NativeQueryParam(value = "filterDate") String filterDate);

    List<DrawingItemTO> tnDrawingItem(@NativeQueryParam(value = "filterDate") String filterDate);

}
