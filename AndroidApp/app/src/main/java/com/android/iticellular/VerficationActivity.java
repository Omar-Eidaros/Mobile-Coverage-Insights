package com.android.iticellular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerficationActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText Code;
    private Button btn_Verify;
    SharedPreferences userStatus;
    SharedPreferences.Editor editor;
    private ApplicationActivity VerficationActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        super.onCreate(savedInstanceState);
        userStatus = getSharedPreferences("placeHolder", 0);
        editor = userStatus.edit();

        setContentView(R.layout.activity_verfication);

        Code = (EditText) findViewById(R.id.Code);

        btn_Verify = (Button) findViewById(R.id.btn_Verify);

        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Code.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please Enter Code",
                            Toast.LENGTH_LONG).show();
                } else {
                    String data = Code.getText().toString();
                    Submit(data);
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(VerficationActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, 3);

                    switchActivities();
                }
            }
        });
    }

    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, VerficationActivity.class);
        startActivity(switchActivityIntent);
    }


    private void SwitchToApp() {
        Intent switchActivityIntent = new Intent(this, ApplicationActivity.class);
        startActivity(switchActivityIntent);
    }


    private void Submit(String data) {
        final String savedata = data;
        Bundle phoneNum = getIntent().getExtras();
        final String phone = phoneNum.getString("phone");
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://192.168.1.2:8080/RF_insight/api/verification/isCodeValid";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("msisdn", phone);
            jsonBody.put("verifCode", savedata);
            Log.i("*******************", phone);
            Log.i("*******************", savedata);
            final String requestBody = jsonBody.toString();
            Log.i("*******************", requestBody);

            // Make request for JSONObject
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("++--**", response.toString());
                            String verifStatus = "";
                            try {
                                verifStatus = response.getString("verified");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (verifStatus.equals("true")) {
                                Log.i("------------------", "from If Block");
                                setDefaults("isUserLoggedIn", true, getApplicationContext());
                                SwitchToApp();

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Invalid Code",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("++++++++++++++++", "Error: " + error.getMessage());
                }
            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(jsonObjReq);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setDefaults(String key, Boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}