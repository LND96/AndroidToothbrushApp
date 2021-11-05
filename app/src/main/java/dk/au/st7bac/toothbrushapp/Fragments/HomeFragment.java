package dk.au.st7bac.toothbrushapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;

import dagger.hilt.android.AndroidEntryPoint;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataCleaner;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataFilter;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataProcessor;
import dk.au.st7bac.toothbrushapp.Model.DataCleaner;
import dk.au.st7bac.toothbrushapp.Model.DataFilter;
import dk.au.st7bac.toothbrushapp.Model.DataProcessor;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;
import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.ViewModels.HomeViewModel;

import dk.au.st7bac.toothbrushapp.R;

public class HomeFragment extends Fragment {

    // ui widgets
    //private TableRow rowHeader, rowMorningBrush, rowMorningTime, rowEveningBrush, rowEveningTime;
    private TextView txtNumberToothbrushesCompletedResult, txtTotalNumberToothbrushes, txtAvgTimeResult;
    private ImageView imgNumberToothbrushesResult, imgAvgTimeResult;

    //private int imgPadding = 15;

    // data
    //private String[] headerStrings;
    //private boolean[] isTbDone;
    //private boolean[] isTimeOk;
    private int numTbCompleted;
    private int totalNumTb;
    private int avgTbTime;
    private boolean isAvgNumTbOk;
    private boolean isAvgTimeOk;


    // skal alle disse data sættes ved contructor injection i controlleren?
    private double offset = 6.0; // hardware offset
    private int minMeasurementDuration = 10; // minimum time in secs that a measurement should last to be considered as a tooth brushing
    private int maxMeasurementDuration = 600; // maximum time in secs that a measurement should last to be considered as a tooth brushing
    private int minAccpTbTime = 90; // minimum time in secs that a tooth brushing should last to be accepted
    private LocalTime morningToEveningTime = LocalTime.parse("11:59"); // time of day where morning transitions to evening
    private LocalTime eveningToMorningTime = LocalTime.parse("00:00"); // time of day where evening transitions to morning
    private int tbEachDay = 2; // ideal number of tooth brushes each day
    private double numTbThres = 0.8; // threshold value for minimum number of tooth brushes compared to ideal number of tooth brushes
    private int timeBetweenMeasurements = 10; // maximum time in minutes between two measurements for them to be counted as one
    private int numIntervalDays = 7; // number of days in interval
    private LocalDate lastDayInInterval = LocalDate.now(); // the last day of the time interval the calculations are made over
    private IDataFilter dataFilter;
    private IDataCleaner dataCleaner;
    private IDataProcessor dataProcessor;

    // view model
    private HomeViewModel vm;

    private UpdateDataCtrl updateDataCtrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // set up user interface
        setupUI(view);

        dataFilter = new DataFilter(offset, minMeasurementDuration, maxMeasurementDuration);
        dataCleaner = new DataCleaner(timeBetweenMeasurements);
        dataProcessor = new DataProcessor(minAccpTbTime, numIntervalDays, tbEachDay,
                morningToEveningTime, eveningToMorningTime, numTbThres, lastDayInInterval);
        updateDataCtrl = new UpdateDataCtrl(dataFilter, dataCleaner, dataProcessor);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this).get(HomeViewModel.class);
        vm.setController(updateDataCtrl);
        vm.getTbStatusData().observe(getViewLifecycleOwner(), new Observer<TbStatus>() {
            @Override
            public void onChanged(TbStatus tbStatus) {
                //headerStrings = tbStatus.getHeaderStrings();
                //isTbDone = tbStatus.getIsTbDone();
                //isTimeOk = tbStatus.getIsTimeOk();
                numTbCompleted = tbStatus.getNumTbCompleted();
                totalNumTb = tbStatus.getTotalNumTb();
                avgTbTime = tbStatus.getAvgTbTime();
                isAvgNumTbOk = tbStatus.isAvgNumTbOk();
                isAvgTimeOk = tbStatus.isAvgTimeOk();

                updateUI(view);
            }
        });

        updateDataCtrl.initUpdateTbData();
    }

    private void setupUI(View view) {
        // find UI elements
        txtNumberToothbrushesCompletedResult = view.findViewById(R.id.txtNumberToothbrushesCompletedResult);
        txtTotalNumberToothbrushes = view.findViewById(R.id.txtTotalNumberToothbrushes);
        txtAvgTimeResult = view.findViewById(R.id.txtAvgTimeResult);
        imgNumberToothbrushesResult = view.findViewById(R.id.imgNumberToothbrushingResult);
        imgAvgTimeResult = view.findViewById(R.id.imgAvgTimeResult);
    }

    // updates UI with data
    private void updateUI(View view) { // denne må ikke kaldes før setupUI - hvordan tager vi højde for dette? Evt. med try catch rundt om i stedet
        // update text views
        txtTotalNumberToothbrushes.setText(String.valueOf(totalNumTb));
        txtNumberToothbrushesCompletedResult.setText(String.valueOf(numTbCompleted));
        txtAvgTimeResult.setText(String.valueOf(avgTbTime));

        // update images
        if (isAvgNumTbOk){
            imgNumberToothbrushesResult.setImageResource(R.drawable.ok_icon);
        } else {
            imgNumberToothbrushesResult.setImageResource(R.drawable.not_ok_icon);
        }
        if (isAvgTimeOk){
            imgAvgTimeResult.setImageResource(R.drawable.ok_icon);
        } else {
            imgAvgTimeResult.setImageResource(R.drawable.not_ok_icon);
        }


        // update table
        //updateTable(view);
    }

    /*
    // adds data to table
    private void updateTable(View view) {
        rowHeader = view.findViewById(R.id.rowHeader);
        rowMorningBrush = view.findViewById(R.id.rowMorningBrush);
        rowMorningTime = view.findViewById(R.id.rowMorningTime);
        rowEveningBrush = view.findViewById(R.id.rowEveningBrush);
        rowEveningTime = view.findViewById(R.id.rowEveningTime);

        for (String headerString : headerStrings) {
            TextView textView = new TextView(getActivity()); // create TextView
            textView.setText(headerString); // set text in TextView to the i'th string
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1)); // make cells equal size
            rowHeader.addView(textView); // add TextView to row
        }


        for (int i = 0; i < isTbDone.length; i++) {
            if (i % 2 == 0) {
                addImgToRow(rowMorningBrush, isTbDone[i]);
                addImgToRow(rowMorningTime, isTimeOk[i]);
            } else {
                addImgToRow(rowEveningBrush, isTbDone[i]);
                addImgToRow(rowEveningTime, isTimeOk[i]);
            }
        }
    }

     */

    /*
    // adds images to table row
    private void addImgToRow(TableRow row, boolean isOk) {
        ImageView imageView = new ImageView(getActivity()); // create ImageView
        imageView.setLayoutParams(new TableRow.LayoutParams(0, 80, 1)); // make cells equal size
        imageView.setPadding(imgPadding, imgPadding, imgPadding, imgPadding); // set padding around images
        if (isOk) // set the right icon
        {
            imageView.setImageResource(R.drawable.ok_icon);
        } else {
            imageView.setImageResource(R.drawable.not_ok_icon);
        }

        row.addView(imageView); // add ImageView to row
    }

     */

}