package com.example.emediconn.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.emediconn.Adapter.SpecialityAdapter;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Fragment.SpecialityFragment;
import com.example.emediconn.Patient.LoginPatient;
import com.example.emediconn.Patient.PatientRegistration;
import com.example.emediconn.Patient.otp_patient;
import com.example.emediconn.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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
                String role="Doctor";

                flag=0;
                if(et_doctor_name.getText().toString().length()==0 || et_doctor_name.getText().toString().trim().matches(namePattern)){
                    et_doctor_name.setError(" User Name should be valid");
                    et_doctor_name.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_doctor_password.getText().toString().length()< 4 || et_doctor_password.length()>10){
                    et_doctor_password.setError(" Password should be between 4 to 10 character");
                    et_doctor_password.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_doctor_mobilenum.getText().toString().length() < 10){
                    et_doctor_mobilenum.setError(" Mobile number should be valid");
                    et_doctor_mobilenum.requestFocus();
                    flag=1;
                }
                if(flag==0){
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
        final String role="doctor";

        JSONObject obj = new JSONObject();
        try {
            obj.put("fullname", fullname);
            obj.put("mobilenumber", mobilenum);
            obj.put("usertype", role);
            obj.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_REGISTERPATIENT,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d(TAG,"TRYYYYYY"+response);
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                String name = response.getString("full_name");
                                String usertype = response.getString("user_type");
                                String userID = response.getString("user_id");

                                prefManager.createUserLoginSession(name,usertype,userID,role,password);
                                prefManager.setLogin(true,userID);

                                Log.d(TAG,"Name"+name);
                                Log.d(TAG,"usertype"+usertype);
                                Log.d(TAG,"ID"+userID);
                                Log.d(TAG,"UserType"+role);
                                Log.d(TAG,"Password"+password);

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