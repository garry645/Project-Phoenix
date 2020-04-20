package com.example.projectphoenix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectphoenix.databinding.FragmentTournamentsBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import static com.example.projectphoenix.MainActivity.user;

public class TournamentsFragment extends Fragment {

    private FragmentTournamentsBinding binding;
    private TournamentAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tournaments, container, false);

        setUpRecyclerView();

        if(user != null) {
            if (user.getUserType().equals("operator")) {
                binding.createTournamentBT.setVisibility(View.VISIBLE);
                binding.createTournamentBT.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tournamentsFragment_to_addTournamentFragment));
            }
        }

        MainActivity.mTournamentsFragment = this;
        MainActivity.enableNavDrawer();
        return binding.getRoot();

    }


    private void setUpRecyclerView() {
        Query query = MainActivity.tournaments.orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Tournament> options = new FirestoreRecyclerOptions.Builder<Tournament>()
                .setQuery(query, Tournament.class)
                .build();

        adapter = new TournamentAdapter(options);

        RecyclerView recyclerView = binding.tournamentRecyclerView;
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

    void onBackPressed() {
        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.gameScreenFragment);
    }

}

class TournamentAdapter extends FirestoreRecyclerAdapter<Tournament, TournamentAdapter.TournamentHolder> {

    TournamentAdapter(@NonNull FirestoreRecyclerOptions<Tournament> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TournamentHolder holder, int position, @NonNull Tournament model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewGameTitle.setText(model.getGameTitle());
        holder.textViewDate.setText(model.getDate());
        holder.textViewTime.setText(model.getTime());
        holder.v.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", model.getTitle());
            bundle.putString("gameTitle", model.getGameTitle());
            bundle.putString("date", model.getDate());
            bundle.putString("time", model.getTime());
            bundle.putString("maxNumPlayers", String.valueOf(model.getMaxNumPlayers()));
            bundle.putString("currNumPlayers", String.valueOf(model.getCurrNumPlayers()));
            Navigation.findNavController(view).navigate(R.id.action_tournamentsFragment_to_tournamentDetailsFragment, bundle);
        });
    }

    @NonNull
    @Override
    public TournamentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_card_layout,
                parent, false);
        return new TournamentHolder(v);
    }

    static class TournamentHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewGameTitle;
        TextView textViewDate;
        TextView textViewTime;
        View v;

        TournamentHolder(View itemView) {
            super(itemView);

            v = itemView;
            textViewTitle = itemView.findViewById(R.id.tournamentTitleTV);
            textViewGameTitle = itemView.findViewById(R.id.tournamentGameTitleTV);
            textViewDate = itemView.findViewById(R.id.dateTV);
            textViewTime = itemView.findViewById(R.id.timeTV);
        }
    }
}
