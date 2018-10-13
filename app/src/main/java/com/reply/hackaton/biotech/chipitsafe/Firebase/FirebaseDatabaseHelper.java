package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.core.ApiFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
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
    public static Map<String, Object> rescuers = new HashMap<>();
    public JSONObject getRescuers(String uid) {
        DocumentReference docRef = db.collection("rescuerConfig").document(uid);
        // asynchronously retrieve the document
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, " " + task.getResult().getData());
                            rescuers =  task.getResult().getData();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        JSONObject json = new JSONObject(rescuers);

        return json;
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

