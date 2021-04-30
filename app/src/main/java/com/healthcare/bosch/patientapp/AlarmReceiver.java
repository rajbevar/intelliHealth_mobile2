package com.healthcare.bosch.patientapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.healthcare.bosch.patientapp.activity.CardRecyclerViewActivity;
import com.healthcare.bosch.patientapp.utils.RealmHelper.RealmManager;
import com.healthcare.bosch.patientapp.utils.model.MedicationRealmModel;

import static com.healthcare.bosch.patientapp.MyBroadcastReceiver.ACTION_ACCEPT;
import static com.healthcare.bosch.patientapp.MyBroadcastReceiver.ACTION_SNOOZE;
import static com.healthcare.bosch.patientapp.MyBroadcastReceiver.EXTRA_NOTIFICATION_ID;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int MESSAGE_NOTIFICATION_ID = 135345;
    public static final String NOTIFICATION_CHANNEL_ID = "1565";

    @Override
    public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = (int) intent.getExtras().get("id");
        int patientId = (int) intent.getExtras().get("patientId");

        int notification_id = id;

        Intent notificationIntent = new Intent(context, CardRecyclerViewActivity.class);
        notificationIntent.putExtra("id", id);
        notificationIntent.putExtra("patientId", patientId);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // get the realm data for the particular pending intent
        RealmManager.getInstance().beginTrans();
        MedicationRealmModel model = RealmManager.getInstance().getMedicationReqById(notification_id);
        RealmManager.getInstance().commitTrans();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notification_id /* Request code */, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Intent snoozeIntent = new Intent(context, MyBroadcastReceiver.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notification_id);


        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, notification_id, snoozeIntent, 0);


        Intent acceptIntent = new Intent(context, MyBroadcastReceiver.class);
        acceptIntent.setAction(ACTION_ACCEPT);
        acceptIntent.putExtra(EXTRA_NOTIFICATION_ID, notification_id);


        PendingIntent sacceptIntentPendingIntent =
                PendingIntent.getBroadcast(context, notification_id, acceptIntent, 0);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.caduceus)
                .setContentTitle("Time to take medicine -" + model.getMedicineName())
                .setContentText("Remainder - " + model.getStartDate())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                // Add the action button
                .addAction(R.drawable.snooze, context.getString(R.string.snooze),
                        snoozePendingIntent)
                .addAction(R.drawable.ic_check_black_24dp, context.getString(R.string.accept),
                        sacceptIntentPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
//            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_lol);
            notificationBuilder.setSmallIcon(R.drawable.caduceus);
            // notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.active));
        }


        notificationManager.notify(notification_id, notificationBuilder.build());
    }
}
