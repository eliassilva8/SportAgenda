package com.eliassilva.sportagenda;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elias on 22/07/2018.
 */
public class EventService extends IntentService {
    FirebaseUser mUser;
    String mNextEventKey;
    Event mEvent;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public EventService() {
        super("EventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.service_name), getString(R.string.service_name_background), NotificationManager.IMPORTANCE_NONE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            service.createNotificationChannel(notificationChannel);

            Notification notification = new NotificationCompat.Builder(this, getString(R.string.service_name))
                    .setOngoing(true)
                    .setPriority(0)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        handleActionUpdateEventWidgets();
    }

    private void handleActionUpdateEventWidgets() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabaseSingleton.getInstance();
        final DatabaseReference events = database.getReference(getString(R.string.db_events));
        DatabaseReference userUid = events.child(mUser.getUid());
        Query orderByDate = userUid.orderByChild(getString(R.string.db_date_in_milliseconds)).limitToFirst(1);

        ChildEventListener eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mEvent = dataSnapshot.getValue(Event.class);
                mNextEventKey = dataSnapshot.getKey();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(EventService.this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(EventService.this, EventWidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.event_widget_layout);
                EventWidgetProvider.updateEventWidget(EventService.this, appWidgetIds, appWidgetManager, mEvent, mNextEventKey);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        orderByDate.addChildEventListener(eventListener);
    }

    public static void startActionUpdateEvent(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, EventService.class));
        } else {
            context.startService(new Intent(context, EventService.class));
        }
    }
}
