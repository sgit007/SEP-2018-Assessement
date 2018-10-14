package uts.sep.westfieldparkmate.Activity.SystemAdminActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uts.sep.westfieldparkmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeFeedBackFragment extends Fragment {


    public SeeFeedBackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_see_feed_back, container, false);
    }

}
