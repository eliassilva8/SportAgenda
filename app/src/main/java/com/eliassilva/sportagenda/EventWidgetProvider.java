package com.eliassilva.sportagenda;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Elias on 21/07/2018.
 */
public class EventWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        EventService.startActionUpdateEvent(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    public static void updateEventWidget(Context context, int[] appWidgetIds, AppWidgetManager appWidgetManager, Event nextEvent, String nextEventKey) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = getEventRemoteView(context, nextEvent);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    private static RemoteViews getEventRemoteView(Context context, Event nextEvent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.event_widget);
        views.setTextViewText(R.id.widget_sport_tv, nextEvent.sports);
        views.setTextViewText(R.id.widget_date_tv, nextEvent.date);
        views.setTextViewText(R.id.widget_local_tv, nextEvent.local);
        views.setTextViewText(R.id.widget_time_tv, nextEvent.time);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
        views.setOnClickPendingIntent(R.id.event_widget_layout, pendingIntent);
        return views;
    }
}
