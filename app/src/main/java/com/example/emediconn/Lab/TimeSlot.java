package com.example.emediconn.Lab;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.example.emediconn.R;

import org.joda.time.DateTime;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;

public class TimeSlot extends AppCompatActivity implements  OnDateClickListener {

    RecyclerView recyclerView;
    RecyclerView recyclerEvening;
    RecyclerView recyclerAfternoon;

    public String isSelected;

    Button btnBookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecttime);
        WeekCalendar weekCalendar=findViewById(R.id.weekCalendar);

        recyclerView=findViewById(R.id.recyclerView);

        btnBookNow=findViewById(R.id.btnBookNow);

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TimeSlot.this,Address.class));
            }
        });
//        recyclerEvening=findViewById(R.id.recyclerEvening);
//        recyclerAfternoon=findViewById(R.id.recyclerAfternoon);
        setAdapter(recyclerView);
//        setAdapter(recyclerEvening);
//        setAdapter(recyclerAfternoon);

        weekCalendar.setOnDateClickListener(this);

    }
    @Override
    public void onDateClick(DateTime dateTime) {
       // Log.e("jjddjd",""+dateTime.getDayOfWeek());
        Log.e("jjddjd",""+dateTime.toLocalDate());
    }
    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerview.setAdapter(new TimeSlotAdapter());
    }

    //*Recyclerview Adapter*//
    private class TimeSlotAdapter  extends RecyclerView.Adapter<Holder> {

        private int last_position=0;
        private int present_position=-1;

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_items, parent, false));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {


            if( last_position!=-1 && present_position==position){
                holder.tvTime.setBackground(getResources().getDrawable(R.drawable.capsule));
                holder.tvTime.setTextColor(getResources().getColor(R.color.white));
                Log.i("TAG",last_position+"--lastPosition--"+position+"--position--"+present_position+"--presentPosition in if");
            }
            else{
               // holder.tvTime.setVisibility(View.GONE);
                holder.tvTime.setBackground(getResources().getDrawable(R.drawable.timecapsule));
                holder.tvTime.setTextColor(getResources().getColor(R.color.blue_900));
                Log.i("TAG",last_position+"--lastPosition--"+position+"--position--"+present_position+"--presentPosition in else");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    present_position = recyclerView.getChildAdapterPosition(view);

                    if(present_position!=RecyclerView.NO_POSITION) {

                        if(present_position!=-1){
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

                            notifyItemChanged(present_position);
                            System.err.println(last_position+" -- myParent");

                            last_position=present_position;
                            System.err.println(last_position+" -- myParent2");

                        }
                    }
                    else {
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
            return 6;
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

            tvTime=itemView.findViewById(R.id.tvTime);
        }
    }
}