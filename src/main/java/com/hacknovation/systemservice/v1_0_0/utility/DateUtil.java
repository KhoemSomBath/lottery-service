package com.hacknovation.systemservice.v1_0_0.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Sombath
 * create at 19/7/22 7:54 PM
 */

public class DateUtil {

    private static final String date_time_format = "yyyy-MM-dd HH:mm:ss";

    private static final String KHMER_TIME_ZONE = "Asia/Phnom_Penh";
    private static final String UTC = "UTC";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(date_time_format).withZone(ZoneId.of(UTC));
    private static final DateTimeFormatter dateTimeFormatterKH = DateTimeFormatter.ofPattern(date_time_format).withZone(ZoneId.of(KHMER_TIME_ZONE));

    public static final String START_OF_DAY = " 00:00:00";
    public static final String END_OF_DAY = " 23:59:59";

    public static String getDateUTC() {
        Instant instant = Instant.now();
        return dateTimeFormatter.format(instant);
    }

    public static String getDatePhnom_Penh() {
        Instant instant = Instant.now();
        return dateTimeFormatterKH.format(instant);
    }

    public static String getDateUTC(String date, String hour) {
        LocalDateTime localDateTime = LocalDateTime.parse(date.concat(hour), dateTimeFormatter);
        Instant instant = localDateTime.atZone(ZoneId.of(KHMER_TIME_ZONE)).toInstant();
        return dateTimeFormatter.format(instant);
    }

    public static String getDateUTC(String hour) {

        int h = 0;
        int m = 0;
        int s = 0;

        if(hour.equals(END_OF_DAY)){
            h = 23;
            m = 59;
            s = 59;
        }

        Instant instant = Instant.now();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(KHMER_TIME_ZONE)).withHour(h).withMinute(m).withSecond(s).withNano(0);
        return dateTimeFormatter.format(zonedDateTime.toInstant());
    }

    public static String getYesterdayUTC(String hour) {
        int h = 0;
        int m = 0;
        int s = 0;

        if(hour.equals(END_OF_DAY)){
            h = 23;
            m = 59;
            s = 59;
        }

        Instant instant = Instant.now().minus(1, ChronoUnit.DAYS);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(KHMER_TIME_ZONE)).withHour(h).withMinute(m).withSecond(s).withNano(0);
        return dateTimeFormatter.format(zonedDateTime.toInstant());
    }


}
