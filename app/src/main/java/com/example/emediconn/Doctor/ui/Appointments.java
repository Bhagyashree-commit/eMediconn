package com.example.emediconn.Doctor.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.emediconn.Model.DoctorListModel;
import com.example.emediconn.Model.PatientModel;
import com.example.emediconn.Patient.LoginPatient;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Appointments extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog ploader;
    ArrayList<PatientModel> arrayList;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager=new PrefManager(Appointments.this);

        setContentView(R.layout.activity_appointments);

        recyclerView=findViewById(R.id.recyclerView);

        arrayList=new ArrayList<>();

        ploader = new ProgressDialog(this);

        HashMap<String, String> user = prefManager.getUserDetails();
        String patientId = user.get(PrefManager.KEY_ROLE);
        if (Utils.isNetworkConnectedMainThred(this)) {
            HitGetAppointment(patientId);
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

    }

    //Doctor Recyclerview
    private class PatientAdapter extends RecyclerView.Adapter<DocHolder> {

        ArrayList<PatientModel> arrayList=new ArrayList<>();

        public PatientAdapter( ArrayList<PatientModel> arrayList) {
            this.arrayList=arrayList;
        }

        public DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DocHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmentitem, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final DocHolder holder, final int position) {
            holder.itemView.setTag(arrayList.get(position));

            holder.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.tvName.setText(arrayList.get(position).getPatient_name());
           // holder.tvDate.setText(arrayList.get(position).getStartTime());
            holder.tvTime.setText(arrayList.get(position).getStartTime());

            holder.tvAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

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

        ImageView ivCall;

        public DocHolder(View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvName);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvTime=itemView.findViewById(R.id.tvTime);
            ivCall=itemView.findViewById(R.id.ivCall);
            tvAccept=itemView.findViewById(R.id.tvAccept);
            tvReject=itemView.findViewById(R.id.tvReject);

        }
    }

    //API
    private void HitGetAppointment( final String patientId){

        ploader.setMessage("Getting List...");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {

            obj.put("mobilenumber", "9960664554");
           // Log.e("objj",""+obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETPATIENTAPPOINTMENT,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("DoctorResponse",""+response);
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
                                 patientModel.setPatient_image(jsonObject.getString("patient_image"));
                                 patientModel.setPatient_name(jsonObject.getString("patient_name"));
                                 patientModel.setStartTime(jsonObject.getString("startTime"));
                                 patientModel.setStatus(jsonObject.getString("status"));
                                 patientModel.setPhonenumber(jsonObject.getString("phonenumber"));
                                 arrayList.add(patientModel);
                             }
                         setAdapter(recyclerView,arrayList);
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
        mRecyclerview.setLayoutManager(new GridLayoutManager(this,1));
        mRecyclerview.setAdapter(new PatientAdapter(arrayList));
    }
}