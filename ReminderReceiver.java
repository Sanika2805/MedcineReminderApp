package com.example.medicinereminderapp;

import android.app.*;
import android.content.*;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String medName = intent.getStringExtra("med_name");

        int notificationId = medName.hashCode();

        // 👉 TAKEN BUTTON
        Intent takenIntent = new Intent(context, ActionReceiver.class);
        takenIntent.putExtra("action", "taken");
        takenIntent.putExtra("name", medName);
        takenIntent.putExtra("id", notificationId);

        PendingIntent takenPending = PendingIntent.getBroadcast(
                context,
                notificationId + 100,
                takenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 👉 SKIP BUTTON (no change here)
        Intent skipIntent = new Intent(context, ActionReceiver.class);
        skipIntent.putExtra("action", "skip");
        skipIntent.putExtra("name", medName);
        skipIntent.putExtra("id", notificationId);

        PendingIntent skipPending = PendingIntent.getBroadcast(
                context,
                notificationId + 200,
                skipIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "med_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Medicine Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("💊 Medicine Reminder")
                .setContentText("Time to take: " + medName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setSound(sound)
                .addAction(0, "Taken", takenPending)
                .addAction(0, "Skip", skipPending);

        manager.notify(notificationId, builder.build());
    }
}