package com.example.emediconn.Doctor.ui;

import android.content.Intent;
import android.os.Bundle;

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

import com.example.emediconn.Doctor.DoctorDashboard;
import com.example.emediconn.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstasblishmentProof#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstasblishmentProof extends Fragment {

    MaterialBetterSpinner upload_doc;
    String[] SPINNDOCUMENT = {"Clinic Registration Proof","Document For WAste Disposal","Tax Receipt"};
    LinearLayout linlay;
    ImageButton iv_idproof;
    Button conti;
    TextView text_proverify;
    ImageView btn_back;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EstasblishmentProof() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstasblishmentProof.
     */
    // TODO: Rename and change types and number of parameters
    public static EstasblishmentProof newInstance(String param1, String param2) {
        EstasblishmentProof fragment = new EstasblishmentProof();
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
        View v= inflater.inflate(R.layout.fragment_estasblishment_proof, container, false);

        upload_doc=v.findViewById(R.id.spin_document);
        iv_idproof=v.findViewById(R.id.upload_idproof);
        conti=v.findViewById(R.id.btn_conti1);
        linlay=v.findViewById(R.id.lin2);
        text_proverify=v.findViewById(R.id.text_proverify);

        linlay.setBackgroundColor(getResources().getColor(R.color.blue_300));
        text_proverify.setTextColor(getResources().getColor(R.color.white));

        ArrayAdapter<String> speciality_s = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNDOCUMENT);

        upload_doc.setAdapter(speciality_s);


        upload_doc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                upload_doc = (MaterialBetterSpinner) adapterView.getItemAtPosition(i);
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
                replaceFragmentWithAnimation(new StartgettingPatient());
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
}