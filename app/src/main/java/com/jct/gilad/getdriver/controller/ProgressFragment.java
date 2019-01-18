package com.jct.gilad.getdriver.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jct.gilad.getdriver.R;


 
public class ProgressFragment extends Fragment {
    EditText nameEditText;
    EditText phoneEditText;
    EditText startLocationEditText;
    EditText EndLocationEditText;
    
    
   
   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View view=inflater.inflate(R.layout.fragment_in_progress, container, false);
        nameEditText=(EditText) view.findViewById(R.id.nameEditText);
        phoneEditText=(EditText) view.findViewById(R.id.PhoneEditText);
        startLocationEditText=(EditText) view.findViewById(R.id.startLocationEditText);
        EndLocationEditText=(EditText) view.findViewById(R.id.endLocationEditText);
        return view;
    }

    private void findView() {

    }


}
