package uts.sep.westfieldparkmate.Activity.SystemAdminActivity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
        parkingRef = FirebaseDatabase.getInstance().getReference(Constant.PARKINGLOTS);
        final EditText numberOfParkingLot = view.findViewById(R.id.get_number_for_parking_lots);
        final Button addParkingBtn = (Button) view.findViewById(R.id.add_parking_btn);
        addParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (parkingLot()) {

                    case 0:
                        int level = level();

                        int parkingPostfix = level() * 100;
                        for (int i = 0; i < Integer.parseInt(numberOfParkingLot.getText().toString()); i++) {
                            String id = String.valueOf(i + parkingPostfix);
                            parkingLot[0] = new ParkingLot(id, true);
                            parkingRef.child(mLevel[0]).child(Constant.TRUE).child(numberOfParkingLot.getText().toString()).setValue(parkingLot[0], new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            Toast.makeText(getContext(), "Parking lot is added successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );}
                            break;

                            case 1:
                                parkingLot[0] = new ParkingLot(numberOfParkingLot.getText().toString(), true);
                                parkingRef.child(mLevel[0]).child(Constant.TRUE).child(numberOfParkingLot.getText().toString()).setValue(parkingLot[0], new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Toast.makeText(getContext(), "Parking lots is added successful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }


                }
            });

        return view;
        }

        private int level () {
            return spinnerLevel.getSelectedItemPosition() + 1;

        }

        private int parkingLot () {
            return spinnerParkingLot.getSelectedItemPosition();

        }
    }
