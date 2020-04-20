package com.example.projectphoenix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectphoenix.databinding.FragmentListDevAppsBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 *  ListDevAppsFragment:
 *      Screen that lists all of the Developer applications
 */
public class ListDevAppsFragment extends Fragment {

    private FragmentListDevAppsBinding binding;

    private DevAppRVAdapter adapter;

    //Method that creates the ListDevAppsFragment screen
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_dev_apps, container, false);
        setUpRecyclerView();
        return binding.getRoot();
    }


    //Method that gets all DevApplication objects from the database and loads them into our RecyclerView
    private void setUpRecyclerView() {
        Query query = MainActivity.devApps.orderBy("email", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<DevApplication> options = new FirestoreRecyclerOptions.Builder<DevApplication>()
                .setQuery(query, DevApplication.class)
                .build();

        adapter = new DevAppRVAdapter(options);

        RecyclerView recyclerView = binding.devAppsRV;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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


/**
 *  DevAppRVAdapter
 *          Class that adapts data from database into the dev_app_card_layout layout
 */
class DevAppRVAdapter extends FirestoreRecyclerAdapter<DevApplication, DevAppRVAdapter.DevAppHolder> {

    DevAppRVAdapter(@NonNull FirestoreRecyclerOptions<DevApplication> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull DevAppHolder holder, int position, @NonNull DevApplication model) {
        holder.textViewEmail.setText(model.getEmail());
        holder.textViewFranchise.setText(model.getFranchise());
        holder.textViewPlatform.setText(model.getPlatform());
        holder.buttonRejectApp.setOnClickListener(view -> getSnapshots().getSnapshot(position).getReference().delete());
        holder.buttonAcceptApp.setOnClickListener(view -> {
            CollectionReference users = FirebaseFirestore.getInstance().collection("users");
            users.document(model.getEmail().toLowerCase()).update("userType", "developer", "franchiseName", model.getFranchise(), "platform", model.getPlatform());
            getSnapshots().getSnapshot(position).getReference().delete();
        });
    }

    @NonNull
    @Override
    public DevAppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dev_app_card_layout,
                parent, false);
        return new DevAppHolder(v);
    }

    static class DevAppHolder extends RecyclerView.ViewHolder {
        TextView textViewEmail;
        TextView textViewFranchise;
        TextView textViewPlatform;
        Button buttonAcceptApp;
        Button buttonRejectApp;
        View v;

        DevAppHolder(View itemView) {
            super(itemView);
            v = itemView;
            textViewEmail = itemView.findViewById(R.id.devAppEmail);
            textViewFranchise = itemView.findViewById(R.id.devAppFranchise);
            textViewPlatform = itemView.findViewById(R.id.devAppPlatform);
            buttonAcceptApp = itemView.findViewById(R.id.devAppAcceptBT);
            buttonRejectApp = itemView.findViewById(R.id.devAppRejectBT);
        }
    }


}