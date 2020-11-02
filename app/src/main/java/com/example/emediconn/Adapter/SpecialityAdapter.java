package com.example.emediconn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.emediconn.Doctor.DoctorRegistration;
import com.example.emediconn.Doctor.LoginDoctor;
import com.example.emediconn.Fragment.SpecialityFragment;
import com.example.emediconn.Model.SpecialityModel;
import com.example.emediconn.R;

import java.util.List;

public class SpecialityAdapter extends RecyclerView.Adapter<SpecialityAdapter.ViewHolder>  {
    private Context context;
    private List<SpecialityModel> smodel;
    public static String Speciality;

    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }

    public SpecialityAdapter(Context context, List smodel) {
        this.context = context;
        this.smodel = smodel;
    }


    @NonNull
    @Override
    public SpecialityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.speciality_list, parent, false);
        SpecialityAdapter.ViewHolder viewHolder = new SpecialityAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialityAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(smodel.get(position));

        final SpecialityModel specialityModel = smodel.get(position);

        Log.d("job id","40 "+specialityModel.getSpeciality_id());
        // holder.job_id.setText(pu.getJob_id());
        holder.s_id.setText(specialityModel.getSpeciality_id());
        holder.s_title.setText(specialityModel.getSpeciality_title());
       // holder.speciality.setText(specialityModel.getSpeciality_title());

        Glide.with(context)
                .load(specialityModel.getSpeciality_image())
                .into(holder.s_image);

        //   holder.s_image.setIm(specialityModel.getSpeciality_image());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.putExtra("Speciality",specialityModel.getSpeciality_title());

                    Speciality=specialityModel.getSpeciality_title();
               // Log.d("speciality","bhagya "+specialityModel.getSpeciality_title());

                context.startActivity(new Intent(context, DoctorRegistration.class));

            }
        });


    }

    @Override
    public int getItemCount() {
        return smodel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView s_id;
        public TextView s_title,speciality;
        public ImageView s_image;

        public ViewHolder(View itemView) {
            super(itemView);

            s_id = (TextView) itemView.findViewById(R.id.sid);
            s_title = (TextView) itemView.findViewById(R.id.stitle);
         //   speciality = (TextView) itemView.findViewById(R.id.speciality);
            s_image = (ImageView) itemView.findViewById(R.id.simage);


        }
    }
}
