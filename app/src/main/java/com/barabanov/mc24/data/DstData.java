package com.barabanov.mc24.data;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by maxim on 01/09/15.
 */
public class DstData {
    public static final int DAY_LAST = -1;
    public static final int DAY_FIRST = 1;
    public static final int DAY_SECOND = 2;

    public int month;
    public int dayOrder;

    public DstData(int month, int dayOrder)
    {
        this.dayOrder = dayOrder;
        this.month = month;
    }

    public long formatDate(int year)
    {
        int dateDay = (dayOrder == DAY_LAST) ? Utility.getDaysCountInMonth(year, month) : 1;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, year + 1900);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dateDay);

        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        int difference;
        int monthDay;

        if(dayOrder > 0) {
            difference = (7 - weekDay) % 7;
            monthDay = difference + (dayOrder - 1) * 7;
        }
        else {
            monthDay = dateDay - weekDay;
        }

        Calendar newCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        return newCalendar.getTimeInMillis();
    }
}
