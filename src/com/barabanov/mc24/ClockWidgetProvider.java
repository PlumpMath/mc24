package com.barabanov.mc24;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.barabanov.mc24.data.Utility;

/**
 * Created by maxim on 02/08/15.
 */
public class ClockWidgetProvider extends AppWidgetProvider {
    RemoteViews views;
    private AlarmManager am;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Toast.makeText(context, "onEnabled():widget instance enabled", Toast.LENGTH_SHORT).show();
        initAlarm(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context,
                ClockWidgetProvider.class);

        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {
            //Get the remote views
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.clock_widget_layout);
            // Set the text with the current time.

            ClockManager.getInstance().updateTime();
            //remoteViews.setTextViewText(R.id.tvTime, Utility.getCurrentTime());
            ControlsManager.getInstance().update(context, remoteViews);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

        initAlarm(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "TimeWidgetRemoved id(s):" + appWidgetIds, Toast.LENGTH_SHORT).show();
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled():last widget instance removed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager, int appWidgetId,
                                          Bundle newOptions) {
        //Do some operation here, once you see that the widget has change its size or position.
        Toast.makeText(context, "onAppWidgetOptionsChanged() called", Toast.LENGTH_SHORT).show();
    }

    private void initAlarm(Context context) {
        if(am == null) {
            Toast.makeText(context, "initAlarm():alarm init", Toast.LENGTH_SHORT).show();
            am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, 60000, pi);
        }
    }

}
