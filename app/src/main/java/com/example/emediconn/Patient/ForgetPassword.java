package com.example.emediconn.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Doctor.DrawerActivity;
import com.example.emediconn.Extras.GenericTextWatcher;
import com.example.emediconn.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {
    private static final String TAG = ForgetPassword.class.getSimpleName();
Button opt_forgetpassbtn,sendotp;
EditText et_otpforgetpassmobnum,otp_textbox_one,otp_textbox_two,otp_textbox_three,otp_textbox_five,otp_textbox_four,otp_textbox_six;
PrefManager prefManager;
ProgressDialog ploader;
LinearLayout lin;
int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        opt_forgetpassbtn=(Button)findViewById(R.id.opt_forgetpassbtn);
        et_otpforgetpassmobnum=(EditText) findViewById(R.id.et_otpforgetpassmobnum);


        otp_textbox_one = findViewById(R.id.editTextone1);
        otp_textbox_two = findViewById(R.id.editTexttwo2);
        otp_textbox_three = findViewById(R.id.editTextthree3);
        otp_textbox_four = findViewById(R.id.editTextfour4);
        otp_textbox_five= findViewById(R.id.editTextfive5);
        otp_textbox_six = findViewById(R.id.editTextsix6);
        sendotp = findViewById(R.id.sendotp);
        lin = findViewById(R.id.layout_otp);

        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four,otp_textbox_five,otp_textbox_six};

        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit));
        otp_textbox_five.addTextChangedListener(new GenericTextWatcher(otp_textbox_five, edit));
        otp_textbox_six.addTextChangedListener(new GenericTextWatcher(otp_textbox_six, edit));



        ploader = new ProgressDialog(ForgetPassword.this);
        prefManager=new PrefManager(ForgetPassword.this);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                if(et_otpforgetpassmobnum.getText().toString().length() < 10){
                    et_otpforgetpassmobnum.setError(" Mobile number should be valid");
                    et_otpforgetpassmobnum.requestFocus();
                    flag=1;
                }
                if(flag==0) {
                    sendotp.setVisibility(View.INVISIBLE);
                    lin.setVisibility(View.VISIBLE);
                    opt_forgetpassbtn.setVisibility(View.VISIBLE);

                }
            }
        });

        opt_forgetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobilenum = et_otpforgetpassmobnum.getText().toString().trim();
                String otp1= otp_textbox_one.getText().toString().trim();
                String otp2= otp_textbox_two.getText().toString().trim();
                String otp3= otp_textbox_three.getText().toString().trim();
                String otp4= otp_textbox_four.getText().toString().trim();
                String otp5= otp_textbox_five.getText().toString().trim();
                String otp6= otp_textbox_six.getText().toString().trim();

                String otp=(otp1+otp2+otp3+otp4+otp5+otp6);

                flag=0;
                if(et_otpforgetpassmobnum.getText().toString().length() < 10){
                    et_otpforgetpassmobnum.setError(" Mobile number should be valid");
                    et_otpforgetpassmobnum.requestFocus();
                    flag=1;
                }
                if(flag==0){
                     getpassword(mobilenum,otp);

                   /* Intent intent = new Intent(ForgetPassword.this, otp_patient.class);
                    startActivity(intent);*/
                }
            }

        });
    }

    private void getpassword( final String mobilenum,final String otp){
        ploader.setMessage("Logging in ...");
        ploader.show();
        final String role="patient";

        JSONObject obj = new JSONObject();
        try {
            obj.put("username", mobilenum);
            obj.put("OTP", otp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_FORGETPASSWORD,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d(TAG,"TRYYYYYY"+response);
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                String message = response.getString("message");
                                Toast.makeText(ForgetPassword.this, "" +message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(ForgetPassword.this, "Creadentials Wrong" +response, Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            Log.d(TAG,"CATCHHHH"+e);
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(ForgetPassword.this, ResetPassword.class);

                        intent.putExtra("mobilenumber",mobilenum);
                        startActivity(intent);
                        Log.d(TAG,"Number"+mobilenum);

                        System.out.println(response);
                        ploader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ploader.dismiss();
                    }
                });
        queue.add(jsObjRequest);

    }
}