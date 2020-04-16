package com.example.projectphoenix;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.example.projectphoenix.GameScreenFragment.setGameDevBT;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    protected static GameScreenFragment mGameScreenFragment;
    protected static TournamentsFragment mTournamentsFragment;
    protected static Fragment currentFragment;

    protected static FirebaseAuth mAuth;
    protected static User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected static CollectionReference users;
    protected static CollectionReference games;
    protected static CollectionReference tournaments;
    protected static DocumentReference userRef;
    protected static DocumentSnapshot userSnapshot;


    static Toolbar toolbar;
    private static DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    protected static NavigationView navView;
    protected static NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        users = db.collection("users");
        games = db.collection("games");
        tournaments = db.collection("tournaments");

        navView = findViewById(R.id.nav_view);


        navController = Navigation.findNavController(this, R.id.navHostFragment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.loginFragment, R.id.signupFragment, R.id.tournamentsFragment, R.id.gameScreenFragment)
                .setDrawerLayout(drawerLayout).build();
        navView.setNavigationItemSelectedListener(this);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);


    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    static void updateUI(FirebaseUser userIn) {
        if (userIn != null) {
            String email = userIn.getEmail();
            assert email != null;
            users.document(email).get().addOnCompleteListener(task -> {
                user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                if(user != null) {
                    TextView usernameTV = navView.getHeaderView(0).findViewById(R.id.headerUsername);
                    usernameTV.setText(user.getUsername());
                    TextView gamerPointsTV = navView.getHeaderView(0).findViewById(R.id.headerGamerPoints);
                    gamerPointsTV.setText(String.valueOf(user.getGamerPoints()));
                    if (user.getUserType().equals("developer")) {
                        setGameDevBT();
                        Menu navMenu = navView.getMenu();
                        navMenu.findItem(R.id.becomeADev).setVisible(false);
                    }
                } else {
                    navController.navigate(R.id.loginFragment);
                }
               // usernameTV.setText(user.getUsername());
                //gamerPoints.setText(String.valueOf(user.getGamerPoints()));
            });
        } else {
            navController.navigate(R.id.loginFragment);
        }
    }

    public static void disableNavDrawer() {
        toolbar.setVisibility(View.GONE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public static void enableNavDrawer() {
        if (user != null) {
           TextView headerUsername = navView.getHeaderView(0).findViewById(R.id.headerUsername);
            headerUsername.setText(user.getUsername());
            TextView headerGamerPoints = navView.getHeaderView(0).findViewById(R.id.headerGamerPoints);
            headerGamerPoints.setText(String.valueOf(user.getGamerPoints()));
            //usernameTV.setText(user.getUsername());
           //gamerPoints.setText(String.valueOf(user.getGamerPoints()));
        }
        toolbar.setVisibility(View.VISIBLE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (mGameScreenFragment.isVisible()) {
            mGameScreenFragment.onBackPressed();
        } else if (mTournamentsFragment.isVisible()) {
            mTournamentsFragment.onBackPressed();
        } else
            super.onBackPressed();
    }

    static void setUser(User userIn) {
        user = userIn;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.gameScreenFragment) {
            navController.navigate(R.id.gameScreenFragment);
        } else if(id == R.id.tournamentsFragment) {
            navController.navigate(R.id.tournamentsFragment);
        } else if(id == R.id.loginFragment) {
            user = null;
            TextView usernameTV = navView.getHeaderView(0).findViewById(R.id.headerUsername);
            TextView gamerPoints = navView.getHeaderView(0).findViewById(R.id.headerGamerPoints);
            usernameTV.setText(R.string.username);
            gamerPoints.setText(R.string.gamerPoints);
            mAuth.signOut();
            navController.navigate(R.id.loginFragment);
        } else if(id == R.id.gameDevApplicationFragment) {
            navController.navigate(R.id.gameDevApplicationFragment);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
