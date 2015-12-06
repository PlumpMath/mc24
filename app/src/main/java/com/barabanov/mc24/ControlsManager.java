package com.barabanov.mc24;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.barabanov.mc24.data.RegionLocalData;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxim on 12/09/15.
 */
public class ControlsManager {
    private static final int CANVAS_SIZE = 250;
    private static ControlsManager instance;

    private static Map<String, RegionControl> map = new HashMap<String, RegionControl>();
    private static Bitmap bitmapSourceM;
    private static Bitmap bitmapSourceH;
    private static DisplayMetrics metrics;
    private static int canvasSizeInDip;

    private boolean isInited = false;
    private Context context;
    private RemoteViews remoteViews;
    private Map<String, Boolean> statusMap = new HashMap<String, Boolean>();

    public static ControlsManager getInstance() {
        if(instance == null) {
            instance = new ControlsManager();
        }
        return instance;
    }

    public static void initBitmaps(Context context) {
        metrics = context.getResources().getDisplayMetrics();
        canvasSizeInDip = (int) dipToPixels(CANVAS_SIZE);

        initHands(context);
        initRegions(context);
    }

    public void init() {
        for (Map.Entry<String, RegionControl> entry : map.entrySet()) {
            RegionControl regionControl = entry.getValue();
            if(regionControl != null) {
                RegionLocalData data = regionControl.getData();
                if(data != null) {
                    statusMap.put(data.nameId, new Boolean(true));
                }
            }
        }
    }

    public void update(Context context, RemoteViews remoteViews) {
        this.context = context;
        this.remoteViews = remoteViews;

        DateTime dateTime = ClockManager.getInstance().currentDate;

        float sec = dateTime.getSecondOfMinute();
        float min = dateTime.getMinuteOfHour() + sec / 60.0f;
        float hours = dateTime.getHourOfDay() + min / 60.0f;

        remoteViews.setImageViewBitmap(R.id.hand_minute, rotate(min / 60.0f, bitmapSourceM));
        remoteViews.setImageViewBitmap(R.id.hand_hour, rotate(hours / 24.0f, bitmapSourceH));

        boolean shouldRedraw = false;
        for (Map.Entry<String, RegionControl> entry : map.entrySet()) {
           RegionControl regionControl = entry.getValue();
           if(regionControl != null) {
               RegionLocalData data = regionControl.getData();
               if(data != null) {
                   boolean status = data.getMarketStatus();
                   Boolean savedStatus = statusMap.get(data.nameId);
                   if(savedStatus == null) {
                       savedStatus = new Boolean(status);
                   }
                   if(!isInited || savedStatus.booleanValue() != status) {
                       regionControl.setCurrentStatus(status);
                       shouldRedraw = true;
                   }
                   statusMap.put(data.nameId, new Boolean(status));
               }
           }
        }

        isInited = true;
        Toast.makeText(context, "shouldRedraw - " + shouldRedraw, Toast.LENGTH_SHORT).show();

        if(shouldRedraw) {
            Matrix matrix = new Matrix();
            Bitmap bitmap = Bitmap.createBitmap(canvasSizeInDip, canvasSizeInDip, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Bitmap regionBitmap;

            for (Map.Entry<String, RegionControl> entry : map.entrySet()) {
                RegionControl regionControl = entry.getValue();
                if(regionControl != null) {
                    regionBitmap = regionControl.getBitmap();

                    matrix.reset();
                    matrix.postTranslate(-regionBitmap.getWidth() / 2, -regionBitmap.getHeight() / 2); // Centers image
                    matrix.postRotate(regionControl.getAngle());
                    matrix.postTranslate(canvasSizeInDip / 2, canvasSizeInDip / 2);
                    canvas.drawBitmap(regionBitmap, matrix, null);
                }
            }
            remoteViews.setImageViewBitmap(R.id.sectors, bitmap);
        }
    }

    private Bitmap rotate(float value, Bitmap bitmapSource){
        int w = bitmapSource.getWidth();
        int h = bitmapSource.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(value * 360.0f);
        Bitmap rotatatedBitmap = Bitmap.createBitmap(bitmapSource, 0, 0, w, h, matrix, true);
        return rotatatedBitmap;
    }

    public void dispose() {

    }

    private static void initHands(Context context) {
        if(bitmapSourceM == null) {
            bitmapSourceM = BitmapFactory.decodeResource(context.getResources(), R.drawable.m);
            bitmapSourceH = BitmapFactory.decodeResource(context.getResources(), R.drawable.h);
        }
    }

    private static void initRegions(Context context) {
        map.put("REGION_WELLINGTON", new RegionControl(context).withBitmap(R.drawable.wellington, R.drawable.wellington_or));
        map.put("REGION_SYDNEY", new RegionControl(context).withBitmap(R.drawable.sydney, R.drawable.sydney_or));
        map.put("REGION_TOKYO", new RegionControl(context).withBitmap(R.drawable.tokyo, R.drawable.tokyo_or));
        map.put("REGION_SINGAPORE", new RegionControl(context).withBitmap(R.drawable.singapore, R.drawable.singapore_or));
        map.put("REGION_SHANGHAI", new RegionControl(context).withBitmap(R.drawable.shanghai, R.drawable.shanghai_or));
        map.put("REGION_DUBAI", new RegionControl(context).withBitmap(R.drawable.dubai, R.drawable.dubai_or));
        map.put("REGION_MOSCOW", new RegionControl(context).withBitmap(R.drawable.moscow, R.drawable.moscow_or));
        map.put("REGION_RIYADH", new RegionControl(context).withBitmap(R.drawable.riyadh, R.drawable.riyadh_or));
        map.put("REGION_JOHANNESB", new RegionControl(context).withBitmap(R.drawable.johannesburg, R.drawable.johannesburg_or));
        map.put("REGION_ZURICH", new RegionControl(context).withBitmap(R.drawable.zurich, R.drawable.zurich_or));
        map.put("REGION_FRANKFURT", new RegionControl(context).withBitmap(R.drawable.frankfurt, R.drawable.frankfurt_or));
        map.put("REGION_LONDON", new RegionControl(context).withBitmap(R.drawable.london, R.drawable.london_or));
        map.put("REGION_NEW_YORK", new RegionControl(context).withBitmap(R.drawable.ny, R.drawable.ny_or));
        map.put("REGION_TORONTO", new RegionControl(context).withBitmap(R.drawable.toronto, R.drawable.toronto_or));
        map.put("REGION_CHICAGO", new RegionControl(context).withBitmap(R.drawable.chicago, R.drawable.chicago_or));

        ClockManager clockManager = ClockManager.getInstance();
        for (int i = 0; i < clockManager.regionsVector.size(); i++) {
            RegionLocalData regionLocalData = clockManager.regionsVector.get(i);
            RegionControl regionControl = map.get(regionLocalData.nameId);
            if(regionControl != null) {
                regionControl.setData(regionLocalData);
            }
        }
    }

    private static float dipToPixels(float dipValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
