package dk.au.st7bac.toothbrushapp;

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

import dk.au.st7bac.toothbrushapp.Model.ToothbrushData;
import dk.au.st7bac.toothbrushapp.ViewModels.HomeViewModel;

public class HomeFragment extends Fragment {

    // ui widgets
    private TableRow rowHeader, rowMorningBrush, rowMorningTime, rowEveningBrush, rowEveningTime;
    private TextView txtNumberToothbrushesCompletedResult, txtTotalNumberToothbrushes, txtAvgTimeResult;
    private ImageView imgNumberToothbrushesResult, imgAvgTimeResult;

    private int imgPadding = 15;

    // data
    private String[] headerStrings; // = {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"}; // hardcoded værdier
    private boolean[] isToothbrushDoneMorning;
    private boolean[] isTimeOkMorning; // = {true, false, true, false, false, false, false}; // hardcoded værdier
    private boolean[] isToothbrushDoneEvening; // = {false, true, true, true, true, false, true}; // hardcoded værdier
    private boolean[] isTimeOkEvening; // = {false, false, true, false, false, false, false}; // hardcoded værdier
    private int toothbrushesCompleted; // = 10; // hardcoded værdi
    private int totalNumberToothbrushes; // = 14; // hardcoded værdi
    private int avgBrushTime; // = 45; // hardcoded værdi
    private boolean isAvgNumberToothbrushesOk; // = true; // hardcoded værdi
    private boolean isAvgTimeOk; // = false; // hardcoded værdi

    // view model
    private HomeViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false); // Hvorfor skal view bruges ved findViewById?


        // set up user interface
        setupUI(view);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //isToothbrushDoneMorning = new boolean[7]; // hvad skal størrelsen være?!

        vm = new ViewModelProvider(this).get(HomeViewModel.class);
        vm.getToothbrushData().observe(getViewLifecycleOwner(), new Observer<ToothbrushData>() {
            @Override
            public void onChanged(ToothbrushData toothbrushData) {
                headerStrings = toothbrushData.getHeaderStrings();
                isToothbrushDoneMorning = toothbrushData.getIsToothbrushDoneMorning();
                isTimeOkMorning = toothbrushData.getIsTimeOkMorning();
                isToothbrushDoneEvening = toothbrushData.getIsToothbrushDoneEvening();
                isTimeOkEvening = toothbrushData.getIsTimeOkEvening();
                toothbrushesCompleted = toothbrushData.getToothbrushesCompleted();
                totalNumberToothbrushes = toothbrushData.getTotalNumberToothbrushes();
                avgBrushTime = toothbrushData.getAvgBrushTime();
                isAvgNumberToothbrushesOk = toothbrushData.isAvgNumberToothbrushesOk();
                isAvgTimeOk = toothbrushData.isAvgTimeOk();

                updateUI(view);
            }
        });


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
        txtTotalNumberToothbrushes.setText(String.valueOf(totalNumberToothbrushes));
        txtNumberToothbrushesCompletedResult.setText(String.valueOf(toothbrushesCompleted));
        txtAvgTimeResult.setText(String.valueOf(avgBrushTime));

        // update images
        if (isAvgNumberToothbrushesOk){
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
        updateTable(view);
    }

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

        addImgToRow(rowMorningBrush, isToothbrushDoneMorning);
        addImgToRow(rowMorningTime, isTimeOkMorning);
        addImgToRow(rowEveningBrush, isToothbrushDoneEvening);
        addImgToRow(rowEveningTime, isTimeOkEvening);
    }

    // adds images to table row
    private void addImgToRow(TableRow row, boolean[] isDone)
    {
        for (boolean b : isDone) {
            ImageView imageView = new ImageView(getActivity()); // create ImageView
            imageView.setLayoutParams(new TableRow.LayoutParams(0, 80, 1)); // make cells equal size
            imageView.setPadding(imgPadding, imgPadding, imgPadding, imgPadding); // set padding around images
            if (b) // set the right icon
            {
                imageView.setImageResource(R.drawable.ok_icon);
            } else {
                imageView.setImageResource(R.drawable.not_ok_icon);
            }

            row.addView(imageView); // add ImageView to row
        }
    }
}