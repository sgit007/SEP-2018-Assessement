package uts.sep.westfieldparkmate.Activity.UserActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.R;

public class UpdateAccountActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    String uid, name;
    TextView nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            uid = mFirebaseUser.getUid();
            name = mFirebaseUser.getDisplayName();
        }

        nameField = (TextView) findViewById(R.id.nameFieldInUpdate);
        nameField.setText(name);
        Button applyButton = (Button) findViewById(R.id.applyButton);
        final CheckBox becomeAdmin = (CheckBox) findViewById(R.id.adminClickBox_inUpdateAccount);


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference uidRef = FirebaseDatabase.getInstance().getReference().child(Constant.USERS).child(uid);

                String name = nameField.getText().toString();
                uidRef.child("name").setValue(name);

                if (becomeAdmin.isChecked()) {
                    uidRef.child("admin").setValue(true);
                } else {
                    uidRef.child("admin").setValue(false);
                }
                Toast.makeText(UpdateAccountActivity.this, "Update account information successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
