package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.core.ApiFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** This class will contain the methods that interact with the Firebase database
 */
public class FirebaseDatabaseHelper {
    private static final String TAG = "Firestore Database";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseDatabaseHelper() {

    }



    /**
     * This method returns a JSONObject containing all of the user's specified rescuer's UID.
     * These are used for sending notifications requesting first aid.
     *
     * @param uid The user's Firebase UID
     */
    public void getRescuers(String uid, OnCompleteListener<DocumentSnapshot> listener) {
        DocumentReference docRef = db.collection("rescuerConfig").document(uid);
        // asynchronously retrieve the document
        docRef.get()
                .addOnCompleteListener(listener);
    }
    public void getUserAppToken(String uid, OnCompleteListener<DocumentSnapshot> listener) {
        DocumentReference docRef = db.collection("users").document(uid);
        // asynchronously retrieve the document
        docRef.get() //String value = document.getString("username");
                .addOnCompleteListener(listener);
    }
    public void createFirstAidDocument(String uid,int bps)
    {
        Map<String, Object> docData = new HashMap<>();
        docData.put("BPS", bps);
        DocumentReference docRef = db.collection("firstAid").document(uid);
        Log.d(TAG,docData.toString());
        docRef.set(docData);

    }
    public void deleteFirstAidDocument(String uid)
    {
        DocumentReference docRef = db.collection("firstAid").document(uid);
        Log.d(TAG,"Deleting first aid document for user id:" + uid);
        docRef.delete();

    }

}

