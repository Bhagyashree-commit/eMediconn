package com.example.emediconn.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Patient.PatientRegistration;
import com.example.emediconn.R;

import org.json.JSONException;
import org.json.JSONObject;

public class DoctorRegistration extends AppCompatActivity {

    private static final String TAG = PatientRegistration.class.getSimpleName();
    Button btnregister_doctor;
    EditText et_doctor_name,et_doctor_mobilenum,et_doctor_password;
    TextView existing_user;
    PrefManager prefManager;
    ProgressDialog ploader;
    int flag;
    String  namePattern = "[a-zA-Z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        et_doctor_name=(EditText)findViewById(R.id.et_doctor_name);
        et_doctor_mobilenum=(EditText)findViewById(R.id.et_doctor_mobnum);
        et_doctor_password=(EditText)findViewById(R.id.et_doctor_password);
        btnregister_doctor=(Button)findViewById(R.id.btn_registerdoctor);
        existing_user=(TextView) findViewById(R.id.text_existingdoctor);
        ploader = new ProgressDialog(DoctorRegistration.this);
        prefManager=new PrefManager(DoctorRegistration.this);

        if (prefManager.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(DoctorRegistration.this,
                    DoctorDashboard.class);
            startActivity(intent);
            finish();
        }


        existing_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoctorRegistration.this, LoginDoctor.class);
                startActivity(i);
            }
        });


        btnregister_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullname = et_doctor_name.getText().toString().trim();
                String mobilenum = et_doctor_mobilenum.getText().toString().trim();
                String password = et_doctor_password.getText().toString().trim();
                String role="doctor";
                prefManager.set("fullname",fullname);
                prefManager.set("mobilenumber",mobilenum);
                prefManager.set("usertype",role);
                prefManager.set("password",password);
                prefManager.commit();

                Log.e("fullname", prefManager.get("fullname"));
                Log.e("mobilenumber", prefManager.get("mobilenumber"));
                Log.e("password", prefManager.get("password"));
                Log.e("usertype", prefManager.get("usertype"));

                if(et_doctor_name.getText().toString().length()==0 || et_doctor_name.getText().toString().trim().matches(namePattern)){
                    et_doctor_name.setError(" User Name should be valid");
                    et_doctor_name.requestFocus();

                }
               else if(et_doctor_password.getText().toString().length()< 4 || et_doctor_password.length()>10){
                    et_doctor_password.setError(" Password should be between 4 to 10 character");
                    et_doctor_password.requestFocus();

                }
                else if(et_doctor_mobilenum.getText().toString().length() < 10){
                    et_doctor_mobilenum.setError(" Mobile number should be valid");
                    et_doctor_mobilenum.requestFocus();

                }
               else{
                    registerPatient(fullname,mobilenum,password);
                }
            }
        });

    }
    private void registerPatient(final String fullname, final String mobilenum,
                                 final String password){
        ploader.setMessage("Logging in ...");
        ploader.show();
        final String role="doctor";

        JSONObject obj = new JSONObject();
        try {
            obj.put("fullname", fullname);
            obj.put("mobilenumber", mobilenum);
            obj.put("usertype", role);
            obj.put("password", password);
            Log.e("OBJECTRTTT","" +obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_VALIDATEMOBILENUM,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d(TAG,"TRYYYYYY"+response);
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                String ab=response.getString("message");
                                Toast.makeText(DoctorRegistration.this, "Response" +response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DoctorRegistration.this, otp_doctor.class);
                                intent.putExtra("mobilenumber",mobilenum);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(DoctorRegistration.this, "Response" +response.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            Log.d(TAG,"CATCHHHH"+e);
                            e.printStackTrace();
                        }

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