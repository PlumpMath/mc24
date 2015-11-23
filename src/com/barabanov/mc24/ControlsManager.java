package com.barabanov.mc24;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.RemoteViews;
import com.barabanov.mc24.data.RegionLocalData;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxim on 12/09/15.
 */
public class ControlsManager {
    private static ControlsManager instance;

    private boolean isInited = false;

    private Context context;
    private RemoteViews remoteViews;

    private Bitmap bitmapSourceM;
    private Bitmap bitmapSourceH;

    private Map<String, RegionControl> map = new HashMap<String, RegionControl>();

    public static ControlsManager getInstance() {
        if(instance == null) {
            instance = new ControlsManager();
        }
        return instance;
    }

    public void init(Context context, RemoteViews remoteViews) {
        initHands();
        initRegions();
    }

    public void update(Context context, RemoteViews remoteViews) {
        this.context = context;
        this.remoteViews = remoteViews;

        if(!isInited) {
            isInited = true;
            init(context, remoteViews);
        }

        DateTime dateTime = ClockManager.getInstance().currentDate;

        long sec = dateTime.getSecondOfMinute();
        long min = dateTime.getMinuteOfHour() + sec/60;
        long hours = dateTime.getHourOfDay() + min/60;

        remoteViews.setImageViewBitmap(R.id.hand_minute, rotate(min, bitmapSourceM));
        remoteViews.setImageViewBitmap(R.id.hand_hour, rotate(hours, bitmapSourceH));

       for (Map.Entry<String, RegionControl> entry : map.entrySet()) {
            RegionControl regionControl = entry.getValue();
           if(regionControl != null) {
               Boolean shouldRedraw = regionControl.update();
               //if (shouldRedraw) {
                   updateRegion(regionControl);
              // }
           }
        }
    }

    private void initHands() {
        if(bitmapSourceM == null) {
            bitmapSourceM = BitmapFactory.decodeResource(context.getResources(), R.drawable.m);
            bitmapSourceH = BitmapFactory.decodeResource(context.getResources(), R.drawable.h);
        }
    }

    private void initRegions() {
        map.put("REGION_WELLINGTON", new RegionControl(context).withBitmap(R.drawable.wellington, R.drawable.wellington_or).withDisplay(R.id.sector_wellington));
        map.put("REGION_SYDNEY", new RegionControl(context).withBitmap(R.drawable.sydney, R.drawable.sydney_or).withDisplay(R.id.sector_sydney));
        map.put("REGION_TOKYO", new RegionControl(context).withBitmap(R.drawable.tokyo, R.drawable.tokyo_or).withDisplay(R.id.sector_tokyo));
        map.put("REGION_SINGAPORE", new RegionControl(context).withBitmap(R.drawable.singapore, R.drawable.singapore_or).withDisplay(R.id.sector_singapore));
        map.put("REGION_SHANGHAI", new RegionControl(context).withBitmap(R.drawable.shanghai, R.drawable.shanghai_or).withDisplay(R.id.sector_shanghai));
        map.put("REGION_DUBAI", new RegionControl(context).withBitmap(R.drawable.dubai, R.drawable.dubai_or).withDisplay(R.id.sector_dubai));
        map.put("REGION_MOSCOW", new RegionControl(context).withBitmap(R.drawable.moscow, R.drawable.moscow_or).withDisplay(R.id.sector_moscow));
        map.put("REGION_RIYADH", new RegionControl(context).withBitmap(R.drawable.riyadh, R.drawable.riyadh_or).withDisplay(R.id.sector_riyadh));
        map.put("REGION_JOHANNESB", new RegionControl(context).withBitmap(R.drawable.johannesburg, R.drawable.johannesburg_or).withDisplay(R.id.sector_johannesburg));
        map.put("REGION_ZURICH", new RegionControl(context).withBitmap(R.drawable.zurich, R.drawable.zurich_or).withDisplay(R.id.sector_zurich));
        map.put("REGION_FRANKFURT", new RegionControl(context).withBitmap(R.drawable.frankfurt, R.drawable.frankfurt_or).withDisplay(R.id.sector_frankfurt));
        map.put("REGION_LONDON", new RegionControl(context).withBitmap(R.drawable.london, R.drawable.london_or).withDisplay(R.id.sector_london));
        map.put("REGION_NEW_YORK", new RegionControl(context).withBitmap(R.drawable.ny, R.drawable.ny_or).withDisplay(R.id.sector_ny));
        map.put("REGION_TORONTO", new RegionControl(context).withBitmap(R.drawable.toronto, R.drawable.toronto_or).withDisplay(R.id.sector_toronto));
        map.put("REGION_CHICAGO", new RegionControl(context).withBitmap(R.drawable.chicago, R.drawable.chicago_or).withDisplay(R.id.sector_chicago));

        ClockManager clockManager = ClockManager.getInstance();
        for (int i = 0; i < clockManager.regionsVector.size(); i++) {
            RegionLocalData regionLocalData = clockManager.regionsVector.get(i);
            RegionControl regionControl = map.get(regionLocalData.nameId);
            if(regionControl != null) {
                regionControl.setData(regionLocalData);
            }
        }
    }

    private void updateRegion(RegionControl regionControl) {
        int displayId = regionControl.getDisplayId();
        Bitmap bitmap = regionControl.getBitmap();
        remoteViews.setImageViewBitmap(displayId, bitmap);
    }

    private Bitmap rotate(long value, Bitmap bitmapSource){
        int w = bitmapSource.getWidth();
        int h = bitmapSource.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(value / 60.0f * 360.0f);
        Bitmap rotatatedBitmap = Bitmap.createBitmap(bitmapSource, 0, 0, w, h, matrix, true);
        return rotatatedBitmap;
    }
}
