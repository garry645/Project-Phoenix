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

import com.example.projectphoenix.databinding.FragmentGameDetailsBinding;

import static com.example.projectphoenix.MainActivity.user;

public class GameDetailsFragment extends Fragment {

    FragmentGameDetailsBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_details, container, false);

    binding.gameDetailsGameTV.setText(getArguments().getString("gameTitle"));
    binding.gameDetailsFranchiseTV.setText(getArguments().getString("gameFranchise"));
    binding.gameDetailsPlatformTV.setText(getArguments().getString("gamePlatform"));


    binding.PlayGameBT.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_gameDetailsFragment_to_playGameFragment));
    return binding.getRoot();
    }
}
