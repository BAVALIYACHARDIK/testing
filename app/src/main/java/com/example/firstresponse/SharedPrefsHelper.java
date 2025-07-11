package com.example.firstresponse;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefsHelper {
    private static final String SHARED_PREFS_NAME = "crypto_holdings";
    private static final Gson gson = new Gson();

    private SharedPreferences prefs;

    public SharedPrefsHelper(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
    }

    public void saveHoldings(List<CryptoHolding> holdings) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // Clear previous data (optional)

        for (int i = 0; i < holdings.size(); i++) {
            CryptoHolding holding = holdings.get(i);
            String holdingJson = gson.toJson(holding);
            editor.putString("holding_" + i, holdingJson);
        }

        editor.apply();
    }

    public List<CryptoHolding> loadHoldings() {
        List<CryptoHolding> holdings = new ArrayList<>();

        for (int i = 0; i < prefs.getAll().size(); i++) {
            String holdingJson = prefs.getString("holding_" + i, null);
            if (holdingJson != null) {
                CryptoHolding holding = gson.fromJson(holdingJson, CryptoHolding.class);
                holdings.add(holding);
            }
        }

        return holdings;
    }
}
