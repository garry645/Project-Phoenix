package com.example.projectphoenix;

import android.annotation.SuppressLint;
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

import com.example.projectphoenix.databinding.FragmentGameScreenBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import static com.example.projectphoenix.MainActivity.mAuth;
import static com.example.projectphoenix.MainActivity.updateUI;
/**
 *  GameScreenFragment:
 *      Screen that lists all of the Games stored in the database.
 */
public class GameScreenFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static FragmentGameScreenBinding binding;

    private GameRVAdapter adapter;

    //Method to create GameScreenFragment object.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_screen, container, false);

        MainActivity.enableNavDrawer();
        MainActivity.mGameScreenFragment = this;
        if (mAuth.getCurrentUser() != null) {
            updateUI(mAuth.getCurrentUser());
        }
        binding.addNewGameBT.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.addGameFragment));

        setUpRecyclerView();

        return binding.getRoot();
    }

    //Sets the visibility of GameDeveloper button
    static void setGameDevBT() {
        binding.addNewGameBT.setVisibility(View.VISIBLE);
    }

    /**
     *  setUpRecyclerView:
     *       Queries the "games" database collection and feeds the data to the GameRVAdapter to be
     *       adapted to fit in the RecyclerView.
     */
    private void setUpRecyclerView() {
        Query query = MainActivity.games.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();

        adapter = new GameRVAdapter(options);

        RecyclerView recyclerView = binding.gameRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     *  onStart:
     *      Tells the adapter to start listening for data changes.
     */
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    /**
     *  onStop:
     *      Tells the adapter to stop listening for data changes.
     */
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    void onBackPressed() {
        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.tournamentsFragment);
    }


}

/*
    * GameRVAdapter:
    *       Adapter class to fit Game data into game_card_layout.xml layout.
 */
class GameRVAdapter extends FirestoreRecyclerAdapter<Game, GameRVAdapter.GameHolder> {

    GameRVAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    /**
     *  onBindViewHolder:
     *      @param holder   Object to hold View objects in game_card_layout
     *      @param position     Position in RecyclerView object
     *      @param model    Game object that holds all data from Game objects in database
     */
    @Override
    protected void onBindViewHolder(@NonNull GameHolder holder, int position, @NonNull Game model) {
        holder.textViewTitle.setText(model.getName());
        holder.textViewFranchise.setText(model.getGameFranchise());
        holder.textViewPlatform.setText(model.getGamePlatform());

        holder.v.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("gameTitle", model.getName());
            bundle.putString("gameFranchise", model.getGameFranchise());
            bundle.putString("gamePlatform", model.getGamePlatform());
            Navigation.findNavController(view).navigate(R.id.action_gameScreenFragment_to_gameDetailsFragment, bundle);
        });
    }

    //Method to create the game_card_layout View
    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card_layout,
                parent, false);
        return new GameHolder(v);
    }

    /*
        * GameHolder:
        *       Class to hold references to the game_card_layout Views.
     */
    static class GameHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewFranchise;
        TextView textViewPlatform;
        View v;

        GameHolder(View itemView) {
            super(itemView);
            v = itemView;
            textViewTitle = itemView.findViewById(R.id.titleTV);
            textViewFranchise = itemView.findViewById(R.id.franchiseTV);
            textViewPlatform = itemView.findViewById(R.id.platformTV);
        }
    }


}





