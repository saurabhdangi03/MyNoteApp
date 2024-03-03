package com.example.mynoteapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/** @noinspection ALL*/
public class MainActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    private static final String PREFS_KEY_SIGNED_IN = "signed_in";

    private GoogleApiClient mGoogleApiClient;
    private Button loginButton;
    private Button logoutButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        loginButton = findViewById(R.id.login_button);
        logoutButton = findViewById(R.id.logout_button);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isSignedIn = sharedPreferences.getBoolean(PREFS_KEY_SIGNED_IN, false);

        if (isSignedIn) {
            // If already signed in, call the fragment screen
            showFragmentScreen();
            loginButton.setVisibility(View.GONE);
        }
        else {
            loginButton.setVisibility(View.VISIBLE);
        }


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                status -> {
                    Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                    // Clear the signed-in status
                    sharedPreferences.edit().putBoolean(PREFS_KEY_SIGNED_IN, false).apply();
                    // Start the LoginActivity
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish(); // Finish the current activity to prevent going back to it when pressing back button
                });
    }
   /* private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                status -> {
                    Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                    // Update UI accordingly, for example hide the logout button
                    logoutButton.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                });
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this, "Welcome " + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            // Update UI accordingly, for example hide the login button and show the logout button
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            sharedPreferences.edit().putBoolean(PREFS_KEY_SIGNED_IN, true).apply();
            showFragmentScreen();
            /*Call Add Fragment*/
           /* FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MainFragment fragment = new MainFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();*/
            /*Call Add Fragment*/
        } else {
            // Signed out, show unauthenticated UI.
            Log.e(TAG, "Google Sign In failed.");
            Toast.makeText(this, "Google Sign In failed.", Toast.LENGTH_SHORT).show();
            // Update UI accordingly, for example hide the logout button and show the login button
            logoutButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    private void showFragmentScreen() {
     MainFragment fragment = new MainFragment(); // Replace YourFragment with your actual fragment class
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}