package uts.sep.westfieldparkmate.Activity.BookingActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.Model.ParkingLot;
import uts.sep.westfieldparkmate.R;

public class GenerateParkingIDActivity extends AppCompatActivity {
    private String level;
    private DatabaseReference levelParkingRef;
    private TextView iDtoUserView;
    private Button confirm, backToMain;
    private ParkingLot parkingLot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_parking_id);

        iDtoUserView = (TextView) findViewById(R.id.parkingIDtoUser);
        confirm = (Button) findViewById(R.id.confirmBookingButtoninGeneratePage);
        backToMain = (Button) findViewById(R.id.backToMainButtoninGeneratePage);

        levelParkingRef = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS);

        Intent intent = getIntent();
        level = intent.getStringExtra(Constant.VALUE);


        if (level != null) {
            levelParkingRef = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS).child(level).child(Constant.TRUE);
//            Query first = levelParkingRef.orderByKey().limitToFirst(1);

            levelParkingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        parkingLot = postSnapshot.getValue(ParkingLot.class);
                        if (parkingLot != null && parkingLot.isStatus()) {
                            try {
                                String iDfromDatabase = parkingLot.getpID();
                                iDtoUserView.setText(iDfromDatabase);
                                break;
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBooking(parkingLot, level);
            }
        });

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void confirmBooking(ParkingLot parkingLot, String level) {
        if (parkingLot != null) {
            parkingLot.setStatus(false);
            String pId = parkingLot.getpID();
            levelParkingRef = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS).child(level).child(Constant.TRUE).child(pId);
            levelParkingRef.removeValue();

            DatabaseReference resultRef = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS);
            resultRef.child(level).child(Constant.FALSE).child(pId).setValue(parkingLot);

            Intent intent = new Intent(this, BookingResultActivity.class);
            intent.putExtra(Constant.VALUE, pId);
            startActivity(intent);

            finish();

        }
    }
}
