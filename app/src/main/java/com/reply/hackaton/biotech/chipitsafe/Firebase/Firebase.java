package com.reply.hackaton.biotech.chipitsafe.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class Firebase {
    public FirebaseUser currentUser;
    private FirebaseServlet firebaseServlet;
    private FirebaseAuth mAuth;


    private static final String TAG = Firebase.class.getName();


    public Firebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseServlet = new FirebaseServlet();
    }

    /**
     * This method logs a user into Firebase using their Email/Password
     *
     * @param email    Valid email address
     * @param password Password over 6 characters in length
     * @param context  The activity that is calling this function
     */
    public void emailPasswordLogin(String email, String password, final Context context) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithEmail:success");

                            Toast.makeText(context, "Logged in successfully.",
                                    Toast.LENGTH_SHORT).show();
                            currentUser = mAuth.getCurrentUser();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    /**
     * This method registers a user with Firebase using their Email/Password
     *
     * @param email    Valid email address
     * @param password Password over 6 characters in length
     * @param context  The activity that is calling this function
     */
    public void emailPasswordRegister(String email, String password, final Context context) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, User can now log in.
                            Log.d(TAG, "createUserWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            Toast.makeText(context, "User registered successfully.",
                                    Toast.LENGTH_SHORT).show();

                            return;
                        } else {
                            // If registration  fails, display message to user
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    /**
     * This method updates the current users Firebase Application ID in the user's configuration
     * file on the database. This application ID is used to identify the user's device in cloud
     * messaging scenarios.
     *
     * @param FID     An Firebase Application ID to be updated in the current users configuration file
     * @param context The activity that is calling this function
     */
    public void updateUserAppToken(String FID, final Context context) {
        JSONObject data = null;
        String dataString = "";
        dataString += String.format("{'uid': '%s','userAppToken': '%s'}", currentUser.getUid(), FID);

        try {

            data = new JSONObject(dataString);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }


        firebaseServlet.updateUserAppToken(data, context);
    }
    /**
     * This method updates the current user variable with the user that is currently signed in
     */
    public void updateCurrentUser() {
        currentUser = mAuth.getCurrentUser();
    }
}
