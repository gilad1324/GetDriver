package com.jct.gilad.getdriver.model.backend;

import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
//import com.jct.gilad.getdriver.model.database.NotifyDataChange;
import com.jct.gilad.getdriver.model.database.NotifyDataChange;
import com.jct.gilad.getdriver.model.entities.Driver;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.List;
import java.util.Date;

public interface Backend {
    List<String> getDriversNames();

    Void addDriver(Driver driver, FireBase_DbManager.Action<String> action);

    List<Ride> getAvailableRides(List<Ride> rides);

    List<Ride> getFinishedRides(List<Ride> rides);

    Ride getProgressRide(List<Ride> notifyRides, String id);

    public List<Ride> progressRides();

    List<Ride> getRidesByDriver(final String driverName);

    boolean RideCanBeProgress(Ride ride) throws Exception;

    void RideBeFINISHED(Ride ride) throws Exception;

    Void updateRide(final Ride toUpdate, final FireBase_DbManager.Action<String> action);

    List<Ride> getAvailableRidesByDestCity(final String City);
    
    List<Ride> getAvailableRidesForDriver(final Driver driver);

    List<Ride> getRidesByDate(Date date);

    List<Ride> getRidesByPayment(final double maxPayment);

    List<String> getDriversEmails();

    boolean checkPassword(String password, String email);

    Driver getDriverByID(final String ID);

    void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange);

    void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange);

    void stopNotifyToRidesList();
}
