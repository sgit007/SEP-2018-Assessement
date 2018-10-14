package uts.sep.westfieldparkmate.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uts.sep.westfieldparkmate.Activity.BookingActivities.BookingActivity;
import uts.sep.westfieldparkmate.Activity.UserActivities.FeedbackActivity;
import uts.sep.westfieldparkmate.Activity.UserActivities.LoginActivity;
import uts.sep.westfieldparkmate.Activity.UserActivities.SignUpActivity;
import uts.sep.westfieldparkmate.Activity.SystemAdminActivity.SystemAdminActivity;
import uts.sep.westfieldparkmate.Activity.UserActivities.UpdateAccountActivity;
import uts.sep.westfieldparkmate.Model.Booking;
import uts.sep.westfieldparkmate.Model.ParkingLot;
import uts.sep.westfieldparkmate.R;
import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.Model.User;

import com.crashlytics.android.Crashlytics;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public ImageView mAccountPhoto;
    public TextView mAccountName, mAccountEmail;
    protected FirebaseAuth mAuth = null;
    protected FirebaseUser mFirebaseUser = null;
    private boolean isAdmin = false;

    private ImageView currentQRCode, logoInMain;
    private TextView noCurrentBoooking, bookingInfo;
    private Button cancelButton;
    final DatabaseReference parkingRef = FirebaseDatabase.getInstance().getReference().child(Constant.PARKINGLOTS);

    private TextView timerView;

    DatabaseReference bookingRef;

    private CountDownTimer countDownTimer;
    boolean running;
    private final static long START_TIME_IN_MILLIS = 60000;
    private long mTimeLeftInMilles = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        date();
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, BookingActivity.class), 1);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void date() {//TODO check login error
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        SharedPreferences.Editor editor = shared.edit();
        if (isfer) {
            //because the first time launch this app, it does'nt contain a account in this app, so call loginActivity to ger an account
            //if it is first time
            Intent in = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(in);
            finish();
            editor.putBoolean("isfer", false);
            editor.apply();
        } else {
            //after user login its account when it was first time, get account info
            mAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                Log.d(Constant.TAG, " UserId : " + mFirebaseUser.getUid() + " , DisplayName" + mFirebaseUser.getDisplayName());

                DatabaseReference uidRef = FirebaseDatabase.getInstance().getReference().child(Constant.USERS).child(mFirebaseUser.getUid());
                uidRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            //Toast.makeText(MainActivity.this, "user object is valid", Toast.LENGTH_LONG).show();
                            //TODO progressBAr
                            String name = user.getName();
                            String email = user.getEmail();
                            String photoUrl = user.getPhotoUrl();
                            isAdmin = user.isAdmin();
                            Uri myUri = Uri.parse(photoUrl);

                            Log.i(Constant.TAG, name);
                            Log.i(Constant.TAG, email);
                            Log.i(Constant.TAG, photoUrl);

                            updateDrawerInfo(name, email, myUri);

                        } else {
                            Toast.makeText(MainActivity.this, "Whoops, user object is null", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    private void updateDrawerInfo(String name, String email, Uri myUrl) {

        mAccountPhoto = (ImageView) findViewById(R.id.localAccountPhotoImageView);
        mAccountName = (TextView) findViewById(R.id.localAccountUserNameTextView);
        mAccountEmail = (TextView) findViewById(R.id.localAccountEmailTextView);

        mAccountName.setText(name);
        mAccountEmail.setText(email);
        Glide.with(MainActivity.this).load(myUrl).apply(RequestOptions.circleCropTransform()).into(mAccountPhoto);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_booking:
                startActivity(new Intent(MainActivity.this, BookingActivity.class));
                break;
            case R.id.nav_feedback:
                startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
                break;
            case R.id.nav_system_admin:
                if (isAdmin) {
                    startActivity(new Intent(this, SystemAdminActivity.class));
                } else {
                    Toast.makeText(this, "Sorry, you have no permission to access this", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_manage:
                startActivity(new Intent(this, UpdateAccountActivity.class));
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.nav_exit:
                System.exit(0);
                break;
            default:
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        logoInMain = (ImageView) findViewById(R.id.westFieldImageInMain);
        noCurrentBoooking = (TextView) findViewById(R.id.noCurrentBookingTextView);

        bookingInfo = (TextView) findViewById(R.id.pidInMain);
        currentQRCode = (ImageView) findViewById(R.id.qrCodeImageViewInMain);
        cancelButton = (Button) findViewById(R.id.cancelButtonInMain);
        timerView = (TextView) findViewById(R.id.timer);

        cancelButton.setVisibility(View.GONE);
        bookingInfo.setVisibility(View.GONE);
        currentQRCode.setVisibility(View.GONE);
        timerView.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        bookingRef = FirebaseDatabase.getInstance().getReference().child(Constant.BOOKING).child(mFirebaseUser.getUid());
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Booking currentBooking = dataSnapshot.getValue(Booking.class);
                if (currentBooking != null) {
                    String pid = getString(R.string.booking_extra_info) + currentBooking.getPid();

                    updateResultQRCodeInMain(currentBooking.getPid());
                    String space = " " + pid;
                    bookingInfo.setText(space);

                    logoInMain.setVisibility(View.GONE);
                    noCurrentBoooking.setVisibility(View.GONE);

                    cancelButton.setVisibility(View.VISIBLE);
                    bookingInfo.setVisibility(View.VISIBLE);
                    currentQRCode.setVisibility(View.VISIBLE);
                    timerView.setVisibility(View.VISIBLE);


                    running = false;
                    startTimer();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }


        });

        currentQRCode.setOnLongClickListener(new View.OnLongClickListener()

        {
            @Override
            public boolean onLongClick(View v) {
                cancelBooking();
                return true;
            }
        });

    }

    private void updateResultQRCodeInMain(String pId) {
        ImageView qrCode = (ImageView) findViewById(R.id.qrCodeImageViewInMain);
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

    private String getLevel(String pid) {
        int mPid = Integer.valueOf(pid);
        if (100 <= mPid && mPid < 200) {
            return "LV1";
        } else if (200 <= mPid && mPid < 300) {
            return "LV2";
        } else if (300 <= mPid) {
            return "LV3";
        }
        return null;
    }

    private void cancelBooking() {
        cancelButton.setVisibility(View.GONE);
        bookingInfo.setVisibility(View.GONE);
        currentQRCode.setVisibility(View.GONE);
        timerView.setVisibility(View.GONE);
        logoInMain.setVisibility(View.VISIBLE);
        noCurrentBoooking.setVisibility(View.VISIBLE);

        String uid = mFirebaseUser.getUid();

        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Booking cancelBooking = dataSnapshot.getValue(Booking.class);
                final String pid = cancelBooking.getPid();

                parkingRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ParkingLot parkingLot = new ParkingLot(pid, true);
                        String level = getLevel(pid);
                        assert level != null;
                        DatabaseReference tempRef = parkingRef.child(level).child(Constant.FALSE).child(pid);
                        tempRef.removeValue();

                        DatabaseReference tempRef2 = parkingRef.child(level).child(Constant.TRUE).child(pid);
                        tempRef2.setValue(parkingLot);

                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            mTimeLeftInMilles = START_TIME_IN_MILLIS;
                            running = false;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bookingRef.removeValue();
    }

    private void startStop() {
        if (running) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void stopTimer() {
        countDownTimer.cancel();
        running = false;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftInMilles, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMilles = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                running = false;
                cancelBooking();
            }
        }.start();

        running = true;
    }

    private void updateTimer() {
        int minute = (int) (mTimeLeftInMilles / 1000) / 60;
        int second = (int) (mTimeLeftInMilles / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minute, second);


        timerView.setText(timeLeftFormatted);

    }
}