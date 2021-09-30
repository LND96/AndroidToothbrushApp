package dk.au.st7bac.toothbrushapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    // ui widgets
    private TableRow rowHeader, rowMorning, rowEvening;
    private TextView txtNumberToothbrushesCompletedResult, txtTotalNumberToothbrushes, txtAvgTimeResult;

    // data
    private String[] headerStrings = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun"}; // hardcoded værdier
    private boolean[] isToothbrushDoneMorning = {true, false, true, true, true, true, false}; // hardcoded værdier
    private boolean[] isToothbrushDoneEvening = {false, true, true, true, true, false, true}; // hardcoded værdier
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
        rowMorning = findViewById(R.id.rowMorning);
        rowEvening = findViewById(R.id.rowEvening);

        for (int i = 0; i < headerStrings.length; i++)
        {
            TextView textView = new TextView(this); // create TextView
            textView.setText(headerStrings[i]); // set text in TextView to the i'th string
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1)); // make cells equal size
            textView.setGravity(Gravity.CENTER); // center text
            rowHeader.addView(textView); // add TextView to row
        }

        addImgToRow(rowMorning, isToothbrushDoneMorning);
        addImgToRow(rowEvening, isToothbrushDoneEvening);

    }

    // adds images to table row
    private void addImgToRow(TableRow row, boolean[] isDone)
    {
        for (int i = 0; i < isDone.length; i++)
        {
            ImageView imageView = new ImageView(this); // create ImageView
            imageView.setLayoutParams(new TableRow.LayoutParams(0, 80, 1)); // make cells equal size
            imageView.setPadding(10, 10, 10, 10); // set padding around images
            if (isDone[i])
            {
                imageView.setImageResource(R.drawable.tic_icon);
            } else {
                imageView.setImageResource(R.drawable.not_ok_icon);
            }

            row.addView(imageView);
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