package com.example.emediconn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.emediconn.Doctor.DoctorRegistration;

public class SelectProvider extends AppCompatActivity {
    LinearLayout ll1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_provider);
        ll1=(LinearLayout)findViewById(R.id.LL1);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectProvider.this, DoctorRegistration.class);
                startActivity(i);
            }
        });
    }
}