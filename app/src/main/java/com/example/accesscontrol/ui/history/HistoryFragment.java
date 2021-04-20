package com.example.accesscontrol.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accesscontrol.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    private RecyclerView recyclerView;
    private myAdapter adapter;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.history_fragment, container, false);
        View root = inflater.inflate(R.layout.history_fragment, container, false);
        recyclerView = root.findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        FirebaseRecyclerOptions<historyModel> options =
                new FirebaseRecyclerOptions.Builder<historyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("History"), historyModel.class)
                        .build();
        //Log.d("History",FirebaseDatabase.getInstance().getReference().child("History").child("62f65c64-d55b-4c7d-ae84-dd4051feb20a").toString());
        adapter = new myAdapter(options);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        // TODO: Use the ViewModel

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }

}