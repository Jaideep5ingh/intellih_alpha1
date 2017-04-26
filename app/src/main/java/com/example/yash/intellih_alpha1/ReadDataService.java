package com.example.yash.intellih_alpha1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YASH on 14-Apr-17.
 */

public class ReadDataService extends Service {
    String sensor_url, pir_url, temp, humid, light, gas, pir_theft, pir_out, pir_in;

    @Override
    public void onCreate() {
        super.onCreate();
        sensor_url = "http://api.thingspeak.com/channels/240516/feeds/last.json?api_key=MQ4X7JSNCON86NA6";
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, sensor_url,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("READ", "GOT RESPONSE");
                        if (response == null) {
                            Log.d("READ", "EMPTY RESPONSE");
                        } else {
                            try {
                                temp = response.getString("field1");
                                humid = response.getString("field2");
                                light = response.getString("field3");
                                gas = response.getString("field4");
                                Log.d("READ", "field1 = " + temp);
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
        MySingleton.getInstance(getApplicationContext()).addToQueue(jsonObjectRequest1);

        pir_url = "http://api.thingspeak.com/channels/240516/feeds/last.json?api_key=NPRVLFYHL74TFMY2";
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, sensor_url,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("READ", "GOT RESPONSE");
                        if (response == null) {
                            Log.d("READ", "EMPTY RESPONSE");
                        } else {

                            try {
                                pir_theft = response.getString("field1");
                                pir_out = response.getString("field2");
                                pir_in = response.getString("field3");
                                Log.d("READ_PIR", "field1 = " + pir_theft);

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
        MySingleton.getInstance(getApplicationContext()).addToQueue(jsonObjectRequest2);

        Intent i = new Intent("ReadData");
        i.putExtra("field1", temp);
        i.putExtra("field2", humid);
        i.putExtra("field3", light);
        i.putExtra("field4", gas);
        if (pir_theft.equalsIgnoreCase("1"))
            i.putExtra("pir_theft", true);
        else
            i.putExtra("pir_theft", false);

        if (pir_out.equalsIgnoreCase("1"))
            i.putExtra("pir_out", true);
        else
            i.putExtra("pir_out", false);

        sendBroadcast(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
