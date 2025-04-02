package com.hacknovation.systemservice.constant;

import java.util.Map;

/**
 * @author Sombath
 * create at 16/6/22 11:58 AM
 */
public class SqlConstant {

    public final static String LIMIT = " limit ";
    public final static String OFFSET = " offset ";
    public final static String FROM = "from ";
    public final static String GROUP = "group ";
    public final static String ORDER = "order ";

    public final static String COUNT_QUERY = "select count(*) ";
    public final static String COUNT_DISTINCT_QUERY = "select count(distinct *) ";
    public static final String EMPTY_STRING = "";

    public static final Map<String, String> DRAW_TABLE = Map.of(
            "leap", "leap_drawing",
            "vn1", "vnone_drawing",
            "vn2", "vntwo_drawing",
            "kh", "kh_drawing",
            "tn", "tn_drawing",
            "sc", "sc_drawing"
    );

    public static final Map<String, String> DRAW_TEMP_TABLE = Map.of(
            "leap", "leap_temp_drawing",
            "vn1", "vnone_temp_drawing",
            "vn2", "vntwo_temp_drawing",
            "kh", "kh_temp_drawing",
            "tn", "tn_temp_drawing",
            "sc", "sc_temp_drawing"
    );

}
