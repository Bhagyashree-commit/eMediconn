package com.example.emediconn.Doctor.ui;

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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.emediconn.Doctor.DoctorDashboard;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.Model.PatientModel;
import com.example.emediconn.Patient.ChatActivity;
import com.example.emediconn.Patient.VideoConferenceActivity;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class VideoConsultFragment extends Fragment {


    ProgressDialog ploader;
    PrefManager prefManager;


    ArrayList<PatientModel> arrayList;
    RecyclerView recyclerView;


    ImageView logout_doctor;
    ImageView backbutton;
    TextView titletext;

    public static String appointmentId;
    private String token;

    public static boolean timeup=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.videoconsultfragment, container, false);


        //findId
        recyclerView=v.findViewById(R.id.recyclerView);
        logout_doctor=v.findViewById(R.id.logout_doctor);
        titletext=v.findViewById(R.id.titletext);
        backbutton=v.findViewById(R.id.backbutton);
        logout_doctor.setVisibility(View.GONE);
        titletext.setText("Video Consultation");
        ploader = new ProgressDialog(getActivity());

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                     startActivity(new Intent(getActivity(), DoctorDashboard.class));
                        return true;
                    }
                }
                return false;
            }

        });

        prefManager=new PrefManager(getActivity());

        arrayList=new ArrayList<>();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DoctorDashboard.class));
            }
        });


        if (Utils.isNetworkConnectedMainThred(getActivity())) {

            String patientId = prefManager.get("mobilenumber");
            HitGetAppointment("8767841781");
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
        // Inflate the layout for this fragment
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


    //Doctor Recyclerview
    private class PatientAdapter extends RecyclerView.Adapter<DocHolder> {

        ArrayList<PatientModel> arrayList= new ArrayList<>();




        public PatientAdapter( ArrayList<PatientModel> arrayList) {
            this.arrayList=arrayList;
        }

        public DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DocHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmentitem, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final DocHolder holder, final int position) {
            holder.itemView.setTag(arrayList.get(position));


            String proimage="http://healthcare.blucorsys.in/account/"+arrayList.get(position).getPatient_image();
            if(arrayList.get(position).getPatient_image().isEmpty())
            {
                holder.ivPatientImage.setImageDrawable(getResources().getDrawable(R.drawable.userr));
            }
            else
            {
                Glide.with(getActivity())
                        .load(proimage)
                        .into(holder.ivPatientImage);
            }

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), ChatActivity.class));
                    appointmentId=arrayList.get(position).getAppointmentId();
                }
            });

            holder.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            String[] separated = (arrayList.get(position).getStartTime()).split(" ");



             holder.tvName.setText(arrayList.get(position).getPatient_name());
             holder.tvDate.setText(separated[0]);
             holder.tvTime.setText(separated[1] + " "+separated[2]);



            String valid_until=separated[0];

            String date_current = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

            Long value=diffTime(dateFormat.format(new Date()),separated[1] +" " +separated[2]);

            if(date_current.equalsIgnoreCase(valid_until))
            {
                if((value <= 0))
                {
                    holder.tvTimer.setVisibility(View.GONE);
                 //   holder.tvConsultnow.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.tvTimer.setVisibility(View.VISIBLE);
                   // holder.tvConsultnow.setVisibility(View.GONE);
                    startTimer(diffTime(dateFormat.format(new Date()),separated[1] +" " +separated[2]),holder.tvTimer);
                }
            }
            else
            {
                holder.tvTimer.setVisibility(View.GONE);
               // holder.tvConsultnow.setVisibility(View.VISIBLE);
            }


            //DoctorListModel dm = arrayList.get(position);
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

        TextView tvName;
        TextView tvDate;
        TextView tvTime;
        TextView tvAccept;
        TextView tvReject;
        TextView tvTimer;

        ImageView ivCall;
        ImageView ivPatientImage;
        CardView cardview;

        public DocHolder(View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvName);
            tvTimer=itemView.findViewById(R.id.tvTimer);
            cardview=itemView.findViewById(R.id.cardview);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvTime=itemView.findViewById(R.id.tvTime);
            ivCall=itemView.findViewById(R.id.ivCall);
            tvAccept=itemView.findViewById(R.id.tvAccept);
            tvReject=itemView.findViewById(R.id.tvReject);
            ivPatientImage=itemView.findViewById(R.id.ivPatientImage);

        }
    }
    //API
    private void HitGetAppointment( final String patientId){

        ploader.setMessage("Getting List...");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("mobilenumber", patientId);
            // Log.e("objj",""+obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETVideoConsult,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.e("DoctorResponse1111",""+response);
                        try {
                            PatientModel patientModel;

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                JSONArray dailyAppointments=response.getJSONArray("dailyAppointments");
                                for(int i=0;i<dailyAppointments.length();i++)
                                {
                                    patientModel=new PatientModel();
                                    JSONObject jsonObject=dailyAppointments.getJSONObject(i);
                                    patientModel.setEmailaddress(jsonObject.getString("emailaddress"));
                                    patientModel.setAppointmentId(jsonObject.getString("appointmentId"));
                                    patientModel.setPatient_image(jsonObject.getString("patient_image"));
                                    patientModel.setPatient_name(jsonObject.getString("patient_name"));
                                    patientModel.setStartTime(jsonObject.getString("startTime"));
                                    patientModel.setStatus(jsonObject.getString("status"));
                                    patientModel.setPhonenumber(jsonObject.getString("phonenumber"));
                                    arrayList.add(patientModel);
                                }

                                Collections.reverse(arrayList);
                                setAdapter(recyclerView,arrayList);
                            }
                            else{
                                Toast.makeText(getActivity(), "Sorry No Record Found", Toast.LENGTH_SHORT).show();
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

    public void setAdapter(RecyclerView mRecyclerview,ArrayList<PatientModel> arrayList)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1));
        mRecyclerview.setAdapter(new PatientAdapter(arrayList));
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
    private void startTimer(long millisec,TextView countdownTimerText) {
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
                /*clearing all fields and displaying countdown finished message          */
                countdownTimerText.setVisibility(View.GONE);
                timeup=true;
            }

        }.start();
    }

}
