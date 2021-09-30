package dk.au.st7bac.toothbrushapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Build;
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