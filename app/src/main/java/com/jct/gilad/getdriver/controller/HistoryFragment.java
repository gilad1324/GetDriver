package com.jct.gilad.getdriver.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jct.gilad.getdriver.R;
import com.jct.gilad.getdriver.model.backend.BackendFactorySingleton;
import com.jct.gilad.getdriver.model.database.NotifyDataChange;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    View view;
    public static List<Ride> FinishRides = new ArrayList<>();
    ListView finishRides;
    HistoryListViewAdapter lv;
    String driverName;

    public void newInstance(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        finishRides = (ListView) view.findViewById(R.id.FinishedRides);
        final Context context = this.getContext();
        BackendFactorySingleton.getBackend().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                FinishRides = BackendFactorySingleton.getBackend().getFinishedRides(obj);
                if (FinishRides.size() != 0) {
                    lv = new HistoryListViewAdapter(context);
                    finishRides.setAdapter(lv);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

        return view;
    }
}
