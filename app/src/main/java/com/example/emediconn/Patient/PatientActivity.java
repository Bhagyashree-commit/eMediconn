package com.example.emediconn.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emediconn.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PatientActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView rvDoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        recyclerView=findViewById(R.id.recyclerView);
        rvDoctor=findViewById(R.id.rvDoctor);
        setAdapter(recyclerView,new RecyAdapter());
        setAdapter(rvDoctor,new DoctAdapter());
    }

    public void setAdapter(RecyclerView mRecyclerview, RecyclerView.Adapter adapter)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerview.setAdapter(adapter);
    }

    //*Recyclerview Adapter*//
    private class RecyAdapter extends RecyclerView.Adapter<Holder> {

        int[] image={R.drawable.consult,R.drawable.pharama,R.drawable.pathalogy,R.drawable.insurance,R.drawable.ambulance,R.drawable.hospitalrr};
        String[] array={"Consult Doctor","Order Medicines","Pathalogy","Insurance","Ambulance","Hospital"};
        String[] description={"Find Doctor near you","Order Medicines to home","Book Test at doorstep","Get Insured with us","Call Ambulance","Find nearby Hospitals"};

        public RecyAdapter() {

        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_items, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
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
                        Toast.makeText(PatientActivity.this,"Comming Soon",Toast.LENGTH_SHORT).show();
                    }
                }
            });


            Timer timerAsync = new Timer();
            TimerTask timerTaskAsync = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
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
    private class DoctAdapter extends RecyclerView.Adapter<DocHolder> {


        public DoctAdapter() {
        }

        public DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DocHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dcotor_near_items, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final DocHolder holder, final int position) {


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