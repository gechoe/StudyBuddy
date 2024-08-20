package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {
    private EditText username, password;
    // Previous test user: // String valid_user = "demo", valid_password = "demo";
    int MAIN_ID = 1, STUDY_ID = 4, SIDEBAR_ID = 0;

    // Log in page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    // On click, with proper log in information (compares login info to database
    // info), leads to Study page
    public void onLogInClick(View v) {
        username = (EditText) findViewById(R.id.email);
        String emailString = username.getText().toString();
        password = (EditText) findViewById(R.id.password);
        String passString = password.getText().toString();

        // Compares login username and password to users database
        // If user exists, proceeds with login, if not shows Toast of "Invalid
        // username or password"
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    URL url = new URL("http://loin.cs.brynmawr.edu:11111/users");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    int count = 0;

                    while (in.hasNext()) {
                        String response = in.nextLine();
                        JSONArray ja = new JSONArray(response);

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = (JSONObject) ja.get(i);
                            String user = jo.getString("username");
                            String pass = jo.getString("password");
                            if (user.equals(emailString)) {

                                if (pass.equals(passString)) {
                                    CurrentUser current = new CurrentUser();

                                    User validUser = current.getUserFromDB(jo);
                                    current.setCurrentUser(validUser);

                                    count = 1;
                                    if (count == 1) {
                                        Intent intent = new Intent(this, StudyHomePage.class);
                                        String uname = user;
                                        intent.putExtra("username", uname);
                                        startActivityForResult(intent, STUDY_ID);
                                    }
                                }
                            }
                        }
                    }

                    Looper.prepare();
                    if (count == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Invalid username or password.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 1030);
                        toast.show();
                    }
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // On click, goes back from login page to home/start page
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