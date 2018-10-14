package uts.sep.westfieldparkmate.Activity.SystemAdminActivity;


import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import uts.sep.westfieldparkmate.Model.Constant;
import uts.sep.westfieldparkmate.Model.Feedback;
import uts.sep.westfieldparkmate.R;

import static android.support.constraint.Constraints.TAG;
import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.Q;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeFeedBackFragment extends Fragment {

    RecyclerView mRecyclerView;
    FeedbackAdapter mFeedbackAdapter;
    List<Feedback> feedbacks = new ArrayList<>();
    DatabaseReference feedbackRef;

    public SeeFeedBackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_feed_back, container, false);
        // Inflate the layout for this fragment
        feedbackRef = FirebaseDatabase.getInstance().getReference().child(Constant.FEEDBACK);
        mFeedbackAdapter = new FeedbackAdapter(getActivity(), getFeedback());
        mRecyclerView = view.findViewById(R.id.feedback_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mFeedbackAdapter);

        return view;
    }


    private List<Feedback> getFeedback() {

        feedbackRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Feedback current = post.getValue(Feedback.class);
                    feedbacks.add(current);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return feedbacks;
    }


}