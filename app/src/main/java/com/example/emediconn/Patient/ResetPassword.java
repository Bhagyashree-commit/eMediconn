package com.example.emediconn.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.emediconn.Doctor.ui.ChangePassword;
import com.example.emediconn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {
    private static final String TAG = String.valueOf(ResetPassword.class);
EditText et_newpass,et_conpass;
Button btn_reset_password;
int flag;
ProgressDialog ploader;
PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        btn_reset_password=(Button)findViewById(R.id.btn_resetpass);
        et_newpass=(EditText) findViewById(R.id.et_newpassPatient);
        et_conpass=(EditText)findViewById(R.id.et_conpassPatient);
        ploader=new ProgressDialog(this);
        prefManager= new PrefManager(this);

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
                  resetPassword();
                }
            }
        });
    }

    private void resetPassword( ){
        ploader.setMessage("Logging in ...");
        ploader.show();


        String newpassword = et_newpass.getText().toString().trim();
        String confirmpassword=et_conpass.getText().toString();


        JSONObject obj = new JSONObject();
        try {
            obj.put("username", getIntent().getStringExtra("mobilenumber"));
            obj.put("newPassword",newpassword);
            obj.put("confirmNewPassword",confirmpassword);

            Log.e("TAG",""+obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_RESETPASSWORD,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d(TAG,"TRYYYYYY"+response);
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                String mesage = response.getString("message");
                                 Toast.makeText(ResetPassword.this, " " +mesage, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPassword.this, LoginPatient.class);

                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(ResetPassword.this, "Password Does Not Change! Try Later On..", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            Log.d(TAG,"CATCHHHH"+e);
                            e.printStackTrace();
                        }

                        System.out.println(response);
                        //ploader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // ploader.dismiss();
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