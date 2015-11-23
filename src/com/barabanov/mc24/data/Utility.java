package com.barabanov.mc24.data;

/**
 * Created by maxim on 29/08/15.
 */

import com.barabanov.mc24.ClockManager;
import org.joda.time.DateTime;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
    public static String getCurrentTime(){
        DateTime dateTime = ClockManager.getInstance().currentDate;
        long sec = dateTime.getSecondOfMinute();
        long min = dateTime.getMinuteOfHour() + sec/60;
        long hours = dateTime.getHourOfDay() + min/60;

        String result = String.valueOf(hours) + ":" + String.valueOf(min);
        return result;
    }

    public static int getDaysCountInMonth(int year, int month)
    {
        switch (month)
        {
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 1:
                if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) {
                    return 29;
                }
                else {
                    return 28;
                }
            default:
        }
        return 31;
    }

    public static String formatTimeToString(int time)
    {
        String timeStr = Integer.toString(time);
        if (time < 10) timeStr = "0" + timeStr;
        return timeStr;
    }
}
