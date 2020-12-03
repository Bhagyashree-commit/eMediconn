package com.example.emediconn.Doctor.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.example.emediconn.Doctor.DoctorDashboard;
import com.example.emediconn.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedRegiProofInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedRegiProofInfoFragment extends Fragment {
    MaterialBetterSpinner upload_doc;
    String[] SPINNDOCUMENT = {"Medical Registration Proof"};
    LinearLayout linlay;
    ImageButton iv_idproof;
    Button conti;
    TextView text_proverify;
    ImageView btn_back,iv_medicalproof;
    String upload_document;
    static final int REQUEST_CODE_DOC = 1;
    private int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MedRegiProofInfoFragment() {
        // Required empty public constructor
    }
    public static MedRegiProofInfoFragment newInstance(String param1, String param2) {
        MedRegiProofInfoFragment fragment = new MedRegiProofInfoFragment();
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
        View v= inflater.inflate(R.layout.fragment_med_regi_proof, container, false);
        requestStoragePermission();
        upload_doc=v.findViewById(R.id.spin_medidocument);
        iv_idproof=v.findViewById(R.id.upload_medicalproof);
        iv_medicalproof=v.findViewById(R.id.iv_medicalproof);

        conti=v.findViewById(R.id.btn_continuemedicalproof);
        linlay=v.findViewById(R.id.lin2);
        text_proverify=v.findViewById(R.id.text_proverify);

        linlay.setBackgroundColor(getResources().getColor(R.color.blue_300));
        text_proverify.setTextColor(getResources().getColor(R.color.white));

        iv_idproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imageBrowse();
            }
        });


        ArrayAdapter<String> speciality_s = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNDOCUMENT);

        upload_doc.setAdapter(speciality_s);


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

               // uploadMultipart();
                replaceFragmentWithAnimation(new EstasblishmentProofFragment());
            }
        });

        return  v;
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




    public void uploadMultipart() {


        //getting the actual path of the image
       // String path = FilePath.getPath(getContext(), filePath);



    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}