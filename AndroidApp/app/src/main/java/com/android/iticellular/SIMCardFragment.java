package com.android.iticellular;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SIMCardFragment extends Fragment {

    TextView firstSIMData = null;
    TextView secondSIMData = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sim, container,false);

        firstSIMData = view.findViewById(R.id.first_sim_text);
        secondSIMData = view.findViewById(R.id.second_sim_text);

        firstSIMData.setText(ApplicationActivity.firstSIMInfo + "\n");
        secondSIMData.setText(ApplicationActivity.secondSIMInfo + "\n");
        return view;
    }
}