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
import android.widget.CheckBox;
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
 * Use the {@link EstablishmentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstablishmentDetailsFragment extends Fragment {
    private static final String TAG = String.valueOf(EstablishmentDetailsFragment.class);

    ImageView btn_back;
    Button btn_continuehospdetails;
    EditText et_hospname,et_hospcity,et_hosplocality;
    int flag;
    CheckBox ch_own,ch_visit;
    PrefManager prefManager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EstablishmentDetailsFragment() {
        // Required empty public constructor
    }

    public static EstablishmentDetailsFragment newInstance(String param1, String param2) {
        EstablishmentDetailsFragment fragment = new EstablishmentDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_establishment_details, container, false);

        btn_back=v.findViewById(R.id.backbutton);
        btn_continuehospdetails=v.findViewById(R.id.btn_continuehospdetails);
        et_hospname=v.findViewById(R.id.et_hospname);
        et_hospcity=v.findViewById(R.id.et_hospcity);
        et_hosplocality=v.findViewById(R.id.et_hosplocality);
        ch_own=v.findViewById(R.id.ch_own);
        ch_visit=v.findViewById(R.id.ch_visit);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DoctorDashboard.class);
                startActivity(intent);
            }
        });

        btn_continuehospdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                flag=0;
                if(et_hospname.getText().toString().length() == 0){
                    et_hospname.setError(" This Feild is Compulsary");
                    et_hospname.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_hospcity.getText().toString().length() == 0){
                    et_hospcity.setError("  This Feild is Compulsary");
                    et_hospcity.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_hosplocality.getText().toString().length() == 0){
                    et_hosplocality.setError("  This Feild is Compulsary");
                    et_hosplocality.requestFocus();
                    flag=1;
                }
                if(flag==0) {
                    sendDetailToServer2();
                    replaceFragmentWithAnimation(new EducationDetailsFragment());
                }

            }
        });

        return  v;
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
    private void sendDetailToServer2( ){

        prefManager = new PrefManager(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

        String mobilenum = user.get(PrefManager.KEY_MOBILENUM);
        String username = user.get(PrefManager.KEY_USERNAME);

        String hospname = et_hospname.getText().toString().trim();
        String hospcity = et_hospcity.getText().toString().trim();
        String hosplocality=et_hosplocality.getText().toString();

        String hosptype = "Selected type";
        if(ch_own.isChecked()){
            hosptype += "\nOwn";
        }
        if(ch_visit.isChecked()){
            hosptype += "\nVisit";
        }
        Log.d(TAG,"HOSPITAL TYPE"+hosptype);
        Toast.makeText(getActivity(), hosptype, Toast.LENGTH_SHORT).show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenum);
            obj.put("hospitalName",hospname);
            obj.put("hospitalCity",hospcity);
            obj.put("locality", hosplocality);
            obj.put("selectHospitalType", hosptype);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_HOSPDETAILS,obj,
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