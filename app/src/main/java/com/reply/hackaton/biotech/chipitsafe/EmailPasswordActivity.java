package com.reply.hackaton.biotech.chipitsafe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.reply.hackaton.biotech.chipitsafe.Firebase.Firebase;
import com.reply.hackaton.biotech.chipitsafe.Firebase.FirebaseDatabaseHelper;
import com.reply.hackaton.biotech.chipitsafe.Firebase.FirstAidRequest;
import com.reply.hackaton.biotech.chipitsafe.Firebase.MessagingService;


public class EmailPasswordActivity extends AppCompatActivity {

    TextView emailText;
    TextView passwordText;
    private static final String TAG = EmailPasswordActivity.class.getName();

    MessagingService messagingService;
    Firebase firebase;
    FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
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

        //TODO: Unit tests

        //TODO: Data validation on views. Password Min 6 characters/Email must include @ symbol etc.
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
        firebase.updateCurrentUser();

        firebase.updateUserAppToken(messagingService.FID, EmailPasswordActivity.this);
    }

    public void updateUI(FirebaseUser user) {

    }


}
