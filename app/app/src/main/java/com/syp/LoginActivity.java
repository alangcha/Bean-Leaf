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

import com.firebase.client.Firebase;
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


        // AUTO LOGIN FOR BLACK BOX TESTING FUNCTIONALITY
        AuthCredential credential = GoogleAuthProvider
                .getCredential("eyJhbGciOiJSUzI1NiIsImtpZCI6ImRiMDJhYjMwZTBiNzViOGVjZDRmODE2YmI5ZTE5NzhmNjI4NDk4OTQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxNjE5Mjg4MDA1OTUtNGRjdmRmcGVsamgwMXE0NGdicGNmaXI2Z3BxdGI5MG0uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxNjE5Mjg4MDA1OTUtbnZnOGtwc2UxaWxsM2k1YXM0cnM4ZmtmdDMyYzhvMDguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ0MDE0ODkzNzM0OTc3MTg4MTIiLCJlbWFpbCI6InN5cHByb2plY3QyMDE5QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJpYXQiOjE1NzQ2MzIwNzcsImV4cCI6MTU3NDYzNTY3N30.p99jVPQUzRF9h548UOluaofMEU3hKOGlZrR4sXC2AgmS2wQdXCvQugj3EW2arvC8LNIZwga4K2GhmiXw7jdsIkmOOSl3-dS94HizQ6ms0Pd5K3whGYY6SISWD7lVkH0ZCEidUJMM-oaum3p6ZoIvjCSO5TiHjSGO34of8fLlB7VyzSa2a13PRWAjIrdq6wseC5JeoWG75sLlrCfMdFey2uBcEyHnygueRF-U9yDpE_fFJ7QnBCQeDxM4zFp2vpYIiNluFbS1Rdhi0Q-VXy1Zq8HQR60Vue3QLN-Kl7kLsbVU9no0HXAzHjUXTgdN_3nq0bSwgjykD1TUCeUPP7VwTg", null);
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
        Log.d("Tag", "firebaseAuthWithGoogle: " + account.getIdToken());

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
