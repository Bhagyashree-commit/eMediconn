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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.example.emediconn.Patient.BookAppointmentFragment;
import com.example.emediconn.Patient.PatientDashboardFragment;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorDiscriptionFragment extends Fragment {
ImageView ivpropic;
ProgressDialog ploader;
PrefManager prefManager;
CircleImageView propic;
RatingBar patientreview;

Button btnBookAppointment;

public static  String proimage;
public static  String hospitalname;
public static  String  fees;
public static  String  speciality;
public static  String    docname;
public static String doc_mobile;

    public static  String date_time;
    TextView tv_docname,tv_specialization,tv_reviewpostdate,tv_education,tv_servicesofdoctor,tv_experiance,tv_fees,tv_avaibility,tv_hospname,tv_destination,tv_PRpatientname,tv_PRheading,tv_PRstory;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    ArrayList<String> mArraylist=new ArrayList<>();

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
btnBookAppointment=v.findViewById(R.id.btnBookAppointment);
propic=v.findViewById(R.id.doctorpropicondescrip);
tv_specialization=v.findViewById(R.id.doctorspecializationondescrip);
tv_education=v.findViewById(R.id.doctordeegreeondescrip);
tv_experiance=v.findViewById(R.id.docexperianceondescrip);
tv_fees=v.findViewById(R.id.feesondescription);
tv_avaibility=v.findViewById(R.id.clinictimeondescrip);
tv_hospname=v.findViewById(R.id.hospitalnameondescrip);
tv_destination=v.findViewById(R.id.hospitaladdressondescrip);
tv_PRpatientname=v.findViewById(R.id.PRpatientname);
tv_PRheading=v.findViewById(R.id.PRheading);
tv_PRstory=v.findViewById(R.id.PRstory);
patientreview=v.findViewById(R.id.patientreview);
tv_reviewpostdate=v.findViewById(R.id.reviewpostdate);
tv_servicesofdoctor=v.findViewById(R.id.doctorservice);

        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(AppConfig.Status.equalsIgnoreCase("1")){
                            replaceFragmentWithAnimation(new PatientDashboardFragment());
                        }else {
                            replaceFragmentWithAnimation(new DoctorListFragment());
                        }

                        return true;
                    }
                }
                return false;
            }
        });


        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentWithAnimation(new BookAppointmentFragment());
            }
        });

        ploader = new ProgressDialog(getActivity());
        prefManager=new PrefManager(getActivity());

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
           // prefManager = new PrefManager(getContext());

            HashMap<String, String> user = prefManager.getUserDetails();

if(AppConfig.Status.equalsIgnoreCase("1")){
    getDoctorDiscription(PatientDashboardFragment.mobilenumber);
}
else {
    getDoctorDiscription(DoctorListFragment.mobilenumber);
}


           // Log.e("Tesssstt",DoctorListFragment.mobilenumber);
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
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETDOCTORDISCRIPTION,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("Descriptionasasasasasas",""+response);
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                JSONArray jsonArray = response .getJSONArray("DoctorDescription");
                                for(int i=0;i<jsonArray .length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                     docname= jsonObject.getString("doctor_name");
                                    String experience= jsonObject.getString("experience");
                                     doc_mobile= jsonObject.getString("mobilenumber");
                                     fees= jsonObject.getString("fees");
                                     hospitalname= jsonObject.getString("hospitalname");
                                    String hospitalcity= jsonObject.getString("hospitalcity");
                                    String degree= jsonObject.getString("degree");
                                    proimage="http://healthcare.blucorsys.in/daccount/"+jsonObject.getString("profile_photo");

                                    tv_docname.setText(docname);
                                    tv_education.setText(degree);
                                    tv_hospname.setText(hospitalname);
                                    tv_destination.setText(hospitalcity);
                                    tv_fees.setText(fees);
                                    tv_experiance.setText(experience);
                                    Glide.with(getActivity())
                                            .load(proimage)
                                            .into(propic);


                                    JSONArray jsonAr = jsonObject .getJSONArray("patient_stories");
                                    for(int j=0;j<jsonAr .length();j++)
                                    {
                                        jsonObject = jsonAr.getJSONObject(j);
                                        String patient_name= jsonObject.getString("patient_name");
                                        String story_heading= jsonObject.getString("story_heading");
                                        String story= jsonObject.getString("story");
                                        String feedBack= jsonObject.getString("feedBack");
                                        String posted_date= jsonObject.getString("posted_date");
                                        tv_PRpatientname.setText(patient_name);
                                        tv_PRheading.setText(story_heading);
                                        tv_PRstory.setText(story);
                                        patientreview.setRating(Float.parseFloat(feedBack));
                                        tv_reviewpostdate.setText(getTimeAgo(Long.parseLong(posted_date)));
                                        Log.e("","Pname"+patient_name);
                                    }

                                    JSONArray jsonA = jsonObject .getJSONArray("specialization");
                                    for(int j=0;j<jsonA .length();j++)
                                    {
                                        jsonObject = jsonA.getJSONObject(j);
                                         speciality= jsonObject.getString("speciality");
                                        tv_specialization.setText(speciality);
                                    }
                                    JSONArray jsoA = jsonObject .getJSONArray("service");
                                    for(int j=0;j<jsoA .length();j++) {
                                        jsonObject = jsoA.getJSONObject(j);
                                        String service = jsonObject.getString("service");
                                        mArraylist.add(service);
                                        //tv_servicesofdoctor.setText(service);
                                    }
                                        StringBuilder builder = new StringBuilder();
                                        int index=0;
                                        for (String details : mArraylist) {
                                            index++;
                                            builder.append(index +") "+details + "\n");
                                        }
                                        tv_servicesofdoctor.setText(builder.toString());

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

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;

        }
        // long now = getCurrentTime(ctx);
        long now = System.currentTimeMillis();

        if (time > now || time <= 0) {
            return null;
        }
        // TODO: localize
        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS)
        {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            //mins = diff / MINUTE_MILLIS ;
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            if((diff/HOUR_MILLIS)==1)
            {
                return  "an hour ago";
            }
            else {
                return diff / HOUR_MILLIS + " hours ago";
            }
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return date_time;
        }    }
}