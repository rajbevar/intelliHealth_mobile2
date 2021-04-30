package com.healthcare.bosch.patientapp;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.healthcare.bosch.patientapp.activity.CardRecyclerViewActivity;
import com.healthcare.bosch.patientapp.activity.MainActivity;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;
import com.healthcare.bosch.patientapp.utils.RealmHelper.RealmManager;
import com.healthcare.bosch.patientapp.utils.model.MedicationRealmModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION_SNOOZE = "OK";
    static final String ACTION_ACCEPT = "ACCEPT";
    static final String EXTRA_NOTIFICATION_ID = "notification-id";

    private static final String TAG = "receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_SNOOZE.equals(intent.getAction())) {
            int notificationId = intent.getExtras().getInt(EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "Cancel notification with id " + notificationId);
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationmanager.cancel(notificationId);
            // get the realm data for the particular pending intent
            RealmManager.getInstance().beginTrans();
            MedicationRealmModel model = RealmManager.getInstance().getMedicationReqById(notificationId);
            RealmManager.getInstance().commitTrans();

            if (model != null) {
                snoozeAlaram(context, model);
            }
        } else if (ACTION_ACCEPT.equals(intent.getAction())) {
            int notificationId = intent.getExtras().getInt(EXTRA_NOTIFICATION_ID);
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationmanager.cancel(notificationId);
// get the realm data for the particular pending intent
            RealmManager.getInstance().beginTrans();
            MedicationRealmModel model = RealmManager.getInstance().getMedicationReqById(notificationId);
            RealmManager.getInstance().commitTrans();

            if (model != null) {
                intent = new Intent(context, CardRecyclerViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intents.addExtra("attachMedia",true); // Extra info
                intent.putExtra("id", notificationId);
                intent.putExtra("patientId", notificationId);
                context.startActivity(intent);

                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);
            }
        }
    }


    public void snoozeAlaram(Context context, MedicationRealmModel model) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //creating a new intent specifying the broadcast receiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("patientId", model.getPendingIntentId());
        intent.putExtra("id", model.getId());
        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(context, model.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //setting the   alarm that will be fired
        am.set(AlarmManager.RTC, calendar.getTimeInMillis(), pi);
    }
}