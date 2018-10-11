package com.reply.hackaton.biotech.chipitsafe.Firebase;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";

    Context context;

    public String FID;

    public MessagingService() {

    }

    public MessagingService(final Context context) {
        this.context = context;

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                FID = instanceIdResult.getToken();
                Log.d(TAG, "Token: " + FID);


            }
        });
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    //TODO: Send new token to FireBase RT-DBS
    public void sendRegistrationToServer(String token) {
        FirebaseServlet firebaseServlet = new FirebaseServlet();
        //firebaseServlet.updateUserAppToken();
    }

    /**
     * This method overrides firebase's default implementation of onMessageReceived. This method handles
     * incoming data/notification payloads (JSONObject)
     *
     * @param remoteMessage An message sent to this device. Containing either a notification payload
     *                      or a data payload.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO: Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //TODO: Handle data when first aid request is requested.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //TODO: Handle data when first aid request is requested.
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

    }

}
