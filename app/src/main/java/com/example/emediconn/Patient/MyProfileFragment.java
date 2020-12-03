package com.example.emediconn.Patient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyProfileFragment extends Fragment {

    //RelativeLayout
    RelativeLayout rrMen;
    RelativeLayout rrWomen;
    RelativeLayout rrDate;

    //Imageview
    ImageView ivMen;
    ImageView ivWomen;
    ImageView ivProfile;

    String gender;


    //textview
    TextView tvDate;
    TextView tvName;
    TextView tvPhone;
    TextView tvMobile;
    PrefManager prefManager;



    //Edittext
    EditText etName;
    EditText etEmail;
    EditText etHeight;
    EditText etWeight;

    ProgressDialog ploader;


    Button btnUpdateProfile;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_my_profile2, container, false);
        rrMen=v.findViewById(R.id.rrMen);
        rrWomen=v.findViewById(R.id.rrWomen);
        rrDate=v.findViewById(R.id.rrDate);
        tvDate=v.findViewById(R.id.tvDate);
        tvName=v.findViewById(R.id.tvName);
        tvPhone=v.findViewById(R.id.tvPhone);
        etName=v.findViewById(R.id.etName);
        tvMobile=v.findViewById(R.id.tvMobile);
        etEmail=v.findViewById(R.id.etEmail);
        etHeight=v.findViewById(R.id.etHeight);
        etWeight=v.findViewById(R.id.etWeight);

        btnUpdateProfile=v.findViewById(R.id.btnUpdateProfile);

        ivMen = (ImageView) v.findViewById(R.id.imageView11);
        ivWomen = (ImageView)v. findViewById(R.id.imageView12);
        ivProfile = (ImageView)v. findViewById(R.id.ivProfile);

        ploader = new ProgressDialog(getActivity());
        prefManager=new PrefManager(getActivity());

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            HitMyprofile(prefManager.get("mobilenumber"));
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        replaceFragmentWithAnimation(new MyAccountFragment());
                        return true;
                    }
                }
                return false;
            }
        });



        rrDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        rrMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMen.setImageResource(R.drawable.ic_check_box_selected);
                ivWomen.setImageResource(R.drawable.ic_check_box_unselected);
                gender = "Male";
            }
        });

        rrWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMen.setImageResource(R.drawable.ic_check_box_unselected);
                ivWomen.setImageResource(R.drawable.ic_check_box_selected);
                gender = "Female";
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnectedMainThred(getActivity())) {
                    HitUpdateProfile(tvPhone.getText().toString(),etName.getText().toString(),etEmail.getText().toString(),gender,tvDate.getText().toString(),etHeight.getText().toString(),etWeight.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
     Calendar myCalendar = Calendar.getInstance();

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        tvDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.nav_host_fragment, fragment);
        FragmentManager mFragmentManager=getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }


    //API
    private void HitMyprofile( final String mobilenumber){

        ploader.setMessage("Getting List...");
        ploader.show();

        final JSONObject obj = new JSONObject();

        try {
            obj.put("mobilenumber", mobilenumber);
            // Log.e("objj",""+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETPATIENTPROFILE,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                             JSONArray jsonArray=response.getJSONArray("PatientDetails");
                             for(int i=0;i<jsonArray.length();i++)
                             {
                                 JSONObject object=jsonArray.getJSONObject(i);

                                if(object.getString("profile_photo").equalsIgnoreCase(""))
                                {
                                    ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.userr));
                                }
                                else
                                {
                                    String image_url="http://healthcare.blucorsys.in/account/"+object.getString("profile_photo");
                                    Log.e("url",image_url);
                                    Glide.with(getActivity())
                                            .load(image_url)
                                            .into(ivProfile);
                                }

                                 tvName.setText(object.getString("full_name"));
                                 etName.setText(object.getString("full_name"));
                                 tvPhone.setText(object.getString("mobilenumber"));
                                 tvMobile.setText(object.getString("mobilenumber"));
                                 tvDate.setText(object.getString("dob"));
                                 etEmail.setText(object.getString("emailaddress"));
                                 etHeight.setText(object.getString("height"));
                                 etWeight.setText(object.getString("weight"));

                                 if(object.getString("gender").equalsIgnoreCase("Male"))
                                 {
                                     ivMen.setImageResource(R.drawable.ic_check_box_selected);
                                     ivWomen.setImageResource(R.drawable.ic_check_box_unselected);
                                     gender = "Male";
                                 }
                                 else if(object.getString("gender").equalsIgnoreCase("Female"))
                                 {
                                     ivMen.setImageResource(R.drawable.ic_check_box_unselected);
                                     ivWomen.setImageResource(R.drawable.ic_check_box_selected);
                                     gender = "Female";
                                 }
                                 else
                                 {
                                     ivMen.setImageResource(R.drawable.ic_check_box_unselected);
                                     ivWomen.setImageResource(R.drawable.ic_check_box_unselected);
                                     gender = "";
                                 }
                             }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                        System.out.println(response);
                        ploader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ploader.dismiss();
                    }
                })
        {
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


    private void HitUpdateProfile( final String mobilenumber,final String full_name,final String emailaddress,final String genderr,final String dob,final String height,final  String weight){
        ploader.setMessage("Please wait...");
        ploader.show();

        final JSONObject obj = new JSONObject();

        try {
            obj.put("mobilenumber", mobilenumber);
            obj.put("full_name", full_name);
            obj.put("emailaddress", emailaddress);
            obj.put("gender", genderr);
            obj.put("dob", dob);
            obj.put("height", height);
            obj.put("weight", weight);
             Log.e("objj",""+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_UPDATEPATIENT,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        try {
                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                            Toast.makeText(getActivity(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {

                        }
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ploader.dismiss();
                    }
                })
        {
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