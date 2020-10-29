package com.example.emediconn.Patient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emediconn.R;

public class DoctorCategory extends AppCompatActivity {



    //recyclerview
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_category);
        recyclerView = findViewById(R.id.recyclerView);
        setAdapter(recyclerView);
    }


    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerview.setAdapter(new CategoryAdapter());
    }

    //*Recyclerview Adapter*//
    private class CategoryAdapter extends RecyclerView.Adapter<Holder> {

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.speciality_items, parent, false));
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
            setScaleAnimation(holder.itemView);
            Typeface typeface = ResourcesCompat.getFont(DoctorCategory.this, R.font.nunitosemibold);
            holder.tvCategoryName.setTypeface(typeface);
        }
        public int getItemCount() {
            return 9;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        ImageView ivCategory;
        TextView tvCategoryName;

        public Holder(View itemView) {
            super(itemView);
            ivCategory=itemView.findViewById(R.id.ivCategory);
            tvCategoryName=itemView.findViewById(R.id.tvCategoryName);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(2000);
        view.startAnimation(anim);
    }
}
