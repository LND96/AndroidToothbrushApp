package dk.au.st7bac.toothbrushapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    // ui widgets
    private TableLayout tableOverview;
    private TableRow rowHeader, rowMorning, rowEvening;
    private TextView day1, day2, day3, day4, day5, day6, day7, txtNumberToothbrushesCompletedResult, txtTotalNumberToothbrushes, txtAvgTimeResult;
    private ImageView morning1, morning2, morning3, morning4, morning5, morning6, morning7;

    private ArrayList<TextView> textViewRowHeaderList;
    private ArrayList<ImageView> imgViewMorningList;

    private int toothbrushesCompleted = 10; // hardcoded værdi
    private int totalNumberToothbrushes = 14; // hardcoded værdi
    private int avgBrushTime = 45; // hardcoded værdi




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        txtNumberToothbrushesCompletedResult = findViewById(R.id.txtNumberToothbrushesCompletedResult);
        txtTotalNumberToothbrushes = findViewById(R.id.txtTotalNumberToothbrushes);
        txtAvgTimeResult = findViewById(R.id.txtAvgTimeResult);

        createTable();
        updateUI();
    }

    private void updateUI() { // denne må ikke kaldes før setupUI - hvordan tager vi højde for dette? Evt. med try catch rundt om i stedet
        txtTotalNumberToothbrushes.setText(String.valueOf(totalNumberToothbrushes));
        txtNumberToothbrushesCompletedResult.setText(String.valueOf(toothbrushesCompleted));
        txtAvgTimeResult.setText(String.valueOf(avgBrushTime));
    }


    private void createTable() {
        tableOverview = findViewById(R.id.tableOverview);
        rowHeader = new TableRow(this);
        rowMorning = new TableRow(this);
        rowEvening = new TableRow(this);

        textViewRowHeaderList = new ArrayList<>();
        textViewRowHeaderList.add(day1);
        textViewRowHeaderList.add(day2);
        textViewRowHeaderList.add(day3);
        textViewRowHeaderList.add(day4);
        textViewRowHeaderList.add(day5);
        textViewRowHeaderList.add(day6);
        textViewRowHeaderList.add(day7);

        imgViewMorningList = new ArrayList<>();
        imgViewMorningList.add(morning1);
        imgViewMorningList.add(morning2);
        imgViewMorningList.add(morning3);
        imgViewMorningList.add(morning4);
        imgViewMorningList.add(morning5);
        imgViewMorningList.add(morning6);
        imgViewMorningList.add(morning7);

        for (TextView textView : textViewRowHeaderList)
        {
            textView = new TextView(this);
            textView.setText("Test");
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            textView.setGravity(Gravity.CENTER);
            rowHeader.addView(textView);
        }

        for (ImageView imageView : imgViewMorningList)
        {
            imageView = new ImageView(this);
            imageView.setLayoutParams(new TableRow.LayoutParams(0, 80, 1));
            imageView.setPadding(10, 10, 10, 10);
            imageView.setImageResource(R.drawable.hourglass_icon);

            rowMorning.addView(imageView);
        }


        tableOverview.addView(rowHeader);
        tableOverview.addView(rowMorning);
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