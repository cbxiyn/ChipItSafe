package com.reply.hackaton.biotech.chipitsafe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.movesense.mds.MdsException;
import com.movesense.mds.MdsResponseListener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HealtStateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HealtStateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealtStateFragment extends Fragment implements MdsResponseListener {


    public static String TAG = "HealtStateFragment";
    HeartRateManager heartRateManager;
    private TextView deviceNameTV;

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
        // Inflate the layout for this fragment
        //For Mario..catch here the view references
        View v =  inflater.inflate(R.layout.fragment_healt_state, container, false);
        deviceNameTV = (TextView) v.findViewById(R.id.deviceName);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //I've never used this override fucntion
        //deviceNameTV = (TextView) getView().findViewById(R.id.deviceName);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public void startDisplayingContents(){
        Toast.makeText(getActivity(), "LOADING CONTENTS.....", Toast.LENGTH_SHORT).show();
        updateDeviceInfo();
    }

    public void updateDeviceInfo(){

        heartRateManager.getDeviceInfo(this);

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
            Log.e("DEVICEINFO", "devname is");
            JSONObject jObj = new JSONObject(data);
            String devName = jObj.getJSONObject("DeviceInfo").getString("Name");
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
