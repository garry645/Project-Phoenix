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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projectphoenix.databinding.FragmentAddTournamentBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddTournamentFragment extends Fragment {

    //Binding object holds all of AddTournamentFragments View objects
    private FragmentAddTournamentBinding binding;

    //Method to create AddTournament screen
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_tournament, container, false);
        binding.createTournamentBT.setOnClickListener(view -> addNewTournament());
        return binding.getRoot();
    }


    /**
     *  addNewTournament:
     *      Gathers the users information and validates it. If any of it is invalid then an error is
     *      set, else a new Tournament is created and added to the database.
     */
    private void addNewTournament() {
        String title = Objects.requireNonNull(binding.addTournamentTitleTV.getEditText()).getText().toString().trim();
        String gameTitle = Objects.requireNonNull(binding.addTournamentGameTV.getEditText()).getText().toString().trim();
        String date = Objects.requireNonNull(binding.addTournamentDateTV.getEditText()).getText().toString();
        String time = Objects.requireNonNull(binding.addTournamentTimeTV.getEditText()).getText().toString();
        String maxPlayers = Objects.requireNonNull(binding.addTournamentMaxNumPlayersTV.getEditText()).getText().toString();
        int maxNumPlay = 0;
        if(!maxPlayers.equals("")) {
            maxNumPlay = Integer.parseInt(maxPlayers);
        }
        String description = Objects.requireNonNull(binding.addTournamentDescriptionTV.getEditText()).getText().toString();
        if (validateForm(title, gameTitle, date, time, maxNumPlay, description)) {
            Tournament tourney = new Tournament(title, gameTitle, date, time, maxNumPlay, description);
            FirebaseFirestore.getInstance().collection("tournaments").document(title).set(tourney).addOnCompleteListener(view -> {
                Toast.makeText(getContext(), "Tournament added succesfully!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addTournamentFragment_to_tournamentsFragment);
            });
        }

    }

    /**
     * validateForm:
     *
     *      @param titleIn  Title of Tournament
     *      @param gameTitleIn  Name of Game to be played
     *      @param dateIn   Date of new Tournament
     *      @param timeIn   Time of new Tournament
     *      @param maxNumPlayersIn  Maximum number of players in Tournament
     *      @param descriptionIn    Description of new Tournament
     *      @return
     *
     *  Checks if titleIn, dateIn, timeIn, or descriptionIn equals an empty string or if maxNumPlayersIn
     *  is > 1 and < 65.
     *
     */
    private boolean validateForm(String titleIn, String gameTitleIn, String dateIn, String timeIn, int maxNumPlayersIn, String descriptionIn) {
        boolean valid = true;

        if(titleIn.equals("")) {
            valid = false;
            Objects.requireNonNull(binding.addTournamentTitleTV.getEditText()).setError("Title must not be empty!");
        }

        if(gameTitleIn.equals("")) {
            valid = false;
            Objects.requireNonNull(binding.addTournamentGameTV.getEditText()).setError("You must list a name!");
        }

        if(dateIn.equals("")) {
            valid = false;
            Objects.requireNonNull(binding.addTournamentDateTV.getEditText()).setError("Date must not be empty!");
        }

        if(timeIn.equals("")) {
            valid = false;
            Objects.requireNonNull(binding.addTournamentTimeTV.getEditText()).setError("You must have a time!");
        }

        if(maxNumPlayersIn > 64) {
            valid = false;
            Objects.requireNonNull(binding.addTournamentMaxNumPlayersTV.getEditText()).setError("Max number of players must not exceed 64");
        }
        if(maxNumPlayersIn < 2) {
            valid = false;
            Objects.requireNonNull(binding.addTournamentMaxNumPlayersTV.getEditText()).setError("Max number of players must be larger than 1");
        }

        if(descriptionIn.equals("")) {
            valid = false;
            Objects.requireNonNull(binding.addTournamentDescriptionTV.getEditText()).setError("You must have a description!");
        }

        return valid;
    }
}
