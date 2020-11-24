package com.example.emediconn.Doctor.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.emediconn.Extras.Utils;
import com.example.emediconn.Patient.DoctorCategory;
import com.example.emediconn.R;

import java.util.ArrayList;

public class DoctorDashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Choose Doctor");

        View v= inflater.inflate(R.layout.doctor_dashboard_fragment, container, false);


        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                     //   replaceFragmentWithAnimation(new DoctorCategory());

                        return true;
                    }
                }
                return false;
            }

        });
        // Inflate the layout for this fragment


        return v;
    }


}
