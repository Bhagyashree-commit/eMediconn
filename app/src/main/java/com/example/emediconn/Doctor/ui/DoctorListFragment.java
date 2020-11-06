package com.example.emediconn.Doctor.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Doctor.DrawerActivity;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.Model.DoctorListModel;
import com.example.emediconn.Patient.DoctorCategory;
import com.example.emediconn.Patient.LoginPatient;
import com.example.emediconn.Patient.SearchActivity;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorListFragment extends Fragment {

    ProgressDialog ploader;

    ArrayList<DoctorListModel> arrayList;

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Choose Doctor");


        View v= inflater.inflate(R.layout.doctorfragment, container, false);
        recyclerView=v.findViewById(R.id.recyclerView);


        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        replaceFragmentWithAnimation(new DoctorCategory());

                        return true;
                    }
                }
                return false;
            }
        });
        // Inflate the layout for this fragment


        ploader = new ProgressDialog(getActivity());

        arrayList=new ArrayList<>();

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
    public void setAdapter(RecyclerView mRecyclerview,ArrayList<DoctorListModel> arrayList)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1));
        mRecyclerview.setAdapter(new DoctAdapter(arrayList));
    }

    //Doctor Recyclerview
    private class DoctAdapter extends RecyclerView.Adapter<DocHolder> {

        ArrayList<DoctorListModel> arrayList=new ArrayList<>();

        public DoctAdapter( ArrayList<DoctorListModel> arrayList) {

            this.arrayList=arrayList;
        }

        public DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DocHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_doctor_list, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final DocHolder holder, final int position) {

            holder.tvDegree.setText(arrayList.get(position).getDegree());
            holder.tvDoctorName.setText(arrayList.get(position).getDoctor_name());
            holder.tvExperience.setText(arrayList.get(position).getExperience() +" Years");
            holder.tvHospitalname.setText(arrayList.get(position).getHospitalname());
            holder.tvFee.setText("\u20b9" +arrayList.get(position).getFees());
            holder.tvRating.setText(arrayList.get(position).getRating());
            holder.ratingbar.setRating(Float.parseFloat(arrayList.get(position).getRating()));

            String image_url="http://healthcare.blucorsys.in/daccount/"+arrayList.get(position).getProfile_photo();
            Glide.with(getActivity())
                    .load(image_url)
                    .into(holder.ivDoctorImage);
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

        ImageView ivDoctorImage;
        TextView tvDoctorName;
        TextView tvDegree;
        TextView tvHospitalname;
        TextView tvExperience;
        TextView tvRating;
        TextView tvFee;
        RatingBar ratingbar;
        public DocHolder(View itemView) {
            super(itemView);

            ivDoctorImage=itemView.findViewById(R.id.ivDoctorImage);
            tvDoctorName=itemView.findViewById(R.id.tvDoctorName);
            tvDegree=itemView.findViewById(R.id.tvDegree);
            tvExperience=itemView.findViewById(R.id.tvExperience);
            tvHospitalname=itemView.findViewById(R.id.tvHospitalname);
            tvRating=itemView.findViewById(R.id.tvRating);
            ratingbar=itemView.findViewById(R.id.ratingbar);
            tvFee=itemView.findViewById(R.id.tvFee);
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
