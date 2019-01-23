package com.jct.gilad.getdriver.model.database;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jct.gilad.getdriver.model.backend.Backend;
import com.jct.gilad.getdriver.model.entities.Driver;
import com.jct.gilad.getdriver.model.entities.Ride;
import com.jct.gilad.getdriver.model.entities.Status1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FireBase_DbManager implements Backend {
    static DatabaseReference RidesRef;
    static List<Ride> RidesList;

    static DatabaseReference DriversRef;
    static List<Driver> DriversList;

    Geocoder gcd;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");
        DriversRef = database.getReference("drivers");
    }

    public FireBase_DbManager(Context context) {
        gcd = new Geocoder(context, Locale.getDefault());
        RidesList = new ArrayList<Ride>();
        DriversList = new ArrayList<Driver>();

        RidesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RidesList.clear();
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    RidesList.add(rideSnapshot.getValue(Ride.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DriversRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DriversList.clear();
                for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                    DriversList.add(driverSnapshot.getValue(Driver.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void RideBeProgress(Ride ride) throws Exception {
        if (ride.getStatus() == Status1.AVAILABLE)
            ride.setStatus(Status1.INPROGRESS);
        else
            throw new Exception("the drive not available!");
    }


    public void RideBeFINISHED(Ride ride) throws Exception {
        if (ride.getStatus() == Status1.INPROGRESS)
            ride.setStatus(Status1.FINISHED);
        else
            throw new Exception("the drive not progress!");
    }
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
                action.onProgress("error upload Driver data", 100);
            }
        });
        return null;
    }

    private static ChildEventListener rideRefChildEventListener;
    public void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (rideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify ride list"));
            }
            RidesList.clear();
            rideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String p = dataSnapshot.getKey();
                    try {
                        ride.setClientPhoneNumber(p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    RidesList.add(ride);
                    notifyDataChange.OnDataChanged(RidesList);
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

    @Override
    public ArrayList<String> getDriversNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Driver driver : DriversList)
            names.add(driver.getFirstName() + " " + driver.getLastName());
        return names;
    }

    public ArrayList<String> getDriversEmails() {
        ArrayList<String> emails = new ArrayList<>();
        for (Driver driver : DriversList)
            emails.add(driver.getEmail());
        return emails;
    }
    public boolean chackPassword(String password,String email) {

        for (Driver driver : DriversList)
            if(driver.getEmail()==email&&driver.getPassword()==password)
                return true;

        return false;
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
                action.onProgress("error upload Driver data", 100);
            }
        });


        return null;
    }

    @Override
    public ArrayList<Ride> getAvailableRides() {
        ArrayList<Ride> rides = new ArrayList<>();
        for (Ride ride : RidesList)
            if (ride.getStatus() == Status1.AVAILABLE)
                rides.add(ride);
        return rides;
    }

    @Override
    public ArrayList<Ride> getFinishedRides() {
        ArrayList<Ride> rides = new ArrayList<>();
        for (Ride ride : RidesList)
            if (ride.getStatus() == Status1.FINISHED)
                rides.add(ride);
        return rides;
    }

    public Ride getProgressRide() {

        for (Ride ride : RidesList)
            if (ride.getStatus() == Status1.INPROGRESS)
                return ride;
        return null;
    }

    @Override
    public ArrayList<Ride> getRidesByDriver(final Driver driver) {
        ArrayList<Ride> rides = new ArrayList<>();
        for (Ride ride : RidesList)
            if (ride.getDriverID() == driver.getId())
                rides.add(ride);
        return rides;
    }

    public String getCityName(Location location) {
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "IOException ...";
    }

    @Override
    public ArrayList<Ride> getAvailableRidesByDestCity(final Location location) {
        ArrayList<Ride> rides = new ArrayList<>();
        for (Ride ride : RidesList)
            if (ride.getStatus() == Status1.AVAILABLE &&
                    getCityName(location) == getCityName(ride.getDestLocation()))
                rides.add(ride);
        return rides;
    }

    @Override
    public ArrayList<Ride> getAvailableRidesCloseToLocation(Location location) {
        ArrayList<Ride> rides = new ArrayList<>();
        for (Ride ride : RidesList)
            if (ride.getStatus() == Status1.AVAILABLE &&
                    location.distanceTo(ride.getSourceLocation()) < 3000)
                rides.add(ride);
        return rides;
    }

    @Override
    public ArrayList<Ride> getRidesByDate(Date date) {
        ArrayList<Ride> rides = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (Ride ride : RidesList)
            if (sdf.format(date).equals(sdf.format(ride.getStartTime())))
                rides.add(ride);
        return rides;
    }

    @Override
    public ArrayList<Ride> getRidesByPayment(double min, double max) {
        ArrayList<Ride> rides = new ArrayList<>();
        double payment;
        for (Ride ride : RidesList) {
            if (ride.getStatus() == Status1.FINISHED) {
                //for every minute the payment is 2 (shekels?)
                payment = (ride.getEndTime().getTime() - ride.getStartTime().getTime()) / 60000.0 * 2;
                if (payment >= min && payment <= max)
                    rides.add(ride);
            }
        }
        return rides;
    }

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }
}
