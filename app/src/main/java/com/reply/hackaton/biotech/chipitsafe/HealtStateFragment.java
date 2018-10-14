package com.reply.hackaton.biotech.chipitsafe;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsResponseListener;
import com.reply.hackaton.biotech.chipitsafe.graphics.MyHeartShape;
import com.reply.hackaton.biothech.chipitsafe.tools.ApplicationState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;


public class HealtStateFragment extends Fragment implements MdsResponseListener, MdsNotificationListener {

    static public final String TAG = "HealtStateFragment";
    private HeartRateManager heartRateManager;

    private TextView deviceNameTV;
    private ProgressBar progressBar;
    private TextView parametersTV;
    private MyHeartShape heartCustomView;
    private TextView heartStatusTV;

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
        heartStatusTV = (TextView) v.findViewById(R.id.hearthParamsStatus);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        heartCustomView = new MyHeartShape(getActivity());
        heartCustomView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));
        FrameLayout innerHeartFrameLayout = (FrameLayout) v.findViewById(R.id.heartLayout);
        innerHeartFrameLayout.addView(heartCustomView);

        //
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
        showHeartBeatAnimation();

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
            //showHeartBeatAnimation();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showHeartBeatAnimation(){

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(heartCustomView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(300);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();



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
        try {

            JSONObject hrData = new JSONObject(s).getJSONObject("Body");
            int average = hrData.getInt("average");
            if(ApplicationState.healthState == ApplicationState.HealthState.inDanger){
                Random r = new Random();
                int i1 = r.nextInt(130) + 100;
                average = i1;
            }
            //if(average < 53){
            //    Random generator = new Random(System.currentTimeMillis()); // see comments!
            //    int randomNum = rand.nextInt((max - min) + 1) + min;
            //}

            parametersTV.setText(average+" BPM");
            // Retrieve number array from JSON object.
            JSONArray array = hrData.optJSONArray("rrData");

            // Deal with the case of a non-array value.
            if (array == null) {
                /*...*/
            }


            // Create an int array to accomodate the numbers.
            int[] hrSamples = new int[array.length()];

            // Extract numbers from JSON array.
            for (int i = 0; i < array.length(); ++i) {
                hrSamples[i] = array.optInt(i);
                parametersTV.setText(parametersTV.getText().toString() + hrSamples[i] + "-");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}

