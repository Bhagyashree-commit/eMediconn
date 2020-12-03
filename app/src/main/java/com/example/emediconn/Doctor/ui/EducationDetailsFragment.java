package com.example.emediconn.Doctor.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
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
 * Use the {@link EducationDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EducationDetailsFragment extends Fragment {
    private static final String TAG = String.valueOf(EducationDetailsFragment.class);

    ImageView btn_back;
    int flag;
    PrefManager prefManager;
    ProgressDialog progressDialog;
    Button btn_conti;

    EditText et_docdegree,et_doccollegename,et_docyearofcompletion,et_docyearofexperiance,yearMonth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EducationDetailsFragment() {
        // Required empty public constructor
    }

    public static EducationDetailsFragment newInstance(String param1, String param2) {
        EducationDetailsFragment fragment = new EducationDetailsFragment();
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
        View v = inflater.inflate(R.layout.fragment_education_details, container, false);
        btn_back=v.findViewById(R.id.backbutton);
        btn_conti=v.findViewById(R.id.btn_educationdetailconti);
        et_docdegree=v.findViewById(R.id.et_docdegree);
        et_doccollegename=v.findViewById(R.id.et_doccollegename);
        et_docyearofcompletion=v.findViewById(R.id.et_docyearofcompletion);
        et_docyearofexperiance=v.findViewById(R.id.et_docyearofexperiance);




        et_docyearofcompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogWithoutDateField().show();


            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DoctorDashboard.class);
                startActivity(intent);
            }
        });
        btn_conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag=0;
                if(et_docdegree.getText().toString().length() == 0){
                    et_docdegree.setError(" This Feild Cannot Be Blank");
                    et_docdegree.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_doccollegename.getText().toString().length() == 0){
                    et_doccollegename.setError(" This Feild Cannot Be Blank");
                    et_doccollegename.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_docyearofcompletion.getText().toString().length() == 0){
                    et_docyearofcompletion.setError(" This Feild Cannot Be Blank");
                    et_docyearofcompletion.requestFocus();
                    flag=1;
                }
                flag=0;
                if(et_docyearofexperiance.getText().toString().length() == 0){
                    et_docyearofexperiance.setError(" This Feild Cannot Be Blank");
                    et_docyearofexperiance.requestFocus();
                    flag=1;
                }

                if(flag==0) {
                    sendDetailToServer1();
                    replaceFragmentWithAnimation(new EstablishmentDetailsFragment());
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
    private void sendDetailToServer1( ){

        prefManager = new PrefManager(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

        String mobilenum = user.get(PrefManager.KEY_MOBILENUM);
        String username = user.get(PrefManager.KEY_USERNAME);

        String degree = et_docdegree.getText().toString().trim();
        String collegename = et_doccollegename.getText().toString().trim();
        String yOFCompletiuon=et_docyearofcompletion.getText().toString();
        String yoexperiance=et_docyearofexperiance.getText().toString();


        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenum);
            obj.put("degreeName",degree);
            obj.put("collegeName",collegename);
            obj.put("completionYear", yOFCompletiuon);
            obj.put("experienceYear", yoexperiance);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_EDUDETAIL,obj,
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


    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(getContext(), null, 2014, 0, 0);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
        }
        return dpd;
    }
}