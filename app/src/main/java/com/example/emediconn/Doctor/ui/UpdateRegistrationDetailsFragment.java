package com.example.emediconn.Doctor.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.R;


public class UpdateRegistrationDetailsFragment extends Fragment {

//Prefereence
 PrefManager prefManager;

 //TextView
    TextView tv_name;


//imageview
    ImageView iv_logout;

 // Edit text
    EditText et_updatereginum,
         etupdate_regicoun,
         etupdate_regiyear;

    public UpdateRegistrationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Registration Details");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_update_registration_details, container, false);


        prefManager =new PrefManager(getActivity());

        tv_name = v.findViewById(R.id.titletext);
        iv_logout = v.findViewById(R.id.logout_doctor);
        et_updatereginum = v.findViewById(R.id.et_updatereginum);
        etupdate_regicoun = v.findViewById(R.id.etupdate_regicoun);
        etupdate_regiyear = v.findViewById(R.id.etupdate_regiyear);

        iv_logout.setVisibility(View.GONE);
        tv_name.setText("Registration Details");

        et_updatereginum.setText(prefManager.get("registrationnumber"));
        etupdate_regicoun.setText(prefManager.get("registrationcouncil"));
        etupdate_regiyear.setText(prefManager.get("registrationyear"));


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