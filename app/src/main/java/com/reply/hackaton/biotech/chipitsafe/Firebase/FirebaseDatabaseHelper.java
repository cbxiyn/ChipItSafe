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
    private static Map<String, Object> returnedRescuers = new HashMap<>();
    public Map<String,Object> getRescuers(String uid) {
        DocumentReference docRef = db.collection("rescuerConfig").document(uid);
        // asynchronously retrieve the document
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, " " + task.getResult().getData());
                            returnedRescuers.putAll(task.getResult().getData());

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return returnedRescuers;
    }
    public ArrayList<String> parseRescuerMap(Map<String, Object> rescuers){
        ArrayList<String> rescuerAppIds = new ArrayList<>();
        for(Map.Entry<String,Object> rescuer: rescuers.entrySet())
        {
            String key = (String) rescuer.getKey();
            rescuerAppIds.add(key);

        }
        return rescuerAppIds;
    }
    /*public JSONObject getRescuers(String uid) {
        DocumentReference docRef = db.collection("rescuerConfig").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> forms = documentSnapshot.getData();
                for (Map.Entry<String, Object> form: forms.entrySet()) {
                    String key = (String) form.getKey();
                    Map<String, Object> values = (Map<String, Object>)form.getValue();
                    String name = (String) values.get("formName");

                }
            }
        })
    }*/
    public void copyData(Map<String, Object> tmp)
    {
        returnedRescuers.putAll(tmp);
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

