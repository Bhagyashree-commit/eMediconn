package com.example.emediconn.Patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.agrawalsuneet.loaderspack.utils.Utils;
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
import com.example.emediconn.Adapter.ListAdapter;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Doctor.DrawerActivity;
import com.example.emediconn.Model.CategoryModel;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.emediconn.databinding.ActivitySearchBinding;


public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding activitySearchBinding;
    ListAdapter adapter;
    List<String> arrayList= new ArrayList<>();
    String SubcatId;

    ProgressDialog ploader;

    public static JSONArray jsonResponse;

    String Search;

    ArrayList<HashMap<String, String>> arrayMap=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        ploader = new ProgressDialog(SearchActivity.this);

        getCategory();

        activitySearchBinding.search.setActivated(true);
        activitySearchBinding.search.setQueryHint("Search here...");
        activitySearchBinding.search.onActionViewExpanded();
        activitySearchBinding.search.setIconified(false);
        activitySearchBinding.search.clearFocus();
        activitySearchBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search=query;
                SubcatId="";
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                adapter.Filter(newText);
                return false;
            }
        });

        activitySearchBinding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(SearchActivity.this,DrawerActivity.class);
                intent.putExtra("page","Search");
                startActivity(intent);

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, DrawerActivity.class);
        i.putExtra("page","patient");
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //********************************Web Services************************//

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

                            arrayList.add(job.getString("speciality"));
                        }
                        adapter= new ListAdapter(arrayList);
                        activitySearchBinding.listView.setAdapter(adapter);
                    }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
                    }

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
                            Toast.makeText(SearchActivity.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            ploader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(SearchActivity.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            ploader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(SearchActivity.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            ploader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(SearchActivity.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            ploader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(SearchActivity.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.nav_host_fragment, fragment);
        FragmentManager mFragmentManager=getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }

}