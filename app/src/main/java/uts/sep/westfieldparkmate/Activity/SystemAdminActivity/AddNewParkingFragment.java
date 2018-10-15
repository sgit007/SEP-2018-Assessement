package uts.sep.westfieldparkmate.Activity.SystemAdminActivity;


import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.crypto.EncryptedPrivateKeyInfo;

import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.Model.ParkingLot;
import uts.sep.westfieldparkmate.R;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewParkingFragment extends Fragment {
    private Spinner spinnerLevel, spinnerParkingLot;
    private DatabaseReference parkingRef;
    boolean ifError = false;
    EditText numberOfParkingLot;

    public AddNewParkingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_new_parking, container, false);
        // Inflate the layout for this fragment
        spinnerLevel = view.findViewById(R.id.spinner_level);
        spinnerLevel.setSelection(0);
        Log.d(Constant.TAG, "This is the spinner value");
        parkingRef = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS);
        numberOfParkingLot = view.findViewById(R.id.get_number_for_parking_lots);
        numberOfParkingLot.setText("50");
        final Button addParkingBtn = (Button) view.findViewById(R.id.add_parking_btn);

        final int newLevel = spinnerLevel.getSelectedItemPosition() + 1;
        addParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfParkingLot.getText().toString() != null && newLevel != 0) {
                    String temp = numberOfParkingLot.getText().toString();
                    if (temp != null) {
                        int number = Integer.parseInt(temp);
                        for (int i = 0; i < number; i++) {
                            String level = spinnerLevel.getSelectedItem().toString();
                            int pid = i + getLevel(level)*100;
                            final String tempString = String.valueOf(pid);
                            ParkingLot newParking = new ParkingLot(tempString, true);
                            parkingRef.child(level).child(Constant.TRUE).child(String.valueOf(pid)).setValue(newParking, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Log.d(TAG, "successful" + tempString);
                                }
                            });
                        }

                    }
                }
            }
        });
        return view;
    }

    private int getLevel(String level) {
        switch (level) {
            case Constant.LV1: {
                return 1;
            }
            case Constant.LV2: {
                return 2;
            }
            case Constant.LV3: {
                return 3;

            }
            default:
                return 0;
        }
    }

}