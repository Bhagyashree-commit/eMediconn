package com.example.emediconn.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.emediconn.Doctor.DoctorRegistration;
import com.example.emediconn.Doctor.LoginDoctor;
import com.example.emediconn.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterPatient extends AppCompatActivity {

    Button btnregisterP;
    EditText et_fnameP,et_lnameP,et_mobilenumP,et_DOB;
    MaterialBetterSpinner spingenderP,spinbloodgroupP,spinheightP;
    String genderholderP,bloodgroupholderP,spinheightholderP;

    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    String[] SPINNERGENDER_P = {"Male","Female"};
    String[] SPINNERBLOODGROUP_P = {"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    String[] SPINNERHEIGHTP = {"4'0","4'1","4'3","4'4","4'5","4'6","4'7","4'8","4'9","4'10","4'11","5'0","5'1","5'2","5'3","5'4","5'5","5'6","5'7","5'8","5'9","5'10","5'11","6'1","6'2","6'3","6'4","6'5","6'6","6'7","6'8","6'9","6'10","6'11","7'1","7'2","7'3","7'4","7'5","7'6","7'7","7'8","7'9","7'10","7'11","8'0"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        spingenderP=(MaterialBetterSpinner)findViewById(R.id.genderP);

        spinbloodgroupP=(MaterialBetterSpinner)findViewById(R.id.bloodgroupP);

        spinheightP=(MaterialBetterSpinner)findViewById(R.id.HeightP);

        btnregisterP=(Button)findViewById(R.id.btn_registerPaitent);

        et_DOB=(EditText)findViewById(R.id.etDOB);

        ArrayAdapter<String> height = new ArrayAdapter<String>(RegisterPatient.this, android.R.layout.simple_dropdown_item_1line, SPINNERHEIGHTP);
        spinheightP.setAdapter(height);


        ArrayAdapter<String> gender = new ArrayAdapter<String>(RegisterPatient.this, android.R.layout.simple_dropdown_item_1line, SPINNERGENDER_P);
        spingenderP.setAdapter(gender);

        ArrayAdapter<String> bloodgroup = new ArrayAdapter<String>(RegisterPatient.this, android.R.layout.simple_dropdown_item_1line, SPINNERBLOODGROUP_P);
        spinbloodgroupP.setAdapter(bloodgroup);

        spingenderP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                genderholderP = (String) adapterView.getItemAtPosition(i);
            }
        });

        spinbloodgroupP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgroupholderP = (String) adapterView.getItemAtPosition(i);
            }
        });

        btnregisterP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterPatient.this, LoginDoctor.class));
            }
        });


        et_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(RegisterPatient.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et_DOB.setText(dateFormatter.format(newDate.getTime()));
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();




            }
        });


    }
}