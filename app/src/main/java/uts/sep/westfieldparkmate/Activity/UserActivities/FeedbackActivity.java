package uts.sep.westfieldparkmate.Activity.UserActivities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.Model.Feedback;
import uts.sep.westfieldparkmate.R;

public class FeedbackActivity extends AppCompatActivity {

    private DatabaseReference feedbackRef;
    private FirebaseAuth mAtuth;
    private FirebaseUser firebaseUser;
    private CheckBox checkBox;
    String uid = Constant.ANONYMOUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAtuth = FirebaseAuth.getInstance();

        if (mAtuth.getCurrentUser() != null) {
            firebaseUser = mAtuth.getCurrentUser();
        }


        final EditText feedbackEt = (EditText) findViewById(R.id.feedback_et);
        checkBox = (CheckBox) findViewById(R.id.feedbackAnonymous);

        final Button feedbackBtn = findViewById(R.id.feedback_btn);
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String feedback = feedbackEt.getText().toString();
                if (checkBox.isChecked() && firebaseUser != null) {
                    uid = firebaseUser.getUid();
                }

                if(!feedbackEt.getText().toString().equals(""))
                {
                    feedbackRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference postsRef = feedbackRef.child(Constant.FEEDBACK);
                    DatabaseReference newPostRef = postsRef.push();
                    String uniqueID = postsRef.getKey();
                    Feedback mFeedback = new Feedback(feedback, uid);
                    newPostRef.setValue(mFeedback, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(FeedbackActivity.this, "Sorry, Please try again to send us feedback!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FeedbackActivity.this, "Appreciate for you feedback!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });
                }
                else
                {
                    Toast.makeText(FeedbackActivity.this, "Sorry, feedback cannot be empty! Please try again.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
