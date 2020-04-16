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

public class GameDevApplicationFragment extends Fragment {
    private FirebaseFirestore db;

    private FragmentGameDevApplicationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_dev_application, container, false);
        db = FirebaseFirestore.getInstance();
        binding.submitAppBT.setOnClickListener(view -> {
            String email = Objects.requireNonNull(binding.devAppEmailTV.getEditText()).getText().toString().toLowerCase().trim();
            String franchise = Objects.requireNonNull(binding.devAppFranchiseTV.getEditText()).getText().toString().toLowerCase().trim();
            String platform = Objects.requireNonNull(binding.devAppPlatformTV.getEditText()).getText().toString().toLowerCase().trim();
            DevApplication devApplication = new DevApplication(email, franchise, platform);
            db.collection("DevApps").document(email).set(devApplication).addOnCompleteListener(task -> {
                Toast.makeText(getContext(), "Application submitted successfully!", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).navigate(R.id.gameScreenFragment);
            });

        });
        return binding.getRoot();
    }
}