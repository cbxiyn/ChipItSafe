package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.content.Context;
import android.util.Log;

import com.reply.hackaton.biotech.chipitsafe.EmailPasswordActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class FirstAidRequest {
    FirebaseDatabaseHelper firebaseDatabaseHelper =  new FirebaseDatabaseHelper();
    private static final String TAG = FirstAidRequest.class.getName();
    /**
     * This method calls the createFirstAidDocument method in the Firebase Database class
     * it checks if the UID is null before calling the method.
     * @param uid   The user's Firebase user ID
     * @param bps The users current Beats per second (BPS) reading from the ecg monitor
     */
    public void updateBPS(String uid, int bps)
    {
        if(uid != null) {

            firebaseDatabaseHelper.createFirstAidDocument(uid, bps);
        }
    }
    /**
     * This method calls the deleteFirstAidDocument method in the Firebase database class
     * it checks if the UID is null before calling the method.
     * @param uid   The user's Firebase user ID
     */
    public void deleteFirstAidDocument(String uid)
    {
        if(uid != null)
        {

            firebaseDatabaseHelper.deleteFirstAidDocument(uid);
        }

    }
    public void sendNotificationToRescuers(String uid,Context context){


        //Map<String,Object> rescuerUIDs = firebaseDatabaseHelper.getRescuersMap(uid);
        Log.w(TAG,"rescuer ids: ");

    }
    public JSONObject constructFirstAidNotification(String uid)
    {
        JSONObject testData = new JSONObject();
        try {
            testData.put("UID",uid);
            testData.put("FirstAid",true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return testData;
    }
}
