package com.example.emediconn.Doctor.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.R;


public class UpdateEducationFragment extends Fragment {
ImageView iv_logout;


//Text View
TextView tv_name;


//EditText
EditText etupdate_degree,
        etupdate_collegename,
        etupdate_completionyear;

//preference
PrefManager prefManager;

//button
Button btn_saveedudetails;

    public UpdateEducationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Education Details");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_update_education, container, false);
        prefManager =new PrefManager(getActivity());

        tv_name = v.findViewById(R.id.titletext);
        iv_logout = v.findViewById(R.id.logout_doctor);
        etupdate_degree = v.findViewById(R.id.etupdate_degree);
        etupdate_collegename = v.findViewById(R.id.etupdate_collegename);
        etupdate_completionyear = v.findViewById(R.id.etupdate_completionyear);
        btn_saveedudetails = v.findViewById(R.id.btn_updateedudetails);
        iv_logout.setVisibility(View.GONE);
        tv_name.setText("Education Details");


        etupdate_degree.setText(prefManager.get("degree"));
        etupdate_collegename.setText(prefManager.get("collegename"));
        etupdate_completionyear.setText(prefManager.get("completionYear"));




        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        replaceFragmentWithAnimation(new MyDoctorProfileFragment());

                        return true;
                    }
                }
                return false;
            }
        });

        return v;
    }

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_doctor, fragment);
        FragmentManager mFragmentManager=getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }
}