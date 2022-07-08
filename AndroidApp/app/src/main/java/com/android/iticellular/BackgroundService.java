package com.android.iticellular;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.json.JSONException;

public class BackgroundService extends Service {

    @Override
    public int  onStartCommand(Intent intent, int flag, int startID){

        try {
            if(ApplicationActivity.firstSIM.get("imsi") != null){
                new CallAPI().execute("http://192.168.94.174:8080/RF_insight/api/DML/post", ApplicationActivity.firstSIM.toString());
            }

            if(ApplicationActivity.secondSIM.get("imsi") != null){
                new CallAPI().execute("http://192.168.94.174:8080/RF_insight/api/DML/post", ApplicationActivity.secondSIM.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        refresh(2000 * 60);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void refresh(int milliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                startService(new Intent(getApplicationContext(),BackgroundService.class));
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }
}