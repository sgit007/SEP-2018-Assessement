package uts.sep.westfieldparkmate.Activity.BookingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.R;

public class BookingActivity extends AppCompatActivity {

    Button LV1, LV2, LV3;
    DatabaseReference levelParkingRef;
    int min = 0;
    int max = 99;
    boolean done = true;
    TextView lv1Count, lv2Count, lv3Count;
    DatabaseReference lv1Ref, lv2Ref, lv3Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        levelParkingRef = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS);
        LV1 = (Button) findViewById(R.id.LV1);
        LV2 = (Button) findViewById(R.id.LV2);
        LV3 = (Button) findViewById(R.id.LV3);
        lv1Count = (TextView) findViewById(R.id.LV1Capa);
        lv2Count = (TextView) findViewById(R.id.LV2Capa);
        lv3Count = (TextView) findViewById(R.id.LV3Capa);


        lv1Ref = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS).child(Constant.LV1).child(Constant.TRUE);
        lv2Ref = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS).child(Constant.LV2).child(Constant.TRUE);
        lv3Ref = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS).child(Constant.LV3).child(Constant.TRUE);

        lv1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String info = getString(R.string.available) + String.valueOf(dataSnapshot.getChildrenCount());
                lv1Count.setText(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lv2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String info = getString(R.string.available) + String.valueOf(dataSnapshot.getChildrenCount());
                lv2Count.setText(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lv3Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String info = getString(R.string.available) + String.valueOf(dataSnapshot.getChildrenCount());
                lv3Count.setText(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        LV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, GenerateParkingIDActivity.class);
                intent.putExtra(Constant.VALUE, "LV1");
                startActivity(intent);
                finish();
            }
        });
        LV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, GenerateParkingIDActivity.class);
                intent.putExtra(Constant.VALUE, "LV2");
                startActivity(intent);
                finish();
            }
        });

        LV3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, GenerateParkingIDActivity.class);
                intent.putExtra(Constant.VALUE, "LV3");
                startActivity(intent);
                finish();
            }
        });


    }

}
