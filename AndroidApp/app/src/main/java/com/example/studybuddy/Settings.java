package com.example.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class Settings extends AppCompatActivity {
    int LOGOUT_ID = 9, MENU_ID = 10, SETTINGS_ID = 11, PROFILE_ID = 12;

    // Main page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
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

    // On click, leads to user logging out and returning to the main
    // opening/start page
    public void onLogoutButtonClick(View v) {
        Intent i = new Intent(this, MainActivity.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, LOGOUT_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Profile page
    public void onProfileButtonClick(View v) {
        Intent i = new Intent(this, Profile.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, PROFILE_ID);
        setResult(RESULT_OK);
        finish();
    }
}