package dk.au.st7bac.toothbrushapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    // ui widgets
    private TableLayout tableOverview;
    private TableRow rowHeader, rowMorning, rowEvening;
    private TextView day1, day2, day3, day4, day5, day6, day7, txtNumberToothbrushesCompletedResult, txtTotalNumberToothbrushes, txtAvgTimeResult;
    private ImageView morning1, morning2, morning3, morning4, morning5, morning6, morning7;

    private ArrayList<TextView> textViewRowHeaderList;
    private ArrayList<ImageView> imgViewMorningList;
    private ArrayList<String> stringRowHeaderList;
    private String[] strings = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun"}; // hardcoded værdier

    private int toothbrushesCompleted = 10; // hardcoded værdi
    private int totalNumberToothbrushes = 14; // hardcoded værdi
    private int avgBrushTime = 45; // hardcoded værdi


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        startService();
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

        stringRowHeaderList = new ArrayList<>(Arrays.asList(strings));

        for (TextView textView : textViewRowHeaderList)
        {
            textView = new TextView(this);
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            textView.setGravity(Gravity.CENTER);
            rowHeader.addView(textView);
        }

        for (int i = 0; i < 7; i++)
        {
            //TextView textView = new TextView(this);
            //textView = textViewRowHeaderList.get(i);
            textViewRowHeaderList.get(i).setText(stringRowHeaderList.get(i));
            //textView.setText(stringRowHeaderList.get(i));
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