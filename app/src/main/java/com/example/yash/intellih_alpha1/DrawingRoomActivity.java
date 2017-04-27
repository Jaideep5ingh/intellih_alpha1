package com.example.yash.intellih_alpha1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DrawingRoomActivity extends AppCompatActivity {

    public static final String drawing_room_write_key = "ZPEK9N1MJOS6PU00";
    public static final String drawing_room_read_key = "RILPSQ2KY0Y1R0HF";
    public static final String drawing_room_channelId = "258518";

    String url = "https://api.thingspeak.com/update?api_key=";

    String last_field2, last_field1;
    String current_field1, current_field2;

    private final String DEBUG_TAG = "Room Activity";

    TextView roomName_textView;
    FloatingActionButton fab_light, fab_fan, switch_light, switch_fan, switch_settings, fab_refresh;

    View parentView;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        parentView = getLayoutInflater().inflate(R.layout.dialog, null);

        roomName_textView = (TextView) findViewById(R.id.room_name);
        roomName_textView.setText("Drawing Room");

        fab_light = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab_fan = (FloatingActionButton) findViewById(R.id.floatingActionButton4);
        switch_light = (FloatingActionButton) parentView.findViewById(R.id.bulb_fab);
        switch_fan = (FloatingActionButton) parentView.findViewById(R.id.fan_fab);
        switch_settings = (FloatingActionButton) parentView.findViewById(R.id.setting_fab);
        fab_refresh = (FloatingActionButton) parentView.findViewById(R.id.refresh_fab);

        fab_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        fab_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        switch_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_field1 = last_field1.equalsIgnoreCase("0") ?"1":"0";
                url = url + drawing_room_write_key + "&field1=" + current_field1 + "&field2=" + last_field2;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch(last_field1){
                            case "1" : switch_light.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                                break;
                            case "0" : switch_light.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FINGERPRINT", "Something went Wrong!");
                        return;
                    }
                });
                MySingleton.getInstance(DrawingRoomActivity.this).addToQueue(stringRequest);
            }
        });

        switch_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_field2 = last_field2.equalsIgnoreCase("0") ?"1":"0";
                url = url + drawing_room_write_key + "&field1=" + last_field1 + "&field2=" + (current_field2);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch(last_field2){
                            case "1" : switch_fan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                                break;
                            case "0" : switch_fan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DR_SEND_DATA", "Something went Wrong!");
                        return;
                    }
                });
                MySingleton.getInstance(getApplicationContext()).addToQueue(stringRequest);
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "Action was UP");
                showBottomSheet();
                Toast.makeText(this, "Action was UP", Toast.LENGTH_SHORT).show();
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void showBottomSheet() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(DrawingRoomActivity.this);
            bottomSheetDialog.setContentView(parentView);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
            bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
            bottomSheetDialog.show();
        } else
            bottomSheetDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String json_url = "http://api.thingspeak.com/channels/" + drawing_room_channelId + "/feeds/last.json?api_key=" + drawing_room_read_key;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("READ", "GOT RESPONSE");
                        if (response == null) {
                            Log.d("READ", "EMPTY RESPONSE");
                        } else {

                            try {
                                last_field1 = response.getString("field1");
                                last_field2 = response.getString("field2");
                                Log.d("READ", "field1 = " + last_field1);
                                Log.d("READ", "field1 = " + last_field2);
                                switch(last_field1){
                                    case "1" : switch_light.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                                        break;
                                    case "0" : switch_light.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                                        break;
                                }
                                switch(last_field2){
                                    case "1" : switch_fan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                                        break;
                                    case "0" : switch_fan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getApplicationContext()).addToQueue(jsonObjectRequest);
    }
}
