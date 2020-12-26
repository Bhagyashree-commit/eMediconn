package com.example.emediconn.Doctor.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DailySchedule extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {

        getActivity().setTitle("Appointments");


        View v= inflater.inflate(R.layout.doctorschedule, container, false);



        return v;
    }
}
