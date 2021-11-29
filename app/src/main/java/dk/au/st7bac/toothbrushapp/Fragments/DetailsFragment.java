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
import android.widget.TableRow;
import android.widget.TextView;

import dk.au.st7bac.toothbrushapp.Model.TbStatus;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.ViewModels.HomeViewModel;

public class DetailsFragment extends Fragment {

    // ui widgets
    private TextView txtMorningTbCompleted, txtEveningTbCompleted, txtTotalNumMorning, txtTotalNumEvening;
    private TableRow rowHeader, rowMorningBrush, rowMorningTime, rowEveningBrush, rowEveningTime;

    // variables
    private final int imgPadding = 15;
    private final int iconPadding = 3;
    private final int tbIconHeight = 80;
    private final int timeIconHeight = 60;
    private final int okIconHeight = 80;

    // data
    private String[] headerStrings;
    private boolean[] isTbDone;
    private boolean[] isTimeOk;
    private int numMorning;
    private int numEvening;
    private int totalNumTb;

    // view model
    private HomeViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // set up ui
        setupUI(view);

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

                updateUI();
            }
        });
    }

    private void setupUI(View view) {
        // find ui elements
        txtTotalNumMorning = view.findViewById(R.id.txtNumMorningTb);
        txtTotalNumEvening = view.findViewById(R.id.txtNumEveningTb);
        txtMorningTbCompleted = view.findViewById(R.id.txtNumMorningTbCompleted);
        txtEveningTbCompleted = view.findViewById(R.id.txtNumEveningTbCompleted);
        rowHeader = view.findViewById(R.id.rowHeader);
        rowMorningBrush = view.findViewById(R.id.rowMorningBrush);
        rowMorningTime = view.findViewById(R.id.rowMorningTime);
        rowEveningBrush = view.findViewById(R.id.rowEveningBrush);
        rowEveningTime = view.findViewById(R.id.rowEveningTime);
    }

    // updates UI with data
    private void updateUI() {
        updateMorningEveningTexts();
        updateTable();
    }

    // updates ui elements regarding morning and evening tb status
    private void updateMorningEveningTexts() {
        txtTotalNumMorning.setText(String.valueOf(totalNumTb/2));
        txtMorningTbCompleted.setText(String.valueOf(numMorning));
        txtTotalNumEvening.setText(String.valueOf(totalNumTb/2));
        txtEveningTbCompleted.setText(String.valueOf(numEvening));
    }

    // adds data to table
    private void updateTable() {
        // remove previous views from rows
        rowHeader.removeAllViews();
        rowMorningBrush.removeAllViews();
        rowMorningTime.removeAllViews();
        rowEveningBrush.removeAllViews();
        rowEveningTime.removeAllViews();

        // add icons to first column in each row
        addImageToRow(rowHeader, R.drawable.empty_icon, 20, iconPadding);
        addImageToRow(rowMorningBrush, R.drawable.toothbrush_icon, tbIconHeight, iconPadding);
        addImageToRow(rowMorningTime, R.drawable.time_icon, timeIconHeight, iconPadding);
        addImageToRow(rowEveningBrush, R.drawable.toothbrush_icon, tbIconHeight, iconPadding);
        addImageToRow(rowEveningTime, R.drawable.time_icon, timeIconHeight, iconPadding);

        // add text for table header
        for (String headerString : headerStrings) {
            // create text view and add text resource
            TextView textView = new TextView(getActivity());
            textView.setText(headerString);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

            // add text view to row
            rowHeader.addView(textView);
        }

        // add images to table body
        for (int i = 0; i < isTbDone.length; i++) {
            int tbResId; // variable for tb icon resource id
            int timeResId; // variable for time icon resource id
            TableRow tbRow; // variable for morning or evening tb row
            TableRow timeRow; // variable for morning or evening time row

            // set correct image in regards to tb completion
            if (isTbDone[i]) {
                tbResId = R.drawable.ok_icon;
            } else {
                tbResId = R.drawable.not_ok_icon;
            }

            // set correct image in regards to tb time
            if (isTimeOk[i]) {
                timeResId = R.drawable.ok_icon;
            } else {
                timeResId = R.drawable.not_ok_icon;
            }

            // decide if the images should be added to morning or evening row
            if (i % 2 == 0) {
                tbRow = rowMorningBrush;
                timeRow = rowMorningTime;
            } else {
                tbRow = rowEveningBrush;
                timeRow = rowEveningTime;
            }

            // add images to rows
            addImageToRow(tbRow, tbResId, okIconHeight, imgPadding);
            addImageToRow(timeRow, timeResId, okIconHeight, imgPadding);
        }
    }

    // adds images to table row
    private void addImageToRow(TableRow row, int resId, int imgHeight, int imgPadding) {
        // create image view and add image resource
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new TableRow.LayoutParams(0, imgHeight, 1));
        imageView.setPadding(imgPadding, imgPadding, imgPadding, imgPadding);
        imageView.setImageResource(resId);

        // add image view to row
        row.addView(imageView);
    }
}