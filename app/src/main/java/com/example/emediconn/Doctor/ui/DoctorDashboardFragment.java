package com.example.emediconn.Doctor.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.emediconn.ChooseRole;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoctorDashboardFragment extends Fragment {
    TextView tv_name,doctor_name,tv_docotoreducation,tv_speciality;
    ImageView iv_logout,ivProfilepic;
    LinearLayout linlayappointment;
    PrefManager prefManager;
    LinearLayout linlaydoctormyprofile,linlaychangepassword;
    ProgressDialog ploader;
    public static String speciality;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.doctor_dashboard_fragment, container, false);
        linlayappointment=v.findViewById(R.id.linlayappointment);
        linlaydoctormyprofile=v.findViewById(R.id.linlayprofile);
        linlaychangepassword=v.findViewById(R.id.linlaychangepassword);
        tv_name=v.findViewById(R.id.titletext);
        doctor_name=v.findViewById(R.id.doctor_name);
        iv_logout=v.findViewById(R.id.logout_doctor);
        ivProfilepic=v.findViewById(R.id.ivProfilepic);
        tv_speciality=v.findViewById(R.id.tv_speciality);
        tv_docotoreducation=v.findViewById(R.id.tv_docotoreducation);
        prefManager= new PrefManager(getActivity());
        ploader= new ProgressDialog(getActivity());

        tv_name.setText("Doctor Dashboard");
        doctor_name.setText(prefManager.get("full_name"));


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            HitMyprofile(prefManager.get("mobilenumber"));
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        linlayappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new AppointmentsFragment());
            }
        });

        linlaydoctormyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new MyDoctorProfileFragment());
            }
        });

        linlaychangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new ChangePasswordFragment());
            }
        });


        //Back
//        v.setFocusableInTouchMode(true);
//        v.requestFocus();
//        v.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                        replaceFragmentWithAnimation(new DoctorDashboardFragment());
//
//                        return true;
//                    }
//                }
//                return false;
//            }
//
//        });
        // Inflate the layout for this fragment


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
    public void logout() {
        //Dialog
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertyesno);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);


        //findId
        TextView tvYes = (TextView) dialog.findViewById(R.id.tvOk);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvcancel);
        TextView tvReason = (TextView) dialog.findViewById(R.id.textView22);
        TextView tvAlertMsg = (TextView) dialog.findViewById(R.id.tvAlertMsg);

        //set value
        tvAlertMsg.setText("Confirmation Alert..!!!");
        tvReason.setText("Are you sure want to logout?");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        //set listener
        tvYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                prefManager.logoutUser();
                startActivity(new Intent(getActivity(), ChooseRole.class));
                getActivity().finishAffinity();
                //  overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
//                pref.set(AppSettings.CustomerID, "");
//                pref.commit();

                // preferences.set("login","no");
                //  preferences.commit();
