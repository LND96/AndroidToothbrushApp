package dk.au.st7bac.toothbrushapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;

import dk.au.st7bac.toothbrushapp.Model.TbStatus;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.ViewModels.HomeViewModel;

public class DetailsFragment extends Fragment {

    // ui widgets
    private TextView txtMorningTbCompleted, txtEveningTbCompleted, txtTotalNumMorning, txtTotalNumEvening;
    private TableRow rowHeader, rowMorningBrush, rowMorningTime, rowEveningBrush, rowEveningTime;
    private TableLayout tableOverview;


    private int imgPadding = 15;
    private int iconPadding = 3;

    // data
    private String[] headerStrings;
    private boolean[] isTbDone;
    private boolean[] isTimeOk;
    private int numMorning;
    private int numEvening;
    private int totalNumTb;

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
                numMorning = tbStatus.getNumMorningOk();
                numEvening = tbStatus.getNumEveningOk();
                totalNumTb = tbStatus.getTotalNumTb();

                updateUI(view);
            }
        });
    }


    // updates UI with data
    private void updateUI(View view) { // denne må ikke kaldes før setupUI - hvordan tager vi højde for dette? Evt. med try catch rundt om i stedet

        // update table
        updateTable(view);

        //update morning and evening view
        updateMorningEvening(view);
    }

    private void updateMorningEvening(View view) {
        txtTotalNumMorning = view.findViewById(R.id.txtNumMorningTb);
        txtTotalNumEvening = view.findViewById(R.id.txtNumEveningTb);
        txtMorningTbCompleted = view.findViewById(R.id.txtNumMorningTbCompleted);
        txtEveningTbCompleted = view.findViewById(R.id.txtNumEveningTbCompleted);

        // update text views
        txtTotalNumMorning.setText(String.valueOf(totalNumTb/2));
        txtMorningTbCompleted.setText(String.valueOf(numMorning));

        txtTotalNumEvening.setText(String.valueOf(totalNumTb/2));
        txtEveningTbCompleted.setText(String.valueOf(numEvening));


    }


    // adds data to table
    private void updateTable(View view) {
        rowHeader = view.findViewById(R.id.rowHeader);
        rowMorningBrush = view.findViewById(R.id.rowMorningBrush);
        rowMorningTime = view.findViewById(R.id.rowMorningTime);
        rowEveningBrush = view.findViewById(R.id.rowEveningBrush);
        rowEveningTime = view.findViewById(R.id.rowEveningTime);

        // remove previous views from rows
        rowHeader.removeAllViews();
        rowMorningBrush.removeAllViews();
        rowMorningTime.removeAllViews();
        rowEveningBrush.removeAllViews();
        rowEveningTime.removeAllViews();

        addImageToRow(rowHeader, R.drawable.empty_icon, 20, iconPadding);
        addImageToRow(rowMorningBrush, R.drawable.toothbrush_icon, 80, iconPadding);
        addImageToRow(rowMorningTime, R.drawable.time_icon, 60, iconPadding);
        addImageToRow(rowEveningBrush, R.drawable.toothbrush_icon, 80, iconPadding);
        addImageToRow(rowEveningTime, R.drawable.time_icon, 60, iconPadding);

        for (String headerString : headerStrings) {
            TextView textView = new TextView(getActivity()); // create TextView
            textView.setText(headerString); // set text in TextView to the i'th string
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1)); // make cells equal size
            rowHeader.addView(textView); // add TextView to row
        }

        for (int i = 0; i < isTbDone.length; i++) {
            int tbResId;
            int timeResId;

            // set correct image in regards to tooth brush completion
            if (isTbDone[i]) {
                tbResId = R.drawable.ok_icon;
            } else {
                tbResId = R.drawable.not_ok_icon;
            }

            // set correct image in regards to time
            if (isTimeOk[i]) {
                timeResId = R.drawable.ok_icon;
            } else {
                timeResId = R.drawable.not_ok_icon;
            }

            TableRow tbRow;
            TableRow timeRow;

            // decide if the images should be added to morning or evening row
            if (i % 2 == 0) {
                tbRow = rowMorningBrush;
                timeRow = rowMorningTime;
            } else {
                tbRow = rowEveningBrush;
                timeRow = rowEveningTime;
            }

            // add images to rows via method
            addImageToRow(tbRow, tbResId, 80, imgPadding);
            addImageToRow(timeRow, timeResId, 80, imgPadding);
        }
    }

    // adds images to table row
    private void addImageToRow(TableRow row, int resId, int imgHeight, int imgPadding) {
        ImageView imageView = new ImageView(getActivity()); // create ImageView
        imageView.setLayoutParams(new TableRow.LayoutParams(0, imgHeight, 1)); // make cells equal size
        imageView.setPadding(imgPadding, imgPadding, imgPadding, imgPadding); // set padding around image
        imageView.setImageResource(resId); // set resource id

        row.addView(imageView); // add ImageView to row
    }
}