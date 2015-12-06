package com.barabanov.mc24;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by maxim on 04/08/15.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "timer tick", Toast.LENGTH_SHORT).show();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here update the widget/remote views.
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.clock_widget_layout);

        ClockManager.getInstance().updateTime();
        //remoteViews.setTextViewText(R.id.tvTime, Utility.getCurrentTime());
       // ControlsManager.getInstance().update(appWidgetID, context, remoteViews);

        ComponentName widget = new ComponentName(context, ClockWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(widget, remoteViews);
        //Release the lock
        wl.release();

    }
}
