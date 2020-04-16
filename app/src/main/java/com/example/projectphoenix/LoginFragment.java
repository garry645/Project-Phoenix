package com.example.projectphoenix;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projectphoenix.databinding.FragmentLoginBinding;

import java.util.Objects;

//import static com.example.projectphoenix.MainActivity.enableNavDrawer;
import static com.example.projectphoenix.MainActivity.mAuth;
import static com.example.projectphoenix.MainActivity.setUser;
import static com.example.projectphoenix.MainActivity.user;
import static com.example.projectphoenix.MainActivity.userRef;
import static com.example.projectphoenix.MainActivity.userSnapshot;
import static com.example.projectphoenix.MainActivity.users;

public class LoginFragment extends Fragment {

    private String email;
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_login, container, false);

        mAuth.signOut();
        MainActivity.currentFragment = this;

        MainActivity.disableNavDrawer();

        binding.loginBT.setOnClickListener(view -> {
            email = binding.loginEmailET.getEditText().getText().toString().trim().toLowerCase();
            login();
        });

        //MainActivity.disableNavDrawer();

        binding.loginSignupBT.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_signupFragment));

        return binding.getRoot();
    }

    private void login() {
        binding.loginBT.setEnabled(false);
        if (validateForm()) {
            mAuth.signInWithEmailAndPassword(email, Objects.requireNonNull(binding.loginPasswordET.getEditText()).getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userRef = users.document(email);
                            userRef.get().addOnCompleteListener(task1 -> {
                                userSnapshot = task1.getResult();
                                if (userSnapshot != null) {
                                    user = userSnapshot.toObject(User.class);
                                    setUser(user);
                                    //enableNavDrawer((AppCompatActivity) getActivity());
                                    Navigation.findNavController(Objects.requireNonNull(this.getView())).navigate(R.id.action_loginFragment_to_gameScreenFragment);
                                }
                            });

                        } else {
                            Log.w("Tag", "signInWithEmail:failure", task.getException());
                            Toast.makeText(this.getActivity(), String.valueOf(task.getException()),
                                    Toast.LENGTH_LONG).show();
                            binding.loginBT.setEnabled(true);
                        }
                    });
        } else {
            binding.loginBT.setEnabled(true);
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        if (email.equals("")) {
            binding.loginEmailET.setError("Email must not be empty!");
            valid = false;
        }
        if (binding.loginPasswordET.getEditText().getText().toString().trim().equals("")) {
            binding.loginPasswordET.setError("Password must not be empty!");
            valid = false;
        }
        return valid;
    }
}

