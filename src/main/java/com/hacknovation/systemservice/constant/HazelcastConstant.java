package com.hacknovation.systemservice.constant;

/*
 * author: kangto
 * createdAt: 14/09/2022
 * time: 13:49
 */
public class HazelcastConstant {
    public static final String MT_RESULT_RENDER_COLLECTION = "mt_result_render";
    public static final String VN1_RESULT_RENDER_COLLECTION = "vnone_result_render";
    public static final String TN_RESULT_RENDER_COLLECTION = "tn_result_render";

    public static final String DRAW_ITEM_COLLECTION = "draw_item_results";

    public static final String VN1_ANALYZE_NUMBER = "vnone_analyze_number";
    public static final String MT_ANALYZE_NUMBER = "mt_analyze_number";
    public static final String LEAP_ANALYZE_NUMBER = "leap_analyze_number";
    public static final String KH_ANALYZE_NUMBER = "kh_analyze_number";
    public static final String TN_ANALYZE_NUMBER = "tn_analyze_number";

    public static final String MEMBER_USERS_COLLECTION = "user_members";
    public static final String MAX_BET_ITEMS_COLLECTION = "max_bet_items";

    public static final String HAS_LOTTERY_TEMPLATE_COLLECTION = "has_lottery_template";

    public static final String USER_HAS_POSTPONE_NUMBER_COLLECTION = "user_has_postpone_number";

    public static final String DRAWS_COLLECTION = "draws";

    public static final String SUBSCRIBE_COLLECTION = "subscribers";

    public static String GET_ANALYZE_NUMBER(String lotteryType) {
        switch (lotteryType) {
            case LotteryConstant.LEAP:
                return LEAP_ANALYZE_NUMBER;
            case LotteryConstant.KH:
                return KH_ANALYZE_NUMBER;
            case LotteryConstant.VN2:
            case LotteryConstant.MT:
                return MT_ANALYZE_NUMBER;
            case LotteryConstant.VN1:
                return VN1_ANALYZE_NUMBER;
            case LotteryConstant.TN:
                return TN_ANALYZE_NUMBER;
            default:
                return "UNKNOWN_ANALYZE_NUMBER";
        }
    }

    public static String CANCEL_TICKET_REMOVE = "cancel_ticket";

}
