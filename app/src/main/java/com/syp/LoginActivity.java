package com.syp;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.maps.errors.ApiException;
import com.syp.model.Singleton;
import com.syp.model.User;

public class LoginActivity extends AppCompatActivity {
    static final int GOOGLE_SIGN = 12345;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Populate with login fragment
        setContentView(R.layout.fragment_google_login);

        signInButton = findViewById(R.id.signInButton);

        // Connect to firebase
        mAuth = FirebaseAuth.getInstance();

        // Create sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signInButton.setOnClickListener(v -> SignInGoogle());

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
        }
    }

    void SignInGoogle() {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("Tag", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    Log.d("Tag", "Sign in credential");
                    if (task.isSuccessful()) {
                        Log.d("Tag", "signin success");
                        // Get firebase user
                        FirebaseUser fbuser = mAuth.getCurrentUser();

                        // return intent
                        Intent returnIntent = new Intent();

                        // push email and display name
                        returnIntent.putExtra("email", fbuser.getEmail());
                        returnIntent.putExtra("displayName", fbuser.getDisplayName());

                        // Finish
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    else {
                        Log.d("Tag", "signin failure");
                        Toast.makeText(this, "SignIn Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
