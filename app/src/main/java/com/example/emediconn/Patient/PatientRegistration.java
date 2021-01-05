package com.example.emediconn.Patient;

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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.emediconn.ChooseRole;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Doctor.DoctorDashboard;
import com.example.emediconn.Doctor.DoctorRegistration;
import com.example.emediconn.Doctor.DrawerActivity;
import com.example.emediconn.MainActivity;
import com.example.emediconn.R;
import com.example.emediconn.SelectProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PatientRegistration extends AppCompatActivity {
    private static final String TAG = PatientRegistration.class.getSimpleName();
Button btn_registerpatinet;
EditText et_userfullname,et_mobilenum,et_password;
TextView existing_user;
int flag;
    String  namePattern = "[a-zA-Z]+";
    ProgressDialog ploader;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        et_userfullname=(EditText)findViewById(R.id.edit_fullname);
        et_mobilenum=(EditText)findViewById(R.id.edit_mobilenum);
        et_password=(EditText)findViewById(R.id.edit_password);
        btn_registerpatinet=(Button)findViewById(R.id.btn_registepaitent);
        existing_user=(TextView) findViewById(R.id.text_existinguser);


         ploader = new ProgressDialog(PatientRegistration.this);
         prefManager=new PrefManager(PatientRegistration.this);


        if (prefManager.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(PatientRegistration.this,
                    DrawerActivity.class);
            startActivity(intent);
            finish();
        }


         existing_user.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(PatientRegistration.this, LoginPatient.class);
                 startActivity(i);
             }
         });

        btn_registerpatinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullname = et_userfullname.getText().toString().trim();
                String mobilenum = et_mobilenum.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String role="patient";
                prefManager.set("fullname",fullname);
                prefManager.set("mobilenumber",mobilenum);
                prefManager.set("user_type",role);
                prefManager.set("password",password);
                prefManager.commit();

                Log.e("fullname", prefManager.get("fullname"));
                Log.e("mobilenumber", prefManager.get("mobilenumber"));
                Log.e("password", prefManager.get("password"));
                Log.e("user_type", prefManager.get("user_type"));

                if(et_userfullname.getText().toString().length()==0 || et_userfullname.getText().toString().trim().matches(namePattern)){
                    et_userfullname.setError(" User Name should be valid");
                    et_userfullname.requestFocus();

                }

                else if(et_password.getText().toString().length()< 4 || et_password.length()>10){
                    et_password.setError(" Password should be between 4 to 10 character");
                    et_password.requestFocus();

                }

               else if(et_mobilenum.getText().toString().length() < 10){
                    et_mobilenum.setError(" Mobile number should be valid");
                    et_mobilenum.requestFocus();

                }
               else {
                    registerPatient(fullname,mobilenum,password);

                   /* Intent intent = new Intent(PatientRegistration.this, otp_patient.class);
                    startActivity(intent);
*/
                }
            }
        });



    }


    private void registerPatient(final String fullname, final String mobilenum,
                                 final String password){
            ploader.setMessage("Logging in ...");
          ploader.show();
            final String role="patient";

            JSONObject obj = new JSONObject();
            try {
                obj.put("full_name", fullname);
                obj.put("mobilenumber", mobilenum);
                obj.put("user_type", role);
                obj.put("password", password);
                Log.e("OBJECT",""+obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,AppConfig.URL_VALIDATEMOBILENUM,obj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            ploader.dismiss();
                            Log.d(TAG,"Validat123"+response);
                            try {

                                if(response.getString("status").equalsIgnoreCase("true"))
                                {

                                    String ab=response.getString("message");
                                    Toast.makeText(PatientRegistration.this, "Response" +response.getString("message"), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(PatientRegistration.this, otp_patient.class);
                                    intent.putExtra("mobilenumber",mobilenum);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(PatientRegistration.this, "Response" +response.getString("message"), Toast.LENGTH_SHORT).show();

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