package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Inventory  extends AppCompatActivity {
    int INVENTORY_ID = 6, STORE_ID = 7, TRADE_ID = 8,  MENU_ID = 10, SETTINGS_ID = 11;
    private RecyclerView recyclerViewInventory;
    private List<String> studyBuddies;

    // Shop page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        recyclerViewInventory = findViewById(R.id.recyclerViewInventory);
        recyclerViewInventory.setLayoutManager(new LinearLayoutManager(this));

        CurrentUser current = new CurrentUser();
        User currUser = current.getCurrentUser();

        // Assuming you have a method to get the list of study buddies for the current user
        studyBuddies = currUser.getStudyBuddies();
        Set uniqueBuddies = new HashSet<>(studyBuddies);
        List uniqueBuddiesList = new ArrayList(uniqueBuddies);
        StudyBuddyAdapter adapter = new StudyBuddyAdapter(uniqueBuddiesList);
        recyclerViewInventory.setAdapter(adapter);
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