package com.android.iticellular;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeviceInfoFragment extends Fragment {

    String deviceBrand;
    String deviceModel;
    String androidVersion;
    String kernalVersion;
    String sdkVersion;
    String cpuType;

    TextView deviceBrandTextView = null;
    TextView deviceModelTextView = null;
    TextView androidVersionTextView = null;
    TextView kernalVersionTextView = null;
    TextView sdkVersionTextView = null;
    TextView cpuTypeTextView = null;
    TextView IEMITextView = null;
    TextView phoneTypeTextView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device,container,false);

        deviceBrand = Build.MANUFACTURER;
        deviceModel = Build.MODEL;
        androidVersion = Build.VERSION.RELEASE;
        kernalVersion = System.getProperty("os.version");
        sdkVersion = Build.VERSION.SDK;
        cpuType = Build.CPU_ABI;


        deviceBrandTextView = view.findViewById(R.id.brand);
        deviceModelTextView = view.findViewById(R.id.model);
        androidVersionTextView = view.findViewById(R.id.version);
        kernalVersionTextView = view.findViewById(R.id.kernel);
        sdkVersionTextView = view.findViewById(R.id.sdk);
        cpuTypeTextView = view.findViewById(R.id.cpu);
        IEMITextView = view.findViewById(R.id.imei);
        phoneTypeTextView = view.findViewById(R.id.device_type);

        deviceBrandTextView.setText(deviceBrand);
        deviceModelTextView.setText(deviceModel);
        androidVersionTextView.setText(androidVersion);
        kernalVersionTextView.setText(kernalVersion);
        sdkVersionTextView.setText(sdkVersion);
        cpuTypeTextView.setText(cpuType);
        IEMITextView.setText(ApplicationActivity.IMEINumber);
        phoneTypeTextView.setText(ApplicationActivity.strPhoneType);

        return view;
    }
}