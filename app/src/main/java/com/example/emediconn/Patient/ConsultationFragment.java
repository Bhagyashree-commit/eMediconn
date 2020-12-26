package com.example.emediconn.Patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.emediconn.Extras.FileCompressor;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.Model.MyAppointmentModel;
import com.example.emediconn.R;
import com.example.emediconn.web_communication.WebCall;
import com.example.emediconn.web_communication.WebConstants;
import com.example.emediconn.web_communication.WebResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ConsultationFragment extends Fragment implements WebResponse {

    ProgressDialog ploader;

    ArrayList<MyAppointmentModel> arrayList=new ArrayList<>();

    RecyclerView recyclerView;

    PrefManager prefManager;

    String room_Id;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("My Profile");

        ploader = new ProgressDialog(getActivity());
        prefManager=new PrefManager(getActivity());

        View v= inflater.inflate(R.layout.activity_appointments, container, false);
        recyclerView=v.findViewById(R.id.recyclerView);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            // ploader.show();
            HashMap<String, String> user = prefManager.getUserDetails();
            String patientId = prefManager.get("user_id");
            HitGetAppointment(patientId);
        }
        else {
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
                        //replaceFragmentWithAnimation(new PatientDashboardFragment());
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }

    //API
    private void HitGetAppointment( final String patientId){
        ploader.setMessage("Getting List...");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("patientId", patientId);

            // Log.e("objj",""+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETVIDEOAPPOINTMENT,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("DoctorResponse",""+response);
                        try {
                            MyAppointmentModel patientModel;
                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                JSONArray dailyAppointments=response.getJSONArray("doctorAppointment");
                                for(int i=0;i<dailyAppointments.length();i++)
                                {
                                    JSONObject jsonObject=dailyAppointments.getJSONObject(i);
                                    patientModel=new MyAppointmentModel();
                                    patientModel.setDoctor_name(jsonObject.getString("doctor_name"));
                                    patientModel.setRoomId(jsonObject.getString("roomId"));

                                    if(jsonObject.getString("doctor_image").isEmpty())
                                    {
                                        patientModel.setDoctor_image("");
                                    }
                                    else
                                    {
                                        String proimage="http://healthcare.blucorsys.in/daccount/"+jsonObject.getString("doctor_image");
                                        patientModel.setDoctor_image(proimage);
                                    }

                                    patientModel.setFees(jsonObject.getString("fees"));
                                    patientModel.setStartTime(jsonObject.getString("startTime"));
                                    patientModel.setSpeciality(jsonObject.getString("speciality"));
                                    arrayList.add(patientModel);
                                }

                                Collections.reverse(arrayList);
                                setAdapter(recyclerView,arrayList);
                               // ArrayList<MyAppointmentModel> list = arrayList();

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                        System.out.println(response);
                        ploader.dismiss();
                    }
                }, new Response.ErrorListener() {
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

    public void setAdapter(RecyclerView mRecyclerview, ArrayList<MyAppointmentModel> arrayList)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1));
        mRecyclerview.setAdapter(new PatientAdapter(arrayList));
    }

    @Override
    public void onWebResponse(String response, int callCode) {
        switch (callCode) {
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

    private void getRoomTokenWebCall() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "user");// Change it
            jsonObject.put("role", "participant");
            jsonObject.put("user_ref", "2236");
            jsonObject.put("roomId", room_Id);

            new WebCall(getActivity(), this, jsonObject, WebConstants.getTokenURL, WebConstants.getTokenURLCode, false, false).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void onVaidateRoomIdSuccess(String response) {
        Log.e("responsevalidate", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("result").trim().equalsIgnoreCase("40001")) {
                Toast.makeText(getActivity(), jsonObject.optString("error"), Toast.LENGTH_SHORT).show();
            } else {

                getRoomTokenWebCall();
            }
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
                Log.e("token", token);
                Intent intent = new Intent(getActivity(), VideoConferenceActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("name", "User");
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), jsonObject.optString("error"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Doctor Recyclerview
    private class PatientAdapter extends RecyclerView.Adapter<DocHolder> {

        ArrayList<MyAppointmentModel> arrayList=new ArrayList<>();

        public PatientAdapter( ArrayList<MyAppointmentModel> arrayList) {
            this.arrayList=arrayList;
        }
        public DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DocHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.consultation_items, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final DocHolder holder, final int position) {

            holder.itemView.setTag(arrayList.get(position).getRoomId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),ChatActivity.class));
                }
            });

            if(arrayList.get(position).getDoctor_image().isEmpty())
            {
                holder.ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.doctorr));
            }
            else
            {
                Glide.with(getActivity())
                        .load(arrayList.get(position).getDoctor_image())
                        .into(holder.ivProfile);
            }
            holder.tvName.setText(arrayList.get(position).getDoctor_name());

            holder.tvSpeciality.setText(arrayList.get(position).getSpeciality());

            if(arrayList.get(position).getRoomId().isEmpty() || arrayList.get(position).getRoomId().equalsIgnoreCase("null"))
            {
                holder.ivJoinNow.setVisibility(View.GONE);
            }
            else
                {
                    holder.ivJoinNow.setVisibility(View.VISIBLE);
            }

            holder.ivJoinNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    room_Id =arrayList.get(position).getRoomId() ;
                    validateRoomIDWebCall();
                }
            });

            String[] separated = (arrayList.get(position).getStartTime()).split(" ");

            String valid_until=separated[0];

            String date_current = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

            Long value=diffTime(dateFormat.format(new Date()),separated[1] +" " +separated[2]);

            if(date_current.equalsIgnoreCase(valid_until))
            {
                if((value <= 0))
                {
                    holder.tvTime.setVisibility(View.GONE);
                    holder.tvConsultnow.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.tvConsultnow.setVisibility(View.GONE);
                    startTimer(diffTime(dateFormat.format(new Date()),separated[1] +" " +separated[2]),holder.tvTime,holder.tvConsultnow);
                }
            }
            else
            {
                holder.tvTime.setVisibility(View.GONE);
                holder.tvConsultnow.setVisibility(View.VISIBLE);
            }

            holder.tvTime.setText("Time Left: "+separated[1] +" " +separated[2]);
        }

        public int getItemCount() {
            return arrayList.size();
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
    private class DocHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        TextView tvFee;
        TextView tvName;
        TextView tvStatus;
        TextView tvLocation;
        TextView tvSpeciality;
        TextView tvDate;
        TextView tvDayName;
        TextView tvConsultnow;
        ImageView ivProfile;
        ImageView ivJoinNow;

        public DocHolder(View itemView) {
            super(itemView);

            tvTime=itemView.findViewById(R.id.tvTime);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvFee=itemView.findViewById(R.id.tvFee);
            tvLocation=itemView.findViewById(R.id.tvLocation);
            tvName=itemView.findViewById(R.id.tvName);
            tvSpeciality=itemView.findViewById(R.id.tvSpeciality);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvDayName=itemView.findViewById(R.id.tvDayName);
            tvConsultnow=itemView.findViewById(R.id.tvConsultnow);
            ivProfile=itemView.findViewById(R.id.ivProfile);
            ivJoinNow=itemView.findViewById(R.id.ivJoinNow);
        }
    }
    private void validateRoomIDWebCall() {
        new WebCall(getActivity(), this, null, WebConstants.validateRoomId + room_Id, WebConstants.validateRoomIdCode, true, false).execute();
    }
    public long diffTime(String time1,String time2) {
        long min = 0;
        long difference ;


        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa"); // for 12-hour system, hh should be used instead of HH
            // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0
            Date date1 = simpleDateFormat.parse(time1);
            Date date2 = simpleDateFormat.parse(time2);

            difference = (date2.getTime() - date1.getTime()) / 1000;
            long hours = difference % (24 * 3600) / 3600; // Calculating Hours
            long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
            min = minute + (hours * 60);

            // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return TimeUnit.MINUTES.toMillis(min);
    }
    private void startTimer(long millisec,TextView countdownTimerText,TextView tvConsultnow) {
        new CountDownTimer(millisec, 1000){
            @Override
            public void onTick(long millisUntilFinished) {

                long millis = millisUntilFinished;

                String hms = String.format("%02d:%02d:%02d:%02d",
                        TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millis)),
                        (TimeUnit.MILLISECONDS.toHours(millis) -
                                TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis))),
                        (TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))), (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                countdownTimerText.setText("Time left: "+hms);//set text
            }

            @Override
            public void onFinish() {

                countdownTimerText.setVisibility(View.GONE);
                /*clearing all fields and displaying countdown finished message          */
                tvConsultnow.setVisibility(View.VISIBLE);
            }

        }.start();
    }
}
