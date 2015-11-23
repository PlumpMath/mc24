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

    private Boolean prevStatus;
    private boolean inited;

    public RegionControl(Context context) {
        this.context = context;
    }

    public RegionControl withBitmap(int bitmanOnId, int bitmatOffId) {
        sourceBitmapOn = BitmapFactory.decodeResource(context.getResources(), bitmanOnId);
        sourceBitmapOff = BitmapFactory.decodeResource(context.getResources(), bitmatOffId);
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

    public void setData(RegionLocalData data) {
        this.data = data;
    }

    public RegionControl build() {
        return this;
    }

    public Bitmap getBitmap() {
        Boolean status = data.getMarketStatus();
        Bitmap sourceBitmap = !status ? sourceBitmapOn : sourceBitmapOff;
        Bitmap rotatedBitmap = rotate(sourceBitmap);
        return rotatedBitmap;
    }

    public int getDisplayId() {
        return displayId;
    }

    private Bitmap rotate(Bitmap bitmapSource){
        long beginTime = data.getMarketStartTimeUTC();
        float angle = (beginTime / 86400.0f) * 360.0f;

        int w = bitmapSource.getWidth();
        int h = bitmapSource.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(angle);
        Bitmap rotatatedBitmap = Bitmap.createBitmap(bitmapSource, 0, 0, w,  h, matrix, true);
        return rotatatedBitmap;
    }

    public Boolean update() {
        if(!inited || prevStatus != data.getMarketStatus()) {
            inited = true;
            prevStatus = data.getMarketStatus();
            return true;
        }
        return false;
    }
}
