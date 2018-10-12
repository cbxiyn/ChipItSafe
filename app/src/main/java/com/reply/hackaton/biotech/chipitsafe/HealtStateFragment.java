package com.reply.hackaton.biotech.chipitsafe;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.movesense.mds.MdsException;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class HealtStateFragment extends Fragment implements MdsResponseListener, MdsNotificationListener {

    static public final String TAG = "HealtStateFragment";
    private HeartRateManager heartRateManager;

    private TextView deviceNameTV;
    private ProgressBar progressBar;
    private TextView parametersTV;

    public HealtStateFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HealtStateFragment newInstance() {
        HealtStateFragment fragment = new HealtStateFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        heartRateManager = HeartRateManager.instanceOfHeartRateManager();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_healt_state, container, false);
        deviceNameTV = (TextView) v.findViewById(R.id.deviceName);
        parametersTV = (TextView) v.findViewById(R.id.hearthParamsTV);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void getDeviceInfo(){}

    public ProgressBar getProgressBar(){
        progressBar.setVisibility(View.VISIBLE); //to show
        return progressBar;
    }

    public void startDisplayingContents(){
        Toast.makeText(getActivity(), "LOADING CONTENTS.....", Toast.LENGTH_SHORT).show();
        updateDeviceInfo();
    }

    public void updateDeviceInfo(){

        heartRateManager.getDeviceInfo(this);
        heartRateManager.subscribeToHeartRateNotifications(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    /**
     * Called when Mds operation has been succesfully finished
     *
     * @param data Response in String format
     */
    @Override
    public void onSuccess(String data) {
        Log.i(TAG, "Device /info request successful: " + data);
        // Display info in alert dialog

        // The onSuccess() gets the result code and the returned data as a JSON string.
        try {
            Log.e("DEVICEINFO", data);
            JSONObject jObj = new JSONObject(data);
            String devName = jObj.getJSONObject("Content").getString("productName");
            Log.e("DEVICEINFO", devName);
            deviceNameTV.setText(devName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when Mds operation failed for any reason
     *
     * @param e Object containing the error
     */
    @Override
    public void onError(MdsException e) {
        Log.e(TAG, "Device /info returned error: " + e);
    }

    // MDS SUBSCRIPTIONS
    @Override
    public void onNotification(String s) {
        Log.e("BPS", s);
        parametersTV.setText(s);

    }

}

