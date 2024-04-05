package com.example.firstresponse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.SimpleEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.google.gson.Gson;

import java.time.format.DateTimeFormatter;

public class coin_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_page);

        Intent intent = getIntent();
        TextView t1 = findViewById(R.id.text);
        ImageView i1 = findViewById(R.id.imageView);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                String value="";
                if (intent.hasExtra("uuid")) { value = intent.getStringExtra("uuid");}


                Request request = new Request.Builder()
                        .url("https://coinranking1.p.rapidapi.com/coin/"+value+"/history?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h")
                        .get()
                        .addHeader("X-RapidAPI-Key", "2b72cdb93fmsh84c52cd5f5f4c40p1e4d89jsn181671185d28")
                        .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    ObjectMapper objectMapper = new ObjectMapper();
                    String js = response.body().string();
                    final coin_data entity = objectMapper.readValue(Objects.requireNonNull(js), coin_data.class);
                    Log.d("json data string ",js);
                    response.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            AnyChartView anyChartView = findViewById(R.id.any_chart_view);

                            Cartesian line = AnyChart.line();

                            List<DataEntry> seriesData = new ArrayList<>();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
                            int si =  entity.getData().getHistory().size();


                            for (int i = si-1; i>=0; i--) {
                                try {
                                    double price = Double.parseDouble(entity.getData().getHistory().get(i).getPrice());
                                    Date date = new Date(entity.getData().getHistory().get(i).getTimestamp() * 1000L); // convert timestamp to Date
                                    String formattedDate = sdf.format(date);
//                                    Log.d("pricesss",entity.getData().getHistory().get(i).getPrice()+"    "+formattedDate);
                                    seriesData.add(new ValueDataEntry(formattedDate, price));
                                } catch (NumberFormatException e) {
                                    System.out.println("Error parsing number: " + e.getMessage());
                                }
                            }

                            line.data(seriesData);

                            com.anychart.scales.Linear yAxis = com.anychart.scales.Linear.instantiate();
                            line.yScale(yAxis);
                            anyChartView.setChart(line);

                            Glide.with(getApplicationContext())
                                    .load("https://github.com/Pymmdrza/Cryptocurrency_Logos/blob/mainx/PNG/"+getIntent().getStringExtra("symbol")+".png?raw=true")
                                    .placeholder(R.drawable.crypto)
                                    .error(R.drawable.crypto)
                                    .into(i1);
                            t1.setText(getIntent().getStringExtra("name"));
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


    }
}