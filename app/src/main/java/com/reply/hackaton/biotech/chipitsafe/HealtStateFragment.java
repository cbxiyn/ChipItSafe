package com.reply.hackaton.biotech.chipitsafe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HealtStateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HealtStateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import android.nfc.NfcAdapter;
import android.widget.Toast;

import java.util.zip.Inflater;


public class HealtStateFragment extends Fragment {

    static private String TAG = "HealtStateFragment";
    private HeartRateManager heartRateManager;
    private NfcAdapter mNfcAdapter;

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
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(getActivity(), "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Log.d(TAG,"NFC is disabled");
        } else {
            Log.d(TAG,"NFC is enabled");
        }

        //handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        // TODO: handle Intent
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_healt_state, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getDeviceInfo(){}

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
