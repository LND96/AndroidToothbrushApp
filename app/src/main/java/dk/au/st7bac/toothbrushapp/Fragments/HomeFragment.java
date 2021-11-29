package dk.au.st7bac.toothbrushapp.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dk.au.st7bac.toothbrushapp.Model.TbStatus;
import dk.au.st7bac.toothbrushapp.ViewModels.HomeViewModel;

import dk.au.st7bac.toothbrushapp.R;

public class HomeFragment extends Fragment {

    // ui widgets
    private TextView txtNumberToothbrushesCompletedResult, txtTotalNumberToothbrushes,
            txtAvgMinutesResult, txtAvgSecsResult, txtDays;
    private ImageView imgNumberToothbrushesResult, imgAvgTimeResult;

    // data
    private int numTbCompleted;
    private int totalNumTb;
    private int avgTbTime;
    private boolean isAvgNumTbOk;
    private boolean isAvgTimeOk;
    private int numIntervalDays;

    // view model
    private HomeViewModel vm;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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
                numTbCompleted = tbStatus.getNumTbCompleted();
                totalNumTb = tbStatus.getTotalNumTb();
                avgTbTime = tbStatus.getAvgTbTime();
                isAvgNumTbOk = tbStatus.isAvgNumTbOk();
                isAvgTimeOk = tbStatus.isAvgTimeOk();
                numIntervalDays = tbStatus.getNumIntervalDays();

                updateUI();
            }
        });
    }


    private void setupUI(View view) {
        // find ui elements
        txtNumberToothbrushesCompletedResult = view.findViewById(R.id.txtNumberToothbrushesCompletedResult);
        txtTotalNumberToothbrushes = view.findViewById(R.id.txtTotalNumberToothbrushes);
        txtAvgMinutesResult = view.findViewById(R.id.txtAvgMinutesResult);
        txtAvgSecsResult = view.findViewById(R.id.txtAvgSecsResult);
        imgNumberToothbrushesResult = view.findViewById(R.id.imgNumberToothbrushingResult);
        imgAvgTimeResult = view.findViewById(R.id.imgAvgTimeResult);
        txtDays = view.findViewById(R.id.txtDaysHeader);
    }

    // updates ui with data
    private void updateUI() {
        // convert secs to mins and secs
        // (inspiration: https://www.codegrepper.com/code-examples/java/java+seconds+to+hours+minutes+seconds)
        int sec = avgTbTime % 60;
        int min = (avgTbTime / 60) % 60;

        // update text views
        txtDays.setText(getString(R.string.txtDaysHeader) + " " + numIntervalDays + " " + getString(R.string.txtDays));
        txtTotalNumberToothbrushes.setText(String.valueOf(totalNumTb));
        txtNumberToothbrushesCompletedResult.setText(String.valueOf(numTbCompleted));
        txtAvgMinutesResult.setText(String.valueOf(min));
        txtAvgSecsResult.setText(String.valueOf(sec));

        // update images
        if (isAvgNumTbOk){
            imgNumberToothbrushesResult.setImageResource(R.drawable.ok_icon);
            if (isAvgTimeOk){
                imgAvgTimeResult.setImageResource(R.drawable.ok_icon);
            } else {
                imgAvgTimeResult.setImageResource(R.drawable.not_ok_icon);
            }
        } else {
            imgNumberToothbrushesResult.setImageResource(R.drawable.not_ok_icon);
        }
    }
}