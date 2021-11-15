package dk.au.st7bac.toothbrushapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


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

import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.Services.AlertReceiver;

// kilde til alarm manager: https://developer.android.com/training/scheduling/alarms#java
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    private UpdateDataCtrl updateDataCtrl;

    //Constants:
    public static final String SHARED_PREF = "SHARED_PREF";
    public static final String SHARED_PREF_BOOL_START = "SHARED_PREF_BOOL_START";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        updateDataCtrl = UpdateDataCtrl.getInstance();

        updateDataCtrl.initUpdateTbData(Constants.FROM_MAIN_ACTIVITY); //kaldes fra main

        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.my_drawer_layout);

        //bottom navigation
        bottomNavigation();


        //drawer navigation
        drawerNavigation();


        //alarm manager for update tb data on specific time
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 19);
        //calendar.set(Calendar.MINUTE, 00);

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
        NavController navController = Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);


    }

    public void drawerNavigation()
    {

        //Inspired by: https://www.geeksforgeeks.org/navigation-drawer-in-android/ (edit text, it is compied)

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        //drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);//setup drawer layout with navigation controller
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //https://www.geeksforgeeks.org/navigation-drawer-in-android/ (edit text!!!)
    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // https://www.youtube.com/watch?v=wwStpiU4nJk&ab_channel=CodingWithMitch
    //handle what happens when selecting an item in navigation drawer.
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_settings:
                Navigation.findNavController(this, R.id.fragment).navigate(R.id.settingsFragment);
                break;
            case R.id.nav_help:
                Navigation.findNavController(this, R.id.fragment).navigate(R.id.helpFragment);
                break;
            case R.id.nav_Signout:
                Navigation.findNavController(this, R.id.fragment).navigate(R.id.signinFragment);
                break;

        }
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}