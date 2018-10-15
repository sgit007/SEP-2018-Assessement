package uts.sep.westfieldparkmate.Activity.UserActivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import uts.sep.westfieldparkmate.Activity.MainActivity;
import uts.sep.westfieldparkmate.R;
import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.Model.User;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference uidRef;
    private GoogleApiClient mGoogleSignInClient;
    private static final String TAG = "Login Activity";
    private static final int RC_SIGN_IN = 1;
    private FirebaseUser mFirebaseUser;

    private EditText emailField;
    private EditText passwordField;


    public static final String defaultLogo = "https://www.weheartswift.com/wp-content/uploads/2017/05/Firebase_16-logo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //setPassedData();

        if (mAuth != null) {
            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }

        mAuth = FirebaseAuth.getInstance();
        uidRef = FirebaseDatabase.getInstance().getReference();

        SignInButton mGoogleButton = (SignInButton) findViewById(R.id.googleSignInButton);
        Button signInWithEmailButton = (Button) findViewById(R.id.signInWithEmailButton);


        // Configure Google Sign In
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LoginActivity.this, "U got a error", Toast.LENGTH_LONG).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        signInWithEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailField = (EditText) findViewById(R.id.emailEditTextLogin);
                passwordField = (EditText) findViewById(R.id.passwordEditTextLogin);
                String email = emailField.getText().toString();
                final String password = passwordField.getText().toString();
                //Async Task for SignIn
                new SignInWithEmailAndPassword(email, password).execute();
            }
        });


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in mFirebaseUser's information
                            Log.d(TAG, "signInWithCredential:success");
                            mFirebaseUser = mAuth.getCurrentUser();

                            if (mFirebaseUser != null && mFirebaseUser.getPhotoUrl() != null) {
                                String name = mFirebaseUser.getDisplayName();
                                String email = mFirebaseUser.getEmail();
                                String photoUri = mFirebaseUser.getPhotoUrl().toString();
                                addGoogleAccToDB(mFirebaseUser.getUid(), name, email, photoUri);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void addGoogleAccToDB(String uid, String name, String email, String photoUrl) {
        boolean isAdmin = false;
        User user = new User(email, name, photoUrl, isAdmin);
        uidRef.child(Constant.USERS).child(uid).setValue(user);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void updatePasswordField() {
        passwordField.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    private class SignInWithEmailAndPassword extends AsyncTask<Void, Void, Void> {
        /**
         * Runs on the UI thread before doInBackground()
         */

        String mEmail, mPassword;

        SignInWithEmailAndPassword(String email, String password) {
            mEmail = email;
            mPassword = password;

            mAuth = FirebaseAuth.getInstance();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    //updateUI(user);TODO
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed, please try again", Toast.LENGTH_SHORT).show();
                                    updatePasswordField();
                                }
                            }
                        });
            } catch (NullPointerException e) {
                Toast.makeText(LoginActivity.this, "Please Enter Email and Password!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Null at doInBackground");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void mVoid) {
            super.onPostExecute(mVoid);
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

    }
}
