package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SignUp  extends AppCompatActivity {
    int MAIN_ID = 1, STUDY_ID = 4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
    }

    // Creates new user when sign up button is clicked
    public void onSignUpClick(View v) {
        EditText user = findViewById(R.id.email);
        EditText pass = findViewById(R.id.password);

        String username = user.getText().toString();
        String password = pass.getText().toString();

        // Make sure username and password are not empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String link = "http://loin.cs.brynmawr.edu:11111/addUserApp?username=" + username + "&password=" + password;

        // Create a new thread to perform network operations
        new Thread(() -> {
            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Get the response code
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Account created successfully, navigate to the next activity
                    runOnUiThread(() -> {
                        CurrentUser current = new CurrentUser();
                        List<String> buds = new ArrayList<>();
                        List<Task> tasks = new ArrayList<>();
                        User newUser = new User(username, password, buds, tasks, 0);
                        current.setCurrentUser(newUser);

                        Toast.makeText(getApplicationContext(), "Account created! Logging in...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, StudyHomePage.class);
                        startActivityForResult(intent, STUDY_ID);
                    });
                } else {
                    // Handle error response
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Failed to create account", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    // Back click function
    public void onBackClick(View v) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivityForResult(intent, MAIN_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
