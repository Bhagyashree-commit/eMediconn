package com.example.emediconn.Patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.emediconn.R;

public class TermsOfServiceFragment extends Fragment {
    WebView web;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Terms And Conditions");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_terms_of_service, container, false);

        // Inflate the layout for this fragment


        web=v.findViewById(R.id.web2);
        web.loadUrl("http://healthcare.blucorsys.in/terms-of-service");
        web.getSettings().setJavaScriptEnabled(true);
        //Back
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        replaceFragmentWithAnimation(new PatientDashboardFragment());
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
        transaction.replace(R.id.nav_host_fragment, fragment);
        FragmentManager mFragmentManager=getFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }
}