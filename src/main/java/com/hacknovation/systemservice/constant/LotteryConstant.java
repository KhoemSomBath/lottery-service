package com.hacknovation.systemservice.constant;

import java.math.BigDecimal;
import java.util.List;

public class LotteryConstant {

    public final static String ONE = "ONE";
    public final static String ALL = "ALL";
    public final static String LEAP = "LEAP";
    public final static String VN1 = "VN1";
    public final static String VN2 = "VN2";
    public final static String MT = "MT";
    public final static String SC = "SC";
    public final static String KH = "KH";
    public final static String TN = "TN";
    public final static String TH = "TH";
    public final static String SIXD = "SIXD";

    public final static List<String> ALL_LOTTERY = List.of(
            LEAP,
            VN1,
            VN2,
            TN,
            KH,
            SC,
            TH
    );

    public final static String SIX6D = "6D";

    public final static String OWED = "OWED";
    public final static String BORROW = "BORROW";
    public final static String GIVE = "GIVE";
    public final static String UNDER_PROTEST = "UNDER_PROTEST";
    public final static String PROTEST_AMOUNT = "PROTEST_AMOUNT";
    public final static String UPPER_PROTEST = "UPPER_PROTEST";


    public final static String IS_PRO_RANDOM_LIST = "IS_PRO_RANDOM_LIST";
    public final static String REBATE6D = "6D";
    public final static String REBATE5D = "5D";
    public final static String REBATE4D = "4D";
    public final static String REBATE3D = "3D";
    public final static String REBATE2D = "2D";
    public final static String REBATE1D = "1D";
    public final static String POST = "POST";

    public static final String LO_GROUP = "LO";

    public final static BigDecimal COMMISSION_RATE_1D = BigDecimal.valueOf(80);
    public final static BigDecimal COMMISSION_RATE_2D = BigDecimal.valueOf(90);
    public final static BigDecimal COMMISSION_RATE_3D = BigDecimal.valueOf(84);
    public final static BigDecimal COMMISSION_RATE_4D = BigDecimal.valueOf(80);

    public final static BigDecimal REBATE_RATE_1D = BigDecimal.valueOf(8);
    public final static BigDecimal REBATE_RATE_2D = BigDecimal.valueOf(90);
    public final static BigDecimal REBATE_RATE_3D = BigDecimal.valueOf(800);
    public final static BigDecimal REBATE_RATE_4D = BigDecimal.valueOf(5000);

    public final static BigDecimal MAX_BET_FIRST_1D = BigDecimal.valueOf(100000);
    public final static BigDecimal MAX_BET_FIRST_2D = BigDecimal.valueOf(100000);
    public final static BigDecimal MAX_BET_FIRST_3D = BigDecimal.valueOf(60000);
    public final static BigDecimal MAX_BET_FIRST_4D = BigDecimal.valueOf(20000);

    public final static BigDecimal MAX_BET_SECOND_1D = BigDecimal.valueOf(60000);
    public final static BigDecimal MAX_BET_SECOND_2D = BigDecimal.valueOf(60000);
    public final static BigDecimal MAX_BET_SECOND_3D = BigDecimal.valueOf(30000);
    public final static BigDecimal MAX_BET_SECOND_4D = BigDecimal.valueOf(10000);

    public final static BigDecimal LIMIT_DIGIT_1D = BigDecimal.valueOf(400000);
    public final static BigDecimal LIMIT_DIGIT_2D = BigDecimal.valueOf(400000);
    public final static BigDecimal LIMIT_DIGIT_3D = BigDecimal.valueOf(80000);
    public final static BigDecimal LIMIT_DIGIT_4D = BigDecimal.valueOf(40000);

    public final static String CURRENCY_KHR = "KHR";
    public final static String CURRENCY_USD = "USD";

    public final static String YES = "YES";
    public final static String NO = "NO";

    public final static String TOTAL = "TOTAL";

    public final static List<String> STRING_DAYS = List.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

    public final static List<String> COLUMNS_LEAP = List.of("LO", "POST", "stopDeleteAt");
    public final static List<String> COLUMNS_SABAY = List.of("POST", "stopDeleteAt");
    public final static List<String> COLUMNS_KH = List.of("ABCD", "stopDeleteAt");
    public final static List<String> COLUMNS_SC = List.of("ABCDE", "stopDeleteAt");
    public final static List<String> COLUMNS_TN = List.of("LoAFIKOZ", "NP", "BCD", "stopDeleteAt");
    public final static List<String> COLUMNS_DAY = List.of("LoFIK", "BCDN", "stopDeleteAt");
    public final static List<String> COLUMNS_NIGHT = List.of("Lo", "A", "BCD", "stopDeleteAt");

    public static final String IS_PRO_RANDOM = "RANDOM";
    public static final String IS_PRO_HIGH = "IS_PRO_HIGH";
    public static final String IS_PRO_LOW = "IS_PRO_LOW";

    public static final String IS_PRO_RANDOM_KEY = "IS PROBABILITY RANDOM";
    public static final String IS_PRO_HIGH_KEY = "IS PROBABILITY HIGH";
    public static final String IS_PRO_LOW_KEY = "IS PROBABILITY LOW";


    public final static String MONDAY = "Monday";
    public static final String TUESDAY = "Tuesday";
    public static final String WEDNESDAY = "Wednesday";
    public static final String THURSDAY = "Thursday";
    public static final String FRIDAY = "Friday";
    public static final String SATURDAY = "Saturday";
    public static final String SUNDAY = "Sunday";

    public final static String MON = "MON";
    public static final String TUE = "TUE";
    public static final String WED = "WED";
    public static final String THU = "THU";
    public static final String FRI = "FRI";
    public static final String SAT = "SAT";
    public static final String SUN = "SUN";

    public static final List<String> PLATFORM_ALLOWS = List.of("LMS168", "LEAPMORHASOMBATHLOTTO");

    public static final String TRANSFER_TYPE_MONEY = "money";

    public final static String APK_FOLDER_PATH = "lottery-core/";
}
