package com.reply.hackaton.biotech.chipitsafe;

import android.app.NotificationChannel;
import android.content.Context;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reply.hackaton.biotech.chipitsafe.Firebase.FirstAidRequest;
import com.reply.hackaton.biotech.chipitsafe.Firebase.MessagingService;
import com.reply.hackaton.biothech.chipitsafe.tools.ApplicationState;
import com.reply.hackaton.biothech.chipitsafe.tools.GeoLocalizer;
import com.reply.hackaton.biothech.chipitsafe.tools.SimulationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;



public class EmergencyFragment extends Fragment implements View.OnClickListener {


    public static String TAG = "EmergencyFragment";
    private Button emergencyButton;
    private TextView questionTextView;
    private String[] questionArray;
    private HashMap<Integer,Boolean> questionHashMap = new HashMap<>();
    private int index = 0;
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
        questionTextView = (TextView)v.findViewById(R.id.emergency_question);
        TextView yesTextView = (TextView)v.findViewById(R.id.yes_button);
        TextView noTextView = (TextView)v.findViewById(R.id.no_button);
        questionArray = getActivity().getResources().getStringArray(R.array.emergency_questions);

        noTextView.setOnClickListener(this);
        yesTextView.setOnClickListener(this);

        questionTextView.setText(questionArray[index]);

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yes_button:
                questionHashMap.put(index,true);
                break;
            case R.id.no_button:
                questionHashMap.put(index,false);
                break;
        }
        index++;
        if(index>=questionArray.length){
            index = 0;
            JSONObject jsonObject = new JSONObject();
            for(int i = 0; i<questionArray.length;i++){
                Boolean b = questionHashMap.get(i);
                String quest = questionArray[i];
                String resp = "No";
                if(b) resp = "Yes";
                String key = "Q"+(i+1);

                try {
                    jsonObject.put(key,quest + " "+ resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            MessagingService.sendNotificationWithData(SimulationConstants.DOCTOR_TOKEN_ID,jsonObject, ApplicationState.openDoctorAction);
        } else {
            questionTextView.setText(questionArray[index]);
        }

    }

}
