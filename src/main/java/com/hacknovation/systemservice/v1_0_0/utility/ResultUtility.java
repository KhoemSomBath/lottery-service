package com.hacknovation.systemservice.v1_0_0.utility;

import com.hacknovation.systemservice.constant.PostConstant;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sombath
 * create at 15/8/22 4:03 PM
 */

@Service
public class ResultUtility {

    private static final String MINUTE_KEY = "minute";
    private static final String SECOND_KEY = "second";

    public static Map<String, Map<String, Integer>> getTNPostMinute(String shiftCode) {
        Map<String, Map<String, Integer>> tnPostMinute = new HashMap<>();
        switch (shiftCode) {
            case PostConstant.TN_SHIFT_1_CODE:
                tnPostMinute.put("A_2D", Map.of(MINUTE_KEY, 15, SECOND_KEY, 30));
                tnPostMinute.put("A_3D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 52));
                tnPostMinute.put("I_2D", Map.of(MINUTE_KEY, 15, SECOND_KEY, 30));
                tnPostMinute.put("I_3D", Map.of(MINUTE_KEY, 17, SECOND_KEY, 5));
                tnPostMinute.put("F_2D", Map.of(MINUTE_KEY, 15, SECOND_KEY, 20));
                tnPostMinute.put("F_3D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 20));

                tnPostMinute.put("B", Map.of(MINUTE_KEY, 34, SECOND_KEY, 50));
                tnPostMinute.put("C", Map.of(MINUTE_KEY, 34, SECOND_KEY, 50));
                tnPostMinute.put("D", Map.of(MINUTE_KEY, 34, SECOND_KEY, 50));
                tnPostMinute.put("N", Map.of(MINUTE_KEY, 29, SECOND_KEY, 20));
                tnPostMinute.put("O", Map.of(MINUTE_KEY, 16, SECOND_KEY, 52));
                tnPostMinute.put("K", Map.of(MINUTE_KEY, 17, SECOND_KEY, 20));
                tnPostMinute.put("Z", Map.of(MINUTE_KEY, 18, SECOND_KEY, 12));
                tnPostMinute.put("P", Map.of(MINUTE_KEY, 36, SECOND_KEY, 20));

                tnPostMinute.put("LO1", Map.of(MINUTE_KEY, 18, SECOND_KEY, 12));
                tnPostMinute.put("LO2", Map.of(MINUTE_KEY, 19, SECOND_KEY, 12));
                tnPostMinute.put("LO3", Map.of(MINUTE_KEY, 20, SECOND_KEY, 52));
                tnPostMinute.put("LO4", Map.of(MINUTE_KEY, 22, SECOND_KEY, 12));
                tnPostMinute.put("LO5", Map.of(MINUTE_KEY, 23, SECOND_KEY, 32));
                tnPostMinute.put("LO6", Map.of(MINUTE_KEY, 24, SECOND_KEY, 50));
                tnPostMinute.put("LO7", Map.of(MINUTE_KEY, 26, SECOND_KEY, 10));
                tnPostMinute.put("LO8", Map.of(MINUTE_KEY, 27, SECOND_KEY, 32));
                tnPostMinute.put("LO9", Map.of(MINUTE_KEY, 28, SECOND_KEY, 50));
                tnPostMinute.put("LO10", Map.of(MINUTE_KEY, 30, SECOND_KEY, 12));
                tnPostMinute.put("LO11", Map.of(MINUTE_KEY, 31, SECOND_KEY, 32));
                tnPostMinute.put("LO12", Map.of(MINUTE_KEY, 32, SECOND_KEY, 50));
                break;
            case PostConstant.TN_SHIFT_2_CODE:
                tnPostMinute.put("A_2D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 0));
                tnPostMinute.put("A_3D", Map.of(MINUTE_KEY, 17, SECOND_KEY, 20));
                tnPostMinute.put("I_2D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 0));
                tnPostMinute.put("I_3D", Map.of(MINUTE_KEY, 17, SECOND_KEY, 30));
                tnPostMinute.put("F_2D", Map.of(MINUTE_KEY, 15, SECOND_KEY, 40));
                tnPostMinute.put("F_3D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 40));

                tnPostMinute.put("B", Map.of(MINUTE_KEY, 39, SECOND_KEY, 40));
                tnPostMinute.put("C", Map.of(MINUTE_KEY, 39, SECOND_KEY, 40));
                tnPostMinute.put("D", Map.of(MINUTE_KEY, 39, SECOND_KEY, 40));
                tnPostMinute.put("N", Map.of(MINUTE_KEY, 33, SECOND_KEY, 40));
                tnPostMinute.put("O", Map.of(MINUTE_KEY, 17, SECOND_KEY, 20));
                tnPostMinute.put("K", Map.of(MINUTE_KEY, 17, SECOND_KEY, 40));
                tnPostMinute.put("Z", Map.of(MINUTE_KEY, 18, SECOND_KEY, 40));
                tnPostMinute.put("P", Map.of(MINUTE_KEY, 43, SECOND_KEY, 0));

                tnPostMinute.put("LO1", Map.of(MINUTE_KEY, 18, SECOND_KEY, 40));
                tnPostMinute.put("LO2", Map.of(MINUTE_KEY, 20, SECOND_KEY, 0));
                tnPostMinute.put("LO3", Map.of(MINUTE_KEY, 21, SECOND_KEY, 20));
                tnPostMinute.put("LO4", Map.of(MINUTE_KEY, 22, SECOND_KEY, 40));
                tnPostMinute.put("LO5", Map.of(MINUTE_KEY, 24, SECOND_KEY, 0));
                tnPostMinute.put("LO6", Map.of(MINUTE_KEY, 25, SECOND_KEY, 20));
                tnPostMinute.put("LO7", Map.of(MINUTE_KEY, 26, SECOND_KEY, 40));
                tnPostMinute.put("LO8", Map.of(MINUTE_KEY, 28, SECOND_KEY, 0));
                tnPostMinute.put("LO9", Map.of(MINUTE_KEY, 29, SECOND_KEY, 20));
                tnPostMinute.put("LO10", Map.of(MINUTE_KEY, 30, SECOND_KEY, 40));
                tnPostMinute.put("LO11", Map.of(MINUTE_KEY, 32, SECOND_KEY, 0));
                tnPostMinute.put("LO12", Map.of(MINUTE_KEY, 33, SECOND_KEY, 20));
                tnPostMinute.put("LO13", Map.of(MINUTE_KEY, 34, SECOND_KEY, 40));
                tnPostMinute.put("LO14", Map.of(MINUTE_KEY, 36, SECOND_KEY, 0));
                tnPostMinute.put("LO15", Map.of(MINUTE_KEY, 37, SECOND_KEY, 20));
                break;
            case PostConstant.TN_SHIFT_3_CODE:
            case PostConstant.TN_SHIFT_5_CODE:
                tnPostMinute.put("A_2D", Map.of(MINUTE_KEY, 35, SECOND_KEY, 0));
                tnPostMinute.put("A_3D", Map.of(MINUTE_KEY, 34, SECOND_KEY, 0));
                tnPostMinute.put("A2_2D", Map.of(MINUTE_KEY, 35, SECOND_KEY, 15));
                tnPostMinute.put("A2_3D", Map.of(MINUTE_KEY, 34, SECOND_KEY, 20));
                tnPostMinute.put("A3_2D", Map.of(MINUTE_KEY, 35, SECOND_KEY, 30));
                tnPostMinute.put("A3_3D", Map.of(MINUTE_KEY, 34, SECOND_KEY, 40));
                tnPostMinute.put("A4_2D", Map.of(MINUTE_KEY, 35, SECOND_KEY, 45));

                tnPostMinute.put("B", Map.of(MINUTE_KEY, 37, SECOND_KEY, 0));
                tnPostMinute.put("C", Map.of(MINUTE_KEY, 37, SECOND_KEY, 0));
                tnPostMinute.put("D", Map.of(MINUTE_KEY, 37, SECOND_KEY, 0));

                tnPostMinute.put("LO1", Map.of(MINUTE_KEY, 15, SECOND_KEY, 0));
                tnPostMinute.put("LO2", Map.of(MINUTE_KEY, 16, SECOND_KEY, 0));
                tnPostMinute.put("LO3", Map.of(MINUTE_KEY, 17, SECOND_KEY, 0));
                tnPostMinute.put("LO4", Map.of(MINUTE_KEY, 18, SECOND_KEY, 0));
                tnPostMinute.put("LO5", Map.of(MINUTE_KEY, 19, SECOND_KEY, 0));
                tnPostMinute.put("LO6", Map.of(MINUTE_KEY, 20, SECOND_KEY, 0));
                tnPostMinute.put("LO7", Map.of(MINUTE_KEY, 21, SECOND_KEY, 0));
                tnPostMinute.put("LO8", Map.of(MINUTE_KEY, 22, SECOND_KEY, 0));
                tnPostMinute.put("LO9", Map.of(MINUTE_KEY, 23, SECOND_KEY, 0));
                tnPostMinute.put("LO10", Map.of(MINUTE_KEY, 24, SECOND_KEY, 0));
                tnPostMinute.put("LO11", Map.of(MINUTE_KEY, 25, SECOND_KEY, 0));
                tnPostMinute.put("LO12", Map.of(MINUTE_KEY, 26, SECOND_KEY, 0));
                tnPostMinute.put("LO13", Map.of(MINUTE_KEY, 27, SECOND_KEY, 0));
                tnPostMinute.put("LO14", Map.of(MINUTE_KEY, 28, SECOND_KEY, 0));
                tnPostMinute.put("LO15", Map.of(MINUTE_KEY, 29, SECOND_KEY, 0));
                tnPostMinute.put("LO16", Map.of(MINUTE_KEY, 30, SECOND_KEY, 0));
                tnPostMinute.put("LO17", Map.of(MINUTE_KEY, 31, SECOND_KEY, 0));
                tnPostMinute.put("LO18", Map.of(MINUTE_KEY, 32, SECOND_KEY, 0));
                tnPostMinute.put("LO19", Map.of(MINUTE_KEY, 33, SECOND_KEY, 0));
                break;
        }

        return tnPostMinute;
    }

    public static Map<String, Map<String, Integer>> getMTPostMinute(Boolean isNight) {
        Map<String, Map<String, Integer>> mtPostMinute = new HashMap<>();

        if (isNight) {
            mtPostMinute.put("A_3D", Map.of(MINUTE_KEY, 24, SECOND_KEY, 30));
            mtPostMinute.put("A2_3D", Map.of(MINUTE_KEY, 24, SECOND_KEY, 55));
            mtPostMinute.put("A3_3D", Map.of(MINUTE_KEY, 25, SECOND_KEY, 20));

            mtPostMinute.put("A_2D", Map.of(MINUTE_KEY, 25, SECOND_KEY, 45));
            mtPostMinute.put("A2_2D", Map.of(MINUTE_KEY, 26, SECOND_KEY, 0));
            mtPostMinute.put("A3_2D", Map.of(MINUTE_KEY, 26, SECOND_KEY, 25));
            mtPostMinute.put("A4_2D", Map.of(MINUTE_KEY, 26, SECOND_KEY, 50));

            mtPostMinute.put("B", Map.of(MINUTE_KEY, 33, SECOND_KEY, 30));

            mtPostMinute.put("LO1", Map.of(MINUTE_KEY, 15, SECOND_KEY, 30));
            mtPostMinute.put("LO2", Map.of(MINUTE_KEY, 15, SECOND_KEY, 55));
            mtPostMinute.put("LO3", Map.of(MINUTE_KEY, 16, SECOND_KEY, 20));
            mtPostMinute.put("LO4", Map.of(MINUTE_KEY, 16, SECOND_KEY, 45));
            mtPostMinute.put("LO5", Map.of(MINUTE_KEY, 17, SECOND_KEY, 10));
            mtPostMinute.put("LO6", Map.of(MINUTE_KEY, 17, SECOND_KEY, 35));
            mtPostMinute.put("LO7", Map.of(MINUTE_KEY, 18, SECOND_KEY, 0));
            mtPostMinute.put("LO8", Map.of(MINUTE_KEY, 18, SECOND_KEY, 25));
            mtPostMinute.put("LO9", Map.of(MINUTE_KEY, 18, SECOND_KEY, 50));
            mtPostMinute.put("LO10", Map.of(MINUTE_KEY, 19, SECOND_KEY, 15));
            mtPostMinute.put("LO11", Map.of(MINUTE_KEY, 19, SECOND_KEY, 40));
            mtPostMinute.put("LO12", Map.of(MINUTE_KEY, 20, SECOND_KEY, 5));
            mtPostMinute.put("LO13", Map.of(MINUTE_KEY, 20, SECOND_KEY, 30));
            mtPostMinute.put("LO14", Map.of(MINUTE_KEY, 20, SECOND_KEY, 55));
            mtPostMinute.put("LO15", Map.of(MINUTE_KEY, 21, SECOND_KEY, 20));
            mtPostMinute.put("LO16", Map.of(MINUTE_KEY, 21, SECOND_KEY, 45));
            mtPostMinute.put("LO17", Map.of(MINUTE_KEY, 22, SECOND_KEY, 10));
            mtPostMinute.put("LO18", Map.of(MINUTE_KEY, 22, SECOND_KEY, 35));
            mtPostMinute.put("LO19", Map.of(MINUTE_KEY, 23, SECOND_KEY, 0));
        } else {
            mtPostMinute.put("A_2D", Map.of(MINUTE_KEY, 15, SECOND_KEY, 30));
            mtPostMinute.put("A_3D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 25));

            mtPostMinute.put("F_2D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 0));
            mtPostMinute.put("F_3D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 55));

            mtPostMinute.put("I_2D", Map.of(MINUTE_KEY, 16, SECOND_KEY, 30));
            mtPostMinute.put("I_3D", Map.of(MINUTE_KEY, 17, SECOND_KEY, 25));

            mtPostMinute.put("B", Map.of(MINUTE_KEY, 33, SECOND_KEY, 30));
            mtPostMinute.put("N", Map.of(MINUTE_KEY, 34, SECOND_KEY, 0));
            mtPostMinute.put("K", Map.of(MINUTE_KEY, 17, SECOND_KEY, 50));

            mtPostMinute.put("LO1", Map.of(MINUTE_KEY, 17, SECOND_KEY, 20));
            mtPostMinute.put("LO2", Map.of(MINUTE_KEY, 18, SECOND_KEY, 15));
            mtPostMinute.put("LO3", Map.of(MINUTE_KEY, 19, SECOND_KEY, 10));
            mtPostMinute.put("LO4", Map.of(MINUTE_KEY, 20, SECOND_KEY, 5));
            mtPostMinute.put("LO5", Map.of(MINUTE_KEY, 21, SECOND_KEY, 0));
            mtPostMinute.put("LO6", Map.of(MINUTE_KEY, 21, SECOND_KEY, 55));
            mtPostMinute.put("LO7", Map.of(MINUTE_KEY, 22, SECOND_KEY, 50));
            mtPostMinute.put("LO8", Map.of(MINUTE_KEY, 23, SECOND_KEY, 45));
            mtPostMinute.put("LO9", Map.of(MINUTE_KEY, 24, SECOND_KEY, 40));
            mtPostMinute.put("LO10", Map.of(MINUTE_KEY, 25, SECOND_KEY, 35));
            mtPostMinute.put("LO11", Map.of(MINUTE_KEY, 26, SECOND_KEY, 30));
            mtPostMinute.put("LO12", Map.of(MINUTE_KEY, 27, SECOND_KEY, 25));
            mtPostMinute.put("LO13", Map.of(MINUTE_KEY, 28, SECOND_KEY, 20));
            mtPostMinute.put("LO14", Map.of(MINUTE_KEY, 29, SECOND_KEY, 15));
            mtPostMinute.put("LO15", Map.of(MINUTE_KEY, 30, SECOND_KEY, 10));
        }

        return mtPostMinute;
    }

    public static Map<String, Map<String, Integer>> getLeapPostMinute() {
        Map<String, Map<String, Integer>> leapPostMinute = new HashMap<>();
            leapPostMinute.put("LO1", Map.of(MINUTE_KEY, 15, SECOND_KEY, 0));
            leapPostMinute.put("LO2", Map.of(MINUTE_KEY, 15, SECOND_KEY, 50));
            leapPostMinute.put("LO3", Map.of(MINUTE_KEY, 16, SECOND_KEY, 40));
            leapPostMinute.put("LO4", Map.of(MINUTE_KEY, 17, SECOND_KEY, 30));
            leapPostMinute.put("LO5", Map.of(MINUTE_KEY, 18, SECOND_KEY, 20));
            leapPostMinute.put("LO6", Map.of(MINUTE_KEY, 19, SECOND_KEY, 10));
            leapPostMinute.put("LO7", Map.of(MINUTE_KEY, 20, SECOND_KEY, 0));
            leapPostMinute.put("LO8", Map.of(MINUTE_KEY, 20, SECOND_KEY, 50));
            leapPostMinute.put("LO9", Map.of(MINUTE_KEY, 21, SECOND_KEY, 40));
            leapPostMinute.put("LO10", Map.of(MINUTE_KEY, 22, SECOND_KEY, 30));
            leapPostMinute.put("LO11", Map.of(MINUTE_KEY, 23, SECOND_KEY, 20));
            leapPostMinute.put("LO12", Map.of(MINUTE_KEY, 24, SECOND_KEY, 10));
            leapPostMinute.put("LO13", Map.of(MINUTE_KEY, 25, SECOND_KEY, 0));
            leapPostMinute.put("LO14", Map.of(MINUTE_KEY, 25, SECOND_KEY, 50));
            leapPostMinute.put("LO15", Map.of(MINUTE_KEY, 26, SECOND_KEY, 40));
            leapPostMinute.put("LO16", Map.of(MINUTE_KEY, 27, SECOND_KEY, 30));
            leapPostMinute.put("LO17", Map.of(MINUTE_KEY, 28, SECOND_KEY, 20));
            leapPostMinute.put("LO18", Map.of(MINUTE_KEY, 29, SECOND_KEY, 10));
            leapPostMinute.put("LO19", Map.of(MINUTE_KEY, 30, SECOND_KEY, 0));
            leapPostMinute.put("LO20", Map.of(MINUTE_KEY, 30, SECOND_KEY, 50));

            leapPostMinute.put("B", Map.of(MINUTE_KEY, 31, SECOND_KEY, 30));
            leapPostMinute.put("C", Map.of(MINUTE_KEY, 32, SECOND_KEY, 10));
            leapPostMinute.put("D", Map.of(MINUTE_KEY, 32, SECOND_KEY, 50));
            leapPostMinute.put("F", Map.of(MINUTE_KEY, 33, SECOND_KEY, 30));
            leapPostMinute.put("I", Map.of(MINUTE_KEY, 34, SECOND_KEY, 10));
            leapPostMinute.put("N", Map.of(MINUTE_KEY, 34, SECOND_KEY, 50));
            leapPostMinute.put("A", Map.of(MINUTE_KEY, 36, SECOND_KEY, 0));

        return leapPostMinute;
    }


    public static Map<String, Map<String, Integer>> getSCPostMinute() {
        Map<String, Map<String, Integer>> leapPostMinute = new HashMap<>();

        leapPostMinute.put("A", Map.of(MINUTE_KEY, 30, SECOND_KEY, 30));
        leapPostMinute.put("B", Map.of(MINUTE_KEY, 31, SECOND_KEY, 30));
        leapPostMinute.put("C", Map.of(MINUTE_KEY, 32, SECOND_KEY, 10));
        leapPostMinute.put("D", Map.of(MINUTE_KEY, 32, SECOND_KEY, 50));
        leapPostMinute.put("E", Map.of(MINUTE_KEY, 33, SECOND_KEY, 30));

        return leapPostMinute;
    }


    public static Boolean isEditable(Date date, Map<String, Integer> postMinute) {
        if (postMinute == null)
            return Boolean.FALSE;

        Calendar currentDate = Calendar.getInstance();
        Calendar postTime = Calendar.getInstance();
        postTime.setTime(date);

        postTime.set(Calendar.MINUTE, postMinute.get(MINUTE_KEY));
        postTime.set(Calendar.SECOND, postMinute.get(SECOND_KEY) - 2); // minus 2 second to prevent edit nearly close

        return currentDate.before(postTime);
    }

}
