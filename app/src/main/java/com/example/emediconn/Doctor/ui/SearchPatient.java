package com.example.emediconn.Doctor.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.emediconn.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchPatient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPatient extends Fragment {

    WebView wv;
    public SearchPatient() {
        // Required empty public constructor
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchPatient.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchPatient newInstance(String param1, String param2) {
        SearchPatient fragment = new SearchPatient();
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
        getActivity().setTitle("About Us");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search_patient, container, false);
        /*wv=v.findViewById(R.id.webview);
        wv.loadUrl("http://healthcare.blucorsys.in/");
*/

        return v;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}