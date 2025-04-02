package com.hacknovation.systemservice.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BettingConstant {

    public final static String APP = "APP";
    public final static String PAPER = "PAPER";

    public final static String WATER = "WATER";
    public final static String REBATE = "REBATE";

    public final static String NORMAL = "NORMAL";
    public final static String MULTIPLE = "MULTIPLE";
    public final static String RANGE = "RANGE";
    public final static String NO_RANGE = "NO_RANGE";
    public final static String SPECIAL_RANGE = "SPECIAL_RANGE";
    public final static String MATRIX = "MATRIX";
    public final static String NOT_PAIR = "NOT_PAIR";

    public final static String TWO_D = "2D";
    public final static String THREE_D = "3D";
    public final static String FOUR_D = "4D";
    public final static String FIVE_D = "5D";
    public final static String SIX_D = "6D";

    public final static String POST_A = "A";
    public final static String POST_Ax4 = "Ax4";
    public final static String POST_A1 = "A1";
    public final static String POST_A2 = "A2";
    public final static String POST_A3 = "A3";
    public final static String POST_A4 = "A4";
    public final static String POST_B = "B";
    public final static String POST_C = "C";
    public final static String POST_D = "D";

    public final static String POST_E = "E";
    public final static String POST_F = "F";
    public final static String POST_I = "I";
    public final static String POST_N = "N";
    public final static String POST_K = "K";
    public final static String POST_P = "P";
    public final static String POST_O = "O";
    public final static String POST_Z = "Z";


    public final static String ONED = "1D";
    public final static String TWOD = "2D";
    public final static String THREED = "3D";
    public final static String FOURD = "4D";

    public final static String USD = "USD";
    public final static String KHR = "KHR";

    public final static String POST = "POST";

    public final static String LO = "Lo";
    public final static Integer LO_QUANTITY = 20;

    public final static String LEAP = "LEAP";

    public final static int BATCH_SIZE = 1000;

    public final static List<String> POST_LO_1_TO_4 =  List.of("LO1", "LO2", "LO3", "LO4");
    public final static List<String> POST_LO_10_TO_19 = List.of("LO10", "LO11", "LO12", "LO13", "LO14", "LO15", "LO16", "LO17", "LO18", "LO19");


    // betting

    public final static Integer LO_2D_NIGHT = 32;
    public final static Integer LO_3D_NIGHT = 25;

    private static List<String> getPostLoDay(List<String> posts) {
        List<String> loList = new ArrayList<>(posts);
        for (int i = 1; i <= 15; i++) {

            loList.add("lo" + i);
        }
        return loList;
    }

    public static List<String> getListPosts(String postStr, boolean isNight) {
        List<String> posts = new ArrayList<>();
        if (postStr.contains("A1")) {
            posts.add("A1");
        }
        if (postStr.contains("A2")) {
            posts.add("A2");
        }
        if (postStr.contains("A3")) {
            posts.add("A3");
        }
        if (postStr.contains("A4")) {
            posts.add("A4");
        }
        String[] postsArr = postStr
                .replace("L", "")
                .replace("o", "")
                .replaceAll("\\s+", "")
                .replace("A1", "")
                .replace("A2", "")
                .replace("A3", "")
                .replace("A4", "")
                .split("");

        if (postsArr.length > 0) {
            posts.addAll(List.of(postsArr));
        }

        if (postStr.contains("Lo")) {
            posts = new ArrayList<>(getPostLoDay(posts));
            posts.addAll(List.of("A", "B", "C", "D"));
        }

        if (isNight) {
            if (postStr.contains("Lo")) {
                posts.addAll(List.of("lo16", "lo17", "lo18", "lo19"));
            }
            if (posts.contains("A")) {
                posts.addAll(List.of("A2", "A3", "A4"));
            }
        }

        return posts.stream().distinct().collect(Collectors.toList());
    }

}
