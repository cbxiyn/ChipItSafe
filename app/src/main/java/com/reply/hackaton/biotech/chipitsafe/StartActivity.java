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

import com.movesense.mds.MdsConnectionListener;
import com.movesense.mds.MdsException;


public class StartActivity extends AppCompatActivity
        implements MdsConnectionListener {



    // UI
    private ProgressBar progressBar;

    private String LOG_TAG = "StartActivity";
    private HeartRateManager heartManager;
    BluetoothDevice selectedDevice = null;

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Create a progress bar to display while the list loads
        progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
        if(!heartManager.isConnectedToDevice){

            BluetoothDevice dev = heartManager.getDeviceAttemptingToConnectTo();
            Toast.makeText(this, "attempting to connect to "+ dev.getName(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE); //to show
            heartManager.connectToDevice(this);
        }
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


        progressBar.setVisibility(View.GONE); // to hide
        Toast.makeText(this, "CONNECTED!", Toast.LENGTH_SHORT).show();
        heartManager.confirmHeartRateDevice();
        heartManager.setHeartRateDeviceSerial(serial);
        heartManager.isConnectedToDevice = true;


        HealtStateFragment fragment = (HealtStateFragment) getSupportFragmentManager.findFragmentById(R.id.your_fragment);
        fragment.receive(sharedUrl);
        if(selectedFragment instanceof HealtStateFragment){
            ((HealtStateFragment)selectedFragment).startDisplayingContents();
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
