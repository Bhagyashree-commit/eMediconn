package com.example.emediconn.Doctor.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Model.CategoryModel;

import com.example.emediconn.Patient.PatientDashboardFragment;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SpecialityListFragment extends Fragment {
    private static final String TAG = SpecialityListFragment.class.getSimpleName();

RecyclerView rv_speciality;
ProgressDialog ploader;
PrefManager prefManager;
public  static String CategoryId,Catname;
List<CategoryModel> categorymodel=new ArrayList<>();
    public SpecialityListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Select Speciality");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_speciality_list, container, false);
        ploader = new ProgressDialog(getActivity());
        prefManager=new PrefManager(getActivity());
        
        rv_speciality=v.findViewById(R.id.specialitylist);
        getSpeciality();



        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        replaceFragmentWithAnimation(new DoctorDashboardFragment());

                        return true;
                    }
                }
                return false;
            }
        });

        return v;
    }

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_doctor, fragment);
        FragmentManager mFragmentManager=getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }
    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(new CategoryAdapter(categorymodel));
    }

    //*Recyclerview Adapter*//
    private class CategoryAdapter extends RecyclerView.Adapter<SpecialityListFragment.Holder> {
        private Context context;
        private List<CategoryModel> categorymodel;

        public CategoryAdapter(List<CategoryModel> categorymodel) {

            this.categorymodel = categorymodel;
        }


        public SpecialityListFragment.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SpecialityListFragment.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.speciality_list_item, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final SpecialityListFragment.Holder holder, final int position) {
            holder.itemView.setTag(categorymodel.get(position));

            final CategoryModel pu = categorymodel.get(position);
            holder.tvCategoryName.setText(pu.getSpeciality());
Log.e("SPecial","BFFFFFFFDS"+pu.getSpeciality());
            holder.catid.setText(pu.getSpeciality_id());

            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.nunitosemibold);
            holder.tvCategoryName.setTypeface(typeface);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  CategoryId=pu.speciality_id;
                  Catname=pu.speciality;
                    Log.e(TAG,"BHAGYASHREE"+CategoryId);
                    Log.e(TAG,"BHAGYASHREE"+Catname);
                    prefManager.set("catid",CategoryId);
                    prefManager.set("catnam",Catname);
                    prefManager.commit();
                    replaceFragmentWithAnimation(new MyDoctorProfileFragment());

                }
            });

        }
        public int getItemCount() {
            return categorymodel.size();
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {


        TextView tvCategoryName;
        TextView catid;

        public Holder(View itemView) {
            super(itemView);

            tvCategoryName=itemView.findViewById(R.id.tvCategoryName1);
            catid=itemView.findViewById(R.id.catid1);
        }
    }


    public void getSpeciality(){

        //loader.setMessage("Loading...Please Wait..");
        ploader.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_GETSPECIALITYNAME,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("testttttttt", response);
                ploader.dismiss();
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    if(array!= null){{
                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {

                            //getting product object from json array
                            JSONObject job = array.getJSONObject(i);
                            CategoryModel catModel = new CategoryModel();
                            //adding the product to product list
                            catModel.setSpeciality_id(job.getString("speciality_id"));
                            catModel.setSpeciality(job.getString("speciality"));


                            categorymodel.add(catModel);
                        }
                    }
                    }
                    else {
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();

                    }

                    Log.d(TAG, "jobgggggggggggggg" + categorymodel.size());
                    //creating adapter object and setting it to recyclerview
                    setAdapter(rv_speciality);
//                    CategoryAdapter adapter = new CategoryAdapter(getActivity(), categorymodel);
//                    rv_speciality.setAdapter(adapter);

                } catch (JSONException e) {

                    Log.e("testerroor",e.toString());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            System.out.println("Time Out and NoConnection...................." + error);
                            ploader.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(getActivity(), "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            ploader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(getActivity(), "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            ploader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(getActivity(), "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            ploader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(getActivity(), "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            ploader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(getActivity(), "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
    
    
    
}