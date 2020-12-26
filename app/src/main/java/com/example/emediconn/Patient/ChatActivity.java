package com.example.emediconn.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Doctor.ui.VideoConsultFragment;
import com.example.emediconn.Model.MyAppointmentModel;
import com.example.emediconn.R;
import com.example.emediconn.web_communication.WebCall;
import com.example.emediconn.web_communication.WebConstants;
import com.example.emediconn.web_communication.WebResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, WebResponse {

    //Recyclerview
    RecyclerView recyclerView;

    //Imageview
    ImageView ivVideo;


    PrefManager prefManager;

    private String room_Id;
    private String token;

    String appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //findId
        recyclerView=findViewById(R.id.recyclerView);
        ivVideo=findViewById(R.id.ivVideo);
        ivVideo.setOnClickListener(this);
        prefManager=new PrefManager(this);

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
        switch (view.getId())
        {
            case R.id.ivVideo:

                if(prefManager.get("usertype").equalsIgnoreCase("doctor"))
                {


                    //change conditions here
                    if(VideoConsultFragment.timeup==false)
                    {
                        Toast.makeText(this,"You can not start video call now",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        getRoomId();
                    }

                }
                else
                {
                Toast.makeText(this,"Your request has been submitted to Doctor",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void getRoomId() {
        new WebCall(this, this, jsonObjectToSend(), WebConstants.getRoomId, WebConstants.getRoomIdCode, false, true).execute();
    }

    private JSONObject jsonObjectToSend() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Test Dev Room");
            jsonObject.put("appointmentId", VideoConsultFragment.appointmentId);
            jsonObject.put("settings", getSettingsObject());
            jsonObject.put("data", getDataObject());
            jsonObject.put("sip", getSIPObject());
            jsonObject.put("owner_ref", "fadaADADAAee");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getSIPObject() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

    private JSONObject getDataObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Rashmi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getSettingsObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("description", "Testing");
            jsonObject.put("scheduled", false);
            jsonObject.put("scheduled_time", "");
            jsonObject.put("duration", 50);
            jsonObject.put("participants", 2);
            jsonObject.put("billing_code", 1234);
            jsonObject.put("auto_recording", false);
            jsonObject.put("active_talker", true);
            jsonObject.put("max_active_talkers", 1);
            jsonObject.put("moderators", 2);
            jsonObject.put("quality", "HD");
            jsonObject.put("wait_moderator", false);
            jsonObject.put("adhoc", false);
            jsonObject.put("mode", "lecture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void getRoomTokenWebCall() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Rashmi");
            jsonObject.put("role", "participant");
            jsonObject.put("user_ref", "2236");
            jsonObject.put("roomId", room_Id);

            new WebCall(this, this, jsonObject, WebConstants.getTokenURL, WebConstants.getTokenURLCode, false, false).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onGetTokenSuccess(String response) {
        Log.e("responseToken", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("result").equalsIgnoreCase("0")) {
                token = jsonObject.optString("token");

                Intent intent = new Intent(this, VideoConferenceActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("name", "Rashmi");
                startActivity(intent);
            } else {
                Toast.makeText(this, jsonObject.optString("error"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onGetRoomIdSuccess(String response) {
        Log.e("responseDashboard", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            room_Id = jsonObject.optJSONObject("room").optString("room_id");
            validateRoomIDWebCall();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void validateRoomIDWebCall() {
        new WebCall(this, this, null, WebConstants.validateRoomId + room_Id, WebConstants.validateRoomIdCode, true, false).execute();
    }

    @Override
    public void onWebResponse(String response, int callCode) {
        switch (callCode) {

            case WebConstants.getRoomIdCode:
                onGetRoomIdSuccess(response);
                break;
            case WebConstants.getTokenURLCode:
                onGetTokenSuccess(response);
                break;
            case WebConstants.validateRoomIdCode:
                onVaidateRoomIdSuccess(response);
                break;
        }
    }

    @Override
    public void onWebResponseError(String error, int callCode) {
        Log.e("errorDashboard", error);
    }


    private void onVaidateRoomIdSuccess(String response) {
        Log.e("responsevalidate", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("result").trim().equalsIgnoreCase("40001")) {
                Toast.makeText(this, jsonObject.optString("error"), Toast.LENGTH_SHORT).show();
            } else {
                // savePreferences();
                getRoomTokenWebCall();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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