package com.barabanov.mc24;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxim on 02/08/15.
 */
public class ClockWidgetProvider extends AppWidgetProvider {
    public static final String CLOCK_UPDATE = "com.barabanov.mc24.CLOCK_UPDATE";
    private static final double TICK = 5000.0;


    RemoteViews views;
    private static AlarmManager am;

    // Time Control
    private static int currentTime = 0;
    private static boolean isFirstCall = true;
    private static Map<String, ControlsManager> map = new HashMap<String, ControlsManager>();

/*
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Toast.makeText(context, "onEnabled(): widget instance enabled", Toast.LENGTH_SHORT).show();
        initAlarm(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Toast.makeText(context, "onUpdate(): id " + appWidgetIds, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(context, "onDeleted(): widget Removed id(s):" + appWidgetIds, Toast.LENGTH_SHORT).show();
        super.onDeleted(context, appWidgetIds);
    }

   @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled(): last widget instance removed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }

     @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        //Do some operation here, once you see that the widget has change its size or position.
        Toast.makeText(context, "onAppWidgetOptionsChanged(): called", Toast.LENGTH_SHORT).show();
    }
*/
    private void initAlarm(Context context) {
        if(am == null) {
            Toast.makeText(context, "initAlarm():alarm initBitmaps", Toast.LENGTH_SHORT).show();
            am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

            am.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis() + 1000, 5000, pi);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "onReceive()", Toast.LENGTH_SHORT).show();
        super.onReceive(context, intent);

        // Get the widget manager and ids for this widget provider, then call the shared clock update method.
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        // Clock Update Event
        if (CLOCK_UPDATE.equals(intent.getAction())) {
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateClock(context, appWidgetManager, appWidgetID);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {

        super.onEnabled(context);

        // Create the Timer
        //initAlarm(context);
        if(am == null) {
            am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 1000, 3000, createClockTickIntent(context));
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < appWidgetIds.length; i++) {

            int appWidgetId = appWidgetIds[i];

            // Get the layout for the App Widget and attach an on-click listener to the button
            AppWidgetProviderInfo appInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
            if (context == null || appInfo == null) {
                return;
            }
            RemoteViews views = new RemoteViews(context.getPackageName(), appInfo.initialLayout);

            // Update The clock label using a shared method
            updateClock(context, appWidgetManager, appWidgetId);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled(): last widget instance removed", Toast.LENGTH_SHORT).show();
        super.onDisabled(context);

        // Stop the Timer
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "onDeleted(): widget Removed id(s):" + appWidgetIds, Toast.LENGTH_SHORT).show();
        super.onDeleted(context, appWidgetIds);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            String widgetIdKey = Integer.toString(appWidgetId);
            ControlsManager control = map.get(widgetIdKey);
            control.dispose();
        }
    }

    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(CLOCK_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private void updateClock(Context context, AppWidgetManager appWidgetManager, int appWidgetID) {
        Toast.makeText(context, "updateClock()", Toast.LENGTH_SHORT).show();
        // Get a reference to our Remote View
        AppWidgetProviderInfo appInfo = appWidgetManager.getAppWidgetInfo(appWidgetID);
        RemoteViews views = new RemoteViews(context.getPackageName(), appInfo.initialLayout);

        // Update clock
        if (isFirstCall) {
            ControlsManager.initBitmaps(context);
            ClockManager.getInstance().initRegions();
            isFirstCall = false;
        }
        String widgetIdKey = Integer.toString(appWidgetID);
        ClockManager.getInstance().updateTime();
        ControlsManager control = map.get(widgetIdKey);
        if(control == null) {
            control = new ControlsManager();
            map.put(widgetIdKey, control);
        }
        control.update(context, views);

        appWidgetManager.updateAppWidget(appWidgetID, views);
    }

}
