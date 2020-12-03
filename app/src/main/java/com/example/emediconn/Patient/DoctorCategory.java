package com.example.emediconn.Patient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.PulseLoader;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Doctor.DrawerActivity;
import com.example.emediconn.Doctor.ui.DoctorListFragment;
import com.example.emediconn.Doctor.ui.PatientDashboard;
import com.example.emediconn.Model.CategoryModel;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class DoctorCategory extends Fragment {
    private static final String TAG = DoctorCategory.class.getSimpleName();
    RecyclerView recyclerView;
    PrefManager prefManager;
    ProgressDialog ploader;
    LinearLayout lll;
    List<CategoryModel> categorymodel=new ArrayList<>();
    public static String categoryId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Speciality");
        View v= inflater.inflate(R.layout.activity_doctor_category, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        lll = v.findViewById(R.id.lll);
        ploader = new ProgressDialog(getActivity());

        getCategory();

        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        replaceFragmentWithAnimation(new PatientDashboard());

                        return true;
                    }
                }
                return false;
            }
        });


        // Inflate the layout for this fragment
        /*wv=v.findViewById(R.id.webview);
        wv.loadUrl("http://healthcare.blucorsys.in/");
*/
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

    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mRecyclerview.setAdapter(new CategoryAdapter(categorymodel));
    }

    //*Recyclerview Adapter*//
    private class CategoryAdapter extends RecyclerView.Adapter<Holder> {
        private Context context;
        private List<CategoryModel> categorymodel;

        public CategoryAdapter(List<CategoryModel> categorymodel) {

            this.categorymodel = categorymodel;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.speciality_items, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
            holder.itemView.setTag(categorymodel.get(position));

            final CategoryModel pu = categorymodel.get(position);
            holder.tvCategoryName.setText(pu.getSpeciality());

            holder.catid.setText(pu.getSpeciality_id());

            setScaleAnimation(holder.itemView);
            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.nunitosemibold);
            holder.tvCategoryName.setTypeface(typeface);


            Glide.with(DoctorCategory.this)
                    .load(pu.getImagepath())
                    .into(holder.ivCategory);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    replaceFragmentWithAnimation(new DoctorListFragment());
                    categoryId=pu.speciality_id;

                    Log.e(TAG,"BHAGYASHREE"+categoryId);

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

        ImageView ivCategory;
        TextView tvCategoryName;
        TextView catid;

        public Holder(View itemView) {
            super(itemView);
            ivCategory=itemView.findViewById(R.id.ivCategory);
            tvCategoryName=itemView.findViewById(R.id.tvCategoryName);
            catid=itemView.findViewById(R.id.catid);
        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(2000);
        view.startAnimation(anim);
    }

    public void getCategory(){

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

                            String imgpath="http://healthcare.blucorsys.in/"+job.getString("imagepath");
                            //String imgpath="http://healthcare.blucorsys.in/assets/img/category/9.jpg";
                            catModel.setImagepath(imgpath);

                            categorymodel.add(catModel);
                        }
                    }
                    }
                    else {
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();

                    }

                    Log.d(TAG, "jobgggggggggggggg" + categorymodel.size());
                    //creating adapter object and setting it to recyclerview
                    setAdapter(recyclerView);

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
