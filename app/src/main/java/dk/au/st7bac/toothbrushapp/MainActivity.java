package dk.au.st7bac.toothbrushapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

import dk.au.st7bac.toothbrushapp.Fragments.SettingsFragment;
import dk.au.st7bac.toothbrushapp.Controllers.SettingsCtrl;
import dk.au.st7bac.toothbrushapp.Controllers.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.Services.AlertReceiver;

// Inspiration for alarm manager: https://developer.android.com/training/scheduling/alarms#java
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    private UpdateDataCtrl updateDataCtrl;
    private SettingsCtrl settingsCtrl;
    private String sensorIDText;
    private SharedPreferences sharedPreferences;
    private Object View;

    private SettingsFragment settingsFragment;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsCtrl = SettingsCtrl.getInstance();
        updateDataCtrl = UpdateDataCtrl.getInstance();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        //bottom navigation
        bottomNavigation();

        //drawer navigation
        drawerNavigation();

        //alarm manager for update tb data on specific time
        alarmManager();



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ToothbrushApp.getAppContext());

        if (savedInstanceState == null) {
            settingsFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment, settingsFragment, "SETTINGS_FRAG")
                    .hide(settingsFragment)
                    .commit();
        } else {
            settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("SETTINGS_FRAG");
            if (settingsFragment == null) {
                settingsFragment = new SettingsFragment();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        updateDataCtrl.initUpdateTbData(Constants.FROM_MAIN_ACTIVITY);
    }

    @Override
    protected void onPause() {

        sharedPreferences.edit().putBoolean(Constants.FIRST_RUN, false).apply();
        super.onPause();
    }

    private void alarmManager() {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);

        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        int requestCode = 0;
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, 0);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+10000,
                86400000, pendingIntent); // 86400000
    }


    public void bottomNavigation()
    {

        //Inspired by: "https://www.section.io/engineering-education/bottom-navigation-bar-in-android/"
        // bottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        //ID passes of different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.detailsFragment).build();

        //Initialize NavController
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    public void drawerNavigation()
    {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);//setup drawer layout with navigation controller
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //https://www.youtube.com/watch?v=MTpVJwFROZE&list=RDCMUCoNZZLhPuuRteu02rh7bzsw&start_radio=1&rv=MTpVJwFROZE&t=596
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


    // https://www.youtube.com/watch?v=wwStpiU4nJk&ab_channel=CodingWithMitch
    //handle what happens when selecting an item in navigation drawer.
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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

                //FirebaseAuth.getInstance().signOut();
                //Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));

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

    //id of the fragment it trying to navigate to.
    private boolean isValidDestination(int destination) {
        //if the destination is the same, don't add it to the back stack.
        return destination != Navigation.findNavController(this, R.id.fragment).getCurrentDestination().getId();

    }

    //Enable to open navigation drawer and enable the back error
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.fragment), drawerLayout);
    }

}