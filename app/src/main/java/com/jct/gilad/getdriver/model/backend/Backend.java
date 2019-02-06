package com.jct.gilad.getdriver.model.backend;

import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
//import com.jct.gilad.getdriver.model.database.NotifyDataChange;
import com.jct.gilad.getdriver.model.database.NotifyDataChange;
import com.jct.gilad.getdriver.model.entities.Driver;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.List;
import java.util.Date;

/**
 * Backend - interface of the firebase database
 */
public interface Backend {
    /**
     *
     * @return all drivers' names
     */
    List<String> getDriversNames();

    /**
     * adding driver to the firebase.
     * @param driver the driver account that should be added.
     * @param action callback if the the driver added successfully.
     * @return Void.
     */
    Void addDriver(Driver driver, FireBase_DbManager.Action<String> action);

    /**
     * filtering available rides from rides.
     * @param rides list of rides.
     * @return only available rides from rides.
     */
    List<Ride> getAvailableRides(List<Ride> rides);

    /**
     * filtering finished rides from rides.
     * @param rides list of rides.
     * @return only finished rides from rides.
     */
    List<Ride> getFinishedRides(List<Ride> rides);

    /**
     * returns in progress ride of specific driver according to id.
     * @param notifyRides rides from where the in progress ride will be founded.
     * @param id of the specific driver
     * @return in progress ride that matches the id.
     */
    Ride getProgressRide(List<Ride> notifyRides, String id);

    /**
     * notifying to changes on all rides that in the firebase, and returns just the in progress ones.
     * @return in progress rides from firebase
     */
    public List<Ride> progressRides();

    /**
     * finds rides that matches driverName.
     * @param driverName name of driver that we want his rides.
     * @return rides that matches driverName.
     */
    List<Ride> getRidesByDriver(final String driverName);

    /**
     * returns all drivers from firebase.
     * @return all drivers from firebase.
     */
    List<Driver> getDrivers();

    /**
     * checks if ride is available (=can make it in progress).
     * @param ride we check its status.
     * @return return true if the ride is Available.
     * @throws Exception the drive isn't available! if the ride not Available.
     */
    boolean RideCanBeProgress(Ride ride) throws Exception;

    /**
     * changes the ride's status from INPROGRESS to FINISHED.
     * @param ride we want to change its status.
     * @throws Exception when the ride isn't in progress.
     */
    void RideBeFINISHED(Ride ride) throws Exception;

    /**
     * update a ride in the firebase.
     * @param toUpdate the updates ride.
     * @param action callback if the ride updated successfully.
     * @return
     */
    Void updateRide(final Ride toUpdate, final FireBase_DbManager.Action<String> action);

    /**
     * returns all available rides from firebase that their destination is in specific City.
     * @param City that the ride's destination should be in.
     * @return list of all available rides that their dest is in city.
     */
    List<Ride> getAvailableRidesByDestCity(final String City);

    /**
     * returns all available rides from firebase that is of driver.
     * @param driver that took the rides.
     * @return all available rides from firebase that is of driver.
     */
    List<Ride> getRidesForDriver(final Driver driver);

    /**
     * return rides from specific date.
     * @param date specific date.
     * @return rides from specific date.
     */
    List<Ride> getRidesByDate(Date date);

    /**
     * returns rides that their payment is less than maxPayment.
     * @param maxPayment
     * @return rides that their payment is less than maxPayment.
     */
    List<Ride> getRidesByPayment(final double maxPayment);

    /**
     *
     * @return all drivers' email.
     */
    List<String> getDriversEmails();

    /**
     * checks if the password is correct.
     * @param password that should match the email.
     * @param email that should match the password.
     * @return true if the password is correct.
     */
    boolean checkPassword(String password, String email);

    /**
     * search for driver according to ID.
     * @param ID of driver.
     * @return Driver that match the id.
     */
    Driver getDriverByID(final String ID);

    /**
     * notifying to changes in the rides that in the firebase.
     * @param notifyDataChange
     */
    void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange);

    /**
     * notifying to changes in the drivers that in the firebase
     * @param notifyDataChange
     */
    void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange);

    /**
     * stops the notifying to rides list.
     */
    void stopNotifyToRidesList();
}
