package com.reply.hackaton.biotech.chipitsafe;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reply.hackaton.biotech.chipitsafe.Firebase.MessagingService;

public class EmailPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView emailText;
    TextView passwordText;
    private static final String TAG = EmailPasswordActivity.class.getName();
    MessagingService messagingService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password2);
        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.emailView);
        passwordText = findViewById(R.id.passwordView);
        messagingService = new MessagingService(EmailPasswordActivity.this);
        emailText.setText(messagingService.FID);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void registerButton_onClick(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        //TODO: Unit tests

        //TODO: Data validation on views. Password Min 6 characters/Email must include @ symbol etc.
        if(!email.contains("@"))
        {
            Toast.makeText(EmailPasswordActivity.this, "Enter a valid email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6)
        {
            Toast.makeText(EmailPasswordActivity.this, "Enter a password longer than 6 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful.
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(EmailPasswordActivity.this, "User registered successfully.",
                                    Toast.LENGTH_SHORT).show();

                            //TODO: Execute cloud function that creates user configuration on real-time database

                            return;
                        } else {
                            // If registration  fails, attempt to continue and try to login.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    public void loginButton_onClick(View view)
    {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(EmailPasswordActivity.this, "Logged in successfully.",
                                    Toast.LENGTH_SHORT).show();
                            //TODO: Get user UID and load user configuration

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void updateUI(FirebaseUser user) {

    }


}
