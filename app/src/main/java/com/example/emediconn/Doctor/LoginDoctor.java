package com.example.emediconn.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Doctor.ui.DoctorProfile;
import com.example.emediconn.Patient.ForgetPassword;
import com.example.emediconn.Patient.LoginPatient;
import com.example.emediconn.Patient.PatientRegistration;
import com.example.emediconn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginDoctor extends AppCompatActivity {
    private static final String TAG = LoginPatient.class.getSimpleName();
    EditText et_doc_mobilenum,et_doc_password;
    TextView existing_user,forgetpassword_doc;
    PrefManager prefManager;
    ProgressDialog ploader;
    int flag;
    Button btn_logindoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        btn_logindoctor=(Button)findViewById(R.id.btn_logindoctor);
        et_doc_mobilenum=(EditText) findViewById(R.id.et_doctor_mobnumlogin);
        et_doc_password=(EditText) findViewById(R.id.et_doctor_passwordlogin);
        existing_user=(TextView) findViewById(R.id.text_existingdoctorlogin);



              //forgetpassword_doc=(TextView) findViewById(R.id.forgetpassword_patient);

        ploader = new ProgressDialog(LoginDoctor.this);
        prefManager=new PrefManager(LoginDoctor.this);

        existing_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginDoctor.this, DoctorRegistration.class);
                startActivity(i);
            }
        });
      /*  forgetpassword_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginDoctor.this, ForgetPassword.class);
                startActivity(i);
            }
        });*/

        btn_logindoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobilenum = et_doc_mobilenum.getText().toString().trim();
                String password = et_doc_password.getText().toString().trim();
                flag=0;
                if(et_doc_password.getText().toString().length()< 4 || et_doc_password.length()>10){
                    et_doc_password.setError(" Password should be between 4 to 10 character");
                    et_doc_password.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_doc_mobilenum.getText().toString().length() < 10){
                    et_doc_mobilenum.setError(" Mobile number should be valid");
                    et_doc_mobilenum.requestFocus();
                    flag=1;
                }
                if(flag==0){
                    logindoctor(mobilenum,password);

                   /* Intent intent = new Intent(LoginPatient.this, DrawerActivity.class);
                    startActivity(intent);*/

                }
            }
        });
    }

    private void logindoctor( final String mobilenum,
                               final String password){
        ploader.setMessage("Logging in ...");
        ploader.show();
        final String role="doctor";

        JSONObject obj = new JSONObject();
        try {
            obj.put("username", mobilenum);
            obj.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_LOGINPATIENT,obj,
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
                                String message=response.getString("message");

                                prefManager.createUserLoginSession(name,usertype,userID,role,password);
                                prefManager.setLogin(true,userID);

                                Toast.makeText(LoginDoctor.this, "Login Response" +message, Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"Name"+name);
                                Log.d(TAG,"usertype"+usertype);
                                Log.d(TAG,"ID"+userID);
                                Log.d(TAG,"ID"+role);

                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                                transaction.replace(R.id.frame_doctor,  new DoctorProfile());
                                FragmentManager mFragmentManager=getSupportFragmentManager();
                                mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                 transaction.addToBackStack(null);
                                transaction.commit();
                            }
                            else
                            {
                                Toast.makeText(LoginDoctor.this, "Creadentials Wrong" +response.getString("message"), Toast.LENGTH_SHORT).show();

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
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                // headers.put("key", "Value");
                return headers;
            }
        };

        queue.add(jsObjRequest);

    }
}