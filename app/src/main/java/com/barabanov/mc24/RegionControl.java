package com.barabanov.mc24;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import com.barabanov.mc24.data.RegionLocalData;

/**
 * Created by maxim on 23/09/15.
 */
public class RegionControl {
    private int bitmanOnId;
    private int bitmatOffId;
    private int displayId;
    private int cx;
    private int cy;

    public Bitmap sourceBitmapOn;
    public Bitmap sourceBitmapOff;

    private RegionLocalData data;
    private Context context;
    private boolean currenStatus;

    public RegionControl(Context context) {
        this.context = context;
    }

    public RegionControl withBitmap(int bitmanOnId, int bitmatOffId) {
        this.bitmanOnId = bitmanOnId;
        this.bitmatOffId = bitmatOffId;
        //sourceBitmapOn = BitmapFactory.decodeResource(context.getResources(), bitmanOnId);
        //sourceBitmapOff = BitmapFactory.decodeResource(context.getResources(), bitmatOffId);
        return this;
    }

    public RegionControl withDisplay(int displayId) {
        this.displayId = displayId;
        return this;
    }

    public RegionControl withCenter(int cx, int cy) {
        this.cx = cx;
        this.cy = cy;
        return this;
    }

    public void setData(RegionLocalData value) {
        this.data = value;
    }

    public RegionLocalData getData() {
        return data;
    }

    public RegionControl build() {
        return this;
    }

    public Bitmap getBitmap() {
        boolean status = data.getMarketStatus();
        Bitmap sourceBitmap = getSourceBitmap(status);
        return sourceBitmap;
    }

    public float getAngle() {
        long beginTime = data.getMarketStartTimeUTC();
        float angle = (beginTime / 86400.0f) * 360.0f;

        return angle;
    }

    public int getDisplayId() {
        return displayId;
    }

    public void setCurrentStatus(boolean value) {
        currenStatus = value;
    }

    public Boolean getCurrentStatus() {
        return currenStatus;
    }

    private Bitmap getSourceBitmap(boolean status) {
        Bitmap bitmap;
        if (!status) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), bitmanOnId);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), bitmatOffId);
        }
        return bitmap;
    }

    private Bitmap rotate(Bitmap bitmapSource) {
        long beginTime = data.getMarketStartTimeUTC();
        float angle = (beginTime / 86400.0f) * 360.0f;

        int w = bitmapSource.getWidth();
        int h = bitmapSource.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(angle);
        Bitmap rotatatedBitmap = Bitmap.createBitmap(bitmapSource, 0, 0, w,  h, matrix, true);
        return rotatatedBitmap;
    }

}
