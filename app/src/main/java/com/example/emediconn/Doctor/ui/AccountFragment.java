package com.example.emediconn.Doctor.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.emediconn.R;

public class AccountFragment extends Fragment {
    public AccountFragment() {
        // Required empty public constructor
    }

    Button btnaddclinic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("My Account");


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, null);

        btnaddclinic=(Button)v.findViewById(R.id.btnaddclinic);



      return v;

    }
}
