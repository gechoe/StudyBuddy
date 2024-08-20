package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {
    int MENU_ID = 10, SETTINGS_ID = 11;

    // Shop page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        setUserInfo();
    }

    // Finds and shows user's information (username, password, and total points)
    public void setUserInfo() {
        TextView usern = findViewById(R.id.username);
        TextView passw = findViewById(R.id.password);
        TextView totalp = findViewById(R.id.totalPoints);

        CurrentUser curr = new CurrentUser();

        usern.setText("Username: " + curr.getCurrentUser().getUsername());
        passw.setText("Password: " + curr.getCurrentUser().getPassword());
        String stringPoints = Integer.toString(curr.getCurrentUser().getPoints());
        totalp.setText("Total Points: " + stringPoints);
    }

    // On click, leads to Menu page
    public void onMenuButtonClick(View v) {
        Intent i = new Intent(this, Menu.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, MENU_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to the previous page (aka the settings page)
    public void onBackClick(View v) {
        Intent i = new Intent(this, Settings.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, SETTINGS_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Settings page (using the settings specific button)
    public void onSettingButtonClick(View v) {
        Intent i = new Intent(this, Settings.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, SETTINGS_ID);
        setResult(RESULT_OK);
        finish();
    }
}