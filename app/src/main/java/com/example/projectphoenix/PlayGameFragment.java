package com.example.projectphoenix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.projectphoenix.databinding.FragmentPlayGameBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.example.projectphoenix.MainActivity.user;
import static com.example.projectphoenix.MainActivity.users;

/**
 * PlayGameFragment:
 * Simple Fragment made to simulate playing the games listed in the GameScreenFragment
 */
public class PlayGameFragment extends Fragment {
    private FragmentPlayGameBinding binding;
    private User opponent;


    //Method to create PlayGameFragment
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_game, container, false);

        binding.P1UNTV.setText(user.getUsername());
        binding.P1GPTV.setText(String.valueOf(user.getGamerPoints()));


        findMatch();


        return binding.getRoot();
    }

    /**
     * findMatch:
     * Method that sets the given User's information in the UI and then finds another
     * User object in the database for the given User to play against.
     */
    private void findMatch() {
        binding.newGameBT.setVisibility(View.INVISIBLE);
        binding.P2UNTV.setText(R.string.finding_user);
        binding.P2GPTV.setVisibility(View.GONE);
        binding.P1WinLoseTV.setVisibility(View.GONE);
        binding.P2WinLoseTV.setVisibility(View.GONE);
        users.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> userList = new ArrayList<>();
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    User user = document.toObject(User.class);
                    assert user != null;
                    if (!user.getEmail().equals(MainActivity.user.getEmail()))
                        userList.add(user);
                }

                int player = new Random().nextInt(userList.size());
                opponent = userList.get(player);
                binding.P2UNTV.setText(opponent.getUsername());
                binding.P2GPTV.setText(String.valueOf(opponent.getGamerPoints()));
                binding.P2GPTV.setVisibility(View.VISIBLE);
                playGame(user, opponent);
            }
        });
    }

    /**
     * playGame:
     * Method that chooses the winner randomly and then rewards the winner with
     * a "gamerPoint"
     *
     * @param user1 The current User using the app
     * @param user2 The User randomly fetched from the database
     */
    private void playGame(User user1, User user2) {
        int winner = new Random().nextInt(2);

        if (winner == 0) {
            users.document(user1.getEmail()).update("gamerPoints", user1.getGamerPoints() + 1);
            user.setGamerPoints(user.getGamerPoints() + 1);
            binding.P1GPTV.setText(String.valueOf(user.getGamerPoints()));
            binding.P1WinLoseTV.setText(R.string.winner);
            binding.P2WinLoseTV.setText(R.string.loser);
        } else {
            users.document(user2.getEmail()).update("gamerPoints", user2.getGamerPoints() + 1);
            binding.P2WinLoseTV.setText(R.string.winner);
            binding.P1WinLoseTV.setText(R.string.loser);
        }
        binding.P2WinLoseTV.setVisibility(View.VISIBLE);
        binding.P1WinLoseTV.setVisibility(View.VISIBLE);
        binding.newGameBT.setVisibility(View.VISIBLE);
        binding.newGameBT.setOnClickListener(view -> findMatch());
    }
}
