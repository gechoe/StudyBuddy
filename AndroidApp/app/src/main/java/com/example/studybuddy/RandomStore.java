package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RandomStore extends AppCompatActivity {
    int MENU_ID = 10, SETTINGS_ID = 11, INVENTORY_ID = 6, STORE_ID = 7, TRADE_ID = 8;
    private StudyBuddies sb = new StudyBuddies();
    private Map<Double, String> rarity = sb.getRarity();
    double[] likelihoods = sb.getLikelihoods();
    private String[] words = sb.getWords();
    int[] imageResources = sb.getImageResources();
    private double chosenBuddy = -1;
    private TextView totalCredits;
    private int points, studyBuddyPrice = 1;
    private User currUser;

    // Shop page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_store);

        // Gets current user
        CurrentUser current = new CurrentUser();
        currUser = current.getCurrentUser();

        // Initialize totalCredits TextView to be connected to xml text
        totalCredits = findViewById(R.id.pointsHeader);

        // Retrieve the current user's points from db
        points = currUser.getPoints();

        // Convert the points integer to a string before setting it as the text
        String pointsString = String.valueOf(points);

        updatePointsHeader(pointsString);

        // Get reference to the Random Button
        Button randomButton = findViewById(R.id.randomButton);

        // Set OnClickListener to the Random Button
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyStudyBuddy();
            }
        });
    }

    // Method to update the pointsHeader TextView with the current user's points
    private void updatePointsHeader(String updatedPoints) {
        totalCredits.setText("Total Credits: " + updatedPoints);
    }

    // Method to handle buying a study buddy
    private void buyStudyBuddy() {
        // Check if the user has enough points to buy the study buddy
        if (points >= studyBuddyPrice) {
            // Generate a random number between 0 and 1
            double random = Math.random();
            // Select a word based on likelihood percentages
            String selectedWord = selectWord(random);
            // Adding the StudyBuddy that was gotten/selected to the user's inventory
            currUser.addStudyBuddy(selectedWord);
            String rarityLevel = rarity.get(chosenBuddy);
            String toastString = "You got a: " + selectedWord + " (" + rarityLevel + ")";
            // Display the selected word
            Toast.makeText(RandomStore.this, toastString, Toast.LENGTH_LONG).show();
            // Display the corresponding image
            displayImage(selectedWord);

            // Deduct the price of the study buddy from the user's points
            points -= studyBuddyPrice;
            // Convert the points integer to a string before setting it as the text
            String pointsString = String.valueOf(points);
            // Update the pointsHeader TextView with the updated points
            updatePointsHeader(pointsString);
            // Save the updated points to db
            currUser.setPoints(points);

            // Update the user's points and study buddies in database
            CurrentUser current = new CurrentUser();
            System.out.println("HEYo");
            current.saveUserData(currUser);
            System.out.println("WOAH");
        } else {
            // Display a message indicating that the user doesn't have enough points
            Toast.makeText(this, "Need more credits to buy a study buddy.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to select a word (aka the StudyBuddy type) based on likelihood
    // or StudyBuddy rarity percentages
    private String selectWord(double random) {
        double cumulativeProbability = 0.0;
        for (int i = 0; i < likelihoods.length; i++) {
            cumulativeProbability += likelihoods[i];

            if (random < cumulativeProbability) {
                chosenBuddy = likelihoods[i];
                return words[i];
            }
        }

        // If for some reason the random number falls out of the cumulative range,
        // return the last word in the list
        return words[words.length - 1];
    }

    // Method to display the corresponding StudyBuddy image for the selected
    // StudyBuddy word
    private void displayImage(String word) {
        int index = -1;

        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            // Get reference to the ImageView
            ImageView buddyImage = findViewById(R.id.buddyImageView);
            // Set the corresponding image
            buddyImage.setImageResource(imageResources[index]);
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