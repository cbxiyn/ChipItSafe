package com.reply.hackaton.biotech.chipitsafe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.reply.hackaton.biotech.chipitsafe.Firebase.Firebase;
import com.reply.hackaton.biotech.chipitsafe.Firebase.FirebaseDatabaseHelper;
import com.reply.hackaton.biotech.chipitsafe.Firebase.FirstAidRequest;
import com.reply.hackaton.biotech.chipitsafe.Firebase.MessagingService;
import com.reply.hackaton.biothech.chipitsafe.tools.ApplicationState;


public class EmailPasswordActivity extends AppCompatActivity implements OnCompleteListener<AuthResult> {

    TextView emailText;
    TextView passwordText;
    private static final String TAG = EmailPasswordActivity.class.getName();

    MessagingService messagingService;
    Firebase firebase;
    FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
    FirstAidRequest firstAidRequest =  new FirstAidRequest();
    FirebaseUser currentUser;

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password2);

        emailText = findViewById(R.id.emailView);
        passwordText = findViewById(R.id.passwordView);

        messagingService = new MessagingService(EmailPasswordActivity.this);
        firebase = new Firebase();

        createNotificationChannel();



    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(ApplicationState.NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebase.currentUser;
        updateUI(currentUser);
    }

    public void registerButton_onClick(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (!email.contains("@")) {
            Toast.makeText(EmailPasswordActivity.this, "Enter a valid email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(EmailPasswordActivity.this, "Enter a password longer than 6 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        firebase.emailPasswordRegister(email, password, EmailPasswordActivity.this);

    }

    public void loginButton_onClick(View view) {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
		

        firebase.emailPasswordLogin(email, password, EmailPasswordActivity.this);


    }

    public void updateUI(FirebaseUser user) {

    }


    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success
            Log.d(TAG, "signInWithEmail:success");

            Toast.makeText(this, "Logged in successfully.",
                    Toast.LENGTH_SHORT).show();
            firebase.updateCurrentUser();
            firebase.updateUserAppToken(messagingService.FID,this);

            Intent intent = new Intent(EmailPasswordActivity.this,DevicePairingActivity.class);

            startActivity(intent);
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();

        }
    }
}
