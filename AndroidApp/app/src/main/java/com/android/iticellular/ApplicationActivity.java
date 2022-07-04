package com.android.iticellular;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    static JSONObject firstSIM = new JSONObject();
    static JSONObject SecondSIM = new JSONObject();
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}