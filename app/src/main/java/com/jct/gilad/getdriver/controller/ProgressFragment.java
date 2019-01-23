package com.jct.gilad.getdriver.controller;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jct.gilad.getdriver.R;
import com.jct.gilad.getdriver.model.backend.BackendFactorySingleton;
import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.jct.gilad.getdriver.model.backend.CurentLocation.getPlace;


public class ProgressFragment extends Fragment {
    EditText nameEditText;
    EditText phoneEditText;
    EditText startLocationEditText;
    EditText EndLocationEditText;
    Button button;
    Context context;
    Ride ride;
    
    
   
   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getContext();
        View view=inflater.inflate(R.layout.fragment_in_progress, container, false);
        nameEditText=(EditText) view.findViewById(R.id.nameEditText);
        phoneEditText=(EditText) view.findViewById(R.id.PhoneEditText);
        startLocationEditText=(EditText) view.findViewById(R.id.startLocationEditText);
        EndLocationEditText=(EditText) view.findViewById(R.id.endLocationEditText);
        button= (Button)view.findViewById(R.id.endRide);
        ride=BackendFactorySingleton.getBackend(context).getProgressRide();
        if(ride==null) {
            Toast.makeText(context, R.string.no_ride, Toast.LENGTH_LONG).show();
            return  view;
        }
        nameEditText.setText(ride.getClientName());
        phoneEditText.setText(ride.getClientPhoneNumber());
        startLocationEditText.setText(getPlace(ride.getSourceLocation(),context));
        EndLocationEditText.setText(getPlace(ride.getDestLocation(),context));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
                        BackendFactorySingleton.getBackend(context).RideBeFINISHED(ride);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = Calendar.getInstance().getTime();
                    ride.setEndTime(date);
                    new AsyncTask<Void,Void,Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            return BackendFactorySingleton.getBackend(context).updateRide(ride, new FireBase_DbManager.Action<String>() {
                                @Override
                                public void onSuccess(String obj) {
                                    Toast.makeText(context, R.string.update, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Exception exception) {

                                }

                                @Override
                                public void onProgress(String status, double percent) {

                                }
                            });
                        }
                    }.execute();
                    Toast.makeText(context, R.string.pass_FINISHED,Toast.LENGTH_LONG).show();
                nameEditText.setText("");
                phoneEditText.setText("");
                startLocationEditText.setText("");
                EndLocationEditText.setText("");
                }
            });

        return view;
    }




}