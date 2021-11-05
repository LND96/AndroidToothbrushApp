package dk.au.st7bac.toothbrushapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.R;

public class DetailsFragment extends Fragment {

    private UpdateDataCtrl updateDataCtrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        updateDataCtrl = UpdateDataCtrl.getInstance();

        return view;

    }
}