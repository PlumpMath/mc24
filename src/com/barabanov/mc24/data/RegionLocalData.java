package com.barabanov.mc24.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by maxim on 01/09/15.
 */
public class RegionLocalData {
    public static final int MILLISECONDS_HOUR = 60 * 60 * 1000;

    public String nameId;
    public long UTCOffset;
    public long beginTime;
    public long workTime;

    private int _id;
    private DstData dstStart;
    private DstData dstEnd;
    private long dstOffset;
    private DateTime dateTime;
    private long curDstOffset = 0;

    ArrayList<HolidayData> vectorHolidays = new ArrayList<HolidayData>();

    public RegionLocalData(int id, String nameId, long UTCOffset, int beginTime, int workTime)
    {
        this.UTCOffset = UTCOffset*60*60*1000;
        this.workTime = workTime;
        this.beginTime = beginTime;
        this.nameId = nameId;
        this._id = id;
    }

    public void initDST(DstData dstStart, DstData dstEnd, int dstOffset)
    {
        this.dstStart = dstStart;
        this.dstEnd = dstEnd;
        this.dstOffset = dstOffset;
    }

    public DateTime getUTCTime()
    {
        return dateTime;
    }

    public long getMarketStartTimeUTC()
    {
        return (beginTime - (UTCOffset + curDstOffset) / 1000);
    }

    public void updateUTCTime(DateTime time)
    {
        Boolean dstActive = checkDst(time.getMillis());
        curDstOffset = dstActive ? dstOffset*60*60*1000 : 0;
        dateTime = time.plus(UTCOffset + curDstOffset);
    }

    public Boolean checkDst(long time)
    {
        if (dstStart == null || dstEnd == null) {
            return false;
        }

        long curTime = time + UTCOffset;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(curTime));

        int curYear = cal.get(Calendar.YEAR);

        long startDstTime = dstStart.formatDate(curYear);
        long endDstTime = dstEnd.formatDate(curYear);

        if (startDstTime < endDstTime) {
            if (startDstTime < curTime && curTime < endDstTime) {
                return true;
            }
        }
        else {
            if (curTime < endDstTime || curTime > startDstTime) {
                return true;
            }
        }

        return false;
    }

    public Boolean getMarketStatus()
    {
        long dayTimeInSeconds = dateTime.getSecondOfDay();
        long dayOfWeek = dateTime.getDayOfWeek();

        if (dayOfWeek == 1 || dayOfWeek == 7) return false;

        long dayOfMonth = dateTime.getDayOfMonth();
        long month = dateTime.getMonthOfYear();

        for (int i = 0; i < vectorHolidays.size(); i++) {
            HolidayData holiday = vectorHolidays.get(i);
            if(holiday.day == dayOfMonth && holiday.month == month) {
                return  false;
            }
        }
        if (dayTimeInSeconds > beginTime && dayTimeInSeconds < beginTime + workTime) return true;

        return false;
    }

    public void setHolidays(String[] array)
    {
        for(int i = 0; i <= array.length - 1; i++) {
            String holidayStr = array[i];
            String[] parts = holidayStr.split(Pattern.quote("."));
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;

            vectorHolidays.add(new HolidayData(day, month));
        }
    }

    public String getName()
    {
        return "";
    }

    public int getId()
    {
        return _id;
    }
}
