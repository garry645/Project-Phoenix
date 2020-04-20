/*
 * AddGameFragment:
 *      Shows a form for the Developer to fill out
 *      User fills out form and presses "Create Game" button
 *      Form is validated, if there is an error then it is shown, else new Game object is created
 *      New Game object is added to database
 */

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

import com.example.projectphoenix.databinding.FragmentAddGameBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.example.projectphoenix.MainActivity.user;

public class AddGameFragment extends Fragment {

    //Binding holds all of the fragments View objects
    FragmentAddGameBinding binding;


    //Method to create AddGameFragment screen
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_game, container, false);

        addGame();

        return binding.getRoot();
    }

    /**
     * Checks the input of the name EditText and verifies it is not null, if it is null then an error
     * is set on the EditText else it creates a new Game object and adds it to the database.
     */
    private void addGame() {
        binding.addGameBT.setOnClickListener(view -> {
            String name = Objects.requireNonNull(binding.addGameNameTV.getEditText()).getText().toString();
            if(!name.trim().toLowerCase().equals("")) {
                Game game = new Game(user.getFranchiseName(), user.getPlatform(), name);
                FirebaseFirestore.getInstance().collection("games").document(name).set(game).addOnCompleteListener(view2 -> Navigation.findNavController(view).navigate(R.id.gameScreenFragment));
            } else {
                binding.addGameNameTV.getEditText().setError("Name must not be empty!");
            }
            });
    }
}
