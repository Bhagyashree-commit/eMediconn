package com.example.emediconn.Doctor.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.Model.DoctorListModel;
import com.example.emediconn.Patient.DoctorCategory;
import com.example.emediconn.Patient.PatientActivity;
import com.example.emediconn.Patient.SearchActivity;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDashboard extends Fragment {

    RecyclerView recyclerView;
    RecyclerView rvDoctor;
    TextView viewdoctor;
    ProgressDialog ploader;

    ArrayList<DoctorListModel> arrayList=new ArrayList<>();
    RelativeLayout rlsearchview;

    public PatientDashboard() {
        // Required empty public constructor
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientDashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDashboard newInstance(String param1, String param2) {
        PatientDashboard fragment = new PatientDashboard();
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
        getActivity().setTitle("Dashboard");
        View v= inflater.inflate(R.layout.fragment_patient_dashboard, container, false);
        ploader = new ProgressDialog(getActivity());

        // Inflate the layout for this fragment
        recyclerView=v.findViewById(R.id.recyclerView);
        rvDoctor=v.findViewById(R.id.rvDoctor);
        viewdoctor=v.findViewById(R.id.viewdoctor);
        setAdapter(recyclerView,new RecyAdapter());


        viewdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new DoctorCategory());
            }
        });

        rlsearchview=v.findViewById(R.id.rlsearchview);

        rlsearchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });


        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            // ploader.show();
            HitDoctorListAPI("17");
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

    public void setAdapter(RecyclerView mRecyclerview, RecyclerView.Adapter adapter)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerview.setAdapter(adapter);
    }

    public void setAdapter(RecyclerView mRecyclerview,ArrayList<DoctorListModel> arrayList)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerview.setAdapter(new DoctAdapter(arrayList) );
    }

    //*Recyclerview Adapter*//
    public class RecyAdapter extends RecyclerView.Adapter<PatientDashboard.Holder> {

        int[] image={R.drawable.consult,R.drawable.pharama,R.drawable.pathalogy,R.drawable.insurance,R.drawable.ambulance,R.drawable.hospitalrr};
        String[] array={"Consult Doctor","Order Medicines","Pathalogy","Insurance","Ambulance","Hospital"};
        String[] description={"Find Doctor near you","Order Medicines to home","Book Test at doorstep","Get Insured with us","Call Ambulance","Find nearby Hospitals"};

        public RecyAdapter() {

        }

        public PatientDashboard.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_items, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final PatientDashboard.Holder holder, final int position) {
//
//            Typeface typeface = ResourcesCompat.getFont(PatientActivity.this, R.font.nunitosemibold);
//            holder.tvProviderName.setTypeface(typeface);
            holder.tvProviderName.setText(array[position]);
            holder.tvDescription.setText(description[position]);
            holder.ivProviderImage.setImageDrawable(getResources().getDrawable(image[position]));


            if(position !=0)
            {
                holder.ivComingSoon.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.ivComingSoon.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position !=0)
                    {
                        Toast.makeText(getActivity(),"Comming Soon",Toast.LENGTH_SHORT).show();
                    }
                    else {
                       replaceFragmentWithAnimation(new DoctorCategory());
                    }
                }
            });



            Timer timerAsync = new Timer();
            TimerTask timerTaskAsync = new TimerTask() {
                @Override
                public void run() {

                    if(getActivity() !=null){
                        //call the ui thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Animation animation = new TranslateAnimation(0, holder.ivProviderImage.getWidth()+holder.shine.getWidth(),0, 0);
                                animation.setDuration(550);
                                animation.setFillAfter(false);
                                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                                holder.shine.startAnimation(animation);
                            }
                        });
                    }


                }
            };
            timerAsync.schedule(timerTaskAsync, 0, 5000);

        }


        public int getItemCount() {
            return 6;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        ImageView ivProviderImage;
        TextView tvProviderName;
        TextView tvDescription;
        ImageView shine;
        ImageView ivComingSoon;


        public Holder(View itemView) {
            super(itemView);
            ivProviderImage=itemView.findViewById(R.id.ivProviderImage);
            tvProviderName=itemView.findViewById(R.id.tvProviderName);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            shine = itemView.findViewById(R.id.shine);
            ivComingSoon = itemView.findViewById(R.id.ivComingSoon);

        }
    }

    //Doctor Recyclerview
    private class DoctAdapter extends RecyclerView.Adapter<DocHolder> {
    ArrayList<DoctorListModel> arrayList=new ArrayList<>();

        public DoctAdapter(ArrayList<DoctorListModel> arrayList) {

            this.arrayList=arrayList;
        }

        public PatientDashboard.DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DocHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dcotor_near_items, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final PatientDashboard.DocHolder holder, final int position) {


            String image_url="http://healthcare.blucorsys.in/daccount/"+arrayList.get(position).getProfile_photo();
            Glide.with(getActivity())
                    .load(image_url)
                    .into(holder.ivProviderImage);

            holder.ratingbar.setRating(Float.parseFloat(arrayList.get(position).getRating()));
            holder.tvDoctorName.setText(arrayList.get(position).getDoctor_name());
            holder.tvExperience.setText(arrayList.get(position).getExperience()+ " Years Exp");
            holder.tvLocation.setText(arrayList.get(position).getHospitalname());

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

        ImageView ivProviderImage;
        TextView tvDoctorName;
        TextView tvSpeciality;
        TextView tvLocation;
        TextView tvExperience;
        RatingBar ratingbar;

        public DocHolder(View itemView) {
            super(itemView);

            ivProviderImage=itemView.findViewById(R.id.ivProviderImage);
            tvDoctorName=itemView.findViewById(R.id.tvDoctorName);
            tvSpeciality=itemView.findViewById(R.id.tvSpeciality);
            tvLocation=itemView.findViewById(R.id.tvLocation);
            tvExperience=itemView.findViewById(R.id.tvExperience);
            ratingbar=itemView.findViewById(R.id.ratingbar);


        }
    }

    //API
    private void HitDoctorListAPI( final String categoryId){
        ploader.setMessage("Getting List...");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("categoryId", categoryId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_GETDOCTORLIST,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("DoctorResponse",""+response);
                        try {
                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                DoctorListModel model;
                                JSONArray jsonArray=response.getJSONArray("listofdoctors");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    model=new DoctorListModel();
                                    model.setUser_id(jsonObject.getString("user_id"));
                                    model.setDoctor_name(jsonObject.getString("doctor_name"));
                                    model.setDegree(jsonObject.getString("degree"));
                                    model.setRating(jsonObject.getString("rating"));
                                    model.setProfile_photo(jsonObject.getString("profile_photo"));
                                    model.setHospitalname(jsonObject.getString("hospitalname"));
                                    model.setExperience(jsonObject.getString("experience"));
                                    model.setFees(jsonObject.getString("fees"));

                                    arrayList.add(model);
                                }

                                setAdapter(rvDoctor,arrayList);
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