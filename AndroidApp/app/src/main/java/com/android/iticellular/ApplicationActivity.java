package com.android.iticellular;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    private static String checkCellType(CellInfo cellInfo) {

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
}