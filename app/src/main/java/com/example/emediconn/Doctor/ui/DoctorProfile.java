package com.example.emediconn.Doctor.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.emediconn.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class DoctorProfile extends Fragment {
TextView tv_medicalinfo,tv_educationalinfo;
   MaterialBetterSpinner speciality;
    String[] SPINNERSPECIALITY = {"9AM-5PM","10AM-6PM","9:30AM-5:30PM","10:30AM-6:30PM"};
LinearLayout linearLayout;


    public DoctorProfile() {
        // Required empty public constructor
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        tv_educationalinfo=v.findViewById(R.id.educational_information);
        tv_medicalinfo=v.findViewById(R.id.medical_registration);
        speciality=v.findViewById(R.id.spin_speciality);
        linearLayout=v.findViewById(R.id.Lin1);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.orange_700));

        ArrayAdapter<String> speciality_s = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERSPECIALITY);

        speciality.setAdapter(speciality_s);


        speciality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                speciality = (MaterialBetterSpinner) adapterView.getItemAtPosition(i);
            }
        });

        tv_medicalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_educationalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
    }
}