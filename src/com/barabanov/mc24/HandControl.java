package com.barabanov.mc24;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.barabanov.mc24.data.RegionLocalData;

/**
 * Created by maxim on 26/09/15.
 */
public class HandControl {
    private int displayId;
    private Bitmap sourceBitmap;

    private RegionLocalData data;
    private Context context;

    private boolean inited;

    public HandControl(Context context) {
        this.context = context;
    }

    public HandControl withBitmap(int bitmanId) {
        sourceBitmap = BitmapFactory.decodeResource(context.getResources(), bitmanId);
        return this;
    }

    public HandControl withDisplay(int displayId) {
        this.displayId = displayId;
        return this;
    }
}
