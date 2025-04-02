package com.hacknovation.systemservice.constant;

import java.util.List;

/**
 * author : phokkinnky
 * date : 6/23/21
 */
public class PostConstant {

    public static final String POST_A = "A";
    public static final String POST_A2 = "A2";
    public static final String POST_A3 = "A3";
    public static final String POST_A4 = "A4";
    public static final String POST_B = "B";
    public static final String POST_C = "C";
    public static final String POST_D = "D";
    public static final String POST_F = "F";
    public static final String POST_I = "I";
    public static final String POST_N = "N";
    public static final String POST_K = "K";
    public static final String POST_Z = "Z";
    public static final String POST_O = "O";
    public static final String POST_O2 = "O2";
    public static final String POST_O3 = "O3";

    public static final List<String> POST_B_N = List.of("B", "N");
    public static final List<String> POST_B_N_P = List.of("B", "N", "P");

    public static final String POST_GROUP = "POST";
    public static final String LO_GROUP = "LO";
    public static final String POST_P = "P";
    public static final String LO4 = "LO4";
    public static final String LO1 = "LO1";
    public static List<String> LO_DAY_ANALYZE = List.of("Lo", "LoF", "LoI", "LoN", "LoK", "LoFI", "LoFN", "LoFK", "LoIN", "LoIK", "LoNK", "LoFIN", "LoFIK", "LoINK", "LoFINK");
    public static List<String> LO_LEAP_ANALYZE = List.of("Lo", "LoF", "LoI", "LoN", "LoFI", "LoFN", "LoIN", "LoFIN");

    public static List<String> A_NIGHT = List.of("Ax4", "A1", "A2", "A3", "A4", "Lo");
    public static List<String> VN_REMOVAL_POST = List.of("F", "I", "N", "K", "C", "D");
    public static List<String> VN1_ALL_POST = List.of("A", "B", "C", "D", "F", "I", "N", "K");

    public static List<String> LO_1_TO_3 = List.of("LO1", "LO2", "LO3");
    public static List<String> LO_1_TO_4 = List.of("LO1", "LO2", "LO3", "LO4");
    public static List<String> LO_1_TO_9 = List.of("LO1", "LO2", "LO3", "LO4", "LO5", "LO6", "LO7", "LO8", "LO9");
    public static List<String> LO_4_TO_15 = List.of("LO4", "LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "LO13", "LO14", "LO15");
    public static List<String> LO_6_TO_15 = List.of("LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "LO13", "LO14", "LO15");
    public static List<String> LO_10_TO_19 = List.of("LO10", "LO11", "LO12", "LO13", "LO14", "LO15", "LO16", "LO17", "LO18", "LO19");
    public static List<String> POST_RESULT_DAY = List.of("A", "LO1", "LO2", "LO3", "LO4", "LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "LO13", "LO14", "LO15", "B", "C", "D", "F", "I", "N", "K");
    public static List<String> POST_RESULT_NIGHT = List.of("A", "A2", "A3", "A4", "B", "C", "D", "LO1", "LO2", "LO3", "LO4", "LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "LO13", "LO14", "LO15", "LO16", "LO17", "LO18", "LO19");
    public static List<String> POST_LEAP = List.of("A", "B", "C", "D", "F", "I", "N");

    public static List<String> POST_SC = List.of("A", "B", "C", "D", "E");
    public static List<String> POST_KH = List.of("A", "B", "C", "D");

    public static List<String> POST_TH = List.of("A", "B", "C", "D");

    public static List<String> POST_TRANSFER_MONEY = List.of("ABCD", "FI", "NK", "LO");

    public static List<String> POST_TRANSFER_MONEY_LEAP = List.of("ABCD", "FIN", "LO");

    public static List<String> POST_TRANSFER_MONEY_SC = List.of("ABCDE");

    public static List<String> COMPOUND_POST = List.of("C", "D");

    public static List<String> TN_SHIFT_1_POST = List.of("A", "LO1", "LO2", "LO3", "LO4", "LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "B", "C", "D", "F", "I", "N", "K", "P", "O", "Z");
    public static List<String> TN_SHIFT_2_POST = List.of("A", "LO1", "LO2", "LO3", "LO4", "LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "LO13", "LO14", "LO15", "B", "C", "D", "F", "I", "N", "K", "P", "O", "Z");
    public static List<String> TN_SHIFT_3_POST = List.of("A", "A2", "A3", "A4", "B", "C", "D", "LO1", "LO2", "LO3", "LO4", "LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "LO13", "LO14", "LO15", "LO16", "LO17", "LO18", "LO19");
    public static List<String> TN_REMOVAL_POST = List.of("C", "D", "F", "I", "N", "K", "O", "Z", "P");

    public static List<String> VN_NIGHT = List.of("A", "A2", "A3", "A4", "B", "C", "D", "LO1", "LO2", "LO3", "LO4", "LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "LO13", "LO14", "LO15", "LO16", "LO17", "LO18", "LO19");
    public static List<String> VN_DAY = List.of("A", "LO1", "LO2", "LO3", "LO4", "LO5", "LO6", "LO7", "LO8", "LO9", "LO10", "LO11", "LO12", "LO13", "LO14", "LO15", "B", "C", "D", "F", "I", "N", "K");

    public static final String TN_SHIFT_1_CODE = "020";
    public static final String TN_SHIFT_2_CODE = "021";
    public static final String TN_SHIFT_3_CODE = "022";
    public static final String TN_SHIFT_4_CODE = "024";
    public static final String TN_SHIFT_5_CODE = "028";


    public static List<String> GET_TN_POST_BY_SHIFT_CODE(String shiftCode) {
        switch (shiftCode) {
            case "020":
                return TN_SHIFT_1_POST;
            case "021":
            case "024":
                return TN_SHIFT_2_POST;
            case "022":
            case "028":
                return TN_SHIFT_3_POST;
            default:
                return List.of();
        }
    }

    public static List<String> getVNPostByIsNight(Boolean isNight) {
        return isNight ? PostConstant.POST_RESULT_NIGHT : PostConstant.POST_RESULT_DAY;
    }

    public static List<String> getPostANight() {
        return List.of(POST_A, POST_A2, POST_A3, POST_A4);
    }
}
