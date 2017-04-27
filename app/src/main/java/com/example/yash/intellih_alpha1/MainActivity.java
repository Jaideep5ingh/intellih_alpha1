package com.example.yash.intellih_alpha1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    FloatingActionButton unlockDoor;

    TextView temperature_textview, humidity_textview;
    BroadcastReceiver br;
    Intent i;


    public static final String bed_room_write_key = "H2W6DCHAD4ZC1M4Q";
    public static final String bed_room_channelId = "258519";

    public static final String pir_write_key = "QTRZFCDL7THTP7MN";
    public static final String pir_read_key = "NPRVLFYHL74TFMY2";
    public static final String pir_channelId = "258900";

    public static final String keyless_write_key = "JTH561P9F52ON96D";
    public static final String keyless_read_key = "RQ18BRJNJ7XS89UT";
    public static final String keyless_channelId = "262765";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperature_textview = (TextView) findViewById(R.id.temperature_textview);
        humidity_textview = (TextView) findViewById(R.id.humidity_textview);
        unlockDoor = (FloatingActionButton) findViewById(R.id.unlock);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();
        addData();
        adapter = new CustomAdapter(getApplicationContext(), data);
        recyclerView.setAdapter(adapter);

        unlockDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FingerprintActivity.class));
            }
        });

    }

    void addData() {
        data.add(new DataModel("Bed Room", R.drawable.bed__2_));
        data.add(new DataModel("Drawing Room", R.drawable.sofa__2_));
        data.add(new DataModel("Kids Room", R.drawable.baby));
        data.add(new DataModel("Kitchen", R.drawable.kitchen));
    }

    @Override
    protected void onResume() {
        super.onResume();

        i = new Intent(getApplicationContext(), ReadDataService.class);
        startService(i);


        if (br == null) {
            br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("READ", "data received");
                    String temp = intent.getStringExtra("field1");
                    temperature_textview.setText(temp + "°C");
                    humidity_textview.append("\t" + intent.getStringExtra("field2") + "%");
                    if (intent.getExtras().getBoolean("pir_theft")) {
                        Toast.makeText(context, "Intruder Detected in Drawing Room", Toast.LENGTH_LONG).show();
                    }
                    if (intent.getExtras().getBoolean("pir_out")) {
                        Toast.makeText(context, "User is present in Bed Room", Toast.LENGTH_LONG).show();
                    }
                }
            };
            registerReceiver(br, new IntentFilter("ReadData"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (i != null) {
            stopService(i);
        }
        if (br != null) {
            unregisterReceiver(br);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (i != null) {
            stopService(i);
        }
        if (br != null) {
            unregisterReceiver(br);
        }
    }
}
