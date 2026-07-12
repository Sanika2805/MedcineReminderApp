package com.example.medicinereminderapp;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Random;

public class AddMedicineActivity extends AppCompatActivity {

    EditText etName, etDose;
    TextView tvTime, tvStartDate, tvEndDate;
    Button btnSave;

    DBHelper db;

    String time = "";
    String startDate = "";
    String endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmed);

        db = new DBHelper(this);

        etName = findViewById(R.id.etName);
        etDose = findViewById(R.id.etDose);
        tvTime = findViewById(R.id.tvTime);
        tvStartDate = findViewById(R.id.tvStart);
        tvEndDate = findViewById(R.id.tvEnd);
        btnSave = findViewById(R.id.btnSave);

        // TIME PICKER
        tvTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            new TimePickerDialog(this, (view, hour, minute) -> {
                time = String.format("%02d:%02d", hour, minute);
                tvTime.setText(time);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        // START DATE
        tvStartDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            new DatePickerDialog(this, (view, year, month, day) -> {
                startDate = day + "/" + (month + 1) + "/" + year;
                tvStartDate.setText(startDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // END DATE
        tvEndDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            new DatePickerDialog(this, (view, year, month, day) -> {
                endDate = day + "/" + (month + 1) + "/" + year;
                tvEndDate.setText(endDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // SAVE BUTTON
        btnSave.setOnClickListener(v -> {

            String name = etName.getText().toString();
            String dose = etDose.getText().toString();

            if (name.isEmpty() || dose.isEmpty() || time.isEmpty()
                    || startDate.isEmpty() || endDate.isEmpty()) {

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // SAVE DATA
            db.insertData(name, dose, time, startDate, endDate);

            // SET ALARM
            setAlarm(name, time);

            Toast.makeText(this, "Medicine Added Successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // 🔔 ALARM FUNCTION
    private void setAlarm(String name, String time) {

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("med_name", name);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }
        }

        Toast.makeText(this, "Alarm set for " + time, Toast.LENGTH_SHORT).show();
    }
}