package com.android.iticellular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private Button submit_Btn;
    private static String data;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        checkAndRequestPermissions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean userStatus = getDefaults("isUserLoggedIn", getApplicationContext());
        if (userStatus.equals(true)){
            switchToAppActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Enter your number", Toast.LENGTH_LONG).show();
        }

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        submit_Btn = (Button) findViewById(R.id.submit_btn);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String phoneVerify=preferences.getString("phoneNumber","");
        Log.e("phone number",phoneVerify);
        if(!phoneVerify.equals("")){
            SwitchToApp();
        }

        submit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Number", Toast.LENGTH_LONG).show();
                } else {
                    data = phoneNumber.getText().toString();
                    setDefaults("phoneNumber", data, getApplicationContext());
                    submit(data);
                    switchToVerifyActivity();
                }
            }
        });
    }

    private void SwitchToApp() {
        Intent switchActivityIntent = new Intent(this, ApplicationActivity.class);
        startActivity(switchActivityIntent);
    }

    public static Boolean getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // Make Application Ask Permissions for location and so on ...
    private void checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    // Switching between Activities
    private void switchToVerifyActivity() {
        Intent switchActivityIntent = new Intent(this, com.android.iticellular.VerficationActivity.class);
        switchActivityIntent.putExtra("phoneNum", phoneNumber.getText().toString());
        startActivity(switchActivityIntent);
    }

    private void switchToAppActivity() {
        Intent switchActivityIntent = new Intent(this, com.android.iticellular.VerficationActivity.class);
        startActivity(switchActivityIntent);
    }

    // Handle Submit Button and Request Verify Code if not login before
    private void submit(String data) {

        final String saveData = data;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://192.168.94.174:8080/RF_insight/api/verification/sendCode";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msisdn", saveData);
            final String requestBody = jsonObject.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY succ", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("VOLLEY error", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                    String responseString = "";
                    if (networkResponse != null) {
                        responseString = String.valueOf(networkResponse.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(networkResponse));
                }
            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}