package com.example.emediconn.Lab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.emediconn.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class LabTest extends AppCompatActivity {

    //Textview
    TextView tvViewReport;


    //Button
    Button btnBookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test);
        tvViewReport=findViewById(R.id.tvViewReport);

        btnBookNow=findViewById(R.id.btnBookNow);

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LabTest.this,TimeSlot.class));
            }
        });

        tvViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });


    }


    public void showBottomSheetDialog()  {
        View view = getLayoutInflater().inflate(R.layout.bottomsheetdialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        GridLayoutManager mGridLayoutManager;


        dialog.show();
    }

}