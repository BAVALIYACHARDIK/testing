package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.firstresponse.coin_data;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class stock_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://financialmodelingprep.com/api/v3/stock/list?apikey=xdcD7pcIUEGaYCHgEwJXpS7wD2HZWjml")
                        .get()
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String js = response.body().string();
                    Log.d("json data string ", "hello");
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}