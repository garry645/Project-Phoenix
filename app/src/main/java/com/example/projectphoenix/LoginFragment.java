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

import com.example.projectphoenix.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.example.projectphoenix.MainActivity.setUser;

/**
 * LoginFragment:
 * Fragment that handles all of the login logic. Uses Google Authentication to verify that
 * the email has an account associated with this application.
 */
public class LoginFragment extends Fragment {

    private String email;
    private FragmentLoginBinding binding;
    private CollectionReference users;
    private FirebaseAuth mAuth;

    private static DocumentReference userRef;

    //Method to create the LoginFragment screen.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_login, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = db.collection("users");
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        MainActivity.mLoginFragment = this;

        MainActivity.disableNavDrawer();

        binding.loginBT.setOnClickListener(view -> {
            email = Objects.requireNonNull(binding.loginEmailET.getEditText()).getText().toString().trim().toLowerCase();
            login();
        });

        binding.loginSignupBT.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_signupFragment));

        return binding.getRoot();
    }

    /**
     * login:
     * Method grabs the given email and password from the User and attempts to login. If the
     * login attempt is successful then the User is sent to the GameScreenFragment else the
     * appropriate errors are set.
     */
    private void login() {
        binding.loginBT.setEnabled(false);
        if (validateForm()) {
            mAuth.signInWithEmailAndPassword(email, Objects.requireNonNull(binding.loginPasswordET.getEditText()).getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userRef = users.document(email);
                            userRef.get().addOnSuccessListener(documentSnapshot -> {
                                User user = documentSnapshot.toObject(User.class);
                                if (user != null) {
                                    setUser(user);
                                    Navigation.findNavController(Objects.requireNonNull(this.getActivity()), R.id.navHostFragment).navigate(R.id.action_loginFragment_to_gameScreenFragment);
                                } else {
                                    Toast.makeText(getContext(), "User is null", Toast.LENGTH_SHORT).show();
                                }
                            });
                          /*  //userSnapshot = task1.getResult();
                            if (task1.getResult() != null) {
                                User user = task1.getResult().toObject(User.class);
                                setUser(user);
                                //MainActivity.enableNavDrawer((AppCompatActivity) getActivity());
                                Navigation.findNavController(Objects.requireNonNull(this.getView())).navigate(R.id.action_loginFragment_to_gameScreenFragment);
                            }*/

                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getContext(), "Invalid email or password!",
                                        Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Error logging in user",
                                        Toast.LENGTH_LONG).show();
                            }
                            binding.loginBT.setEnabled(true);
                        }
                    });
        } else {
            binding.loginBT.setEnabled(true);
        }

    }

    /**
     * validateForm:
     * Verifies that the given email or password is not null.
     *
     * @return true or false depending on if the given values are valid.
     */
    private boolean validateForm() {
        boolean valid = true;
        if (email.equals("")) {
            binding.loginEmailET.setError("Email must not be empty!");
            valid = false;
        }
        if (Objects.requireNonNull(binding.loginPasswordET.getEditText()).getText().toString().trim().equals("")) {
            binding.loginPasswordET.setError("Password must not be empty!");
            valid = false;
        }
        return valid;
    }

    void onBackPressed() {
        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.signupFragment);
    }
}

