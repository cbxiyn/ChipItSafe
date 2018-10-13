package com.reply.hackaton.biothech.chipitsafe.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class GeoLocalizer {


    public static void openMapsToTheRescueLocation2(Context c, double latitude, double longitude){
        String destinationLabel = "";//"(Rescue+Location)";
        // Creates an Intent that will load a map
        String geoString = "geo:"+latitude+","+longitude+destinationLabel;// "geo:37.7749,-122.4194"
        Uri gmmIntentUri = Uri.parse(geoString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        c.startActivity(mapIntent);

    }

    public static void openMapsToTheRescueLocation(Context c, double latitude, double longitude)
    {
        String locationString = "google.navigation:q="+latitude+","+longitude; //"google.navigation:q=Taronga+Zoo,+Sydney+Australia"
        Uri gmmIntentUri = Uri.parse(locationString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        c.startActivity(mapIntent);

    }

}
