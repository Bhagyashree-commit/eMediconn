package com.example.emediconn.Doctor.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DoctorPersonelInfoFragment extends Fragment {
    private static final String TAG = String.valueOf(DoctorPersonelInfoFragment.class);

TextView tv_medicalinfo,tv_educationalinfo,tv_medicalregistration,tv_establishment_details;
EditText edittext_docname,edittext_city,edittext_email;
RadioGroup gender;
AutoCompleteTextView autocomplete;
   String[] arr ;
LinearLayout linearLayout;
TextView text1;
Button btn_continue1,btn_save1;
int flag;
ProgressDialog ploader;
PrefManager prefManager;
String gendervalue;
    String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    public DoctorPersonelInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        getSpeciality();

        tv_educationalinfo=v.findViewById(R.id.educational_information);
        //tv_medicalinfo=v.findViewById(R.id.medical_registration);
        autocomplete = (AutoCompleteTextView)v.findViewById(R.id.auto_specialitytextview);
        tv_medicalregistration=v.findViewById(R.id.medical_registration);
        tv_establishment_details=v.findViewById(R.id.establishment_details);
        gender=v.findViewById(R.id.rg_docgender);
        gendervalue = ((RadioButton) v.findViewById(gender.getCheckedRadioButtonId())).getText().toString();
        edittext_docname=v.findViewById(R.id.editText_doctorName);
        edittext_city=v.findViewById(R.id.et_doccityname);
        edittext_email=v.findViewById(R.id.et_docemailid);
         linearLayout=v.findViewById(R.id.Lin1);
         text1=v.findViewById(R.id.text);
         btn_continue1=v.findViewById(R.id.btn_continue1);
        btn_save1=v.findViewById(R.id.btn_save1);

        linearLayout.setBackgroundColor(getResources().getColor(R.color.blue_300));
        text1.setTextColor(getResources().getColor(R.color.white));

        tv_medicalregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new MedicalRegInfoFragment());
            }
        });
        tv_educationalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new EducationDetailsFragment());

            }
        });
        tv_establishment_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new EstablishmentDetailsFragment());

            }
        });
        prefManager = new PrefManager(getContext());
        ploader =new ProgressDialog(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

        String username = user.get(PrefManager.KEY_USERNAME);

        edittext_docname.setText(username);

        Log.d(TAG,"NAVVV"+prefManager.KEY_USERNAME);

        btn_continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.callnowpopup);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                new Handler().postDelayed(new Runnable() {


                    @Override public void run() {
                        replaceFragmentWithAnimation(new Doctor_profileVerifyFragment());
                        dialog.dismiss();

                    }
                }, 2000);

            }
        });




        btn_save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edittext_email.getText().toString().trim();
                String city = edittext_city.getText().toString().trim();
                String speciality=autocomplete.getText().toString();
                //String gender= ((RadioButton)v.findViewById(gender.getCheckedRadioButtonId())).getText().toString();

                flag=0;
                if(edittext_email.getText().toString().length()==0 || edittext_email.getText().toString().trim().matches(emailPattern)){
                    edittext_email.setError(" Email should Be valid");
                    edittext_email.requestFocus();
                    flag=1;

                }
                flag=0;
                if(edittext_city.getText().toString().length() == 0){
                    edittext_city.setError(" Enter City");
                    edittext_city.requestFocus();
                    flag=1;
                }
                flag=0;
                if(autocomplete.getText().toString().length() == 0){
                    autocomplete.setError(" Enter Speciality");
                    autocomplete.requestFocus();
                    flag=1;
                }
                if(flag==0){
                    doctorProfileDetails();
                    replaceFragmentWithAnimation(new MedicalRegInfoFragment());

                    /* */

                }
            }
        });


        return v;
    }
    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_doctor, fragment);
        FragmentManager mFragmentManager=getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }


    private void getSpeciality() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_GETSPECIALITYNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("geetttttttt", response);
                try {
                    JSONArray array = new JSONArray(response);
                    Log.d(TAG, array.toString());
                    arr= new String[array.length()];

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject job = array.getJSONObject(i);
                        arr[i]=job.getString("speciality");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (getContext(),android.R.layout.select_dialog_item, arr);

                    autocomplete.setThreshold(3);
                    autocomplete.setAdapter(adapter);


                } catch (JSONException e) {

                    Log.e("testerroor", e.toString());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            System.out.println("Time Out and NoConnection...................." + error);

                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(getActivity(), "Connection Time Out.. Please Check Your Internet Connection", duration).show();

                        }
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void doctorProfileDetails( ){
        ploader.setMessage("Logging in ...");
        ploader.show();

        Log.d(TAG,"GENDERRR"+gendervalue);


        prefManager = new PrefManager(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

        String mobilenum = user.get(PrefManager.KEY_MOBILENUM);
        String username = user.get(PrefManager.KEY_USERNAME);

        String email = edittext_email.getText().toString().trim();
        String city = edittext_city.getText().toString().trim();
        String speciality=autocomplete.getText().toString();


        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenum);
            obj.put("full_name", username);
            obj.put("gender",gendervalue);
            obj.put("city",city);
            obj.put("speciality", speciality);
            obj.put("emailaddress",email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_DOCTORPROFILE1,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       ploader.dismiss();
                        Log.d(TAG,"TRYYYYYY"+response);
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                String mesage = response.getString("message");
                               // Toast.makeText(getActivity(), "Suucess" +mesage, Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Creadentials Wrong" +response.getString("message"), Toast.LENGTH_SHORT).show();

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