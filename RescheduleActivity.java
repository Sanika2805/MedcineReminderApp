package com.example.medicinereminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RescheduleActivity extends AppCompatActivity {

    EditText minutesInput;
    Button yesBtn, noBtn;
    Button btn10, btn30, btn60;

    String medName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule);

        medName = getIntent().getStringExtra("med_name");

        minutesInput = findViewById(R.id.minutesInput);
        yesBtn = findViewById(R.id.yesBtn);
        noBtn = findViewById(R.id.noBtn);

        // ✅ Quick buttons
        btn10 = findViewById(R.id.btn10);
        btn30 = findViewById(R.id.btn30);
        btn60 = findViewById(R.id.btn60);

        btn10.setOnClickListener(v -> {
            scheduleReminder(10);
            finish();
        });

        btn30.setOnClickListener(v -> {
            scheduleReminder(30);
            finish();
        });

        btn60.setOnClickListener(v -> {
            scheduleReminder(60);
            finish();
        });

        // ✅ Custom minutes
        yesBtn.setOnClickListener(v -> {

            String minStr = minutesInput.getText().toString();

            if (minStr.isEmpty()) {
                Toast.makeText(this, "Enter minutes", Toast.LENGTH_SHORT).show();
                return;
            }

            int minutes = Integer.parseInt(minStr);

            scheduleReminder(minutes);
            finish();
        });

        // ✅ Skip completely
        noBtn.setOnClickListener(v -> {
            DBHelper db = new DBHelper(this);
            db.insertHistory(medName, "SKIPPED", getCurrentTime());
            Toast.makeText(this, "Marked as Skipped", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // ✅ Alarm scheduling
    private void scheduleReminder(int minutes) {

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("med_name", medName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (medName + System.currentTimeMillis()).hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        long triggerTime = System.currentTimeMillis() + (minutes * 60 * 1000);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                );
            }
        }

        Toast.makeText(this,
                "Rescheduled in " + minutes + " minutes",
                Toast.LENGTH_SHORT).show();
    }

    // ✅ Time for DB
    private String getCurrentTime() {
        return new java.text.SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss",
                java.util.Locale.getDefault()
        ).format(new java.util.Date());
    }
}