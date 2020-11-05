package com.example.emediconn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.emediconn.Patient.PatientActivity;
import com.example.emediconn.Patient.PatientRegistration;
import com.example.emediconn.Patient.RegisterPatient;

public class ChooseRole extends AppCompatActivity {
    CardView cv1,cv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        cv1=(CardView)findViewById(R.id.CV1);
        cv2=(CardView)findViewById(R.id.CV2);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseRole.this, SelectProvider.class);
                startActivity(i);
            }
        });


        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseRole.this, PatientRegistration.class);
                startActivity(i);
            }
        });

    }
}