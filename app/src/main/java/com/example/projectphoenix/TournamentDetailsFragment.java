package com.example.projectphoenix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projectphoenix.databinding.FragmentTournamentDetailsBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.example.projectphoenix.MainActivity.user;

public class TournamentDetailsFragment extends Fragment {
    FragmentTournamentDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tournament_details, container, false);
        assert getArguments() != null;
        String title = getArguments().getString("title");
        binding.tourneyDetailsTitleTV.setText(getArguments().getString("title"));
        binding.tourneyDetailsGameTitleTV.setText(getArguments().getString("gameTitle"));
        binding.tourneyDetailsDateTV.setText(getArguments().getString("date"));
        binding.tourneyDetailsTimeTV.setText(getArguments().getString("time"));
        binding.currNumberOfParticipantsTV.setText(getArguments().getString("currNumPlayers"));
        binding.tourneyDetailsMaxNumPlayersTV.setText(getArguments().getString("maxNumPlayers"));

        int curr = Integer.parseInt(Objects.requireNonNull(getArguments().getString("currNumPlayers")));
        int max = Integer.parseInt(Objects.requireNonNull(getArguments().getString("maxNumPlayers")));

        assert title != null;
        FirebaseFirestore.getInstance().collection("tournaments").document(title).get().addOnCompleteListener(task -> {
            Tournament tournament = Objects.requireNonNull(task.getResult()).toObject(Tournament.class);
            assert tournament != null;
            if (!tournament.containsPlayer(user.getEmail()) && curr < max) {
                binding.tourneyDetailsJoinTourneyBT.setVisibility(View.VISIBLE);
                binding.tourneyDetailsJoinTourneyBT.setOnClickListener(view -> {
                    tournament.addPlayer(user.getEmail());
                    FirebaseFirestore.getInstance().collection("tournaments").document(title).update("currNumPlayers", curr + 1, "players", tournament.getPlayers())
                            .addOnCompleteListener(view2 ->
                                    Navigation.findNavController(view).navigate(R.id.action_tournamentDetailsFragment_to_tournamentsFragment));
                });
            } else {
                binding.tourneyDetailsJoinTourneyBT.setVisibility(View.GONE);
            }
        });

        return binding.getRoot();
    }
}
