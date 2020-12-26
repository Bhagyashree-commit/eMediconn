
package com.example.emediconn.Patient;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.text.format.DateFormat;
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
import com.bumptech.glide.Glide;

import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;

import com.example.emediconn.Extras.Utils;
import com.example.emediconn.Model.MyAppointmentModel;

import com.example.emediconn.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyAppointmentFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog ploader;
    PrefManager prefManager;

    ArrayList<MyAppointmentModel> arrayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_appointments, container, false);
        getActivity().setTitle("My Appointments");


        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        replaceFragmentWithAnimation(new MyAccountFragment());
                        return true;
                    }
                }
                return false;
            }
        });

        recyclerView=v.findViewById(R.id.recyclerView);
        ploader = new ProgressDialog(getActivity());
        prefManager=new PrefManager(getActivity());


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            // ploader.show();
            HashMap<String, String> user = prefManager.getUserDetails();
            String patientId = prefManager.get("user_id");
            HitGetAppointment(patientId);
        }
        else {
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


    public void setAdapter(RecyclerView mRecyclerview,ArrayList<MyAppointmentModel> arrayList)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1));
        mRecyclerview.setAdapter(new PatientAdapter(arrayList));
    }

    //Doctor Recyclerview
    private class PatientAdapter extends RecyclerView.Adapter<DocHolder> {

        ArrayList<MyAppointmentModel> arrayList=new ArrayList<>();

        public PatientAdapter( ArrayList<MyAppointmentModel> arrayList) {
            this.arrayList=arrayList;
        }
        public DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DocHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_myappointments, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final DocHolder holder, final int position) {
            holder.itemView.setTag(arrayList.get(position));
            //DoctorListModel dm = arrayList.get(position);

            holder.tvFee.setText(arrayList.get(position).getFees());
            holder.tvLocation.setText(arrayList.get(position).getHospital_location());
            holder.tvName.setText(arrayList.get(position).getDoctor_name());
            holder.tvSpeciality.setText(arrayList.get(position).getSpeciality());
            holder.tvStatus.setText("Received");
            holder.tvTime.setText(arrayList.get(position).getStartTime());


            String[] separated = (arrayList.get(position).getStartTime()).split(" ");
            holder.tvTime.setText(separated[1] +" " +separated[2]);
            String[] separateddate = separated[0].split("-");
            holder.tvDate.setText(separateddate[0]);
            Log.e("",""+separated[0]);

            String dateString = separated[0];
            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = inFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
            String goal = outFormat.format(date);

            holder.tvDayName.setText(goal);

//            Date readDate = null;
//            try {
//                readDate = df.parse(dateString);
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(readDate.getTime());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            Log.e("",""+separated[0]);

//            String dayOfTheWeek = (String) DateFormat.format("EEEE", Long.parseLong(separated[0])); // Thursday
//
//            Log.e("defsdf","blanck"+dayOfTheWeek);
//            Calendar c = Calendar.getInstance();
//            c.setTimeInMillis(Long.parseLong(arrayList.get(position).getStartTime()));
//
//
//            String dayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
//            Log.e("ddddd",""+dayOfWeek);
//
//            holder.tvDayName.setText(dayOfWeek);
//
//
//            System.out.println(c.get(Calendar.DAY_OF_WEEK));
//            String[] separated = getDate(Long.parseLong(arrayList.get(position).getStartTime())).split("-");
//            holder.tvDate.setText(separated[0]);

            if(arrayList.get(position).getDoctor_image().isEmpty())
            {
                holder.ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.doctorr));
            }
            else
            {
                Glide.with(MyAppointmentFragment.this)
                        .load(arrayList.get(position).getDoctor_image())
                        .into(holder.ivProfile);
            }
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
        ImageView ivProfile;

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
            ivProfile=itemView.findViewById(R.id.ivProfile);
        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    //API
    private void HitGetAppointment( final String patientId){

        ploader.setMessage("Getting List...");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("patientId", patientId);
            Log.e("patientid",""+obj);
            // Log.e("objj",""+obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETMYAPPOINTMENT,obj,
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
                                    patientModel.setPhonenumber(jsonObject.getString("phonenumber"));
                                    patientModel.setDoctor_name(jsonObject.getString("doctor_name"));
                                   // patientModel.setStatus(jsonObject.getString("status"));
                                    patientModel.setHospital_location(jsonObject.getString("hospital_location"));

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



