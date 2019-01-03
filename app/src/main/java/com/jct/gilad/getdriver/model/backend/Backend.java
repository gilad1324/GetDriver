package com.jct.gilad.getdriver.model.backend;

import android.location.Location;

import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
import com.jct.gilad.getdriver.model.entities.Driver;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface Backend {
    ArrayList<String> getDriversNames();

    void addDriver(Driver driver, FireBase_DbManager.Action<String> action);

    ArrayList<Ride> getAvailableRides();

    ArrayList<Ride> getFinishedRides();

    ArrayList<Ride> getRidesByDriver(Driver driver);

    ArrayList<Ride> getAvailableRidesByDestCity(Location location);

    ArrayList<Ride> getAvailableRidesCloseToDriver(Driver driver);

    ArrayList<Ride> getRidesByDate(Date date);

    ArrayList<Ride> getRidesByPayment();
}
