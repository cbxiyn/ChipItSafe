package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RescuerUIDtoRescuerAppToken implements OnCompleteListener<DocumentSnapshot> {
    private static final String TAG = RescuerUIDtoRescuerAppToken.class.getName();
    FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
    public RescuerUIDtoRescuerAppToken()
    {

    }
    String userAppToken = null;
    public String getUserAppToken(String uid)
    {
       firebaseDatabaseHelper.getUserAppToken(uid,this);
       return userAppToken;
    }

    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
                userAppToken = task.getResult().getString("userAppToken");
        } else {
            Log.e(TAG,task.getException().toString());
        }
    }
}
