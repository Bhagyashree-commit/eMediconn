package com.example.emediconn.Doctor.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.Model.DoctorListModel;
import com.example.emediconn.Patient.DoctorCategory;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorDiscriptionFragment extends Fragment {
ImageView ivpropic;
ProgressDialog ploader;
PrefManager prefManager;
TextView tv_docname,tv_specialization,tv_education,tv_experiance,tv_fees,tv_avaibility;

    public DoctorDiscriptionFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Summary");

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_doctor_discription, container, false);
tv_docname=v.findViewById(R.id.doctornameondescrip);
tv_specialization=v.findViewById(R.id.doctorspecializationondescrip);
tv_education=v.findViewById(R.id.doctordeegreeondescrip);
tv_experiance=v.findViewById(R.id.docexperianceondescrip);
tv_fees=v.findViewById(R.id.feesondescription);
tv_avaibility=v.findViewById(R.id.clinictimeondescrip);
        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        replaceFragmentWithAnimation(new DoctorCategory());

                        return true;
                    }
                }
                return false;
            }
        });

        ploader = new ProgressDialog(getActivity());
        prefManager=new PrefManager(getActivity());

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            getDoctorDiscription("9960664554");
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }


        return v;
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

    private void getDoctorDiscription( final String mobilenumber){
        ploader.setMessage("Getting List...");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenumber);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETDOCTORDISCRIPTION,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("DoctorResponse121",""+response);
                        try {
                            if(response.getString("status").equalsIgnoreCase("true"))
                            {

                                JSONArray jsonArray=response.getJSONArray("DoctorDiscription");
                                for(int i=0;i<jsonArray.length();i++)
                                {


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