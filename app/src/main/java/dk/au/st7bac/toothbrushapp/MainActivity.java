package dk.au.st7bac.toothbrushapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {


    //drawer navigation
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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