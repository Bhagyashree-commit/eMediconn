package com.example.emediconn.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.emediconn.Doctor.ui.DoctorDashboardFragment;
import com.example.emediconn.Doctor.ui.DoctorProfile;
import com.example.emediconn.R;

public class DoctorDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        replaceFragmentWithAnimation(new DoctorDashboardFragment());
    }

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_doctor, fragment);
        FragmentManager mFragmentManager=getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }

}