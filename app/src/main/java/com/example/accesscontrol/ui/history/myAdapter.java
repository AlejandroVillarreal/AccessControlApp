package com.example.accesscontrol.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accesscontrol.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myAdapter extends FirebaseRecyclerAdapter<historyModel, myAdapter.myviewholder> {


    public myAdapter(@NonNull FirebaseRecyclerOptions<historyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder myviewholder, int i, @NonNull historyModel historyModel) {

        myviewholder.date.setText(historyModel.getDate());
        myviewholder.time.setText(historyModel.getHour());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_history, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView date, time;
        RelativeLayout layout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.datetext);
            time = itemView.findViewById(R.id.timetext);
            layout = itemView.findViewById(R.id.profile_view);
        }
    }
}
