package com.barabanov.mc24;

import com.barabanov.mc24.data.DstData;
import com.barabanov.mc24.data.RegionLocalData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;

/**
 * Created by maxim on 12/09/15.
 */
public class ClockManager {
    public static final int REGIONS_COUNT = 15;
    static public final int REGION_UTC = 16;

    private static ClockManager _instance = new ClockManager();;

    public DateTime currentDate;
    public ArrayList<RegionLocalData> regionsVector;

    public ClockManager()
    {
        initRegions();
    }

    public static synchronized ClockManager getInstance()
    {
        //if (_instance == null) _instance = new ClockManager();
        return _instance;
    }

    public void initRegions()
    {
        regionsVector = new ArrayList<RegionLocalData>();

        regionsVector.add(new RegionLocalData(1, "REGION_WELLINGTON", 12, 36000, 24300));
        regionsVector.add(new RegionLocalData(2, "REGION_SYDNEY", 10, 36000, 21600));
        regionsVector.add(new RegionLocalData(3, "REGION_TOKYO", 9, 32400, 21600));
        regionsVector.add(new RegionLocalData(4, "REGION_SINGAPORE", 8, 32400, 28800));
        regionsVector.add(new RegionLocalData(5, "REGION_SHANGHAI", 8, 34200, 19800));
        regionsVector.add(new RegionLocalData(6, "REGION_DUBAI", 4, 36000, 14400));
        regionsVector.add(new RegionLocalData(7, "REGION_MOSCOW", 3, 36000, 31500));
        regionsVector.add(new RegionLocalData(8, "REGION_RIYADH", 3, 39600, 16200));
        regionsVector.add(new RegionLocalData(9, "REGION_JOHANNESB", 2, 32400, 28800));
        regionsVector.add(new RegionLocalData(10, "REGION_ZURICH", 1, 32400, 30600));
        regionsVector.add(new RegionLocalData(11, "REGION_FRANKFURT", 1, 32400, 30600));
        regionsVector.add(new RegionLocalData(12, "REGION_LONDON", 0, 28800, 30600));
        regionsVector.add(new RegionLocalData(13, "REGION_NEW_YORK", -5, 34200, 23400));
        regionsVector.add(new RegionLocalData(14, "REGION_TORONTO", -5, 34200, 23400));
        regionsVector.add(new RegionLocalData(15, "REGION_CHICAGO", -6, 30600, 23400));
        regionsVector.add(new RegionLocalData(16, "REGION_UTC", 0, 32400, 21600));

        getRegionData(1).initDST(new DstData(8, DstData.DAY_LAST), new DstData(3, DstData.DAY_FIRST), 1);
        getRegionData(2).initDST(new DstData(9, DstData.DAY_FIRST), new DstData(3, DstData.DAY_FIRST), 1);

        getRegionData(10).initDST(new DstData(2, DstData.DAY_LAST), new DstData(9, DstData.DAY_LAST), 1);
        getRegionData(11).initDST(new DstData(2, DstData.DAY_LAST), new DstData(9, DstData.DAY_LAST), 1);
        getRegionData(12).initDST(new DstData(2, DstData.DAY_LAST), new DstData(9, DstData.DAY_LAST), 1);

        getRegionData(13).initDST(new DstData(2, DstData.DAY_SECOND), new DstData(10, DstData.DAY_FIRST), 1);
        getRegionData(14).initDST(new DstData(2, DstData.DAY_SECOND), new DstData(10, DstData.DAY_FIRST), 1);
        getRegionData(15).initDST(new DstData(2, DstData.DAY_SECOND), new DstData(10, DstData.DAY_FIRST), 1);

        String[] s1 = {"01.01","04.01","06.02","03.04","06.04","25.04","27.04","01.06","26.10","25.12","28.12"};
        getRegionData(1).setHolidays(s1);

        String[] s2 = {"01.01","26.01","03.04","06.04","25.04","08.06","25.12","28.12"};
        getRegionData(2).setHolidays(s2);

        String[] s3 = {"01.01","02.01","12.01","11.02","21.03","29.04","03.05","04.05","05.05","06.05","20.07","21.09","22.09","23.09","12.10","03.11","23.11","23.12","31.12"};
        getRegionData(3).setHolidays(s3);

        String[] s4 = {"01.01","20.02","03.04","01.05","01.06","17.07","09.08","24.09","10.11","25.12"};
        getRegionData(4).setHolidays(s4);

        String[] s5 = {"01.01","18.02","19.02","20.02","23.02","24.02","06.04","01.05","22.06","27.09","01.10","02.10","05.10","06.10","07.10"};
        getRegionData(5).setHolidays(s5);

        String[] s6 = {"01.01","15.05","20.07","24.09","25.09","15.10","02.12","03.12","24.12"};
        getRegionData(6).setHolidays(s6);

        String[] s7 = {"01.01","02.01","07.01","09.03","01.05","11.05","12.06","04.11","31.12"};
        getRegionData(7).setHolidays(s7);

        String[] s8 = {"20.07","24.07","21.09","22.09","23.09","24.09","25.09","28.09","29.09"};
        getRegionData(8).setHolidays(s8);

        String[] s9 = {"01.01","03.04","06.04","27.04","01.05","16.06","10.08","24.09","16.12","25.12"};
        getRegionData(9).setHolidays(s9);

        String[] s10 = {"01.01","02.01","03.01","03.04","06.04","01.05","14.05","25.05","24.12","25.12","31.12"};
        getRegionData(10).setHolidays(s10);

        String[] s11 = {"01.01","03.04","06.04","01.05","25.05","24.12","25.12","31.12"};
        getRegionData(11).setHolidays(s11);

        String[] s12 = {"01.01","03.04","06.04","04.05","25.05","31.08","24.12","25.12","28.12"};
        getRegionData(12).setHolidays(s12);

        String[] s13 = {"01.01","19.01","16.02","03.04","25.05","04.07","07.09","26.11","25.12"};
        getRegionData(13).setHolidays(s13);

        String[] s14 = {"01.01","16.02","03.04","18.05","01.07","03.08","07.09","12.10","25.12","28.12"};
        getRegionData(14).setHolidays(s14);

        String[] s15 = {"01.01","19.01","16.02","03.04","25.05","04.07","07.09","26.11","25.12"};
        getRegionData(15).setHolidays(s15);

        updateTime();
    }

    public void updateTime()
    {
        currentDate = new DateTime(DateTimeZone.UTC);
        long utcTime = currentDate.getMillis();

        for (int i = 0; i < regionsVector.size(); i++)
        {
            RegionLocalData regionData = regionsVector.get(i);
            regionData.updateUTCTime(currentDate);
        }
    }

    public RegionLocalData getRegionData(int index)
    {
        return regionsVector.get(index - 1);
    }

}
