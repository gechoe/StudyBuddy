package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class Shop extends AppCompatActivity {
    int MENU_ID = 10, SETTINGS_ID = 11, INVENTORY_ID = 6, STORE_ID = 7, TRADE_ID = 8;

    // Shop page set up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);
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
