package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirstAidRequest implements OnCompleteListener<DocumentSnapshot>{
    private static FirstAidRequest firstAidRequest;
    FirebaseDatabaseHelper firebaseDatabaseHelper =  new FirebaseDatabaseHelper();
    MessagingService messagingService = new MessagingService();
    private static final String TAG = FirstAidRequest.class.getName();


    public FirstAidRequest(){

    }


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

    static Map<String,Object> returnedRescuers = new HashMap<>();
    static String uid = "";
    public void sendNotificationToRescuers(String uid){

        if(uid != null)
        {
            this.uid = uid;
            firebaseDatabaseHelper.getRescuers(uid,this);
        }



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
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            returnedRescuers.putAll(task.getResult().getData());
            for(String rescuer: returnedRescuers.keySet())
            {

                RescuerUIDtoRescuerAppToken rescuerUIDtoRescuerAppToken = new RescuerUIDtoRescuerAppToken();
                String rescuerAppID = rescuerUIDtoRescuerAppToken.getUserAppToken(rescuer);
                if(rescuerAppID != null)
                {
                    messagingService.sendNotificationWithData(rescuerAppID,constructFirstAidNotification(uid));
                }

            }
        } else {
            Log.e(TAG,task.getException().toString());
        }
    }
    }

