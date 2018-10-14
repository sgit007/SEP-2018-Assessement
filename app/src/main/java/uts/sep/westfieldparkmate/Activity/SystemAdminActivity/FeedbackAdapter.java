package uts.sep.westfieldparkmate.Activity.SystemAdminActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import uts.sep.westfieldparkmate.Model.Feedback;
import uts.sep.westfieldparkmate.R;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private Context mContext;
    private List<Feedback> mFeedback;

    FeedbackAdapter(Context context, List<Feedback> feedback) {
        mContext = context;
        mFeedback = feedback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

            holder.userName.setText(mFeedback.get(position).getUser());
            holder.feedback.setText(mFeedback.get(position).getFeedback());

    }

    @Override
    public int getItemCount() {
        return mFeedback.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, feedback;

        public MyViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_feedback);
            feedback = itemView.findViewById(R.id.user_feedback);

        }
    }
}
