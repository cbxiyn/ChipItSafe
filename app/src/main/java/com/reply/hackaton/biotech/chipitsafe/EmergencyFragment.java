package com.reply.hackaton.biotech.chipitsafe;

import android.app.NotificationChannel;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.reply.hackaton.biotech.chipitsafe.Firebase.FirstAidRequest;
import com.reply.hackaton.biotech.chipitsafe.Firebase.MessagingService;
import com.reply.hackaton.biothech.chipitsafe.tools.GeoLocalizer;
import com.reply.hackaton.biothech.chipitsafe.tools.SimulationConstants;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmergencyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmergencyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmergencyFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    public static String TAG = "EmergencyFragment";
    private Button emergencyButton;

    public EmergencyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EmergencyFragment newInstance() {
        EmergencyFragment fragment = new EmergencyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_emergency, container, false);
        emergencyButton = (Button) v.findViewById(R.id.startEmergencySimulationButton);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FirstAidRequest.instanceOf().sendNotificationToRescuers(SimulationConstants.DOCTOR_TOKEN_ID, getActivity());
                MessagingService.sendNotification(SimulationConstants.DOCTOR_TOKEN_ID);
                //emergencyButton.setAlpha(0);
            }
        });


        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
