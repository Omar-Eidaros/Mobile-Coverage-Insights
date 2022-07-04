package com.android.iticellular;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    static JSONObject firstSIM = new JSONObject();
    static JSONObject secondSIM = new JSONObject();
    static String networkInfo = null;
    static String IMSINumber = null;
    static String IMEINumber = null;
    static String isDeviceRoaming = null;
    static String firstSIMInfo = null;
    static String secondSIMInfo = null;
    static String networkType = null;
    static int cellID = 0;
    static double longitude;
    static double latitude;
    boolean isRoaming = true;
    static int MNC = 0;
    static int MCC = 0;
    static LocationManager locationManager;
    static int signalStrength = 0;
    static String mobilePhoneInformation = null;
    static String strPhoneType = "";
    static String firstNetworkInformation = "";
    static String secondNetworkInformation = "";
    static int firstSignalStrength;
    static int secondSignalStrength;
    static SubscriptionManager subscriptionManager;
    static TelephonyManager telephonyManager = null;
    TelephonyManager subscriptionInfo = null;
    SubscriptionManager sm = null;
    String softwareVersion = null;
    String networkCountryISO = null;
    String SIMCountryISO = null;
    String msisdn = null;
    Toolbar toolbar = null;
    List<CellInfo> cellInfo = new ArrayList<>();
    String simInfo = null;
    String deviceInfo = null;
    String operator = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        msisdn = getDefaults("phoneNumber", getApplicationContext());
        try {
            firstSIM.put("msisdn", msisdn);
            secondSIM.put("msisdn", msisdn);
        } catch (JSONException e){
            e.printStackTrace();
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        sm = (SubscriptionManager) getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE);
        subscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("RF Insight App");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout , toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NetworkInfoFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_network);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        } else {
            requestLocationUpdate();
        }

        try {
            setParameters(telephonyManager,sm,0);
            setDeviceParameters();
            getSlotCellInfo(0);
            firstSignalStrength = signalStrength;
            firstSIMInfo = setSIMInfo();
            firstNetworkInformation = setNetworkInfo();
            firstSIM.put("operator", operator);
            firstSIM.put("country", networkCountryISO);
            firstSIM.put("device_model", Build.MANUFACTURER);
            firstSIM.put("imei", IMEINumber);
            firstSIM.put("imsi", IMSINumber);
            firstSIM.put("cell_type", networkType);
            firstSIM.put("cell_id", cellID);
            firstSIM.put("mnc",MNC);
            firstSIM.put("mcc",MCC);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            firstSIMInfo = "You don't have SIM card in the first slot!";
        }

        try {
            setParameters(telephonyManager,sm,1);
            setDeviceParameters();
            getSlotCellInfo(1);
            secondSignalStrength = signalStrength;
            secondSIMInfo = setSIMInfo();
            secondNetworkInformation = setNetworkInfo();
            secondSIM.put("operator", operator);
            secondSIM.put("country", networkCountryISO);
            secondSIM.put("device_model", Build.MANUFACTURER);
            secondSIM.put("imei", IMEINumber);
            secondSIM.put("imsi", IMSINumber);
            secondSIM.put("cell_type", networkType);
            secondSIM.put("cell_id", cellID);
            secondSIM.put("mnc",MNC);
            secondSIM.put("mcc",MCC);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            firstSIMInfo = "You don't have SIM card in the second slot!";
        }

        getPhoneType();
        setDeviceParameters();
        mobilePhoneInformation = setDeviceInfo();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static CellInfo getSlotCellInfo(int slotIndex) {

        @SuppressLint("MissingPermission")
        ArrayList<CellInfo> allCellInfo = new ArrayList<>(telephonyManager.getAllCellInfo());

        @SuppressLint("MissingPermission")
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        SubscriptionInfo subscriptionInfo = null;

        for (int i = 0; i < activeSubscriptionInfoList.size(); i++) {
            SubscriptionInfo temp = activeSubscriptionInfoList.get(i);
            if (temp.getSimSlotIndex() == slotIndex) {
                subscriptionInfo = temp;
                break;
            }
        }
        for (int index = 0; index < allCellInfo.size(); index++) {
            CellInfo temp = allCellInfo.get(index);
            String cellType = checkCellType(temp);
            signalStrength = getSignalStrengthLevel(temp);

            if (cellType == "GSM") {
                CellIdentityGsm identity = (((CellInfoGsm) temp).getCellIdentity());
                MNC = identity.getMnc();
                MCC = identity.getMcc();
                cellID = identity.getCid();
            } else if (cellType == "W-CDMA") {
                CellIdentityWcdma identity = (((CellInfoWcdma) temp).getCellIdentity());
                MNC = identity.getMnc();
                MCC = identity.getMcc();
                cellID = identity.getCid();
            } else if (cellType == "LTE") {
                CellIdentityLte identity = (((CellInfoLte) temp).getCellIdentity());
                MNC = identity.getMnc();
                MCC = identity.getMcc();
                cellID = identity.getCi();
            }

            if (MNC == subscriptionInfo.getMnc()) {
                return temp;
            }
        }
        return null;
    }

    public static String checkCellType(CellInfo cellInfo) {

        if (cellInfo instanceof CellInfoWcdma) {
            networkType = "W-CDMA";
            return "W-CDMA";
        }
        if (cellInfo instanceof CellInfoGsm) {
            networkType = "GSM";
            return "GSM";
        }
        if (cellInfo instanceof CellInfoLte) {
            networkType = "LTE";
            return "LTE";
        }
        return null;
    }

    public static int getSignalStrengthLevel(CellInfo cellInfo) {

        if (cellInfo instanceof CellInfoWcdma) {
            return ((CellInfoWcdma) cellInfo).getCellSignalStrength().getDbm();
        }
        if (cellInfo instanceof CellInfoGsm) {
            return ((CellInfoGsm) cellInfo).getCellSignalStrength().getDbm();
        }
        if (cellInfo instanceof CellInfoLte) {
            return ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
        }
        return -1;
    }

    public static String getDefaults(String key, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key,"");
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdate() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000 * 60, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                try {
                    firstSIM.put("longitude", longitude);
                    firstSIM.put("latitude", latitude);
                    secondSIM.put("longitude", longitude);
                    secondSIM.put("latitude", latitude);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                startService(new Intent(getApplicationContext(), BackgroundService.class));
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(@NonNull String provider) {}
            @Override
            public void onProviderDisabled(@NonNull String provider) {}
        });
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    public void setParameters(TelephonyManager tm, SubscriptionManager sm, int simSlot) {

        subscriptionInfo = tm.createForSubscriptionId(sm.getActiveSubscriptionInfoForSimSlotIndex(simSlot).getSubscriptionId());
        cellInfo = subscriptionInfo.getAllCellInfo();
        try {
            IMSINumber = subscriptionInfo.getSubscriberId();
        } catch (SecurityException ex) {
            IMSINumber = "Blocked By Google for Android 10+";
        }
        operator = subscriptionInfo.getSimOperatorName();
        networkCountryISO = subscriptionInfo.getNetworkCountryIso();
        SIMCountryISO = subscriptionInfo.getSimCountryIso();
        softwareVersion = subscriptionInfo.getDeviceSoftwareVersion();
        isRoaming = tm.isNetworkRoaming();
        isDeviceRoaming = isRoaming ? "YES" : "NO";
    }

    public void setDeviceParameters() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IMEINumber = telephonyManager.getImei();
            }
        } catch (SecurityException ex) {
            IMEINumber = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    public String setSIMInfo() {
        simInfo = "";
        simInfo += "\n operator: " + operator;
        simInfo += "\n Network Country ISO: " + networkCountryISO;
        simInfo += "\n Software Version: " + softwareVersion;
        simInfo += "\n IMSI Number: " + IMSINumber;
        simInfo += "\n MNC: " + MNC;
        simInfo += "\n MCC: " + MCC;

        return simInfo;
    }

    public String setNetworkInfo() {
        networkInfo = "";
        networkInfo += "\nRoaming: " + isDeviceRoaming;
        networkInfo += "\nNetwork Type: " + networkType;
        return networkInfo;
    }

    public void getPhoneType() {
        int phoneType = telephonyManager.getPhoneType();

        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                strPhoneType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                strPhoneType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                strPhoneType = "SIP";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                strPhoneType = "NONE";
                break;
        }
    }

    public String setDeviceInfo() {
        deviceInfo = "device details:\n";
        deviceInfo += "\nDevice ID: " + IMEINumber;
        return deviceInfo;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}