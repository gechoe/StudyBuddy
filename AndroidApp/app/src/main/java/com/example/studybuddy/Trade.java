package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Trade  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText username;
    int MENU_ID = 10, SETTINGS_ID = 11, INVENTORY_ID = 6, STORE_ID = 7, TRADE_ID = 8;
    private Spinner haveSpinner, getSpinner;
    private StudyBuddies sb = new StudyBuddies();
    private String[] words = sb.getWords();
    private int[] imageResourcesBackdrop = sb.imageResourcesBackdrop();

    private User currUser, otherUser;

    // Shop page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade);

        username = findViewById(R.id.email);

        // Gets current user
        CurrentUser current = new CurrentUser();
        currUser = current.getCurrentUser();
    }

    // Populates spinners with the current user's StudyBuddies collection and the
    // searched user's StudyBuddies collection
    public void populateSpinners() {
        haveBuddiesSpinner();
        wantBuddiesSpinner();

        // Set up spinner listeners
        haveSpinner.setOnItemSelectedListener(this);
        getSpinner.setOnItemSelectedListener(this);
    }

    // Method to get the StudyBuddies for the current user
    public void haveBuddiesSpinner() {
        haveSpinner = findViewById(R.id.spinnerBuddyHave);

        // Retrieve study buddies of the logged in user from SharedPreferences
        List<String> currbuddies = currUser.getStudyBuddies();

        System.out.println("CURR BUDDIES: " + currbuddies);

        if (currbuddies.size() < 1) {
            Toast.makeText(this, "No study buddies to trade for.", Toast.LENGTH_SHORT).show();
        } else {
            // Create an ArrayAdapter to populate the Spinner with the study buddies data
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currbuddies);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            haveSpinner.setAdapter(adapter);
        }
    }

    // Method to check if the other user that is searched for exists
    public void setOtherUser(String username) {
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
                    System.out.println("HERE4");

                    while (in.hasNext()) {
                        String response = in.nextLine();
                        JSONArray ja = new JSONArray(response);
                        System.out.println("HERE5");

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = (JSONObject) ja.get(i);
                            String user = jo.getString("username");
                            System.out.println("USER FROM DB: " + user);
                            System.out.println("USER ENTERED UN: " + username);

                            if (user.equals(username)) {
                                CurrentUser current = new CurrentUser();
                                otherUser = current.getUserFromDB(jo);
                            }
                        }
                    }
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get the StudyBuddies of the other user (that was searched for)
    public void wantBuddiesSpinner() {
        String enteredEmail = username.getText().toString().trim();
        getSpinner= findViewById(R.id.spinnerBuddyWant);

        setOtherUser(enteredEmail);

        try {
            CurrentUser currUser = new CurrentUser();

            // Retrieve study buddies of the other user from SharedPreferences
            List<String> otherBuddies = otherUser.getStudyBuddies();

            if (otherBuddies.size() < 1) {  // Checks if there are no StudyBuddies
                Toast.makeText(this, "No study buddies to get.", Toast.LENGTH_SHORT).show();
            } else {
                // Create an ArrayAdapter to populate the Spinner with the study buddies data
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, otherBuddies);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                getSpinner.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for selecting a StudyBuddy item from the spinners and
    // displaying the selected StudyBuddy items' images/characters
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerBuddyHave:
                String selectedHaveItem = parent.getItemAtPosition(position).toString();
                displayHaveImage(selectedHaveItem);
                break;
            case R.id.spinnerBuddyWant:
                String selectedGetItem = parent.getItemAtPosition(position).toString();
                displayWantImage(selectedGetItem);
                break;
        }
    }

    // Method for nothing being selected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Remove spinner listeners
        haveSpinner.setOnItemSelectedListener(null);
        getSpinner.setOnItemSelectedListener(null);

        // Handle when nothing is selected in the spinners
        // Get reference to the ImageView
        ImageView buddyHaveImage = findViewById(R.id.buddyHaveImageView);
        // Set the corresponding image
        buddyHaveImage.setImageResource(R.drawable.buddy_have);
        // Sets image in center of frame
        buddyHaveImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        // Get reference to the ImageView
        ImageView buddyWantImage = findViewById(R.id.buddyWantImageView);
        // Set the corresponding image
        buddyWantImage.setImageResource(R.drawable.buddy_want);
        // Sets image in center of frame
        buddyWantImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    // Method for searching for another user
    public void onFindButtonClick(View v) {
        String enteredEmail = username.getText().toString().trim();
        CurrentUser current = new CurrentUser();
        
        if ((enteredEmail.isEmpty()) || (!current.isUserValid(enteredEmail))) {
            Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
        } else {
            populateSpinners();
        }
    }

    // Method for requesting a trade of StudyBuddies
    public void onRequestButtonClick(View v) {
        String enteredEmail = username.getText().toString().trim();
        CurrentUser current = new CurrentUser();

        if (!current.isUserValid(enteredEmail)) {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
        } else {
            // Get the selected item from the "Have" Spinner
            String selectedHaveItem = haveSpinner.getSelectedItem().toString();

            // Get the selected item from the "Want" Spinner
            String selectedWantItem = getSpinner.getSelectedItem().toString();

            // Handling the study buddy the logged in user wants
            currUser.addStudyBuddy(selectedWantItem);
            otherUser.removeStudyBuddy(selectedWantItem);

            // Handling the study buddy the logged in user has/trades for
            otherUser.addStudyBuddy(selectedHaveItem);
            currUser.removeStudyBuddy(selectedHaveItem);

            current.saveUserData(currUser);
            current.saveUserData(otherUser);

            Toast.makeText(this, "Trade made!", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to display the corresponding image for the Study Buddy the user has
    private void displayHaveImage(String word) {
        int index = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            // Get reference to the ImageView
            ImageView buddyImage = findViewById(R.id.buddyHaveImageView);
            // Set the corresponding image
            buddyImage.setImageResource(imageResourcesBackdrop[index]);
            // Sets image in center of frame
            buddyImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    // Method to display the corresponding image for the Study Buddy the user wants
    private void displayWantImage(String word) {
        int index = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            // Get reference to the ImageView
            ImageView buddyImage = findViewById(R.id.buddyWantImageView);
            // Set the corresponding image
            buddyImage.setImageResource(imageResourcesBackdrop[index]);
            // Sets image in center of frame
            buddyImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
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

    // On click, leads to Inventory page
    public void onInventoryButtonClick(View v) {
        Intent i = new Intent(this, Inventory.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, INVENTORY_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Store page
    public void onStoreButtonClick(View v) {
        Intent i = new Intent(this, RandomStore.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, STORE_ID);
        setResult(RESULT_OK);
        finish();
    }

    // On click, leads to Trade page
    public void onTradeButtonClick(View v) {
        Intent i = new Intent(this, Trade.class);

        // Pass intent to activity, using specified request code
        startActivityForResult(i, TRADE_ID);
        setResult(RESULT_OK);
        finish();
    }
}
