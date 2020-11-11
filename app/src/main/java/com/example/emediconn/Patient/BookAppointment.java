package com.example.emediconn.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Doctor.DoctorDashboard;
import com.example.emediconn.Doctor.LoginDoctor;
import com.example.emediconn.Doctor.ui.DoctorDiscriptionFragment;
import com.example.emediconn.Doctor.ui.PatientDashboard;
import com.example.emediconn.Extras.Utils;
import com.example.emediconn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookAppointment extends Fragment {

    ProgressDialog ploader;
    Spinner spTimeSlot;

    static final int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    RelativeLayout rrUpload;

    Button btnBookAppointment;

    EditText etPatientname;
    EditText etPatientPhone;
    EditText etPatientEmail;

    TextView tvSpeciality;
    TextView tvDoctorName;
    TextView tvFee;

    ImageView ivProfile;

    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_book_appointment, container, false);

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

        ploader = new ProgressDialog(getActivity());
        rrUpload = v.findViewById(R.id.rrUpload);
        tvSpeciality=v.findViewById(R.id.tvSpeciality);
        tvDoctorName=v.findViewById(R.id.tvDoctorName);
        tvFee=v.findViewById(R.id.tvFee);
        etPatientname = v.findViewById(R.id.etPatientname);
        etPatientPhone = v.findViewById(R.id.etPatientPhone);
        etPatientEmail = v.findViewById(R.id.etPatientEmail);
        btnBookAppointment = v.findViewById(R.id.btnBookAppointment);
        ivProfile=v.findViewById(R.id.ivProfile);

        //setvalues
        tvSpeciality.setText(DoctorDiscriptionFragment.speciality);

        Glide.with(getActivity())
                .load(DoctorDiscriptionFragment.proimage)
                .into(ivProfile);


        tvDoctorName.setText(DoctorDiscriptionFragment.docname);
        tvFee.setText(DoctorDiscriptionFragment.fees);

        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etPatientname.getText().toString().trim().isEmpty())
                {
                    etPatientname.setError("Enter Patient Name");
                    etPatientname.requestFocus();
                }
                else if(etPatientPhone.getText().toString().trim().isEmpty())
                {
                    etPatientPhone.setError("Enter Patient Contact Number");
                    etPatientPhone.requestFocus();
                }
                else if(etPatientEmail.getText().toString().trim().isEmpty())
                {
                    etPatientEmail.setError("Enter Patient Email");
                    etPatientPhone.requestFocus();
                }
                else if(spTimeSlot.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getActivity(),"Select Time slot",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    BookAppointment(DoctorDiscriptionFragment.doc_mobile, etPatientname.getText().toString(), spTimeSlot.getSelectedItem().toString(), etPatientPhone.getText().toString(), etPatientEmail.getText().toString());
                }
            }
        });

        rrUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrowse();
            }
        });
        spTimeSlot = v.findViewById(R.id.spTimeSlot);

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            AppointmentTime(DoctorDiscriptionFragment.doc_mobile);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

       private void AppointmentTime( final String mobilenumber){
        ploader.setMessage("Logging in ...");
        ploader.show();
        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_TIMESLOT,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("Response1233","TRYYYYYY"+response);
                        try {
                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                HashMap<String,String> map;
                                map=new HashMap<>();
                                map.put("slot","Select Time Slot");
                                arrayList.add(map);
                                JSONArray jsonArray=response.getJSONArray("DoctorAppointments");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    JSONArray array=object.getJSONArray("available_slots");
                                    for (int j=0;j<array.length();j++)
                                    {
                                        JSONObject jObject=array.getJSONObject(j);
                                        map=new HashMap<>();
                                        map.put("slot",jObject.getString("slot"));
                                        arrayList.add(map);
                                    }
                                    spTimeSlot.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_adapter,arrayList));
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Creadentials Wrong" +response.getString("message"), Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Log.d("Exception","CATCHHHH"+e);
                            e.printStackTrace();
                        }
                        ploader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ploader.dismiss();
                    }
                }){

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

    private void BookAppointment( final String mobilenumber,String patient_name,String startDate,String patient_phonenumber,String patient_emailaddress){
        ploader.setMessage("Loading");
        ploader.show();

        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenumber);
            obj.put("patient_name", patient_name);
            obj.put("startDate", startDate);
            obj.put("patient_phonenumber", patient_phonenumber);
            obj.put("patient_emailaddress", patient_emailaddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_BOOKAPPOINTMENT,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d("Response1233","TRYYYYYY"+response);
                        try {
                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                Toast.makeText(getActivity(), "Appointment Booked successfully", Toast.LENGTH_SHORT).show();
                                replaceFragmentWithAnimation(new PatientDashboard());
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Log.d("Exception","CATCHHHH"+e);
                            e.printStackTrace();
                        }
                        ploader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ploader.dismiss();
                    }
                }){

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

    public class SpinnerAdapter extends ArrayAdapter<HashMap<String,String>> {

        ArrayList<HashMap<String,String>> list;

        public SpinnerAdapter(Context context, int textViewResourceId,ArrayList<HashMap<String,String>> list) {

            super(context, textViewResourceId, list);

            this.list = list;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View row = inflater.inflate(R.layout.spinner_adapter, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvSpinnerText);
            //label.setTypeface(typeface3);
            label.setText(list.get(position).get("slot"));
            return row;
        }
    }

    private void imageBrowse() {
       /* Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);*/
        Intent intent = new Intent();
        // intent.setType("application/pdf,application/msword");
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);

                //Setting image to ImageView
                // image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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



}