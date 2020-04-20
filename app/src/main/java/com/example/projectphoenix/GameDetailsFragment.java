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

/**
 * GameDetailsFragment:
 * Shows details of the chosen game. If "Join Match" button is pressed then User is placed
 * in a game.
 */
public class GameDetailsFragment extends Fragment {

    //Method to create the GameDetailsFragment screen
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.projectphoenix.databinding.FragmentGameDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_details, container, false);

        assert getArguments() != null;
        binding.gameDetailsGameTV.setText(getArguments().getString("gameTitle"));
        binding.gameDetailsFranchiseTV.setText(getArguments().getString("gameFranchise"));
        binding.gameDetailsPlatformTV.setText(getArguments().getString("gamePlatform"));


        binding.PlayGameBT.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_gameDetailsFragment_to_playGameFragment));
        return binding.getRoot();
    }
}
