package com.hacknovation.systemservice.constant;

import java.util.List;

/*
 * author: kangto
 * createdAt: 07/02/2022
 * time: 23:31
 */
public class SettlementConstant {

    public static final String INITIAL_BALANCE = "INITIAL_BALANCE";
    public static final String OWED = "OWED";
    public static final String BORROW = "BORROW";
    public static final String GIVE = "GIVE";
    public static final String UNDER_PROTEST = "UNDER_PROTEST";
    public static final String UPPER_PROTEST = "UPPER_PROTEST";
    public static final String PROTEST_AMOUNT = "PROTEST_AMOUNT";
    public static final String RETURN = "RETURN";
    public static final String LEAP = "LEAP";
    public static final String VN1 = "VN1";
    public static final String MT = "MT";
    public static final String TN = "TN";
    public static final String KH = "KH";


    public static final List<String> MAIN_SETTLEMENT_ITEMS = List.of(GIVE, BORROW);
}