//                Toasty.success(DrawerActivity.this, "Logged out..!!!", Toast.LENGTH_SHORT).show();
                //  Finish();
                dialog.dismiss();
            }
        });


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void HitMyprofile( final String mobilenumber){

        ploader.setMessage("Loading...Please Wait..");
        ploader.show();

        final JSONObject obj = new JSONObject();

        try {
            obj.put("mobilenumber", mobilenumber);
            // Log.e("objj",""+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETDOCTORDISCRIPTION,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                JSONArray jsonArray=response.getJSONArray("DoctorDescription");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

//                                    if(object.getString("profile_photo").equalsIgnoreCase(""))
//                                    {
//                                        ivProfilepic.setImageDrawable(getResources().getDrawable(R.drawable.userr));
//                                    }
//                                    else
//                                    {
//                                        String image_url="http://healthcare.blucorsys.in/account/"+object.getString("profile_photo");
//                                        Log.e("url",image_url);
//                                        Glide.with(getActivity())
//                                                .load(image_url)
//                                                .into(ivProfilepic);
//                                    }


//                                    etName.setText(object.getString("full_name"));
//                                    tvPhone.setText(object.getString("mobilenumber"));
//                                    tvMobile.setText(object.getString("mobilenumber"));
//                                    tvDate.setText(object.getString("dob"));
//                                    etEmail.setText(object.getString("emailaddress"));
//                                    etHeight.setText(object.getString("height"));
//                                    etWeight.setText(object.getString("weight"));

//                                    if(object.getString("gender").equalsIgnoreCase("Male"))
//                                    {
//                                        ivMen.setImageResource(R.drawable.ic_check_box_selected);
//                                        ivWomen.setImageResource(R.drawable.ic_check_box_unselected);
//                                        gender = "Male";
//                                    }
//                                    else if(object.getString("gender").equalsIgnoreCase("Female"))
//                                    {
//                                        ivMen.setImageResource(R.drawable.ic_check_box_unselected);
//                                        ivWomen.setImageResource(R.drawable.ic_check_box_selected);
//                                        gender = "Female";
//                                    }
//                                    else
//                                    {
//                                        ivMen.setImageResource(R.drawable.ic_check_box_unselected);
//                                        ivWomen.setImageResource(R.drawable.ic_check_box_unselected);
//                                        gender = "";
//                                    }

                                    prefManager.set("user_id",response.getString("user_id"));
                                    prefManager.set("doctor_id",response.getString("doctor_id"));
                                    prefManager.set("doctor_name",response.getString("doctor_name"));
                                    prefManager.set("mobilenumber",response.getString("mobilenumber"));
                                    prefManager.set("emailaddress",response.getString("emailaddress"));
                                    prefManager.set("profile_photo",response.getString("profile_photo"));
                                    prefManager.set("gender",response.getString("gender"));
                                    prefManager.set("dob",response.getString("dob"));
                                    prefManager.set("address",response.getString("address"));
                                    prefManager.set("city",response.getString("city"));
                                    prefManager.set("state",response.getString("state"));
                                    prefManager.set("country",response.getString("country"));
                                    prefManager.set("pincode",response.getString("pincode"));
                                    prefManager.set("specialization",response.getString("specialization"));
                                    prefManager.set("registrationnumber",response.getString("registrationnumber"));
                                    prefManager.set("registrationcouncil",response.getString("registrationcouncil"));
                                    prefManager.set("registrationyear",response.getString("registrationyear"));
                                    prefManager.set("degree",response.getString("degree"));
                                    prefManager.set("collegename",response.getString("collegename"));
                                    prefManager.set("completionYear",response.getString("completionYear"));
                                    prefManager.set("experience",response.getString("experience"));
                                    prefManager.set("hospitalname",response.getString("hospitalname"));
                                    prefManager.set("hospitalcity",response.getString("hospitalcity"));
                                    prefManager.set("locality",response.getString("locality"));
                                    prefManager.set("fees",response.getString("fees"));
                                    prefManager.set("session1_startTime",response.getString("session1_startTime"));
                                    prefManager.set("session1_endTime",response.getString("session1_endTime"));
                                    prefManager.set("session2_startTime",response.getString("session2_startTime"));
                                    prefManager.set("session2_endTime",response.getString("session2_endTime"));
                                    prefManager.set("identity_documentName",response.getString("identity_documentName"));
                                    prefManager.set("identity_documentPath",response.getString("identity_documentPath"));
                                    prefManager.set("medical_documentName",response.getString("medical_documentName"));
                                    prefManager.set("medical_documentPath",response.getString("medical_documentPath"));
                                    prefManager.set("hospital_documentName",response.getString("hospital_documentName"));
                                    prefManager.set("hospital_documentPath",response.getString("hospital_documentPath"));
                                    prefManager.set("patient_stories",response.getString("patient_stories"));
                                    prefManager.commit();
                                    Log.e("userid",""+response.get("user_id"));

                                    tv_docotoreducation.setText(jsonObject.getString("degree"));
                                    JSONArray jsonA = jsonObject.getJSONArray("specialization");
                                    for (int j = 0; j < jsonA.length(); j++) {
                                        jsonObject = jsonA.getJSONObject(j);
                                        speciality = jsonObject.getString("speciality");
                                        tv_speciality.setText(speciality);
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
}
