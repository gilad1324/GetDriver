package com.jct.gilad.getdriver.model.database;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FireBase_DbManager extends Activity implements Backend {
    static DatabaseReference RidesRef;
    static List<Ride> RidesList;

    static DatabaseReference DriversRef;
    static List<Driver> DriversList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");
        RidesList = new ArrayList<Ride>();

        DriversRef = database.getReference("drivers");
        DriversList = new ArrayList<Driver>();
    }

    @Override
    public ArrayList<String> getDriversNames() {
        final ArrayList<String> driversNames = new ArrayList<>();
        DriversRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot driver : dataSnapshot.getChildren()) {
                    String name = driver.getValue(Driver.class).getFirstName() + driver.getValue(Driver.class).getLastName();

                    driversNames.add(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return driversNames;
    }

    @Override
    public void addDriver(final Driver driver, final Action<String> action) {
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
    }

    @Override
    public ArrayList<Ride> getAvailableRides() {
        final ArrayList<Ride> rides = new ArrayList<>();
        RidesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ride : dataSnapshot.getChildren()) {
                    if (ride.getValue(Ride.class).getStatus() == Status1.AVAILABLE)
                        rides.add(ride.getValue(Ride.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return rides;
    }

    @Override
    public ArrayList<Ride> getFinishedRides() {
        final ArrayList<Ride> rides = new ArrayList<>();
        RidesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ride : dataSnapshot.getChildren()) {
                    if (ride.getValue(Ride.class).getStatus() == Status1.FINISHED)
                        rides.add(ride.getValue(Ride.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return rides;
    }

    @Override
    public ArrayList<Ride> getRidesByDriver(final Driver driver) {
        final ArrayList<Ride> rides = new ArrayList<>();
        RidesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ride : dataSnapshot.getChildren()) {
                    if (ride.getValue(Ride.class).getDriverID() == driver.getId())
                        rides.add(ride.getValue(Ride.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return rides;
    }

    public String getCityName(Location location) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
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
        final ArrayList<Ride> rides = new ArrayList<>();
        RidesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot r : dataSnapshot.getChildren()) {
                    Ride ride = r.getValue(Ride.class);
                    if (ride.getStatus() == Status1.AVAILABLE && getCityName(location) == getCityName(ride.getDestLocation()))
                        rides.add(ride);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return rides;
    }

    @Override
    public ArrayList<Ride> getAvailableRidesCloseToDriver(Driver driver) {
        return null;
    }

    @Override
    public ArrayList<Ride> getRidesByDate(Date date) {
        return null;
    }

    @Override
    public ArrayList<Ride> getRidesByPayment() {
        return null;
    }

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }
}
