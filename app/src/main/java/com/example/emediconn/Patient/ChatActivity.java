package com.example.emediconn.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.emediconn.Model.MyAppointmentModel;
import com.example.emediconn.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    //Recyclerview
    RecyclerView recyclerView;

    //Imageview
    ImageView ivVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //findId
        recyclerView=findViewById(R.id.recyclerView);
        ivVideo=findViewById(R.id.ivVideo);

        //SetListner
        setAdapter(recyclerView);
    }


    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(this,1));

        mRecyclerview.setAdapter(new ChatAdapter());
    }


    @Override
    public void onClick(View view) {

    }
    //Doctor Recyclerview
    private class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

        ArrayList<MyAppointmentModel> arrayList=new ArrayList<>();

        public ChatAdapter() {
        }

        public ChatAdapter(ArrayList<MyAppointmentModel> arrayList) {
            this.arrayList=arrayList;
        }
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ChatHolder holder, final int position) {

            //holder.itemView.setTag(arrayList.get(position));

        }



        public int getItemCount() {
            return 1;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class ChatHolder extends RecyclerView.ViewHolder {

        public ChatHolder(View itemView) {
            super(itemView);

        }
    }
}