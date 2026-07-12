package com.example.medicinereminderapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    DBHelper db;
    ArrayList<String> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listViewHistory);
        db = new DBHelper(this);
        historyList = new ArrayList<>();

        loadHistory();
    }

    private void loadHistory() {

        Cursor cursor = db.getHistory();
        historyList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String status = cursor.getString(2);
                String date = cursor.getString(3);

                String statusText;

                if (status.equals("Taken")) {
                    statusText = "✅ Taken";
                } else if (status.equals("SKIPPED")) {
                    statusText = "⏭ Skipped";
                } else {
                    statusText = "❌ Missed";
                }

                String record = "💊 " + name +
                        "\n" + statusText +
                        "\n🕒 " + date;

                historyList.add(record);

            } while (cursor.moveToNext());

            cursor.close(); // ✅ important
        } else {
            historyList.add("No history available");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                historyList
        );

        listView.setAdapter(adapter);
    }
}