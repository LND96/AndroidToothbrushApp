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

import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;
import java.time.LocalDate;
import java.time.LocalTime;

import dk.au.st7bac.toothbrushapp.Interfaces.IDataCleaner;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataFilter;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataProcessor;
import dk.au.st7bac.toothbrushapp.Model.DataCleaner;
import dk.au.st7bac.toothbrushapp.Model.DataFilter;
import dk.au.st7bac.toothbrushapp.Model.DataProcessor;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;
import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.ViewModels.HomeViewModel;

public class DetailsFragment extends Fragment {

    // ui widgets
    private TableRow rowHeader, rowMorningBrush, rowMorningTime, rowEveningBrush, rowEveningTime;


    private int imgPadding = 15;

    // data
    private String[] headerStrings;
    private boolean[] isTbDone;
    private boolean[] isTimeOk;

    // view model
    private HomeViewModel vm;


    private UpdateDataCtrl updateDataCtrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        updateDataCtrl = UpdateDataCtrl.getInstance();

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this).get(HomeViewModel.class);
        vm.getTbStatusData().observe(getViewLifecycleOwner(), new Observer<TbStatus>() {
            @Override
            public void onChanged(TbStatus tbStatus) {
                headerStrings = tbStatus.getHeaderStrings();
                isTbDone = tbStatus.getIsTbDone();
                isTimeOk = tbStatus.getIsTimeOk();

                updateUI(view);
            }
        });
    }


    // updates UI with data
    private void updateUI(View view) { // denne må ikke kaldes før setupUI - hvordan tager vi højde for dette? Evt. med try catch rundt om i stedet

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

}