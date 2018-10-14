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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.crypto.EncryptedPrivateKeyInfo;

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
        spinnerLevel = view.findViewById(R.id.spinner_level);
        Log.d(Constant.TAG,"This is the spinner value");
        Log.d(Constant.TAG,spinnerParkingLot.getSelectedItem().toString());
        parkingRef = FirebaseDatabase.getInstance().getReference(Constant.PARKINGLOTS);
        final EditText numberOfParkingLot = view.findViewById(R.id.get_number_for_parking_lots);
        final Button addParkingBtn = (Button) view.findViewById(R.id.add_parking_btn);
        final int newLevel = level();
        addParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfParkingLot.getText().toString() != null && newLevel != 0 )
                {
                    String temp = numberOfParkingLot.getText().toString();
                    int number = Integer.parseInt(temp);
                    for(int i=0; i<number; i++)
                    {
                        String tempString = String.valueOf(i);
                        ParkingLot newParking = new ParkingLot(tempString, true );
                        parkingRef.setValue()
                    }

                }
                }

            }


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
