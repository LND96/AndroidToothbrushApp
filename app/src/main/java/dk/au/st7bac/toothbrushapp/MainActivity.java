package dk.au.st7bac.toothbrushapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

import dk.au.st7bac.toothbrushapp.Fragments.SettingsFragment;
import dk.au.st7bac.toothbrushapp.Controllers.SettingsCtrl;
import dk.au.st7bac.toothbrushapp.Controllers.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.Services.AlertReceiver;

// inspiration for alarm manager: https://developer.android.com/training/scheduling/alarms
// inspiration for bottom navigation: https://www.section.io/engineering-education/bottom-navigation-bar-in-android/
// inspiration for drawer layout: https://www.youtube.com/watch?v=MTpVJwFROZE&list=RDCMUCoNZZLhPuuRteu02rh7bzsw&start_radio=1&rv=MTpVJwFROZE&t=596
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UpdateDataCtrl updateDataCtrl;
    private SettingsFragment settingsFragment;

    // ui widgets
    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private BottomNavigationView bottomNav;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate controllers
        SettingsCtrl settingsCtrl = SettingsCtrl.getInstance();
        updateDataCtrl = UpdateDataCtrl.getInstance();

        // setup ui widgets
        setupUIWidgets();

        // setup alarm manager for continuously updating tb data
        setupAlarmManager();

        // instantiate settings fragment to load settings
        settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, settingsFragment, Constants.SETTINGS_FRAG)
                .hide(settingsFragment)
                .commit();
    }

    private void setupUIWidgets() {
        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navController = Navigation.findNavController(this, R.id.fragment);
        bottomNav = findViewById(R.id.bottomNavigationView);
        setupBottomNavigation();
        setupDrawerNavigation();
    }

    private void setupBottomNavigation()
    {
        // bottom navigation bar with id to relevant fragments
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment,
                R.id.detailsFragment).build();

        // setup bottom navigation with navigation controller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    private void setupDrawerNavigation()
    {
        // setup drawer layout with navigation controller
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupAlarmManager() {
        // set calendar to 21:00
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);

        // setup alarm manager and intents
        AlarmManager alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent, 0);

        // interval between alarms in ms
        int intervalms = 24 * 60 * 60 * 1000;

        // set inexact repeating alarm that triggers at given time provided by calendar and with given interval
        // once the alarm is triggered, the pending intent will fire
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis() + 10000, intervalms, pendingIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else {
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // handles what happens when selecting an item in navigation drawer
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // if new destination is the same as current destination, do not add it to the back stack
        switch (item.getItemId()) {
            case R.id.nav_settings: {
                if (isValidDestination(R.id.signinFragment)) {
                    Navigation.findNavController(this, R.id.fragment).navigate(R.id.settingsFragment);
                }
                break;
            }
            case R.id.nav_help: {
                if (isValidDestination(R.id.helpFragment)) {
                    Navigation.findNavController(this, R.id.fragment).navigate(R.id.helpFragment);
                }
                break;
            }
            case R.id.nav_Signout: {
                if (isValidDestination(R.id.signinFragment)) {
                    Navigation.findNavController(this, R.id.fragment).navigate(R.id.signinFragment);
                }
                break;
            }
        }
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // validates if id of the destination fragment is equal to current fragment
    private boolean isValidDestination(int destination) {
        return destination != Navigation.findNavController(this, R.id.fragment)
                .getCurrentDestination().getId();
    }

    // enables navigation drawer to open and navigate back when back arrow is pressed
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.fragment), drawerLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDataCtrl.initUpdateTbData(Constants.FROM_MAIN_ACTIVITY);
    }

    @Override
    protected void onPause() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(ToothbrushApp.getAppContext());
        sharedPreferences.edit().putBoolean(Constants.FIRST_RUN, false).apply();
        super.onPause();
    }
}