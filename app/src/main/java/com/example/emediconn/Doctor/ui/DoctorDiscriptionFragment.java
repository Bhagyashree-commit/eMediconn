package com.example.emediconn.Doctor.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.emediconn.Lab.TimeSlot;
import com.example.emediconn.Patient.BookAppointmentFragment;
import com.example.emediconn.Patient.PatientDashboardFragment;
import com.example.emediconn.Patient.VideoConferenceActivity;
import com.example.emediconn.R;
import com.example.emediconn.web_communication.WebCall;
import com.example.emediconn.web_communication.WebConstants;
import com.example.emediconn.web_communication.WebResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorDiscriptionFragment extends Fragment implements WebResponse, AdapterView.OnItemClickListener {
    ImageView ivpropic;
    ProgressDialog ploader;
    PrefManager prefManager;
    CircleImageView propic;
    RatingBar patientreview;

    Button btnBookAppointment;
    Button btnVideo;

    public static String proimage;
    public static String hospitalname;
    public static String fees;
    public static String speciality;
    public static String docname;
    public static String doc_mobile;
    public static String timeslot;

    private String token;
    private String room_Id;


    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

    public static String date_time;
    TextView tv_docname, tv_specialization, tv_reviewpostdate, tv_education, tv_servicesofdoctor, tv_experiance, tv_fees, tv_avaibility, tv_hospname, tv_destination, tv_PRpatientname, tv_PRheading, tv_PRstory;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    ArrayList<String> mArraylist = new ArrayList<>();

    public DoctorDiscriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Summary");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_discription, container, false);
        tv_docname = v.findViewById(R.id.doctornameondescrip);
        btnBookAppointment = v.findViewById(R.id.btnBookAppointment);
        propic = v.findViewById(R.id.doctorpropicondescrip);
        tv_specialization = v.findViewById(R.id.doctorspecializationondescrip);
        tv_education = v.findViewById(R.id.doctordeegreeondescrip);
        tv_experiance = v.findViewById(R.id.docexperianceondescrip);
        tv_fees = v.findViewById(R.id.feesondescription);
        tv_avaibility = v.findViewById(R.id.clinictimeondescrip);
        tv_hospname = v.findViewById(R.id.hospitalnameondescrip);
        tv_destination = v.findViewById(R.id.hospitaladdressondescrip);
        tv_PRpatientname = v.findViewById(R.id.PRpatientname);
        tv_PRheading = v.findViewById(R.id.PRheading);
        tv_PRstory = v.findViewById(R.id.PRstory);
        patientreview = v.findViewById(R.id.patientreview);
        tv_reviewpostdate = v.findViewById(R.id.reviewpostdate);
        tv_servicesofdoctor = v.findViewById(R.id.doctorservice);
        btnVideo = v.findViewById(R.id.btnVideo);

        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (AppConfig.Status.equalsIgnoreCase("1")) {
                            replaceFragmentWithAnimation(new PatientDashboardFragment());
                        } else {
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

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getRoomId();
                showBottomSheetDialog();
            }
        });

        ploader = new ProgressDialog(getActivity());
        prefManager = new PrefManager(getActivity());

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            // prefManager = new PrefManager(getContext());
            HashMap<String, String> user = prefManager.getUserDetails();
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            if (AppConfig.Status.equalsIgnoreCase("1")) {
                getDoctorDiscription(PatientDashboardFragment.mobilenumber);

                AppointmentTime(PatientDashboardFragment.mobilenumber,date);
            } else {
                getDoctorDiscription(DoctorListFragment.mobilenumber);

                AppointmentTime(DoctorListFragment.mobilenumber,date);
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
        FragmentManager mFragmentManager = getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getDoctorDiscription(final String mobilenumber) {
        ploader.setMessage("Getting List...");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETDOCTORDISCRIPTION, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("Descriptionasasasasasas", "" + response);
                        try {

                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray jsonArray = response.getJSONArray("DoctorDescription");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    docname = jsonObject.getString("doctor_name");
                                    String experience = jsonObject.getString("experience");
                                    doc_mobile = jsonObject.getString("mobilenumber");
                                    fees = jsonObject.getString("fees");
                                    hospitalname = jsonObject.getString("hospitalname");
                                    String hospitalcity = jsonObject.getString("hospitalcity");
                                    String degree = jsonObject.getString("degree");
                                    proimage = "http://healthcare.blucorsys.in/daccount/" + jsonObject.getString("profile_photo");


                                    tv_docname.setText(docname);

                                    tv_education.setText(degree);
                                    tv_hospname.setText(hospitalname);
                                    tv_destination.setText(hospitalcity);
                                    tv_fees.setText("\u20b9 "+fees);
                                    tv_experiance.setText(experience);
                                    Glide.with(getActivity())
                                            .load(proimage)
                                            .into(propic);




                                    JSONArray jsonAr = jsonObject.getJSONArray("patient_stories");
                                    for (int j = 0; j < jsonAr.length(); j++) {
                                        jsonObject = jsonAr.getJSONObject(j);
                                        String patient_name = jsonObject.getString("patient_name");
                                        String story_heading = jsonObject.getString("story_heading");
                                        String story = jsonObject.getString("story");
                                        String feedBack = jsonObject.getString("feedBack");
                                        String posted_date = jsonObject.getString("posted_date");
                                        tv_PRpatientname.setText(patient_name);
                                        tv_PRheading.setText(story_heading);
                                        tv_PRstory.setText(story);
                                        patientreview.setRating(Float.parseFloat(feedBack));
                                        tv_reviewpostdate.setText(getTimeAgo(Long.parseLong(posted_date)));
                                        Log.e("", "Pname" + patient_name);
                                    }

                                    JSONArray jsonA = jsonObject.getJSONArray("specialization");
                                    for (int j = 0; j < jsonA.length(); j++) {
                                        jsonObject = jsonA.getJSONObject(j);
                                        speciality = jsonObject.getString("speciality");
                                        tv_specialization.setText(speciality);
                                    }
                                    JSONArray jsoA = jsonObject.getJSONArray("service");
                                    for (int j = 0; j < jsoA.length(); j++) {
                                        jsonObject = jsoA.getJSONObject(j);
                                        String service = jsonObject.getString("service");
                                        mArraylist.add(service);
                                        //tv_servicesofdoctor.setText(service);
                                    }
                                    StringBuilder builder = new StringBuilder();
                                    int index = 0;
                                    for (String details : mArraylist) {
                                        index++;
                                        builder.append(index + ") " + details + "\n");
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
                }) {
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
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            //mins = diff / MINUTE_MILLIS ;
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            if ((diff / HOUR_MILLIS) == 1) {
                return "an hour ago";
            } else {
                return diff / HOUR_MILLIS + " hours ago";
            }
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return date_time;
        }
    }

    @Override
    public void onWebResponse(String response, int callCode) {
        switch (callCode) {

            case WebConstants.getRoomIdCode:
                onGetRoomIdSuccess(response);
                break;
            case WebConstants.getTokenURLCode:
                onGetTokenSuccess(response);
                break;
            case WebConstants.validateRoomIdCode:
                onVaidateRoomIdSuccess(response);
                break;
        }
    }

    @Override
    public void onWebResponseError(String error, int callCode) {
        Log.e("errorDashboard", error);
    }

    public void getRoomId() {
        new WebCall(getActivity(), this, jsonObjectToSend(), WebConstants.getRoomId, WebConstants.getRoomIdCode, false, true).execute();
    }

    private void onVaidateRoomIdSuccess(String response) {
        Log.e("responsevalidate", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("result").trim().equalsIgnoreCase("40001")) {
                Toast.makeText(getActivity(), jsonObject.optString("error"), Toast.LENGTH_SHORT).show();
            } else {
                // savePreferences();
                getRoomTokenWebCall();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject jsonObjectToSend() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Test Dev Room");
            jsonObject.put("settings", getSettingsObject());
            jsonObject.put("data", getDataObject());
            jsonObject.put("sip", getSIPObject());
            jsonObject.put("owner_ref", "fadaADADAAee");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getSIPObject() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

    private JSONObject getDataObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Rashmi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getSettingsObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("description", "Testing");
            jsonObject.put("scheduled", false);
            jsonObject.put("scheduled_time", "");
            jsonObject.put("duration", 50);
            jsonObject.put("participants", 2);
            jsonObject.put("billing_code", 1234);
            jsonObject.put("auto_recording", false);
            jsonObject.put("active_talker", true);
            jsonObject.put("max_active_talkers", 1);
            jsonObject.put("moderators", 2);
            jsonObject.put("quality", "HD");
            jsonObject.put("wait_moderator", false);
            jsonObject.put("adhoc", false);
            jsonObject.put("mode", "lecture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void getRoomTokenWebCall() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Rashmi");
            jsonObject.put("role", "participant");
            jsonObject.put("user_ref", "2236");
            jsonObject.put("roomId", room_Id);

            new WebCall(getActivity(), this, jsonObject, WebConstants.getTokenURL, WebConstants.getTokenURLCode, false, false).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onGetTokenSuccess(String response) {
        Log.e("responseToken", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("result").equalsIgnoreCase("0")) {
                token = jsonObject.optString("token");

                Intent intent = new Intent(getActivity(), VideoConferenceActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("name", "Rashmi");
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), jsonObject.optString("error"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onGetRoomIdSuccess(String response) {
        Log.e("responseDashboard", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            room_Id = jsonObject.optJSONObject("room").optString("room_id");
            validateRoomIDWebCall();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void validateRoomIDWebCall() {
        new WebCall(getActivity(), this, null, WebConstants.validateRoomId + room_Id, WebConstants.validateRoomIdCode, true, false).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.payment_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ImageView ivDoctorImage = dialog.findViewById(R.id.ivDoctorImage);

        TextView tvDoctorName = dialog.findViewById(R.id.tvDoctorName);
        TextView tvFee = dialog.findViewById(R.id.tvFee);

        TextView btnPaynow=dialog.findViewById(R.id.btnPaynow);

        tvDoctorName.setText(docname);

        tvFee.setText("Fee: \u20b9 "+fees);

        Glide.with(getActivity())
                .load(proimage)
                .into(ivDoctorImage);

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

        setAdapter(recyclerView);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnectedMainThred(getActivity())) {
                    // ploader.show();
                    HashMap<String, String> user = prefManager.getUserDetails();
                    String patientId = prefManager.get("user_id");
                    BookAppointment(doc_mobile, prefManager.get("full_name"), timeslot, prefManager.get("mobilenumber"),"dwivedirashmi321@gmail.com",patientId);

                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void setAdapter(RecyclerView mRecyclerview) {
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerview.setAdapter(new TimeSlotAdapter(mRecyclerview,arrayList));
    }
    //*Recyclerview Adapter*//
    private class TimeSlotAdapter extends RecyclerView.Adapter<Holder> {

        private int last_position = 0;
        private int present_position = -1;

        ArrayList<HashMap<String,String>> arrayList;

        RecyclerView recyclerView;

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_items, parent, false));
        }

        public TimeSlotAdapter(RecyclerView recyclerView,ArrayList<HashMap<String,String>> arrayList) {
            this.recyclerView = recyclerView;
            this.arrayList=arrayList;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {

            holder.tvTime.setText(arrayList.get(position).get("slot"));

            if (last_position != -1 && present_position == position) {
                holder.tvTime.setBackground(getResources().getDrawable(R.drawable.capsule));
                holder.tvTime.setTextColor(getResources().getColor(R.color.white));
                Log.i("TAG", last_position + "--lastPosition--" + position + "--position--" + present_position + "--presentPosition in if");
            } else {
                // holder.tvTime.setVisibility(View.GONE);
                holder.tvTime.setBackground(getResources().getDrawable(R.drawable.timecapsule));
                holder.tvTime.setTextColor(getResources().getColor(R.color.blue_900));
                Log.i("TAG", last_position + "--lastPosition--" + position + "--position--" + present_position + "--presentPosition in else");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    present_position = recyclerView.getChildAdapterPosition(view);

                    if (present_position != RecyclerView.NO_POSITION) {

                        if (present_position != -1) {
                            //img=layoutManager.findViewByPosition(last_position).findViewById(R.id.check);
                            //  holder.tvTime.setVisibility(View.GONE);
                            // holder.tvTime.setBackgroundColor(getResources().getColor(R.color.yellow_500));
                            holder.tvTime.setBackground(getResources().getDrawable(R.drawable.timecapsule));
                            holder.tvTime.setTextColor(getResources().getColor(R.color.blue_900));

                            notifyItemChanged(last_position);
                            //img=layoutManager.findViewByPosition(present_position).findViewById(R.id.check);
                            // holder.tvTime.setVisibility(View.VISIBLE);
                            //  holder.tvTime.setBackgroundColor(getResources().getColor(R.color.green_200));
                            holder.tvTime.setBackground(getResources().getDrawable(R.drawable.capsule));
                            holder.tvTime.setTextColor(getResources().getColor(R.color.white));

                            timeslot=holder.tvTime.getText().toString();

                            notifyItemChanged(present_position);
                            System.err.println(last_position + " -- myParent");

                            last_position = present_position;
                            System.err.println(last_position + " -- myParent2");

                        }
                    } else {
                        // holder.tvTime.setVisibility(View.VISIBLE);
                        // holder.tvTime.setBackgroundColor(getResources().getColor(R.color.green_200));
                        holder.tvTime.setBackground(getResources().getDrawable(R.drawable.capsule));
                        holder.tvTime.setTextColor(getResources().getColor(R.color.white));
                        notifyItemChanged(present_position);
                        System.err.println("Called extra else");
                    }
                }
            });
//            Typeface typeface = ResourcesCompat.getFont(TimeSlot.this, R.font.nunitosemibold);
//            holder.tvCategoryName.setTypeface(typeface);
        }

        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        TextView tvTime;

        public Holder(View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    private void AppointmentTime( final String mobilenumber,final String dateOfBooking){
        ploader.setMessage("Loading ...");
        ploader.show();
        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenumber);
            obj.put("dateOfBooking", dateOfBooking);

            Log.e("objjjjj",""+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_TIMESLOT,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        arrayList.clear();
                        Log.e("Response1233","TRYYYYYY"+response);
                        try {
                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                HashMap<String,String> map;
                                JSONArray jsonArray=response.getJSONArray("DoctorAppointments");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    JSONArray array=object.getJSONArray("available_slots");

                                    for (int j=0;j<array.length();j++)
                                    {
                                        JSONObject jObject=array.getJSONObject(j);
                                        map=new HashMap<>();
                                        map.put("slot",jObject.getString("slot"));
                                        arrayList.add(map);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Sorry No Appointments Available", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.d("Exception","CATCHHHH"+e);
                            e.printStackTrace();
                        }

                        ploader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ploader.dismiss();
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

    private void BookAppointment( final String mobilenumber,String patient_name,String startDate,String patient_phonenumber,String patient_emailaddress,String patientId){
        ploader.setMessage("Loading");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenumber);
            obj.put("patient_name", patient_name);
            obj.put("startDate", startDate);
            obj.put("patient_phonenumber", patient_phonenumber);
            obj.put("appointmentType", "video");
            obj.put("patient_emailaddress", patient_emailaddress);
            obj.put("user_id", patientId);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_BOOKAPPOINTMENT,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("Response1233","TRYYYYYY"+response);
                        try {
                            if(response.getString("status").equalsIgnoreCase("true"))
                            {

                                SuccessPopup();
                                Toast.makeText(getActivity(), "Appointment Booked successfully", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Log.d("Exception","CATCHHHH"+e);
                            e.printStackTrace();
                        }
                        ploader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ploader.dismiss();
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

    public void SuccessPopup() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //  dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_trans);
        dialog.show();

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2700);
                    replaceFragmentWithAnimation(new PatientDashboardFragment());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();

                }
            }
        };

        timerThread.start();
    }
}