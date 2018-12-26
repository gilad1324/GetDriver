package com.jct.gilad.getdriver.model.backend;

import android.location.Location;

import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
import com.jct.gilad.getdriver.model.entities.Driver;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.Date;
import java.util.List;

public interface Backend {
    List<String> getDriversNames();
    void addDriver(Driver driver, FireBase_DbManager.Action<String> action);
    List<Ride> getAvailableRides();
    List<Ride> getFinishedRides();
    List<Ride> getRidesByDriver(Driver driver);
    List<Ride> getAvailableRidesByDestCity(Location location);
    List<Ride> getAvailableRidesCloseToDriver(Driver driver);
    List<Ride> getRidesByDate(Date date);
    List<Ride> getRidesByPayment();

}
