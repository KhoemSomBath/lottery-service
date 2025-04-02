package com.hacknovation.systemservice.v1_0_0.utility;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class UniqueUtility {


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Generate unique code
     * -----------------------------------------------------------------------------------------------------------------
     * @param lastCode
     * @return
     */
    public static String getUniqueCode(String lastCode) {

        Calendar calendar = Calendar.getInstance();

        String newYear = Integer.toString(calendar.get(Calendar.YEAR)).substring(2, 4);
        if (lastCode == null) {
            return "00000001" + newYear;
        }

        String currentYear = lastCode.substring(lastCode.length() - 2).substring(0, 2);
        if (!currentYear.equalsIgnoreCase(newYear)) {
            return "00000001" + newYear;
        }

        String numIncr = lastCode.substring(0, 1);
        String num7Digit = lastCode.substring(1, 8);

        if (num7Digit.equalsIgnoreCase("9999999")) {

            num7Digit = "0000001";
            if (numIncr.equalsIgnoreCase("9")) {

                return "A" + num7Digit + currentYear;

            } else if (!NumberUtils.isNumber(numIncr)) {

                return (char) (numIncr.charAt(0) + 1) + num7Digit + currentYear;

            } else {

                return (NumberUtils.toInt(numIncr) + 1) + num7Digit + currentYear;
            }

        } else {

            String increase = Integer.toString(NumberUtils.toInt(num7Digit) + 1);
            return numIncr + "0000000".substring(increase.length()) + increase + currentYear;

        }

    }


}
