package com.example.emediconn.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Extras.GenericTextWatcher2;
import com.example.emediconn.Extras.GenericTextWatcher3;
import com.example.emediconn.Patient.LoginPatient;
import com.example.emediconn.Patient.otp_patient;
import com.example.emediconn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class otp_doctor extends AppCompatActivity {
    private static final String TAG = otp_doctor.class.getSimpleName();
    Button otpsubmit;
    PrefManager prefManager;
    ProgressDialog ploader;
    int flag=0;
    EditText otp_textbox_one,otp_textbox_two,otp_textbox_three,otp_textbox_five,otp_textbox_four,otp_textbox_six;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_doctor);

            otpsubmit=(Button)findViewById(R.id.otpsubmit_doctor);
            otp_textbox_one = findViewById(R.id.editTextone_one1);
            otp_textbox_two = findViewById(R.id.editTexttwo_two2);
            otp_textbox_three = findViewById(R.id.editTextthree_three3);
            otp_textbox_four = findViewById(R.id.editTextfour_four4);
            otp_textbox_five= findViewById(R.id.editTextfive_five5);
            otp_textbox_six = findViewById(R.id.editTextsix_six6);

            EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four,otp_textbox_five,otp_textbox_six};

            otp_textbox_one.addTextChangedListener(new GenericTextWatcher3(otp_textbox_one, edit));
            otp_textbox_two.addTextChangedListener(new GenericTextWatcher3(otp_textbox_two, edit));
            otp_textbox_three.addTextChangedListener(new GenericTextWatcher3(otp_textbox_three, edit));
            otp_textbox_four.addTextChangedListener(new GenericTextWatcher3(otp_textbox_four, edit));
            otp_textbox_five.addTextChangedListener(new GenericTextWatcher3(otp_textbox_five, edit));
            otp_textbox_six.addTextChangedListener(new GenericTextWatcher3(otp_textbox_six, edit));



            ploader = new ProgressDialog(otp_doctor.this);
            prefManager=new PrefManager(otp_doctor.this);


            otpsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String otp1= otp_textbox_one.getText().toString().trim();
                    String otp2= otp_textbox_two.getText().toString().trim();
                    String otp3= otp_textbox_three.getText().toString().trim();
                    String otp4= otp_textbox_four.getText().toString().trim();
                    String otp5= otp_textbox_five.getText().toString().trim();
                    String otp6= otp_textbox_six.getText().toString().trim();

                    String otp=(otp1+otp2+otp3+otp4+otp5+otp6);
                    String otpnew="123456";


                    flag=0;
                    if (otp1.isEmpty() || otp2.isEmpty() || otp3.isEmpty() || otp4.isEmpty() || otp5.isEmpty() || otp6.isEmpty()) {

                        Toast.makeText(otp_doctor.this, "otp is not correct..", Toast.LENGTH_SHORT).show();

                        flag=1;
                    }
                    flag=0;
                    if (otp.isEmpty() || !otpnew.equals(otp)) {

                        Toast.makeText(otp_doctor.this, "OTP Does Not Match..Please fill correct OTP!", Toast.LENGTH_SHORT).show();
                        flag=1;
                    }

                    if( flag==0){
                        HashMap<String, String> user = prefManager.getUserDetails();

                        // name
                        String username = user.get(PrefManager.KEY_USERNAME);

                        // email
                        String mobilenum = user.get(PrefManager.KEY_MOBILENUM);

                        String password = user.get(PrefManager.KEY_PASSWORD);

                        String usertype = user.get(PrefManager.KEY_ROLE);

                        validateotp(username,mobilenum,password,usertype);

                    }

             /*   Intent i = new Intent(otp_patient.this, LoginPatient.class);
                startActivity(i);*/
                }
            });
        }
        private void validateotp(final String username, final String mobilenum,
        final String password,final String usertype){
            ploader.setMessage("Logging in ...");
            ploader.show();
            final String role="doctor";

            JSONObject obj = new JSONObject();
            try {
                obj.put("fullname", username);
                obj.put("mobilenumber", mobilenum);
                obj.put("usertype", role);
                obj.put("password", password);

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
                                    String message = response.getString("message");

                                    Toast.makeText(otp_doctor.this, "Mobile number validated sucessfully! Try login now!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(otp_doctor.this, LoginDoctor.class);
                                    intent.putExtra("mobilenumber",mobilenum);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(otp_doctor.this, "Response" +response.getString("message"), Toast.LENGTH_SHORT).show();

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