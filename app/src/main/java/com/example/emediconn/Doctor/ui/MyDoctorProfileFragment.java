
package com.example.emediconn.Doctor.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.R;


public class MyDoctorProfileFragment extends Fragment {

    //text view
    TextView tv_name,doctor_name,tvdp_phonenumber,doctor_mobilenum;

    //edit text
    EditText etdp_doctorname;

    //image view
    ImageView iv_logout;


    //prefrence
    PrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_my_doctor_profile, container, false);
        tv_name=v.findViewById(R.id.titletext);
        iv_logout=v.findViewById(R.id.logout_doctor);
        etdp_doctorname=v.findViewById(R.id.etdp_doctorname);
        doctor_name=v.findViewById(R.id.doctor_name);
        tvdp_phonenumber=v.findViewById(R.id.tvdp_phonenumber);
        doctor_mobilenum=v.findViewById(R.id.doctor_mobilenum);
        iv_logout.setVisibility(View.GONE);
        tv_name.setText("Doctor Profile");
        prefManager=new PrefManager(getContext());


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            //getDoctorDetails(prefManager.get("mobilenumber"));
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        etdp_doctorname.setText(prefManager.get("full_name"));
        doctor_name.setText(prefManager.get("full_name"));
        tvdp_phonenumber.setText(prefManager.get("mobilenumber"));
        doctor_mobilenum.setText(prefManager.get("mobilenumber"));
        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        replaceFragmentWithAnimation(new DoctorDashboardFragment());
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