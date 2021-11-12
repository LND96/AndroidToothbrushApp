package dk.au.st7bac.toothbrushapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

import dk.au.st7bac.toothbrushapp.Fragments.DetailsFragment;
import dk.au.st7bac.toothbrushapp.Fragments.HelpFragment;
import dk.au.st7bac.toothbrushapp.Fragments.HomeFragment;
import dk.au.st7bac.toothbrushapp.Fragments.SettingsFragment;
import dk.au.st7bac.toothbrushapp.Fragments.SignInFragment;
import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.Services.AlertReceiver;
import dk.au.st7bac.toothbrushapp.Services.NotificationService;

// kilde til alarm manager: https://developer.android.com/training/scheduling/alarms#java
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    private UpdateDataCtrl updateDataCtrl;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        updateDataCtrl = UpdateDataCtrl.getInstance();

        updateDataCtrl.initUpdateTbData(); //kaldes fra main

        //start service
        //startService();

        //bottom navigation
        bottomNavigation();

        //drawer navigation
        drawerNavigation();

        //alarm manager for update tb data on specific time


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 45);

        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        int requestCode = 0;
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, 0);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+10000,
                300000, pendingIntent);

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
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
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


    private void displaySelectedScreen(int id) {
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_help:
                fragment = new HelpFragment();
                break;
            case R.id.nav_Signout:
                fragment = new SignInFragment();
                break;

        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.settings_container, fragment);
            ft.commit();
        }
        //DrawerLayout drawer  = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    // https://www.youtube.com/watch?v=5kmjCzQBieY & https://www.youtube.com/watch?v=-SUvA1fXaKw&ab_channel=SimplifiedCoding
    //handle what happens when selecting an item in navigation drawer.
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        displaySelectedScreen(id);

        /*
        //erstat evt. dette med case...
        if (id == R.id.nav_settings) {

            Toast.makeText(this,R.string.Settings, Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.nav_help) {
            Toast.makeText(this,R.string.Help, Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.nav_Signout){
            Toast.makeText(this,R.string.SignOut, Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        }

         */
        //return false;
        return true;
    }



    //handle service
    private void startService() {
        Intent notificationServiceIntent = new Intent(this, NotificationService.class);
        startService(notificationServiceIntent);
    }


    private void stopService() {
        Intent notificationServiceIntent = new Intent(this, NotificationService.class);
        stopService(notificationServiceIntent);
    }

    @Override
    protected void onDestroy() {
        stopService();
        super.onDestroy();
    }
}