package com.example.emediconn.Doctor.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Patient.PatientDashboardFragment;
import com.example.emediconn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordFragment extends Fragment {
    private static final String TAG = String.valueOf(ChangePasswordFragment.class);
TextView titletext;
TextView cp_mobilenumber;
EditText cp_oldpassword,cp_newpassword,cp_confirmpassword;
PrefManager prefManager;
ProgressDialog ploader;
Button btnchangepassword;
int flag;
ImageView backbtn,logout;
LinearLayout linlay;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Change Password");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_change_password, container, false);
linlay=v.findViewById(R.id.heraderlinlay);
        prefManager=new PrefManager(getContext());
        prefManager.get("user_type");

        if(prefManager.get("user_type").equalsIgnoreCase("patient")){
            linlay.setVisibility(View.GONE);

            Log.e("1111","Yes");
        }
        else {
            linlay.setVisibility(View.VISIBLE);
            Log.e("111111","NO");

        }

        titletext=v.findViewById(R.id.titletext);
        titletext.setText("Change Password");
        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(prefManager.get("user_type").equalsIgnoreCase("patient")){
                            replaceFragmentWithAnimation(new PatientDashboardFragment());

                            Log.e("1111","Yes");
                        }
                        else {
                            replaceFragmentWithAnimation(new DoctorDashboardFragment());
                            Log.e("111111","NO");

                        }
                        //
                        return true;
                    }
                }
                return false;
            }
        });

        cp_mobilenumber=v.findViewById(R.id.cp_mobilenumber);
        cp_newpassword=v.findViewById(R.id.cp_newpassword);
        cp_oldpassword=v.findViewById(R.id.cp_oldpassword);
        cp_confirmpassword=v.findViewById(R.id.cp_confirmpassword);
        btnchangepassword=v.findViewById(R.id.btn_changepassword);
        backbtn=v.findViewById(R.id.backbutton);
        logout=v.findViewById(R.id.logout_doctor);
        logout.setVisibility(View.GONE);


        prefManager = new PrefManager(getContext());
        ploader =new ProgressDialog(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

        String mobilenumber = user.get(PrefManager.KEY_MOBILENUM);

cp_mobilenumber.setText(prefManager.get("mobilenumber"));




        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new DoctorDashboardFragment());
            }
        });

        btnchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldpassword = cp_oldpassword.getText().toString().trim();
                String newpassword = cp_newpassword.getText().toString().trim();
                String confirmpassword=cp_confirmpassword.getText().toString();

                flag=0;
                if(cp_oldpassword.getText().toString().length()==0){
                    cp_oldpassword.setError(" Enter Old Password");
                    cp_oldpassword.requestFocus();
                    flag=1;

                }
                flag=0;
                if(cp_newpassword.getText().toString().length() == 0){
                    cp_newpassword.setError(" Enter New Password");
                    cp_newpassword.requestFocus();
                    flag=1;
                }
                flag=0;
                if(cp_confirmpassword.getText().toString().length() == 0){
                    cp_confirmpassword.setError(" Enter Confirm Password");
                    cp_confirmpassword.requestFocus();
                    flag=1;
                }
                if(flag==0){
                    changePassword();

                }
            }
        });

        return v;
    }


    private void changePassword( ){
        ploader.setMessage("Loading ...");
        ploader.show();

        prefManager = new PrefManager(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

        String mobilenum =  prefManager.get("mobilenumber");
        Log.e(TAG,"BHagyaa"+mobilenum);

        String oldpassword = cp_oldpassword.getText().toString().trim();
        String newpassword = cp_newpassword.getText().toString().trim();
        String confirmpassword=cp_confirmpassword.getText().toString();


        JSONObject obj = new JSONObject();
        try {
            obj.put("username", mobilenum);
            obj.put("oldPassword", oldpassword);
            obj.put("newPassword",newpassword);
            obj.put("confirmNewPassword",confirmpassword);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_CHANGEPASSWORD,obj,
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

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_doctor, fragment);
        FragmentManager mFragmentManager=getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }
}