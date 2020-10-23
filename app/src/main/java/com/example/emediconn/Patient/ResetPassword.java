package com.example.emediconn.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.emediconn.R;

public class ResetPassword extends AppCompatActivity {
EditText et_newpass,et_conpass;
Button btn_reset_password;
int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        btn_reset_password=(Button)findViewById(R.id.btn_resetpass);
        et_newpass=(EditText) findViewById(R.id.et_newpassPatient);
        et_conpass=(EditText)findViewById(R.id.et_conpassPatient);

        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newpassword = et_newpass.getText().toString().trim();
                String confirmpassword = et_conpass.getText().toString().trim();

                flag = 0;
                if (et_newpass.getText().toString().length() < 4 || et_newpass.length() > 10) {
                    et_newpass.setError(" Password should be between 4 to 10 character");
                    et_newpass.requestFocus();
                    flag = 1;
                }
                flag = 0;
                if (et_conpass.getText().toString().length() < 4 || et_conpass.length() > 10) {
                    et_conpass.setError(" Password should be between 4 to 10 character");
                    et_conpass.requestFocus();
                    flag = 1;
                }

                if (flag == 0) {
                   // getpassword(mobilenum, otp);
                }
            }
        });
    }
}