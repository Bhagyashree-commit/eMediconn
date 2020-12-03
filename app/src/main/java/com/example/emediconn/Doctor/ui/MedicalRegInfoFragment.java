package com.example.emediconn.Doctor.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.emediconn.Doctor.DoctorDashboard;
import com.example.emediconn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicalRegInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicalRegInfoFragment extends Fragment {
    private static final String TAG = String.valueOf(MedicalRegInfoFragment.class);
    ImageView  btn_back;
    Button btn_medregcontinue;
    PrefManager prefManager;
    EditText et_regno,et_regcouncil,et_regyear;
    int flag;


    public MedicalRegInfoFragment() {
        // Required empty public constructor
    }


    public static MedicalRegInfoFragment newInstance(String param1, String param2) {
        MedicalRegInfoFragment fragment = new MedicalRegInfoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_medical_reg, container, false);
        btn_back=v.findViewById(R.id.backbutton);
        btn_medregcontinue=v.findViewById(R.id.btn_medregcontinue);
        et_regno=v.findViewById(R.id.et_registrationumber);
        et_regcouncil=v.findViewById(R.id.et_regcouncil);
        et_regyear=v.findViewById(R.id.et_regyear);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DoctorDashboard.class);
                startActivity(intent);
            }
        });

        btn_medregcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                flag=0;
                if(et_regno.getText().toString().length() == 0){
                    et_regno.setError(" Enter Registration Number");
                    et_regno.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_regcouncil.getText().toString().length() == 0){
                    et_regcouncil.setError(" Enter Registration Council");
                    et_regcouncil.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_regyear.getText().toString().length() == 0){
                    et_regyear.setError(" Enter Registration Year");
                    et_regyear.requestFocus();
                    flag=1;
                }
                if(flag==0) {
                    sendDetailToServer();
                    replaceFragmentWithAnimation(new EducationDetailsFragment());
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

    private void sendDetailToServer( ){

        prefManager = new PrefManager(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

        String mobilenum = user.get(PrefManager.KEY_MOBILENUM);
        String username = user.get(PrefManager.KEY_USERNAME);

        String regNo = et_regno.getText().toString().trim();
        String regCouncil = et_regcouncil.getText().toString().trim();
        String regYear=et_regyear.getText().toString();


        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenum);
            obj.put("registrationNumber",regNo);
            obj.put("registrationCouncil",regCouncil);
            obj.put("registrationYear", regYear);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_DOCTORMEDICALREGISTRATION,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  ploader.dismiss();
                        Log.d(TAG,"TRYYYYYY"+response);
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                String mesage = response.getString("message");
                               // Toast.makeText(getActivity(), "Suucess111" +mesage, Toast.LENGTH_SHORT).show();


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