package com.example.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    int LOGIN_ID = 2, SIGNUP_ID = 3;

    // Main page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // On click, leads to Log In page
    public void onLogInClick(View v) {
        Intent i = new Intent(this, Login.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, LOGIN_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Sign Up page
    public void onSignUpClick(View v) {
        Intent i = new Intent(this, SignUp.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, SIGNUP_ID);
        setResult(RESULT_OK);
        finish();
    }
}