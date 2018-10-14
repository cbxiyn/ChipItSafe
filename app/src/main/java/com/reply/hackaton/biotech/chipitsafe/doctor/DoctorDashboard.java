package com.reply.hackaton.biotech.chipitsafe.doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reply.hackaton.biotech.chipitsafe.R;

public class DoctorDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        //hearthParamsTV2
        TextView t = (TextView)findViewById(R.id.hearthParamsTV2);
        t.setText("130 BPM");
        t.setVisibility(View.VISIBLE);
    }
}
