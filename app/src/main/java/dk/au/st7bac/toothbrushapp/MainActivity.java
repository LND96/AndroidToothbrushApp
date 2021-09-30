package dk.au.st7bac.toothbrushapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;

import dk.au.st7bac.toothbrushapp.Fragments.HelpFragment;
import dk.au.st7bac.toothbrushapp.Fragments.HomeFragment;
import dk.au.st7bac.toothbrushapp.Fragments.SettingsFragment;
import dk.au.st7bac.toothbrushapp.Fragments.SigninFragment;


public class MainActivity extends AppCompatActivity {

    // ui widgets
    private TableRow rowHeader, rowMorningBrush, rowMorningTime, rowEveningBrush, rowEveningTime;
    private TextView txtNumberToothbrushesCompletedResult, txtTotalNumberToothbrushes, txtAvgTimeResult;

    private int imgPadding = 15;

    // data
    private String[] headerStrings = {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"}; // hardcoded værdier
    private boolean[] isToothbrushDoneMorning = {true, false, true, true, true, true, false}; // hardcoded værdier
    private boolean[] isTimeOkMorning = {true, false, true, false, false, false, false}; // hardcoded værdier
    private boolean[] isToothbrushDoneEvening = {false, true, true, true, true, false, true}; // hardcoded værdier
    private boolean[] isTimeOkEvening = {false, false, true, false, false, false, false}; // hardcoded værdier
    private int toothbrushesCompleted = 10; // hardcoded værdi
    private int totalNumberToothbrushes = 14; // hardcoded værdi
    private int avgBrushTime = 45; // hardcoded værdi


    /*
    //drawer navigation
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

     */

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sets up user interface
        setupUI();
        startService();



        //Inspired by: "https://www.section.io/engineering-education/bottom-navigation-bar-in-android/"
        // bottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        //bottomNav.setOnItemSelectedListener(navListener);
        //bottomNav.setOnNavigationItemSelectedListener(navListener);

        //ID passes of different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.detailsFragment).build();

        //Initialize NavController
        NavController navController = Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);




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



        /*


        //https://guides.codepath.com/android/fragment-navigation-drawer
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        drawerToggle = setupDrawerToggle();

        //toggle to display hamburger icon
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Find our drawer view
        //nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        //setupDrawerContent(nvDrawer);

        //Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

         */


    }



    private void setupUI() {
        // finds UI elements
        txtNumberToothbrushesCompletedResult = findViewById(R.id.txtNumberToothbrushesCompletedResult);
        txtTotalNumberToothbrushes = findViewById(R.id.txtTotalNumberToothbrushes);
        txtAvgTimeResult = findViewById(R.id.txtAvgTimeResult);

        updateUI();
    }

    // updates UI with data
    private void updateUI() { // denne må ikke kaldes før setupUI - hvordan tager vi højde for dette? Evt. med try catch rundt om i stedet
        txtTotalNumberToothbrushes.setText(String.valueOf(totalNumberToothbrushes));
        txtNumberToothbrushesCompletedResult.setText(String.valueOf(toothbrushesCompleted));
        txtAvgTimeResult.setText(String.valueOf(avgBrushTime));
        updateTable();
    }

    // adds data to table
    private void updateTable() {
        rowHeader = findViewById(R.id.rowHeader);
        rowMorningBrush = findViewById(R.id.rowMorningBrush);
        rowMorningTime = findViewById(R.id.rowMorningTime);
        rowEveningBrush = findViewById(R.id.rowEveningBrush);
        rowEveningTime = findViewById(R.id.rowEveningTime);

        for (int i = 0; i < headerStrings.length; i++)
        {
            TextView textView = new TextView(this); // create TextView
            textView.setText(headerStrings[i]); // set text in TextView to the i'th string
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1)); // make cells equal size
            rowHeader.addView(textView); // add TextView to row
        }

        addImgToRow(rowMorningBrush, isToothbrushDoneMorning);
        addImgToRow(rowMorningTime, isTimeOkMorning);
        addImgToRow(rowEveningBrush, isToothbrushDoneEvening);
        addImgToRow(rowEveningTime, isTimeOkEvening);
    }

    // adds images to table row
    private void addImgToRow(TableRow row, boolean[] isDone)
    {
        for (int i = 0; i < isDone.length; i++)
        {
            ImageView imageView = new ImageView(this); // create ImageView
            imageView.setLayoutParams(new TableRow.LayoutParams(0, 80, 1)); // make cells equal size
            imageView.setPadding(imgPadding, imgPadding, imgPadding, imgPadding); // set padding around images
            if (isDone[i]) // set the right icon
            {
                imageView.setImageResource(R.drawable.tic_icon);
            } else {
                imageView.setImageResource(R.drawable.not_ok_icon);
            }

            row.addView(imageView); // add ImageView to row
        }
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

     /*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_help:
                fragmentClass = HelpFragment.class;
                break;
            case R.id.nav_Signout:
                fragmentClass = SigninFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.nav_open,  R.string.nav_close);
    }

      */




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