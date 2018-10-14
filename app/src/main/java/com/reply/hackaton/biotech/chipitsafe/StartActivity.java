package com.reply.hackaton.biotech.chipitsafe;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
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
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "OnCreate");

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);
        setNFCSettings();
        heartManager = HeartRateManager.instanceOfHeartRateManager();
        heartManager.initMds(this);

        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            Log.d(TAG, "ACTION_NDEF_DISCOVERED");
            selectNFCFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();
            ((NFCFragment) selectedFragment).handleIntent(intent);

        }  else {

            Fragment f = fragmentHashMap.get(HealtStateFragment.TAG);
            if (f == null) {
                f = HealtStateFragment.newInstance();
                fragmentHashMap.put(HealtStateFragment.TAG, f);
            }
            selectedFragment = f;
        }
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, selectedFragment);
        transaction.commit();


        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.navigation_healt_state:
                                selectHealtSateSection();
                                break;
                            case R.id.navigation_emergency:
                                selectEmergencyFragment();
                                break;
                            case R.id.navigation_nfc:
                                selectNFCFragment();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
    }

    private void selectHealtSateSection(){
        Fragment f = fragmentHashMap.get(HealtStateFragment.TAG);
        if(f == null){
            f = HealtStateFragment.newInstance();
            fragmentHashMap.put(HealtStateFragment.TAG,f);
        }
        selectedFragment = f;
    }

    private void selectNFCFragment(){
        Fragment fra = fragmentHashMap.get(NFCFragment.TAG);
        if(fra == null){
            fra = NFCFragment.newInstance();
            fragmentHashMap.put(NFCFragment.TAG,fra);
        }
        selectedFragment = fra;
    }

    private void selectEmergencyFragment(){
        Fragment fr = fragmentHashMap.get(EmergencyFragment.TAG);
        if(fr == null){
            fr = EmergencyFragment.newInstance();
            fragmentHashMap.put(EmergencyFragment.TAG,fr);
        }
        selectedFragment = fr;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mNfcAdapter!=null && pendingIntent !=null){
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }

       if(heartManager.isConnectedToDevice){

            BluetoothDevice dev = heartManager.getDeviceAttemptingToConnectTo();
            Toast.makeText(this, "attempting to connect to "+ dev.getName(), Toast.LENGTH_SHORT).show();

            heartManager.connectToDevice(this);
       }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mNfcAdapter!=null && pendingIntent !=null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }

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
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.d(TAG, "Catch new intent:"+intent+" tag: "+tag);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            if(!(selectedFragment instanceof NFCFragment)){
                selectNFCFragment();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();
            ((NFCFragment) selectedFragment).handleIntent(intent);
        }

    }

    private void setNFCSettings(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Log.d(TAG,"NFC is disabled");
        } else {
            Log.d(TAG,"NFC is enabled");
        }


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
