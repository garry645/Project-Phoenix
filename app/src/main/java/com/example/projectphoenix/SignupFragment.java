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

import com.example.projectphoenix.databinding.FragmentSignupBinding;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

//import static com.example.projectphoenix.MainActivity.disableNavDrawer;
//import static com.example.projectphoenix.MainActivity.enableNavDrawer;
import static com.example.projectphoenix.MainActivity.mAuth;
import static com.example.projectphoenix.MainActivity.setUser;
import static com.example.projectphoenix.MainActivity.users;

public class SignupFragment extends Fragment {

    private String email;
    private String username;
    private String age;
    private FragmentSignupBinding binding;
    private static DocumentReference userRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_signup, container, false);

        //disableNavDrawer();
        MainActivity.currentFragment = this;
        MainActivity.disableNavDrawer();

        binding.signUpButton.setOnClickListener(view -> {
            email = binding.signupEmailET.getEditText().getText().toString();
            username = Objects.requireNonNull(binding.signupUsernameET.getEditText()).getText().toString();
            age = binding.signupAgeET.getEditText().getText().toString();

            signup();
        });


        binding.swapToLoginButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signupFragment_to_loginFragment));

        return binding.getRoot();
    }


    private void signup() {
        binding.signUpButton.setEnabled(false);
        if (validateForm()) {
            mAuth.createUserWithEmailAndPassword(email, Objects.requireNonNull(binding.signupPasswordET.getEditText()).getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = new User(email, username, Integer.parseInt(age));
                    setUser(user);
                    userRef = users.document(user.getEmail());
                    userRef.set(user).addOnCompleteListener(task1 -> {
                        //enableNavDrawer((AppCompatActivity) getActivity());
                        Navigation.findNavController(Objects.requireNonNull(this.getView())).navigate(R.id.action_signupFragment_to_gameScreenFragment);
                    });
                } else {
                    Log.w("tag", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed." + Objects.requireNonNull(task.getException()).toString(),
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            binding.signUpButton.setEnabled(true);
        }
    }


    private boolean validateForm() {
        boolean valid = true;
        if (email.equals("")) {
            binding.signupEmailET.setError("Email must not be empty!");
            valid = false;
        }
        if (binding.signupPasswordET.getEditText().getText().toString().equals("")) {
            binding.signupPasswordET.setError("Password must not be empty!");
            valid = false;
        }
        if (binding.signupUsernameET.getEditText().getText().toString().equals("")) {
            binding.signupUsernameET.setError("Username must not be empty!");
            valid = false;
        }
        if (binding.signupAgeET.getEditText().getText().toString().equals("")) {
            binding.signupAgeET.setError("Age must not be empty");
            valid = false;
        }
        return valid;
    }
}
