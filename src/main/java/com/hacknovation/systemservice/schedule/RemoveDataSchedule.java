package com.hacknovation.systemservice.schedule;

import com.hacknovation.systemservice.v1_0_0.utility.SqlUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sombath
 * create at 23/2/22 2:10 PM
 */

@EnableScheduling
@Component
@RequiredArgsConstructor
public class RemoveDataSchedule {

    private final SqlUtility sqlUtility;

    private final List<String> TABLE = List.of(

            "vnone_temp_orders",
            "vnone_temp_drawing",
            "vnone_temp_drawing_items",
            "vnone_temp_order_items",
            "vnone_temp_win_order_items",

            "leap_temp_orders",
            "leap_temp_drawing",
            "leap_temp_drawing_items",
            "leap_temp_order_items",
            "leap_temp_win_order_items",

            "vntwo_temp_orders",
            "vntwo_temp_drawing",
            "vntwo_temp_drawing_items",
            "vntwo_temp_order_items",
            "vntwo_temp_win_order_items",

            "khr_temp_orders",
            "khr_temp_drawing",
            "khr_temp_drawing_items",
            "khr_temp_order_items",
            "khr_temp_win_order_items",

            "tn_temp_orders",
            "tn_temp_drawing",
            "tn_temp_drawing_items",
            "tn_temp_order_items",
            "tn_temp_win_order_items",

            "sc_temp_orders",
            "sc_temp_drawing",
            "sc_temp_drawing_items",
            "sc_temp_order_items",
            "sc_temp_win_order_items",

            "paging_temp"

    );

    private static final String DELETE_QUERY = "delete from %s where date(created_at) < now() - interval 60 day";
    private static final String DELETE_TEMP_QUERY = "delete from %s where date(created_at) < now() - interval 2 day";
    private static final String DELETE_USER_POSTPONE_QUERY = "delete from user_has_postpone_number where date(created_at) < now() - interval 30 day";


    @Scheduled(cron = "10 10 1 * * ?", zone = "Asia/Phnom_Penh")
    public void deleteData() {

        System.out.println("Starting delete data...");

        sqlUtility.executeQuery(DELETE_USER_POSTPONE_QUERY);

        for (String table: TABLE){

            String queryTemp = String.format(DELETE_TEMP_QUERY, table);
            String query = String.format(DELETE_QUERY, table.replace("_temp", ""));

            sqlUtility.executeQuery(queryTemp);
            sqlUtility.executeQuery(query);

        }


        System.out.println("Data deleted successfully");

    }

}
