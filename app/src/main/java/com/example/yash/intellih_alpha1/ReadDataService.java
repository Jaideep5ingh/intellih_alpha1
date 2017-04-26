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
    String sensor_url, pir_url, created_at, field1, field2;

    @Override
    public void onCreate() {
        super.onCreate();
        sensor_url = "http://api.thingspeak.com/channels/240516/feeds/last.json?api_key=MQ4X7JSNCON86NA6";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, sensor_url,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("READ", "GOT RESPONSE");
                        if (response == null){
                            Log.d("READ", "EMPTY RESPONSE");
                        } else {

                            try {
                                created_at = response.getString("created_at");
                                field1 = response.getString("field1");
                                field2 = response.getString("field2");
                                Log.d("READ", "field1 = "+field1);
                                Intent i = new Intent("ReadData");
                                i.putExtra("field1", field1);
                                i.putExtra("field2", field2);
                                sendBroadcast(i);

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
