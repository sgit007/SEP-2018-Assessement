package uts.sep.westfieldparkmate.Activity.SystemAdminActivity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.Model.ParkingLot;
import uts.sep.westfieldparkmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewParkingFragment extends Fragment {
    private Spinner spinnerLevel, spinnerParkingLot;
    private DatabaseReference parkingRef;
    boolean ifError = false;
    private static final String INDUVIDUAL_PARKING_ENTRY = "Individual Entry";
    private static final String BULK_PARKING_ENTRY = "Bulk Entry";

    public AddNewParkingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_new_parking, container, false);
        // Inflate the layout for this fragment
        final String[] mLevel = new String[1];
        final ParkingLot[] parkingLot = new ParkingLot[1];
        spinnerLevel = view.findViewById(R.id.spinner_level);
        mLevel[0] = spinnerLevel.getSelectedItem().toString();
        spinnerParkingLot = view.findViewById(R.id.spinner_parking_lot);
        Log.d(Constant.TAG,"This is the spinner value");
        Log.d(Constant.TAG,spinnerParkingLot.getSelectedItem().toString());
        parkingRef = FirebaseDatabase.getInstance().getReference(Constant.PARKINGLOTS);
        final EditText numberOfParkingLot = view.findViewById(R.id.get_number_for_parking_lots);
        final Button addParkingBtn = (Button) view.findViewById(R.id.add_parking_btn);
        final String parkingLotArray[] = getResources().getStringArray(R.array.parking_lot);
        addParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (parkingLot()) {

                    case BULK_PARKING_ENTRY:

                        int parkingPostfix = level() * 100;
                        for (int i = 0; i < Integer.parseInt(numberOfParkingLot.getText().toString()); i++) {
                            int id = i + parkingPostfix;
                            parkingLot[0] = new ParkingLot(id+"", true);
                            parkingRef.child(mLevel[0]).child(Constant.TRUE).child(numberOfParkingLot.getText().toString()).setValue(parkingLot[0], new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        }
                                    }
                            );
                        }
                        break;

                    case INDUVIDUAL_PARKING_ENTRY:
                        parkingLot[0] = new ParkingLot(numberOfParkingLot.getText().toString(), true);
                        parkingRef.child(mLevel[0]).child(Constant.TRUE).child(numberOfParkingLot.getText().toString()).setValue(parkingLot[0], new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            }
                        });break;
                }


            }
        });

        return view;
    }

    private int level() {
        Log.d(Constant.TAG,"at Level() current value is :"+spinnerLevel.getSelectedItemPosition());
        return spinnerLevel.getSelectedItemPosition() + 1;

    }

    private String parkingLot() {
        return spinnerParkingLot.getSelectedItem().toString();

    }
}
