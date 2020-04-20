package com.example.projectphoenix;

import android.annotation.SuppressLint;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.example.projectphoenix.GameScreenFragment.setGameDevBT;

/**
 * MainActivity:
 *      Main screen that holds all of our fragment objects. This is what the NavigationController uses
 *      to switch between the different screens within the application.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    protected static GameScreenFragment mGameScreenFragment;
    protected static TournamentsFragment mTournamentsFragment;
    protected static LoginFragment mLoginFragment;
    protected static SignupFragment mSignupFragment;

    protected static FirebaseAuth mAuth;
    protected static User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected static CollectionReference users;
    protected static CollectionReference games;
    protected static CollectionReference tournaments;
    protected static CollectionReference devApps;


    @SuppressLint("StaticFieldLeak")
    static Toolbar toolbar;
    private static DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    protected static NavigationView navView;
    @SuppressLint("StaticFieldLeak")
    protected static NavController navController;
    protected static Menu navMenu;


    //Method to create the MainActivity screen.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        users = db.collection("users");
        games = db.collection("games");
        tournaments = db.collection("tournaments");
        devApps = db.collection("devApps");

        navView = findViewById(R.id.nav_view);
        navMenu = navView.getMenu();

        navController = Navigation.findNavController(this, R.id.navHostFragment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.loginFragment, R.id.signupFragment, R.id.tournamentsFragment, R.id.gameScreenFragment,
                R.id.listDevAppsFragment)
                .setDrawerLayout(drawerLayout).build();
        navView.setNavigationItemSelectedListener(this);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);


    }

    /**
     * onStart:
     *      Checks if there is already a User logged in and starts the updateUI function.
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     *      updateUI:
     *              If the given User is null (IE a User is not logged in currently) then this function
     *              navigates to the LoginFragment screen. If the User is not null then the User is
     *              fetched from the database and the NavigationDrawer is updated based on the User's
     *              "userType".
     *
     * @param userIn    A user in the application. Can be null.
     */
    static void updateUI(FirebaseUser userIn) {
        navMenu.findItem(R.id.listDevAppsFragment).setVisible(false);
        if (userIn != null) {
            String email = Objects.requireNonNull(userIn.getEmail()).toLowerCase().trim();
            users.document(email).get().addOnCompleteListener(task -> {
                user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                if (user != null) {
                    TextView usernameTV = navView.getHeaderView(0).findViewById(R.id.headerUsername);
                    usernameTV.setText(user.getUsername());
                    TextView gamerPointsTV = navView.getHeaderView(0).findViewById(R.id.headerGamerPoints);
                    gamerPointsTV.setText(String.valueOf(user.getGamerPoints()));
                    switch (user.getUserType()) {
                        case "developer":
                            setGameDevBT();
                            navMenu.findItem(R.id.becomeADev).setVisible(false);
                            break;
                        case "operator":
                            setGameDevBT();
                            navMenu.findItem(R.id.becomeADev).setVisible(false);
                            navMenu.findItem(R.id.listDevAppsFragment).setVisible(true);
                            break;
                        case "user":
                            navMenu.findItem(R.id.becomeADev).setVisible(true);
                            navMenu.findItem(R.id.listDevAppsFragment).setVisible(false);
                            break;
                    }
                }
            });
        } else {
            navController.navigate(R.id.loginFragment);
        }
    }

    /**
     *  disableNavDrawer:
     *          Function used to disable the NavigationDrawer object. Used in the LoginFragment and
     *          SignupFragment screens.
     */
    public static void disableNavDrawer() {
        toolbar.setVisibility(View.GONE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     *  enableNavDrawer:
     *          Function that unlocks the NavigationDrawer after User has access to the application
     *          by signing up or logging in. Used in the GameScreenFragment and TournamentsFragment
     *          screens.
     */
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

    /**
     *  onBackPressed:
     *          Method used to override what the back button does when pressed in each screen.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if(mGameScreenFragment != null) {
            if (mGameScreenFragment.isVisible()) {
                mGameScreenFragment.onBackPressed();
            }
        } else if (mTournamentsFragment != null) {
            if (mTournamentsFragment.isVisible()) {
                mTournamentsFragment.onBackPressed();
            }
        } else if (mLoginFragment != null) {
            if(mLoginFragment.isVisible()) {
                mLoginFragment.onBackPressed();
            }
        } else if (mSignupFragment != null) {
            if(mSignupFragment.isVisible()) {
                mSignupFragment.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }
    }

    /**
     *  setUser:
     *      Function that sets the global User object.
     * @param userIn    User to be set
     */
    static void setUser(User userIn) {
        user = userIn;
    }

    /**
     *  dispatchTouchEvent:
     *          Method that hides the keyboard when the user presses elsewhere on the screen.
     *      @param event    Touch event to process
     *      @return     a boolean
     */
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

    /**
     *  onNavigationItemSelected:
     *          Function that tells the navController where to go when a menu item is pressed in the
     *          DrawerLayout
     *      @param item MenuItem that was pressed.
     *      @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.gameScreenFragment) {
            navController.navigate(R.id.gameScreenFragment);
        } else if (id == R.id.tournamentsFragment) {
            navController.navigate(R.id.tournamentsFragment);
        } else if (id == R.id.loginFragment) {
            user = null;
            TextView usernameTV = navView.getHeaderView(0).findViewById(R.id.headerUsername);
            TextView gamerPoints = navView.getHeaderView(0).findViewById(R.id.headerGamerPoints);
            usernameTV.setText(R.string.username);
            gamerPoints.setText(R.string.gamerPoints);
            mAuth.signOut();
            navController.navigate(R.id.loginFragment);
        } else if (id == R.id.gameDevApplicationFragment) {
            navController.navigate(R.id.gameDevApplicationFragment);
        } else if (id == R.id.listDevAppsFragment) {
            navController.navigate(R.id.listDevAppsFragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
