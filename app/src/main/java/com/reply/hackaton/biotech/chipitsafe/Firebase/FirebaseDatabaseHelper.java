package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public Map<String, Object> rescuers = new HashMap<>();

    /**
     * This method returns a JSONObject containing all of the user's specified rescuer's UID.
     * These are used for sending notifications requesting first aid.
     *
     * @param uid The user's Firebase UID
     */
    public JSONObject getRescuers(String uid) {
        DocumentReference docRef = db.collection("rescuerConfig").document(uid);
        HashMap<String, Object> returnData = new HashMap<>();
        // asynchronously retrieve the document
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, " " + task.getResult().getData());
                            rescuers = task.getResult().getData();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        JSONObject json = new JSONObject(rescuers);

        return json;
    }
}

