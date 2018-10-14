package com.reply.hackaton.biotech.chipitsafe.casualrescuer;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reply.hackaton.biotech.chipitsafe.R;
import com.reply.hackaton.biothech.chipitsafe.tools.GeoLocalizer;
import com.reply.hackaton.biothech.chipitsafe.tools.SimulationConstants;

public class CasualRescuerDashboard extends AppCompatActivity implements OnMapReadyCallback {



    GoogleMap googleMap;
    private double lat;
    private double lon;
    private String personToBeRescuedName= "hardcoded string";
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casual_rescuer_dashboard);

        TextView tv = findViewById(R.id.)

        personToBeRescuedName = intent.getStringExtra("person");
        lat = SimulationConstants.hLat;//intent.getDoubleExtra("lat", SimulationConstants.hLat);
        lon = SimulationConstants.hLong;//intent.getDoubleExtra("long", SimulationConstants.hLong);
    }

}
