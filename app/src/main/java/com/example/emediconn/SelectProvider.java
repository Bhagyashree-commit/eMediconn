package com.example.emediconn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.emediconn.Doctor.DoctorRegistration;
import com.example.emediconn.Doctor.LoginDoctor;

public class SelectProvider extends AppCompatActivity {
    LinearLayout ll1,ll2,ll3,ll4,ll5,ll6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_provider);
        ll1=(LinearLayout)findViewById(R.id.LL1);
        ll2=(LinearLayout)findViewById(R.id.LL2);
        ll3=(LinearLayout)findViewById(R.id.LL3);
        ll4=(LinearLayout)findViewById(R.id.LL4);
        ll5=(LinearLayout)findViewById(R.id.LL5);
        ll6=(LinearLayout)findViewById(R.id.LL6);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectProvider.this, LoginDoctor.class);
                startActivity(i);
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(SelectProvider.this, LoginDoctor.class);
//                startActivity(i);
                AlertDialog alertDialog = new AlertDialog.Builder(SelectProvider.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(SelectProvider.this, LoginDoctor.class);
//                startActivity(i);
                AlertDialog alertDialog = new AlertDialog.Builder(SelectProvider.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(SelectProvider.this, LoginDoctor.class);
//                startActivity(i);
                AlertDialog alertDialog = new AlertDialog.Builder(SelectProvider.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(SelectProvider.this, LoginDoctor.class);
//                startActivity(i);
                AlertDialog alertDialog = new AlertDialog.Builder(SelectProvider.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        ll6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(SelectProvider.this, LoginDoctor.class);
//                startActivity(i);

                AlertDialog alertDialog = new AlertDialog.Builder(SelectProvider.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }


        });
    }
}