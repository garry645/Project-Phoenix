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

import com.example.projectphoenix.databinding.FragmentGameDevApplicationBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.example.projectphoenix.MainActivity.user;

public class GameDevApplicationFragment extends Fragment {
    private FirebaseFirestore db;

    private FragmentGameDevApplicationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_dev_application, container, false);
        db = FirebaseFirestore.getInstance();
        binding.submitAppBT.setOnClickListener(view -> {
            String email = user.getEmail();
            String franchise = Objects.requireNonNull(binding.devAppFranchiseTV.getEditText()).getText().toString().toLowerCase().trim();
            String platform = Objects.requireNonNull(binding.devAppPlatformTV.getEditText()).getText().toString().trim();
            if(validateForm(franchise, platform)) {
                DevApplication devApplication = new DevApplication(email, franchise, platform);
                db.collection("devApps").document(email).set(devApplication).addOnCompleteListener(task -> {
                    Toast.makeText(getContext(), "Application submitted successfully!", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).navigate(R.id.gameScreenFragment);
                });
            }
        });
        return binding.getRoot();
    }

    private boolean validateForm(String franchiseIn, String platformIn) {
        boolean valid = true;
        if(franchiseIn.equals("")) {
            Objects.requireNonNull(binding.devAppFranchiseTV.getEditText()).setError("Franchise name must not be empty!");
            valid = false;
        }
        if(platformIn.equals("")) {
            Objects.requireNonNull(binding.devAppPlatformTV.getEditText()).setError("You must have a platform!");
            valid = false;
        }
        return valid;
    }
}