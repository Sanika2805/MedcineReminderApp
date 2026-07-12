package com.example.medicinereminderapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    DBHelper db;
    ListView listView;
    TextView tvTotal, tvUpcoming, tvCompleted;
    Button btnAdd, btnHistory;

    ArrayList<Integer> ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        listView = findViewById(R.id.listView);
        tvTotal = findViewById(R.id.tvTotal);
        tvUpcoming = findViewById(R.id.tvUpcoming);
        tvCompleted = findViewById(R.id.tvCompleted);
        btnAdd = findViewById(R.id.btnAdd);
        btnHistory = findViewById(R.id.btnHistory);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddMedicineActivity.class)));

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        loadData();

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            db.deleteData(ids.get(position));
            loadData();
            return true;
        });
    }

    private void loadData() {

        Cursor cursor = db.getData();

        ArrayList<String> list = new ArrayList<>();
        ids.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                ids.add(id);

                String name = cursor.getString(1);
                String dose = cursor.getString(2);
                String time = cursor.getString(3);
                String endDate = cursor.getString(5);

                String remaining;

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date end = sdf.parse(endDate);
                    Date today = new Date();

                    long diff = end.getTime() - today.getTime();
                    long days = diff / (1000 * 60 * 60 * 24);

                    remaining = "Remaining: " + days + " days";
                } catch (Exception e) {
                    remaining = "Remaining: --";
                }

                String item =
                        "💊 " + name + "\n" +
                                "Dose: " + dose + "   ⏰ " + time + "\n" +
                                remaining;

                list.add(item);

            } while (cursor.moveToNext());
        }

        listView.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list
        ));

        // ===============================
        // ✅ UPDATED COUNTER LOGIC
        // ===============================

        String todayDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        int total = list.size();
        int done = db.getTodayDoneCount(todayDate);
        int upcoming = total - done;

        if (upcoming < 0) upcoming = 0;

        tvTotal.setText(total + "\nTotal");
        tvCompleted.setText(done + "\nDone");
        tvUpcoming.setText(upcoming + "\nUpcoming");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}