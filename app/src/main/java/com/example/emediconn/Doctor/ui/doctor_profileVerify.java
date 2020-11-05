package com.example.emediconn.Doctor.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.emediconn.Database.AppConfig;
import com.example.emediconn.Database.PrefManager;
import com.example.emediconn.Doctor.DoctorDashboard;
import com.example.emediconn.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.emediconn.Doctor.ui.MedRegiProof.REQUEST_CODE_DOC;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link doctor_profileVerify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class doctor_profileVerify extends Fragment {
    private static final String TAG = String.valueOf(doctor_profileVerify.class);
    MaterialBetterSpinner upload_doc;
    String[] SPINNDOCUMENT = {"Adhar Card","Driving Licence","Voter Id","Any Other Gov Id"};
    LinearLayout linlay;
    ImageButton iv_idproof;
    Button conti;
    TextView text_proverify;
    ImageView btn_back,image_idproof;
    ProgressDialog ploader;
    PrefManager prefManager;
    String upload_document;
    static final int PICK_IMAGE_REQUEST = 1;
    String filePath;
    Bitmap bitmap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public doctor_profileVerify() {
        // Required empty public constructor
    }

    public static doctor_profileVerify newInstance(String param1, String param2) {
        doctor_profileVerify fragment = new doctor_profileVerify();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v= inflater.inflate(R.layout.fragment_doctor_profile_verify, container, false);


      upload_doc=v.findViewById(R.id.spin_document);
      iv_idproof=v.findViewById(R.id.upload_idproof);
      conti=v.findViewById(R.id.btn_continueidproof);
      linlay=v.findViewById(R.id.lin2);
      text_proverify=v.findViewById(R.id.text_proverify);
      image_idproof=v.findViewById(R.id.iv_idproof);


        linlay.setBackgroundColor(getResources().getColor(R.color.blue_300));
        text_proverify.setTextColor(getResources().getColor(R.color.white));

        ploader =new ProgressDialog(getContext());

        ArrayAdapter<String> speciality_s = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNDOCUMENT);

        upload_doc.setAdapter(speciality_s);

        iv_idproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });

        upload_doc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 upload_document  = (String) adapterView.getItemAtPosition(i);
            }
        });

        btn_back=v.findViewById(R.id.backbutton);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DoctorDashboard.class);
                startActivity(intent);
            }
        });
        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (filePath != null) {
                    imageUpload(filePath);
                    replaceFragmentWithAnimation(new MedRegiProof());
                } else {
                    Toast.makeText(getActivity(), "file not selected!", Toast.LENGTH_LONG).show();
                }*/

                //sendIdProofToServer();



                ploader = new ProgressDialog(getActivity());
                ploader.setMessage("Uploading, please wait...");
                ploader.show();

                //converting image to base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                //sending image to server
                StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOADIDPROOF, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        ploader.dismiss();
                        if(s.equals("true")){
                            Toast.makeText(getContext(), "Uploaded Successful", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    //adding parameters to send
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("image", imageString);
                        return parameters;
                    }
                };

                RequestQueue rQueue = Volley.newRequestQueue(getActivity());
                rQueue.add(request);
            }
        });








      return v;
    }

   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CODE_DOC){
                Uri picUri = data.getData();
                filePath = getPath(picUri);
                image_idproof.setImageURI(picUri);
            }
        }
    }
    // Get Path of selected image
    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getActivity(),    contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }*/

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_doctor, fragment);
        FragmentManager mFragmentManager=getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
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


    private void imageUpload( final  String filePath){
        ploader.setMessage("Loding...");
        ploader.show();

        Log.d(TAG,"IMAGE PATH "+filePath);


        prefManager = new PrefManager(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

        String mobilenum = user.get(PrefManager.KEY_MOBILENUM);

        String idproof = "identityProof";


        JSONObject obj = new JSONObject();
        try {
            obj.put("mobilenumber", mobilenum);
            obj.put("formName", idproof);
            obj.put("documentName",upload_document);
            obj.put("user_doc",filePath);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_UPLOADIDPROOF,obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ploader.dismiss();
                        Log.d(TAG,"TRYYYYYY"+response);
                        try {

                            if(response.getString("status").equalsIgnoreCase("true"))
                            {
                                String mesage = response.getString("message");
                                 Toast.makeText(getActivity(), "success" +mesage, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //Toast.makeText(getActivity(), "unsuccess", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG,"CATCHHHH"+e);
                            e.printStackTrace();
                        }
                        System.out.println(response);
                        //ploader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // ploader.dismiss();
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

  /*  private void imageUpload(final  String imagePath){

        prefManager=new PrefManager(getContext());

        HashMap<String, String> user = prefManager.getUserDetails();

      final String mobilenum = user.get(PrefManager.KEY_MOBILENUM);

      final String idproof = "identityProof";


        // Showing progress dialog at user registration time.
        //loader.setMessage("Loading...Please Wait..");
        ploader.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOADIDPROOF,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                ploader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jsonObject = new JSONObject(response);





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {


                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                Log.d("PARAMS",""+params);

                params.put("mobilenumber", mobilenum);
                params.put("formName", idproof);
                params.put("documentName",upload_document);
                params.put("user_doc",imagePath);
                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }*/



}