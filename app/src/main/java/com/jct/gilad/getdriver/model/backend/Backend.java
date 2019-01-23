package com.jct.gilad.getdriver.model.backend;

import android.location.Location;

import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
import com.jct.gilad.getdriver.model.database.NotifyDataChange;
import com.jct.gilad.getdriver.model.entities.Driver;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface Backend {
    ArrayList<String> getDriversNames();

    Void addDriver(Driver driver, FireBase_DbManager.Action<String> action);

    ArrayList<Ride> getAvailableRides();

    ArrayList<Ride> getFinishedRides();

    Ride getProgressRide();

    ArrayList<Ride> getRidesByDriver(Driver driver);

    public void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange);

    public void RideBeProgress(Ride ride) throws Exception;

    void RideBeFINISHED(Ride ride) throws Exception;

    public Void updateRide(final Ride toUpdate, final FireBase_DbManager.Action<String> action);

    ArrayList<Ride> getAvailableRidesByDestCity(Location location);

    ArrayList<Ride> getAvailableRidesCloseToLocation(Location location);

    ArrayList<Ride> getRidesByDate(Date date);

    ArrayList<Ride> getRidesByPayment(double min, double max);

    ArrayList<String> getDriversEmails();

    public boolean chackPassword(String password,String email);
}
