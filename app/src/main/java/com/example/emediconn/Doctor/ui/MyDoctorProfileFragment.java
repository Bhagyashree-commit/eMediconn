
package com.example.emediconn.Doctor.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MyDoctorProfileFragment extends Fragment {

    //Relative Layout
    RelativeLayout  rrmen,
                    rrwomen,
                    rrdate,
                    rr_registrationdeatils,
                    rr_educationdetails,
                    rr_spec;

    //text view
    TextView tv_name,
            doctor_name,
            tvdp_education,
            doctor_mobilenum,
            dob,
            tvdp_specialization,
            tvdp_registrationdeatails,
            tvdp_date,
            tvdp_clinicname;

 String gender;

    //edit text
    EditText etdp_doctorname,
            etdp_emailadd,
            tvdp_experience;

    //image view
    ImageView iv_logout,
              ivMen,
              ivWomen;


    //prefrence
    PrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_doctor_profile, container, false);
        tv_name = v.findViewById(R.id.titletext);
        iv_logout = v.findViewById(R.id.logout_doctor);
        etdp_doctorname = v.findViewById(R.id.etdp_doctorname);
        doctor_name = v.findViewById(R.id.doctor_name);
        tvdp_experience = v.findViewById(R.id.tvdp_experience);
        doctor_mobilenum = v.findViewById(R.id.doctor_mobilenum);
        etdp_emailadd = v.findViewById(R.id.etdp_emailadd);
        tvdp_specialization = v.findViewById(R.id.tvdp_specialization);
        tvdp_education = v.findViewById(R.id.tvdp_education);
        rrdate = v.findViewById(R.id.relative_dpdate);
        tvdp_date = v.findViewById(R.id.tvdp_date);
        rrmen = v.findViewById(R.id.relative_men);
        rrwomen = v.findViewById(R.id.relative_women);
        ivMen = v.findViewById(R.id.imageView112);
        ivWomen = v.findViewById(R.id.imageView123);
        rr_spec = v.findViewById(R.id.rr_spec);
        tvdp_clinicname = v.findViewById(R.id.tvdp_clinicname);
        rr_registrationdeatils = v.findViewById(R.id.rr_registrationdeatils);
        rr_educationdetails = v.findViewById(R.id.rr_educationdetails);
        tvdp_registrationdeatails = v.findViewById(R.id.tvdp_registrationdeatails);
        iv_logout.setVisibility(View.GONE);
        tv_name.setText("Doctor Profile");
        prefManager = new PrefManager(getContext());


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            //getDoctorDetails(prefManager.get("mobilenumber"));
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        etdp_doctorname.setText(prefManager.get("full_name"));
        doctor_name.setText(prefManager.get("full_name"));
        tvdp_experience.setText(prefManager.get("experience"));
        doctor_mobilenum.setText(prefManager.get("mobilenumber"));
        etdp_emailadd.setText(prefManager.get("emailaddress"));
        tvdp_specialization.setText(prefManager.get("speciality"));
        tvdp_clinicname.setText(prefManager.get("hospitalname"));
        try {
            JSONArray jsonA = new JSONArray(prefManager.get("specialization"));
            for (int j = 0; j < jsonA.length(); j++) {
                JSONObject jsonObject = jsonA.getJSONObject(j);
                String speciality = jsonObject.getString("speciality");
                Log.e("", "" + speciality);
                tvdp_specialization.setText(speciality);
                tvdp_specialization.append(" , ");
                tvdp_specialization.append(speciality);
                tvdp_specialization.append(" , ");
                tvdp_specialization.append(prefManager.get("catnam"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tvdp_education.setText(prefManager.get("degree"));
        tvdp_registrationdeatails.setText(prefManager.get("registrationnumber") + " , " + prefManager.get("registrationcouncil") + " , " + prefManager.get("registrationyear"));


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

        tvdp_clinicname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new UpdateClinicUpdate());
            }
        });
        rr_spec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new SpecialityListFragment());
            }
        });

        rr_educationdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new UpdateEducationFragment());
            }
        });
        rr_registrationdeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new UpdateRegistrationDetailsFragment());
            }
        });

        rrdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        rrmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMen.setImageResource(R.drawable.ic_check_box_selected);
                ivWomen.setImageResource(R.drawable.ic_check_box_unselected);
                gender = "Male";
            }
        });

        rrwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMen.setImageResource(R.drawable.ic_check_box_unselected);
                ivWomen.setImageResource(R.drawable.ic_check_box_selected);
                gender = "Female";
            }
        });





        /////////////

        if(prefManager.get("gender").equalsIgnoreCase("Male"))
        {
            ivMen.setImageResource(R.drawable.ic_check_box_selected);
            ivWomen.setImageResource(R.drawable.ic_check_box_unselected);
            gender = "Male";
        }
        else if(prefManager.get("gender").equalsIgnoreCase("Female"))
        {
            ivMen.setImageResource(R.drawable.ic_check_box_unselected);
            ivWomen.setImageResource(R.drawable.ic_check_box_selected);
            gender = "Female";
        }
        else
        {
            ivMen.setImageResource(R.drawable.ic_check_box_unselected);
            ivWomen.setImageResource(R.drawable.ic_check_box_unselected);
            gender = "";
        }

        return v;
    }


    Calendar myCalendar = Calendar.getInstance();

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        tvdp_date.setText(sdf.format(myCalendar.getTime()));
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