package com.example.yash.intellih_alpha1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context appContext;
    String url = "https://api.thingspeak.com/update?api_key=";

    public FingerprintHandler(Context context) {
        appContext = context;
    }

    public void startAuth(FingerprintManager manager,
                          FingerprintManager.CryptoObject
                                  cryptoObject) {
        cancellationSignal = new CancellationSignal();

        if (ActivityCompat.checkSelfPermission
                (appContext,
                        Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError
            (int errorCode, CharSequence errString) {
        Toast.makeText(appContext, "" +
                        "Authentication Error\n" + errString,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAuthenticationHelp(int helpCode,
                                     CharSequence helpString) {
        Toast.makeText(appContext, "" +
                        "Authentication Help\n" + helpString,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(appContext, "" +
                        "Authentication Failed.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        Toast.makeText(appContext, "" +
                        "Authentication Successful.",
                Toast.LENGTH_SHORT).show();
        url = url + "JTH561P9F52ON96D" + "&field1=1" ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FINGERPRINT", "Something went Wrong!");
                return;
            }
        });
        MySingleton.getInstance(appContext).addToQueue(stringRequest);
    }

}
