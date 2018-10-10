package com.reply.hackaton.biotech.chipitsafe;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.movesense.mds.MdsConnectionListener;
import com.movesense.mds.MdsException;


public class StartActivity extends AppCompatActivity
        implements MdsConnectionListener {

    // UI

    private String LOG_TAG = "StartActivity";
    private HeartRateManager heartManager;
    BluetoothDevice selectedDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_healt_state:
                                selectedFragment = HealtStateFragment.newInstance();
                                break;
                            case R.id.navigation_emergency:
                                selectedFragment = EmergencyFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, HealtStateFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

        heartManager = HeartRateManager.instanceOfHeartRateManager();
        heartManager.initMds(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothDevice dev = heartManager.getDeviceAttemptingToConnectTo();
        Toast.makeText(this, "attempting to connect to "+ dev.getName(), Toast.LENGTH_SHORT).show();

        heartManager.connectToDevice(this);
    }

    // MDS CONNECTION LISTENER METHODS
    /**
     * Called when Mds / Whiteboard link-layer connection (BLE) has been succesfully established
     *
     */
    @Override
    public void onConnect(String s) {
        Log.d(LOG_TAG, "onConnect:" + s);

    }

    /**
     * Called when the full Mds / Whiteboard connection has been succesfully established
     *
     */
    @Override
    public void onConnectionComplete(String macAddress, String serial) {
        Log.d(LOG_TAG, "onConnectionComplete:devSerial" + serial);

        heartManager.confirmHeartRateDevice();
        heartManager.setHeartRateDeviceSerial(serial);


                    /*
                    for (MyScanResult sr : mScanResArrayList) {
                        if (sr.macAddress.equalsIgnoreCase(macAddress)) {
                            sr.markConnected(serial);
                            break;
                        }
                    }
                    */
    }

    /**
     * Called when Mds connect() call fails with error
     *
     */
    @Override
    public void onError(MdsException e) {
        Log.e(LOG_TAG, "onError:" + e);

        //showConnectionError(e);
    }

    /**
     * Called when Mds connection disconnects (e.g. device out of range)
     *
     */
    @Override
    public void onDisconnect(String bleAddress) {
        Log.d(LOG_TAG, "onDisconnect: " + bleAddress);

    }
}
