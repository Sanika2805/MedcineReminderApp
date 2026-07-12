package com.example.medicinereminderapp;

import android.app.*;
import android.content.*;
import android.widget.Toast;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getStringExtra("action");
        String medName = intent.getStringExtra("name");
        int id = intent.getIntExtra("id", 0);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(id); // ✅ remove notification

        DBHelper db = new DBHelper(context);

        if ("taken".equals(action)) {

            db.insertHistory(medName, "Taken", getCurrentTime());
            Toast.makeText(context, "Marked as Taken", Toast.LENGTH_SHORT).show();

        } else if ("skip".equals(action)) {

            // 👉 Open reschedule screen
            Intent i = new Intent(context, RescheduleActivity.class);
            i.putExtra("med_name", medName);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    // ✅ ADD THIS METHOD (IMPORTANT)
    private String getCurrentTime() {
        return new java.text.SimpleDateFormat(
                "dd/MM/yyyy",
                java.util.Locale.getDefault()
        ).format(new java.util.Date());
    }
}