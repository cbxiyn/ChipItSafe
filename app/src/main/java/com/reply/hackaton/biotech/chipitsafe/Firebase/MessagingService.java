package com.reply.hackaton.biotech.chipitsafe.Firebase;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.reply.hackaton.biotech.chipitsafe.R;
import com.reply.hackaton.biotech.chipitsafe.casualrescuer.CasualRescuerDashboard;
import com.reply.hackaton.biotech.chipitsafe.doctor.DoctorDashboard;
import com.reply.hackaton.biothech.chipitsafe.tools.ApplicationState;
import com.squareup.okhttp.MediaType;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

/** This class is for sending and receiving messages via the Firebase Cloud Messaging service
 */

public class MessagingService extends FirebaseMessagingService {
    private NotificationCompat.Builder mBuilder;
    //private static MessagingService messagingService;

    public static class Consants{
        public static final String LEGACY_SERVER_KEY = "AIzaSyCYm_q-C09DnXQrreTakfFOds1EaMo7gy0";
    }
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

    /*
    public static MessagingService instanceOf(final Context context){
        if(messagingService == null) messagingService = new MessagingService(context);

        return messagingService;
    }
    */

    @Override
    public void onCreate() {
        super.onCreate();
        mBuilder = new NotificationCompat.Builder(this, ApplicationState.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.googleg_standard_color_18)
                //.setContentTitle("Hey Doctor!")
                .setContentText("I am in danger!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // set notification click action stuff
        Intent notifyIntent;
        if(ApplicationState.state == ApplicationState.UserState.rescuer) {
            notifyIntent = new Intent(this, CasualRescuerDashboard.class);
            mBuilder.setContentTitle("Please Rescue Me!");
        } else /*if(ApplicationState.state == ApplicationState.UserState.doctor)*/{
            // TODO: link to doctor section
            notifyIntent = new Intent(this, DoctorDashboard.class);
            mBuilder.setContentTitle("DOCTOR HELP ME!");
        }
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(notifyPendingIntent);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    public void registerNotificationClickAction(){


        /*
        Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_snooze, getString(R.string.snooze),
                        snoozePendingIntent);
         */
    }

    //TODO: Send new token to FireBase RT-DBS
    public void sendRegistrationToServer(String token) {
        FirebaseServlet firebaseServlet = new FirebaseServlet();
        //firebaseServlet.updateUserAppToken();
    }


    int notifID = 1;
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

        if(ApplicationState.healthState != ApplicationState.HealthState.inDanger){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notifID++, mBuilder.build());

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
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @SuppressLint("StaticFieldLeak")
    public static void sendNotification(final String regToken) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    dataJson.put("body","Hi this is sent from device to device");
                    dataJson.put("title","dummy title");
                    json.put("notification",dataJson);
                    json.put("to",regToken);
                    RequestBody body = RequestBody.create(JSON,json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+Consants.LEGACY_SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();

    }
}
