package com.jct.gilad.getdriver.model.database;

import android.location.Geocoder;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jct.gilad.getdriver.model.backend.Backend;
import com.jct.gilad.getdriver.model.entities.Driver;
import com.jct.gilad.getdriver.model.entities.Ride;
import com.jct.gilad.getdriver.model.entities.Status1;
import com.jct.gilad.getdriver.model.backend.CurrentLocation;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FireBase_DbManager implements Backend {
    public List<Ride> rides = new ArrayList<>();
    public List<Driver> drivers = new ArrayList<>();
    public CurrentLocation location;

    private static ChildEventListener rideRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;

    static DatabaseReference RidesRef;

    static DatabaseReference DriversRef;

    Geocoder gcd;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");
        DriversRef = database.getReference("drivers");
    }

    @Override
    public boolean RideCanBeProgress(Ride ride) throws Exception {
        if (ride.getStatus() == Status1.AVAILABLE)
            return true;
        else
            throw new Exception("the drive isn't available!");



    }

    @Override
    public void RideBeFINISHED(Ride ride) throws Exception {
        if (ride.getStatus() == Status1.INPROGRESS)
            ride.setStatus(Status1.FINISHED);
        else
            throw new Exception("the drive isn't in progress!");
    }

    @Override
    public Void updateRide(final Ride toUpdate, final Action<String> action) {
        final String key = (toUpdate.getClientPhoneNumber());
        RidesRef.child(key).setValue(toUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(toUpdate.getClientPhoneNumber());
                action.onProgress("upload Driver data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("ERROR upload Driver data", 100);
            }
        });
        return null;
    }

    @Override
    public List<String> getDriversNames() {
        final List<String> userNames = new ArrayList<String>();
        notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> notifyDrivers) {
                drivers = notifyDrivers;
                for (Driver driver : drivers) {
                    String fullName = driver.getFirstName() + " " + driver.getLastName();
                    userNames.add(fullName);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return userNames;
    }

    @Override
    public List<String> getDriversEmails() {
        final List<String> emails = new ArrayList<String>();
        notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> notifyDrivers) {
                drivers = notifyDrivers;
                for (Driver driver : drivers) {
                    emails.add(driver.getEmail());
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return emails;
    }

    @Override
    public boolean checkPassword(final String password, final String email) {
        final boolean[] resultFlag = {false};
        notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> notifyDrivers) {
                for (Driver driver : drivers) {
                    if (driver.getEmail().trim().equals(email.trim()) && driver.getPassword().trim().equals(password.trim()))
                        resultFlag[0] = true;
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return resultFlag[0];
    }

    @Override
    public Void addDriver(final Driver driver, final Action<String> action) {
        String key = driver.getId();
        DriversRef.child(key).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(driver.getId());
                action.onProgress("upload driver data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload driver data", 100);
            }
        });
        return null;
    }

    @Override
    public List<Ride> getAvailableRides(List<Ride> notifyRides) {
        for (Ride ride : notifyRides) {
            if (ride.getStatus() != Status1.AVAILABLE)
                if (ride.getStatus() != Status1.AVAILABLE)
                    notifyRides.remove(ride);
        }
        return notifyRides;
    }

    @Override
    public List<Ride> getFinishedRides(List<Ride> notifyRides) {
        for (Ride ride : notifyRides)
            if (ride.getStatus() != Status1.FINISHED)
                notifyRides.remove(ride);
        return notifyRides;
    }

    public List<Ride> progressRides() {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                for (Ride ride : rides) {
                    if (ride.getStatus() != Status1.INPROGRESS)
                        rides.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }

    public Ride getProgressRide(String id) {
        for (Ride ride : progressRides())
            if (ride.getDriverID() == id)
                return ride;
        return null;
    }

    @Override
    public List<Ride> getRidesByDriver(final String driverId) {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                String id;
                for (Ride ride : rides) {
                    id = ride.getDriverID();
                    if (!driverId.equals(id))
                        rides.remove(ride);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }
    @Override
    public Driver getDriverByID(final String ID) {
        final Driver[] d = new Driver[1];
        notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> notifyDrivers) {
                for (Driver driver : notifyDrivers) {
                    if (driver.getId().equals(ID))
                        d[0] = driver;
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return d[0];
    }

//    public String getCityName(Location location) {
//        List<Address> addresses = null;
//        try {
//            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
//            if (addresses.size() > 0) {
//                return addresses.get(0).getLocality();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "IOException ...";
//    }

    @Override
    public List<Ride> getAvailableRidesByDestCity(final String city) {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                for (Ride ride : rides) {
                    if (!location.getPlace(ride.getDestLocation()).matches(city))
                        rides.remove(ride);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }

    @Override
    public List<Ride> getAvailableRidesForDriver(final Driver driver) {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                for (Ride ride : rides) {
                    if (ride.getSourceLocation().distanceTo(driver.getCurrentLocation()) / 1000 >= 3)
                        rides.remove(ride);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }

    @Override
    public List<Ride> getRidesByDate(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                for (Ride ride : rides) {
                    if (ride.getStatus() == Status1.FINISHED)
                        if (!sdf.format(date).equals(sdf.format(ride.getStartTime())))
                            rides.remove(ride);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }

    @Override
    public List<Ride> getRidesByPayment(final double maxPayment) {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                double payment;
                for (Ride ride : rides) {
                    if (ride.getStatus() == Status1.FINISHED) {
                        payment = (ride.getEndTime().getTime() - ride.getStartTime().getTime()) / 60000.0 * 2;
                        if (payment > maxPayment)
                            rides.remove(ride);
                    }
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }

    public void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (rideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify ride list"));
            }
            rides.clear();
            rideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String phone = dataSnapshot.getKey();
                    try {
                        ride.setClientPhoneNumber(phone);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    rides.add(ride);
                    notifyDataChange.OnDataChanged(rides);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            RidesRef.addChildEventListener(rideRefChildEventListener);
        }
    }

    public static void stopNotifyToRidesList() {
        if (rideRefChildEventListener != null) {
            RidesRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;
        }
    }

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }

    public void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driverRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            drivers.clear();
            driverRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    try {
                        driver.setId(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    drivers.add(driver);
                    notifyDataChange.OnDataChanged(drivers);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            DriversRef.addChildEventListener(driverRefChildEventListener);
        }
    }

    public static void stopNotifyToDriversList() {
        if (driverRefChildEventListener != null) {
            DriversRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }
}
