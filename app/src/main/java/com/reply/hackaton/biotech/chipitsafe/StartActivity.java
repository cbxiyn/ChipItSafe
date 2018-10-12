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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.movesense.mds.*;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class StartActivity extends AppCompatActivity
        implements MdsConnectionListener {



    // UI

    private static final String TAG = "StartActivity";
    private HeartRateManager heartManager;
    BluetoothDevice selectedDevice = null;
    HashMap<String,Fragment> fragmentHashMap = new HashMap<String,Fragment>();
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);

        Fragment f = fragmentHashMap.get(HealtStateFragment.TAG);
        if(f == null){
            f = HealtStateFragment.newInstance();
            fragmentHashMap.put(HealtStateFragment.TAG,f);
        }
        selectedFragment = f;

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.navigation_healt_state:
                                Fragment f = fragmentHashMap.get(HealtStateFragment.TAG);
                                if(f == null){
                                    f = HealtStateFragment.newInstance();
                                    fragmentHashMap.put(HealtStateFragment.TAG,f);
                                }
                                selectedFragment = f;
                                break;
                            case R.id.navigation_emergency:
                                Fragment fr = fragmentHashMap.get(EmergencyFragment.TAG);
                                if(fr == null){
                                    fr = EmergencyFragment.newInstance();
                                    fragmentHashMap.put(EmergencyFragment.TAG,fr);
                                }
                                selectedFragment = fr;
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
        transaction.replace(R.id.container, selectedFragment);
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

        heartManager = HeartRateManager.instanceOfHeartRateManager();
        heartManager.initMds(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!heartManager.isConnectedToDevice){

            BluetoothDevice dev = heartManager.getDeviceAttemptingToConnectTo();
            Toast.makeText(this, "attempting to connect to "+ dev.getName(), Toast.LENGTH_SHORT).show();

            heartManager.connectToDevice(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        heartManager.unsubscribeAll();
    }

    // MDS CONNECTION LISTENER METHODS
    /**
     * Called when Mds / Whiteboard link-layer connection (BLE) has been succesfully established
     *
     */
    @Override
    public void onConnect(String s) {
        Log.d(TAG, "onConnect:" + s);

    }

    /**
     * Called when the full Mds / Whiteboard connection has been succesfully established
     *
     */
    @Override
    public void onConnectionComplete(String macAddress, String serial) {
        Log.d(TAG, "onConnectionComplete:devSerial" + serial);



        Toast.makeText(this, "CONNECTED!", Toast.LENGTH_SHORT).show();
        heartManager.confirmHeartRateDevice();
        heartManager.setHeartRateDeviceSerial(serial);
        heartManager.isConnectedToDevice = true;

        if(selectedFragment instanceof HealtStateFragment){
            ((HealtStateFragment)selectedFragment).startDisplayingContents();
            ((HealtStateFragment)selectedFragment).getProgressBar().setVisibility(View.GONE); // to hide
        }

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
        Log.e(TAG, "onError:" + e);

        //showConnectionError(e);
    }

    /**
     * Called when Mds connection disconnects (e.g. device out of range)
     *
     */
    @Override
    public void onDisconnect(String bleAddress) {
        Log.d(TAG, "onDisconnect: " + bleAddress);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //HealtStateFragment.newInstance().handleIntent(intent);
    }

    private Boolean isIrregularHeartBeat(List<Integer> list){
        int max = Collections.max(list);
        int distance;
        int prevDistance = -1;
        int firstMaxPosition = -1;


        for(int i=0; i<list.size(); i++){
            if (list.get(i) == max) {
                if (firstMaxPosition == -1){
                    firstMaxPosition = i;
                } else {
                    distance = i - firstMaxPosition;
                    firstMaxPosition = -1;
                    if(prevDistance == -1){
                        prevDistance = distance;
                    } else if (prevDistance != distance ){
                        return false;
                    }
                }

            }

        }

        return true;
    }

}
