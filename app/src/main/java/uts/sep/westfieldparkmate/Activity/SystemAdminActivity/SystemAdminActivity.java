package uts.sep.westfieldparkmate.Activity.SystemAdminActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import uts.sep.westfieldparkmate.R;

public class SystemAdminActivity extends AppCompatActivity {

    private Button addNewParking, feedback;
    private ViewPager mViewPager;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentSearchAdapter adapter = new FragmentSearchAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddNewParkingFragment(), "Add New Parking");
        adapter.addFragment(new SeeFeedBackFragment(), "See Feedback");
        viewPager.setAdapter(adapter);
    }

}