package uts.sep.westfieldparkmate.Activity.UserActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uts.sep.westfieldparkmate.Activity.MainActivity;
import uts.sep.westfieldparkmate.R;
import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.Model.User;

//TODO check mAUth
public class SignUpActivity extends AppCompatActivity {

    private EditText emailEdit, nameEdit, passwordEdit;
    private Button signUpButton, goToLoginButton;
    private DatabaseReference uidRef;
    private CheckBox adminCheck;
    private FirebaseAuth mAuth;

    public static final String defaultLogo = "https://www.weheartswift.com/wp-content/uploads/2017/05/Firebase_16-logo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        uidRef = FirebaseDatabase.getInstance().getReference();

        emailEdit = (EditText) findViewById(R.id.emailEditTextSignUp);
        nameEdit = (EditText) findViewById(R.id.nameEditTextSignUp);
        passwordEdit = (EditText) findViewById(R.id.passwordEditTextSignUp);
        adminCheck = (CheckBox) findViewById(R.id.adminCheckBox);
        signUpButton = (Button) findViewById(R.id.signUpButtonSignUp);
        goToLoginButton = (Button) findViewById(R.id.goToLoginButtonSignUp);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(emailEdit.getText().toString())) {
                    if (isValidName(nameEdit.getText().toString())) {
                        if (isValidPassword(passwordEdit.getText().toString())) {
                            //Start pass these value to somewhere TODO
                            String email = emailEdit.getText().toString();
                            String name = nameEdit.getText().toString();
                            String password = passwordEdit.getText().toString();
                            boolean isAdmin = false;
                            if(adminCheck.isChecked())
                            {
                                isAdmin = true;
                            }
                            signInWithEmail(email, password, name, isAdmin);

                        } else {
                            displayError(3);
                        }
                    } else {
                        displayError(2);
                    }
                } else {
                    displayError(1);
                }

            }
        }); //TODO it must have some logic error

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });


    }

    private boolean isValidName(String name) {
        boolean isValid = false;
        String expression = getString(R.string.nameExpression);
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        boolean isValid = false;
        String expression = getString(R.string.nameExpression);
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    private void displayError(int type) {
        switch (type) {
            case 1:
                Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Please enter valid name", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "Please enter password with at least 3 characters", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void signInWithEmail(String email, String password, String name, final boolean isAdmin) {
        final String inputName = name;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(Constant.TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                String email = user.getEmail();
                                addEmailAccToDB(uid, inputName, email, isAdmin);//updateUI(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(Constant.TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void addEmailAccToDB(String uid, String name, String email, boolean isAdmin) {
        User user = new User(email, name,defaultLogo, isAdmin);
        uidRef.child(Constant.USERS).child(uid).setValue(user);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}
