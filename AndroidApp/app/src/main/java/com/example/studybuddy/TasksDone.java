package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class TasksDone extends AppCompatActivity {
    int MENU_ID = 10, SETTINGS_ID = 11;

    // Shop page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_done);
    }

    // On click, leads to Menu page
    public void onMenuButtonClick(View v) {
        Intent i = new Intent(this, Menu.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, MENU_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Settings page
    public void onSettingButtonClick(View v) {
        Intent i = new Intent(this, Settings.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, SETTINGS_ID);
        setResult(RESULT_OK);
        finish();
    }
}