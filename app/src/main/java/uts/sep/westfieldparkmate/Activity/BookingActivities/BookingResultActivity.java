package uts.sep.westfieldparkmate.Activity.BookingActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import uts.sep.westfieldparkmate.Model.Booking;
import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.R;

public class BookingResultActivity extends AppCompatActivity {

    String pID = "";
    TextView resultIDView;
    Button backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_result);

        resultIDView = (TextView) findViewById(R.id.resultIDVew);
        backToMain = (Button) findViewById(R.id.backToMainInResultPage);

        Intent intent = getIntent();
        pID = intent.getStringExtra(Constant.VALUE);

        updateResultQRCode(pID);
        String resultID = getString(R.string.your_parking_id_is) + pID;
        resultIDView.setText(resultID);

        updateToBookingDatabase(pID);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateResultQRCode(String pId) {
        ImageView qrCode = (ImageView) findViewById(R.id.qrView);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(pId, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void updateToBookingDatabase(String pid) {

        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference().child(Constant.BOOKING);
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mFirebaseUser != null;
        String uid = mFirebaseUser.getUid();
        Booking newBooking = new Booking(uid, pid);

        bookingRef.child(uid).setValue(newBooking);


    }

}
