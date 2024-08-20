package com.example.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class Menu extends AppCompatActivity {
    int STUDY_ID = 4, SHOP_ID = 5, MENU_ID = 10, SETTINGS_ID = 11, TASKS_ID = 13;

    // Main page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
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

    // On click, leads to Home/Study page
    public void onHomePageButtonClick(View v) {
        Intent i = new Intent(this, StudyHomePage.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, STUDY_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Tasks page
    public void onTasksButtonClick(View v) {
        Intent i = new Intent(this, TasksDone.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, TASKS_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Shop page
    public void onShoppingButtonClick(View v) {
        Intent i = new Intent(this, Shop.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, SHOP_ID);
        setResult(RESULT_OK);
        finish();
    }
}