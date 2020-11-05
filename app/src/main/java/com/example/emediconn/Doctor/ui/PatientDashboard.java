package com.example.emediconn.Doctor.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emediconn.Patient.DoctorCategory;
import com.example.emediconn.Patient.PatientActivity;
import com.example.emediconn.R;

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
        getActivity().setTitle("");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_patient_dashboard, container, false);
        recyclerView=v.findViewById(R.id.recyclerView);
        rvDoctor=v.findViewById(R.id.rvDoctor);
        viewdoctor=v.findViewById(R.id.viewdoctor);
        setAdapter(recyclerView,new RecyAdapter());
        setAdapter(rvDoctor,new DoctAdapter());

        viewdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), DoctorCategory.class);
                getActivity().startActivity(myIntent);
            }
        });
        return v;
    }



    public void setAdapter(RecyclerView mRecyclerview, RecyclerView.Adapter adapter)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerview.setAdapter(adapter);
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
                        Intent myIntent = new Intent(getActivity(), DoctorCategory.class);
                        getActivity().startActivity(myIntent);
                    }
                }
            });


            Timer timerAsync = new Timer();
            TimerTask timerTaskAsync = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            Animation animation = new TranslateAnimation(0, holder.ivProviderImage.getWidth()+holder.shine.getWidth(),0, 0);
                            animation.setDuration(550);
                            animation.setFillAfter(false);
                            animation.setInterpolator(new AccelerateDecelerateInterpolator());
                            holder.shine.startAnimation(animation);
                        }
                    });
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
    private class DoctAdapter extends RecyclerView.Adapter<PatientDashboard.DocHolder> {


        public DoctAdapter() {
        }

        public PatientDashboard.DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DocHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dcotor_near_items, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final PatientDashboard.DocHolder holder, final int position) {


        }
        public int getItemCount() {
            return 6;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class DocHolder extends RecyclerView.ViewHolder {

        public DocHolder(View itemView) {
            super(itemView);



        }
    }
}