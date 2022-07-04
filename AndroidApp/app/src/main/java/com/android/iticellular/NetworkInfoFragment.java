package com.android.iticellular;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.telecom.TelecomManager;
import android.telephony.CellInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class NetworkInfoFragment extends Fragment {

    static int firstSIMSignalStrength;
    static int secondSIMSignalStrength;

    TextView firstSIMNetworkData = null;
    TextView secondSIMNetworkData = null;
    TextView firstSIMSignalStrengthTextView = null;
    TextView secondSIMSignalStrengthTextView = null;
    ProgressBar firstSIMSignalProgress = null;
    ProgressBar secondSIMSignalProgress = null;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network,container,false);

        firstSIMNetworkData = view.findViewById(R.id.first_network_text);
        secondSIMNetworkData = view.findViewById(R.id.second_network_text);
        firstSIMSignalProgress = view.findViewById(R.id.first_signal_progress);
        secondSIMSignalProgress = view.findViewById(R.id.second_signal_progress);
        firstSIMSignalStrengthTextView = view.findViewById(R.id.first_signal_strength);
        secondSIMSignalStrengthTextView = view.findViewById(R.id.second_signal_strength);
        displayData();
        return view;
    }

    @SuppressLint("Missing Permission")
    @RequiresApi( api = Build.VERSION_CODES.O)
    public void displayData(){

        ApplicationActivity.getSlotCellInfo(0);
        firstSIMSignalProgress.setMin(-120);
        firstSIMSignalProgress.setMax(-24);
        firstSIMSignalStrength = ApplicationActivity.signalStrength;
        firstSIMSignalProgress.setProgress(firstSIMSignalStrength);
        firstSIMSignalStrengthTextView.setText(firstSIMSignalStrength + "dbm");
        try {
            ApplicationActivity.firstSIM.put("signal_strength_level", firstSIMSignalStrength);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            firstSIMSignalProgress.setVisibility(View.GONE);
            firstSIMSignalStrengthTextView.setText("No SIM Card");
        }

        ApplicationActivity.getSlotCellInfo(1);
        secondSIMSignalProgress.setMin(-120);
        secondSIMSignalProgress.setMax(-24);
        secondSIMSignalStrength = ApplicationActivity.signalStrength;
        secondSIMSignalProgress.setProgress(secondSIMSignalStrength);
        secondSIMSignalStrengthTextView.setText(secondSIMSignalStrength + "dbm");
        try {
            ApplicationActivity.secondSIM.put("signal_strength_level", secondSIMSignalStrength);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            secondSIMSignalProgress.setVisibility(View.GONE);
            secondSIMSignalStrengthTextView.setText("No SIM Card");
        }

        if(ApplicationActivity.firstNetworkInformation.equals("")){
            firstSIMNetworkData.setText("No SIM Card");
        } else {
            firstSIMNetworkData.setText(ApplicationActivity.firstNetworkInformation);
        }

        if(ApplicationActivity.secondNetworkInformation.equals("")){
            secondSIMNetworkData.setText("No SIM Card");
        } else {
            secondSIMNetworkData.setText(ApplicationActivity.secondNetworkInformation);
        }

        refresh(100);
    }

    public void refresh(int milliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                displayData();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

}