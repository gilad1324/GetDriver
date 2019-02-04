package com.jct.gilad.getdriver.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
import com.jct.gilad.getdriver.R;
import com.jct.gilad.getdriver.model.backend.BackendFactorySingleton;
import com.jct.gilad.getdriver.model.database.NotifyDataChange;
import com.jct.gilad.getdriver.model.entities.Ride;
import com.jct.gilad.getdriver.model.entities.Status1;

import java.util.ArrayList;
import java.util.List;


public class AvailableFragment extends Fragment {
    View view;
    public static ExpandableListAdapter listAdapter;
    EditText distanceFilter;
    String driverId;
    private ExpandableListView lv;
    public static List<Ride> rideArrayList = new ArrayList<>();

    public void newInstance(String id) {
        this.driverId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_available, container, false);
        lv = (ExpandableListView) view.findViewById(R.id.lv);
        distanceFilter = (EditText) view.findViewById(R.id.distanceFilter);
        lv.setTextFilterEnabled(true);
        distanceFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    listAdapter.resetData();
                }

                listAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final Context context = this.getContext();
        BackendFactorySingleton.getBackend().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                rideArrayList = obj;
                for (Ride ride : rideArrayList) {
                    if (ride.getStatus() != Status1.AVAILABLE)
                        rideArrayList.remove(ride);
                }
                if (rideArrayList.size() != 0) {
                    listAdapter = new ExpandableListAdapter(context, rideArrayList, driverId);
                    lv.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return view;
    }
}
